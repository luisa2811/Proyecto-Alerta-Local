# Instrucciones del proyecto Alerta Local

## Contexto del proyecto

Alerta Local es una app Android (Jetpack Compose + Kotlin) de **reportes ciudadanos**: los
vecinos reportan situaciones de riesgo o emergencia en su barrio y los moderadores verifican
su veracidad. Equipo de 3 integrantes, curso Construcción de aplicaciones móviles
(Universidad del Quindío).

- **Roles:** RESIDENT (usuario) y MODERATOR. Los moderadores están precargados.
- **Entidad principal:** reporte de incidente. Categorías: Seguridad, Emergencias,
  Infraestructura, Mascotas y Comunidad.
- **Estados del reporte:** PENDING (se crea sin verificar), VERIFIED, REJECTED (con motivo
  obligatorio; no aparece en el feed público) y RESOLVED.
- **Severidad:** se determina por los votos "Es importante" (Baja, Media, Alta, Crítica).
- **Reputación:** puntos por participación, 4 niveles (Novato, Colaborador, Guardián,
  Héroe Comunitario) e insignias.
- **Función adicional (plus):** detección de reportes duplicados al publicar.

El enunciado completo está en `docs/enunciado.md` y los requerimientos de cada integrante en
`docs/requerimientos/integrante-N.md`. **Lo que manda son los documentos de requerimientos**;
el enunciado es contexto.

### Alcance de la fase 2

La app debe ser funcional con **datos en memoria**. Aunque el enunciado los pida, en esta fase
**NO** se implementan: persistencia (Room, DataStore, Firebase), sesión persistente, mapas,
latitud y longitud, carga o visualización de imágenes, envío de correos ni permisos.
Donde el diseño los muestra, se dejan como en el prototipo o se reemplazan por texto
(por ejemplo, "Dirección o punto de referencia" en lugar de elegir en el mapa).

## Estructura de paquetes (paquete raíz `uniquindio.proyectoalertalocal`)

```
core/theme            Tema, colores (DesignSystem) y tipografía
core/component        Composables y helpers de presentación reutilizables
core/util             RequestResult, TimeFormatter y utilidades
domain/model          Modelos de dominio y reglas puras (ReputationRules, AuthorNames)
domain/repository     Interfaces de los repositorios
domain/usecase/<area> Casos de uso: auth, user, report, comment, moderation, notification
data/repository/memory Implementaciones en memoria de los repositorios
di                    Módulos de Hilt (RepositoryModule)
navigation            AppNavigation, AuthRoutes, SessionViewModel
features/<feature>    Pantalla + ViewModel + UiState de cada funcionalidad
docs/                 Enunciado y documentos de requerimientos
```

No crees paquetes nuevos fuera de esta estructura.

## Reglas por capa

### ViewModel
- Anotado con `@HiltViewModel` y constructor `@Inject constructor(...)`.
- `Inject` y `Singleton` se importan desde `jakarta.inject`, como en las guías del curso.
- Recibe **solo casos de uso**, nunca repositorios.
- Estado: `private val _uiState = MutableStateFlow(XUiState())` y
  `val uiState: StateFlow<XUiState> = _uiState.asStateFlow()`. Se actualiza con `_uiState.update { }`.
- Los casos de uso `suspend` se llaman dentro de `viewModelScope.launch`.
- El resultado de una acción se publica con `RequestResult` (`Loading`, `Success`, `Failure`)
  dentro del UiState.
- Las validaciones de formularios van en el UiState como propiedad calculada `isFormValid`
  (igual que en el proyecto base). La función de la acción empieza con
  `if (!currentState.isFormValid) return`.

### Pantalla (Composable)
- Obtiene el ViewModel con `hiltViewModel()`, importado desde
  `androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel` (NO usar `viewModel()` ni la versión
  de `androidx.hilt.navigation.compose`).
- Observa el estado con `val uiState by viewModel.uiState.collectAsStateWithLifecycle()`.
- Navega solo mediante parámetros lambda (`onNavigateToX`, `onBack`). La pantalla nunca recibe
  el `NavController`.
- No contiene listas de datos de ejemplo ni lógica de negocio.
- Reacciona a los resultados con `LaunchedEffect(uiState.result)`.

### Caso de uso
- Una clase por acción, con `@Inject constructor` y una sola función `operator fun invoke(...)`.
- Va en `domain/usecase/<area>` y su nombre describe la acción: `CreateReportUseCase`,
  `VerifyReportUseCase`.
