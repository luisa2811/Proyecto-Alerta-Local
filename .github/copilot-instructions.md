# Instrucciones del proyecto Alerta Local

Aplicación Android con Jetpack Compose, desarrollada por un equipo de 3 personas.
Estamos en la **fase 2**: la app debe ser funcional con **datos en memoria**.
NO se implementa: persistencia (Room, DataStore, Firebase), mapas, carga de imágenes,
permisos ni sesión persistente.

La arquitectura sigue el proyecto base del curso (capas de presentación, dominio,
datos e inyección de dependencias con Hilt). Respétala siempre.

## Estructura de paquetes (paquete raíz `uniquindio.proyectoalertalocal`)

```
core/theme            Tema, colores (DesignSystem) y tipografía
core/component        Composables reutilizables entre features
core/util             RequestResult y utilidades
domain/model          Modelos de dominio (Report, User, Comment, Notification, ...)
domain/repository     Interfaces de los repositorios
domain/usecase/<area> Casos de uso: auth, user, report, comment, moderation, notification
data/repository/memory Implementaciones en memoria de los repositorios
di                    Módulos de Hilt (RepositoryModule)
navigation            AppNavigation, AuthRoutes, SessionViewModel
features/<feature>    Pantalla + ViewModel + UiState de cada funcionalidad
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
- El resultado de una acción (login, guardar, crear, etc.) se publica con `RequestResult`
  (`Loading`, `Success`, `Failure`) dentro del UiState.
- Las validaciones de formularios van en el UiState como propiedad calculada `isFormValid`
  (igual que en el proyecto base). La función de la acción empieza con `if (!currentState.isFormValid) return`.

### Pantalla (Composable)
- Obtiene el ViewModel con `hiltViewModel()`, importado desde
  `androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel` (NO usar `viewModel()` ni la versión de `androidx.hilt.navigation.compose`).
- Observa el estado con `val uiState by viewModel.uiState.collectAsStateWithLifecycle()`.
- Navega solo mediante parámetros lambda (`onNavigateToX`, `onBack`). La pantalla nunca recibe el `NavController`.
- No contiene listas de datos de ejemplo ni lógica de negocio.
- Reacciona a los resultados con `LaunchedEffect(uiState.result)`.

### Caso de uso
- Una clase por acción, con `@Inject constructor` y una sola función `operator fun invoke(...)`.
- Va en `domain/usecase/<area>` y su nombre describe la acción: `CreateReportUseCase`, `VerifyReportUseCase`.

### Repositorio
- Interfaz en `domain/repository`, con operaciones `suspend` (salvo los flujos observables).
- Implementación en `data/repository/memory`, anotada con `@Singleton` y `@Inject constructor()`.
- Toda interfaz nueva se vincula con `@Binds` en `di/RepositoryModule`.
- Los datos de ejemplo viven SOLO en las implementaciones de `data/repository/memory`.

### Modelos
- Los modelos de dominio NO importan nada de Compose (ni `Color`, ni `ImageVector`).
- Los colores e íconos asociados a un modelo (por ejemplo, a cada categoría de reporte) se
  definen en la capa de presentación, en `core/component`, como hace el proyecto base en
  `ReportComponents.kt`.
- NO crees modelos `UiModel` paralelos que dupliquen un modelo de dominio.

## Archivos compartidos (cuidado)

Estos archivos los usan las 3 integrantes:
`domain/model`, `domain/repository`, `data/repository/memory`, `di`, `navigation`,
`features/dashboard/navigation`, `core/theme`, `core/util`, `gradle/libs.versions.toml`, `build.gradle.kts`.

En ellos SOLO se permite **agregar** (un método nuevo en una interfaz, una ruta nueva, un color nuevo).
Nunca cambies ni elimines lo que ya existe sin acordarlo con el equipo.
Si una tarea requiere cambiar algo existente en estos archivos, detente y avísame en vez de hacerlo.

## Estilo de código

- Identificadores en inglés; textos visibles para el usuario en español.
- Usa imports, nunca nombres completos en línea (`androidx.compose...TextStyle(...)`).
- Sin imports sin usar, sin código comentado, sin `TODO`, sin `println` ni `Log`.
- No declares colores sueltos (`Color(0x...)`): usa `DesignSystem.Colors` o `MaterialTheme.colorScheme`.
- No agregues dependencias nuevas a Gradle.
- No cambies el diseño visual de las pantallas: solo conectas su funcionamiento.
- Elimina los mensajes de demostración ("demostración", "próximamente", "no se publicó",
  "temporal") cuando conectes la acción real. Solo pueden quedar los de ubicación, mapas y fotos,
  porque están fuera del alcance de esta fase.

## Forma de trabajar

1. Antes de editar, muestra un plan breve: archivos a crear, archivos a modificar y casos de uso.
2. Modifica solo los archivos de la tarea asignada.
3. Al terminar, entrega:
   - la lista de archivos creados y modificados,
   - los casos de uso nuevos,
   - si tocaste algún archivo compartido y qué agregaste,
   - cómo probar la funcionalidad en el emulador.
