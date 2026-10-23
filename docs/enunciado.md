# Enunciado del proyecto: Alerta Local

Enunciado del curso Construcción de aplicaciones móviles. Es contexto para el equipo y para los
agentes de IA: lo que manda en cada funcionalidad son los documentos de `docs/requerimientos/`.

## Fase 2: alcance de la entrega

La segunda fase debe incluir una implementación funcional de la aplicación que contemple todas
las pantallas, la navegación y las funcionalidades definidas para el proyecto. La implementación
debe respetar la arquitectura definida en clase y aplicar buenas prácticas de desarrollo.

En esta etapa, los datos pueden gestionarse en memoria, por lo que no es necesario implementar
mecanismos de persistencia en una base de datos u otro medio de almacenamiento. Tampoco se
requiere implementar mapas, carga de imágenes, gestión de permisos ni sesiones de usuario.

## Requisitos comunes (aplican a todas las temáticas)

### Roles

Cada aplicación debe manejar dos tipos de usuarios: Usuario y Moderador.

**Moderador:**

- Iniciar sesión.
- Gestionar las publicaciones de los usuarios (verificando o rechazando).
- Si un moderador rechaza una publicación debe escribir un motivo por el que lo hizo y la
  publicación rechazada no se muestra en el feed público.
- Marcar cualquier publicación como resuelta/finalizada.

**Usuario:**

- Registro (creación de nuevas cuentas).
- Login (inicio de sesión con email y password).
- Pantalla de inicio con un feed de publicaciones en modo lista o en un mapa. El feed se puede
  filtrar por categoría y por ubicación (cercanos, en la ciudad, etc.).
- Crear publicaciones, editarlas o borrarlas. La publicación se crea sin estar verificada.
- Cambiar el estado de su publicación a Resuelto/Finalizado cuando lo considere.
- Botón de "Es importante" / "Me interesa" para destacar una publicación. Los usuarios pueden
  votar y con base en esto determinar la relevancia.
- Agregar comentarios en las publicaciones para aportar información adicional.
- Ver el detalle de cada publicación (con los comentarios, ubicación exacta, fotos y la
  información completa).
- Apartado de notificaciones (en forma de lista) dentro de la aplicación, donde el usuario ve los
  eventos que le conciernen (comentarios en sus publicaciones, verificación o rechazo por parte
  de un moderador, nuevos logros, etc.). Las notificaciones se almacenan en la base de datos y se
  consultan al abrir la aplicación o al entrar a dicho apartado; no se requiere envío en tiempo
  real con Firebase Cloud Messaging.
- Editar sus datos personales y eliminar su cuenta.
- Estadísticas personales: dashboard con métricas del usuario (publicaciones activas,
  finalizadas y pendientes de verificación).
- Sistema de reputación y logros: puntos por participación (crear publicaciones, comentar,
  recibir votos, etc.).
- Niveles de usuario (4 niveles según puntos acumulados, definidos por cada grupo según su
  temática).
- Insignias/badges por diferentes logros (primera publicación, 10 publicaciones verificadas,
  publicación más destacada del mes, etc.).

### Requisitos técnicos

- El proyecto se debe implementar usando Jetpack Compose con Kotlin.
- Cada publicación debe tener: título, categoría, descripción, ubicación (latitud y longitud) y
  al menos una imagen.
- La dirección del usuario y la ubicación de las publicaciones deben obtenerse a través de un
  mapa, almacenando latitud y longitud en la base de datos.
- Para los mapas se recomienda usar Mapbox o Google Maps.
- Tanto el usuario como el moderador pueden recuperar la contraseña si la olvidan, por medio de
  un link enviado a su correo electrónico.
- Cuando un usuario hace un comentario en una publicación, se debe generar una notificación para
  el autor de dicha publicación.
- Las notificaciones deben marcarse como leídas/no leídas y permitir navegar a la publicación
  relacionada.
- Para el manejo de imágenes se debe usar un servicio externo (Cloudinary, AWS S3, Google Cloud
  Storage, etc.).
- El código fuente debe estar en un repositorio de GitHub. Todos los integrantes del grupo deben
  contribuir en el desarrollo.
- Los moderadores están precargados en la base de datos.
- Para la autenticación y el almacenamiento de datos (incluidas las notificaciones) puede usar
  Firebase, Supabase o cualquier otro servicio similar. No es necesario implementar un backend
  propio, pero se puede hacer si el grupo lo desea.

**Importante:** cada grupo debe pensar e implementar una necesidad adicional (no trivial) a su
sistema. En Alerta Local es la **detección de reportes duplicados** al publicar.

## Temática asignada: Reportes ciudadanos

**Contexto:** la seguridad ciudadana y el bienestar comunitario son preocupaciones crecientes. Se
necesita una plataforma que permita a los ciudadanos reportar situaciones de riesgo o emergencia
en su entorno.

**Entidad principal:** reporte de incidente.

**Categorías:** Seguridad (robos, actividades delictivas), Emergencias médicas (accidentes,
desmayos), Infraestructura (calles en mal estado, alumbrado público), Mascotas (perdidas,
encontradas), Comunidad (contaminación, basuras).

**Particularidades:**

- Los reportes tienen un estado de severidad que se determina por los votos de "Es importante".
- Los moderadores verifican la veracidad de los reportes.
- Los niveles de usuario son: Novato, Colaborador, Guardián, Héroe Comunitario.