- Si puede fallar por una regla de negocio (no existe, duplicado, estado o rol no permitido),
  retorna `Result<T>` y usa `Result.failure(Exception("mensaje exacto"))`. El ViewModel convierte
  el fallo en `RequestResult.Failure(mensaje)`.
- Las notificaciones y los logros se disparan **desde el caso de uso**, nunca desde el ViewModel,
  usando los casos de uso compartidos de la lista de abajo.

### Repositorio
- Interfaz en `domain/repository`, con operaciones `suspend` (salvo los flujos observables).
- Implementación en `data/repository/memory`, anotada con `@Singleton` y `@Inject constructor()`.
- Toda interfaz nueva se vincula con `@Binds` en `di/RepositoryModule`.
- Los datos de ejemplo viven SOLO en `data/repository/memory/InMemoryDemoData.kt`.

### Modelos
- Los modelos de dominio NO importan nada de Compose (ni `Color`, ni `ImageVector`).
- Los colores, íconos y textos de un modelo se definen en `core/component`.
- NO crees modelos `UiModel` paralelos que dupliquen un modelo de dominio.

## Utilidades compartidas (úsalas, no las dupliques)

| Para… | Usa |
| --- | --- |
| Usuario en sesión | `GetCurrentUserUseCase` (`domain/usecase/user`) |
| Crear una notificación | `NotifyUserUseCase` (`domain/usecase/notification`) |
| Revisar logros después de una acción que da puntos | `CheckNewAchievementsUseCase` (`domain/usecase/user`) |
| Marcar un reporte como resuelto (autor o moderador) | `ResolveReportUseCase` (`domain/usecase/report`) |
| Puntos, nivel e insignias | `ReputationRules.calculate(...)` (`domain/model`) |
| Nombre del autor ("Vecino anónimo", "Usuario eliminado") | `AuthorNames.displayName(user)` |
| Armar listas de reportes con autor, comentarios y votos | `ReportSummary.build(...)` |
| Comentarios con nombre del autor | `CommentSummary.build(...)` |
| Severidad de un reporte | `ReportSummary.severity` o `ReportSeverity.fromVotes(...)` |
| Texto, color e ícono de categoría, estado y severidad | `label()`, `color()`, `icon()` en `core/component/ReportCategoryUi.kt` |
| Código visible de un reporte ("#INF-8492") | `Report.displayCode()` |
| Tiempo relativo ("hace 3 h") | `Instant.toRelativeTime()` en `core/util/TimeFormatter.kt` |
| Validar un correo | `String.isValidEmail()` en `core/util/Validators.kt` |

Si una de estas utilidades todavía no existe en el código, no la crees por tu cuenta: avísame.

## Archivos compartidos (cuidado)

Estos archivos los usan las 3 integrantes:
`domain/model`, `domain/repository`, `data/repository/memory`, `di`, `navigation`,
`features/dashboard/navigation`, `core`, `gradle/libs.versions.toml`, `build.gradle.kts`.

En ellos SOLO se permite **agregar** (un método nuevo en una interfaz, una ruta nueva, un color
nuevo). Nunca cambies ni elimines lo que ya existe sin acordarlo con el equipo.
Si una tarea requiere cambiar algo existente en estos archivos, detente y avísame en vez de hacerlo.

## Estilo de código

- Identificadores en inglés; textos visibles para el usuario en español, terminados en punto
  cuando son mensajes.
- Usa imports, nunca nombres completos en línea (`androidx.compose...TextStyle(...)`).
- Sin imports sin usar, sin código comentado, sin `TODO`, sin `println` ni `Log`.
- No declares colores sueltos (`Color(0x...)`): usa `DesignSystem.Colors` o `MaterialTheme.colorScheme`.
- No agregues dependencias nuevas a Gradle.
- No cambies el diseño visual de las pantallas: solo conectas su funcionamiento.
- Elimina los mensajes de demostración ("demostración", "próximamente", "no se publicó",
  "temporal") cuando conectes la acción real. Solo pueden quedar los de mapas, ubicación y fotos.

## Datos de prueba

- Residente: `maria@alertalocal.test` / `Residente123!` (todos los residentes usan esa contraseña).
- Moderador: `moderador@alertalocal.test` / `Moderador123!` / código `MOD-2026`.

## Forma de trabajar

1. Antes de editar, muestra un plan breve: archivos a crear, archivos a modificar y casos de uso.
2. Modifica solo los archivos de la tarea asignada.
3. Al terminar, entrega:
   - la lista de archivos creados y modificados,
   - los casos de uso nuevos,
   - si tocaste algún archivo compartido y qué agregaste,
   - cómo probar la funcionalidad en el emulador.
