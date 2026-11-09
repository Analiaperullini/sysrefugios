# Decisiones técnicas — SysRefugios

Este documento reúne las decisiones técnicas del prototipo en su estado actual. Cada una sigue el mismo formato: **qué se decidió**, **por qué**, **qué problema resuelve**, **qué alternativa se descartó** y **por qué es adecuada para un proyecto individual**. Las reglas de negocio se citan con la numeración del TFG (RN-01 a RN-07, HU-014, HU-016, capítulo 14).

---

## 1. Alcance del sistema

**Qué se decidió**: un sistema de gestión **interna** para dos organizaciones de protección animal de Jujuy, Narices Frías y Hogar San Roque. Cubre el ciclo rescate → tránsito → adopción → seguimiento, la derivación de animales entre las dos organizaciones, el control sanitario (vacunaciones y castraciones) y las campañas de castración con cupos.

**Por qué**: es lo que identificó el relevamiento. Hoy la información está dispersa en celulares, redes sociales y registros manuales.

**Qué problema resuelve**: la falta de un registro único y consultable del estado de cada animal y de la coordinación entre las dos organizaciones.

**Qué alternativa se descartó**: una página pública de adopciones. El relevamiento no menciona una necesidad de difusión pública: el problema es de gestión interna.

**Por qué es adecuada para un proyecto individual**: acota el alcance a lo relevado y evita construir pantallas y flujos que no aportan al objetivo del Seminario.

---

## 2. Arquitectura en tres capas

**Qué se decidió**: frontend (HTML, CSS y JavaScript sin frameworks) → backend (Java, API REST en JSON) → base de datos (MySQL). El frontend se comunica con el backend por HTTP con `fetch()` y el backend con la base por JDBC.

**Por qué**: es una separación de responsabilidades estándar y fácil de explicar: presentación, lógica de negocio y persistencia no se mezclan.

**Qué problema resuelve**: permite desarrollar y probar cada capa por separado. La API se probó completa con `curl` antes y después de tener la interfaz.

**Qué alternativa se descartó**: páginas generadas en el servidor (JSP o Thymeleaf). El enunciado pedía un frontend HTML/CSS/JS separado del backend Java.

**Por qué es adecuada para un proyecto individual**: cada capa se puede verificar de forma independiente, lo que simplifica encontrar errores.

---

## 3. Backend: Spring Boot en lugar de Servlets y JDBC

**Qué se decidió**: Java 17 con Spring Boot 3.3 (Spring Web y Spring Data JPA), sin Lombok.

**Por qué**: el modelo tiene 14 tablas con operaciones de alta, consulta, edición y baja muy parecidas entre sí. Spring Data JPA evita escribir a mano el SQL de cada operación, y Spring Web simplifica la definición de los endpoints.

**Qué problema resuelve**: el tiempo limitado de un Seminario individual. Con Servlets y JDBC puro habría que programar el mapeo objeto-tabla y el manejo de JSON de cada entidad.

**Qué alternativa se descartó**: Servlets y JDBC puro. Es más transparente, pero exige mucho más código repetitivo para el mismo alcance.

**Por qué es adecuada para un proyecto individual**: es la tecnología Java más usada en la industria y tiene documentación oficial abundante para justificar cada anotación ante el tribunal.

---

## 4. Modelo de datos

**Qué se decidió**: 14 tablas: `organizacion`, `usuario`, `animal`, `rescate`, `espacio_transito`, `transito`, `derivacion`, `vacunacion`, `castracion`, `adoptante`, `adopcion`, `seguimiento`, `campania_castracion` y `asignacion_castracion`. Los nombres de tablas y columnas siguen el diccionario de datos del TFG. Las claves foráneas usan el prefijo `id_` (`id_animal`, `id_organizacion`, etc.).

**Por qué**: son las entidades que el relevamiento y el TFG permiten justificar sin inventar procesos.

**Qué problema resuelve**: cubre el ciclo completo del animal, la derivación entre organizaciones, el control de capacidad de los espacios y los cupos de las campañas.

**Qué alternativa se descartó**: modelar donaciones. El relevamiento las menciona, pero no describe el proceso con el detalle necesario para modelarlas sin adivinar.

**Por qué es adecuada para un proyecto individual**: un modelo totalmente justificable es más fácil de defender que uno más grande con tablas que no se pueden sostener con el relevamiento.

Campos de cada tabla, según el diccionario de datos (Tablas 15 a 28 del TFG):

| Tabla | Clave primaria | Campos |
|---|---|---|
| `organizacion` | `id_organizacion` | nombre, descripcion, telefono, direccion, email, etapa_vida_atendida, activo |
| `usuario` | `id_usuario` | nombre, apellido, email (único, se usa para ingresar), contrasena, id_organizacion, estado, intentos_fallidos, cambio_obligatorio, fecha_cambio_contrasena |
| `animal` | `id_animal` | nombre, especie, sexo, fecha_nacimiento, color, etapa_vida, tamanio, estado, descripcion, id_organizacion, activo |
| `rescate` | `id_rescate` | id_animal, fecha_rescate, lugar, descripcion, observaciones, id_organizacion, estado, id_usuario, activo |
| `espacio_transito` | `id_espacio` | id_organizacion, nombre, descripcion, capacidad, estado, observaciones, activo |
| `transito` | `id_transito` | id_animal, id_espacio, fecha_inicio, fecha_fin, estado (`EN_CURSO` o `FINALIZADO`), observaciones, activo |
| `derivacion` | `id_derivacion` | id_animal, id_organizacion_origen, id_organizacion_destino, fecha_derivacion, motivo, observaciones, activo |
| `vacunacion` | `id_vacunacion` | id_animal, fecha, vacuna, dosis, observaciones, activo |
| `castracion` | `id_castracion` | id_animal, fecha, veterinario, observaciones, activo |
| `adoptante` | `id_adoptante` | nombre, apellido, documento, telefono, email, direccion, observaciones, id_organizacion, consentimiento_datos, fecha_consentimiento, activo |
| `adopcion` | `id_adopcion` | id_animal, id_adoptante, id_organizacion, fecha_adopcion, cumple_mayor_edad, cumple_responsabilidad, cumple_espacio_cerrado, compromiso_vacunacion, compromiso_castracion, condiciones, observaciones, fecha_limite_castracion, alerta_castracion_vencida, activo |
| `seguimiento` | `id_seguimiento` | id_adopcion, fecha_seguimiento, estado_animal, observaciones, referencia_multimedia, informacion_contacto, activo |
| `campania_castracion` | `id_campania` | id_organizacion, nombre, fecha, lugar, cantidad_cupos, descripcion, estado, activo |
| `asignacion_castracion` | `id_asignacion` | id_campania, id_animal, fecha_asignacion, estado, observaciones |

Los campos obligatorios que agregó el diccionario se validan en el backend y en las pantallas: `vacunacion.dosis`, `castracion.veterinario` (también al marcar realizada una castración de campaña) y `seguimiento.estado_animal`. El tránsito toma la organización de su espacio, por eso `transito` no tiene `id_organizacion`.

Tres campos no figuran en el diccionario del TFG y se agregan al documento:

- `activo` en todas las tablas salvo `usuario` y `asignacion_castracion`, que usan su estado: baja lógica (sección 7).
- `adopcion.alerta_castracion_vencida`: marca de la alerta de RN-05 (sección 11).
- `organizacion.etapa_vida_atendida` (VARCHAR(20), admite nulos): qué etapa de vida recibe cada organización; la usa RN-03 (sección 10). Nulo significa que atiende todas las etapas.

---

## 5. Estructura de la base definida por `schema.sql` y validada al arrancar

**Qué se decidió**: la estructura de la base la define únicamente `database/schema.sql`, que además carga los datos de demostración. El backend arranca con `spring.jpa.hibernate.ddl-auto=validate`: Hibernate compara las entidades con las tablas y, si no coinciden, no arranca. Nunca modifica la base.

**Por qué**: con `update`, Hibernate agrega columnas pero no cambia tipos ni elimina columnas viejas. Durante el desarrollo eso dejó columnas obsoletas, y los enums quedaron como `ENUM` de MySQL en lugar de los `VARCHAR` del diccionario.

**Qué problema resuelve**: la base es reproducible y coincide con el diccionario de datos. Cualquier diferencia entre el código y la base se detecta al arrancar, no en medio de una operación.

**Qué alternativa se descartó**: herramientas de migraciones como Flyway o Liquibase. Son la práctica correcta en un sistema en producción, pero suman una dependencia y un proceso que un prototipo con una sola versión de la base no necesita.

**Por qué es adecuada para un proyecto individual**: un único script, legible y sin dependencias, alcanza para instalar el sistema desde cero y volver a los datos de demostración.

---

## 6. Estados y enumerados como VARCHAR

**Qué se decidió**: todos los estados y enumerados se guardan como `VARCHAR`, con las longitudes del diccionario: `estado` VARCHAR(30); `etapa_vida`, `tamanio`, `rescate.estado` y `etapa_vida_atendida` VARCHAR(20). En Java siguen siendo enums con `@Enumerated(EnumType.STRING)`, y `@JdbcTypeCode(SqlTypes.VARCHAR)` indica el tipo de columna.

**Por qué**: Hibernate 6 crea columnas `ENUM` de MySQL por defecto, y el diccionario de datos del TFG define VARCHAR.

**Qué problema resuelve**: la base coincide con el diccionario. Agregar un valor nuevo a un estado no requiere un `ALTER TABLE`.

**Qué alternativa se descartó**: el tipo `ENUM` de MySQL. Valida los valores en la base, pero obliga a modificar la tabla por cada valor nuevo. La validación ya la hace Java: un valor inexistente se rechaza con un 400 que lista los valores aceptados.

**Por qué es adecuada para un proyecto individual**: mantiene el control de valores en un solo lugar (los enums de Java).

---

## 7. Baja lógica en lugar de borrado

**Qué se decidió**: ningún registro se borra. Cada tabla tiene un campo `activo`, y "dar de baja" lo pone en falso. Los listados muestran solo registros activos. El **historial del animal** (HU-014) es la única consulta que muestra también los registros dados de baja, marcados como tales. En `usuario` la baja es el estado `BAJA`; en `asignacion_castracion`, el estado `CANCELADA`.

**Por qué**: el historial de un animal es información que las organizaciones necesitan conservar, aunque un registro se haya cargado mal o deje de estar vigente.

**Qué problema resuelve**: nada se pierde por un error de carga, y siempre se puede reconstruir qué pasó con cada animal.

**Qué alternativa se descartó**: el borrado físico. Es más simple, pero pierde historia y puede dejar registros huérfanos.

**Por qué es adecuada para un proyecto individual**: es un campo por tabla y un filtro en los listados (métodos `...ActivoTrue` de los repositorios), sin lógica adicional.

---

## 8. Vacunación y castración como historial

**Qué se decidió**: `vacunacion` y `castracion` son tablas de historial, con una fila por evento y su fecha, en lugar de un campo booleano en `animal`.

**Por qué**: un animal puede recibir varias vacunas a lo largo del tiempo, y un booleano perdería esa información.

**Qué problema resuelve**: permite responder qué vacunas tiene el animal y cuándo se las dieron. Además, la alerta de castración (RN-05) puede verificar si existe una castración registrada.

**Qué alternativa se descartó**: un campo `vacunado` o `castrado` en `animal`.

**Por qué es adecuada para un proyecto individual**: cuesta una tabla más, sin lógica adicional, a cambio de conservar información valiosa.

---

## 9. Ciclo del animal y transiciones automáticas de estado

**Qué se decidió**: los estados del animal son `RESCATADO`, `EN_TRANSITO`, `DERIVADO`, `EN_ADOPCION` y `ADOPTADO`. Cada evento cambia el estado automáticamente:

| Evento | Efecto |
|---|---|
| Rescate (ingreso) | El animal pasa a `RESCATADO` y el rescate empieza `EN_CURSO` |
| Tránsito sin fecha de fin | El animal pasa a `EN_TRANSITO` |
| Derivación | El animal pasa a `DERIVADO` y a la organización destino; el rescate en curso pasa a `DERIVADO`; el tránsito en curso se cierra con la fecha de la derivación |
| Adopción | El animal pasa a `ADOPTADO`; el tránsito en curso se cierra con la fecha de adopción |
| Baja de una adopción | El animal vuelve a `EN_ADOPCION` |

La derivación es un registro directo, sin solicitud ni aceptación, porque el relevamiento no describe un proceso formal de aprobación.

**Por qué**: el estado del animal tiene que coincidir siempre con su historial.

**Qué problema resuelve**: evita animales "adoptados" sin una adopción registrada, o tránsitos que siguen ocupando lugar en un espacio después de que el animal se fue.

**Qué alternativa se descartó**: dejar el estado como un campo de edición libre, porque permitiría estados contradictorios con el historial.

**Por qué es adecuada para un proyecto individual**: la lógica está concentrada en los servicios de Rescate, Tránsito, Derivación y Adopción, fácil de ubicar y explicar. Las operaciones que modifican varias tablas se ejecutan en una transacción (`@Transactional`): si una parte falla, no queda nada a medias.

---

## 10. Reglas de negocio del TFG

| Regla | Implementación |
|---|---|
| RN-01: capacidad de los espacios de tránsito | Cada `espacio_transito` tiene una `capacidad` (Narices Frías: 50; Hogar San Roque: 400). Si los tránsitos activos (sin fecha de fin) llegan al tope, el ingreso se rechaza. Un espacio `INACTIVO` no recibe ingresos. |
| RN-02: cachorros que se alimentan solos | Al registrar el ingreso de un cachorro hay que confirmar que ya puede alimentarse solo; si no se confirma, no se registra. Se exige tanto en el ingreso por Rescates como en un tránsito nuevo (campo `confirmaAlimentacionAutonoma` del pedido); el backend rechaza el ingreso sin la confirmación aunque la pantalla no la envíe. |
| RN-03: etapa de vida por organización | Los cachorros corresponden a Narices Frías y los adultos al Hogar San Roque (`etapa_vida_atendida`). Si no corresponde, el sistema avisa que el animal debe derivarse (en Rescates, Animales e Historial), pero no bloquea el ingreso, porque puede no haber lugar en la otra organización. En Derivaciones, al elegir el animal se propone como destino la organización que atiende su etapa de vida y se muestra el aviso; si se elige otra, aparece una advertencia, pero la derivación se puede registrar igual. |
| RN-04: requisitos del adoptante | Mayor de edad, responsabilidad y espacio cerrado. Si falta alguno, la adopción se rechaza indicando cuáles faltan. |
| RN-05 / HU-016: plazo de castración | `fecha_limite_castracion` = fecha de adopción + 6 meses (sección 11). |
| RN-07: datos del adoptante | El adoptante pertenece a la organización que lo registra. La otra organización ve la adopción, pero no los datos personales. Sin consentimiento no se registran los datos, y del documento se guarda solo el número. |
| HU-014: historial del animal | Todos los eventos del animal en una sola vista, en orden cronológico, incluidos los registros dados de baja. |

**Espacios de tránsito**: cada organización da de alta y edita sus espacios desde la pantalla Tránsitos. Cada espacio se muestra en una tarjeta con los ocupados, la capacidad y los lugares disponibles; la tarjeta se ve en rojo cuando el espacio está completo. La capacidad no puede quedar por debajo de la ocupación actual.

**Búsqueda de animales**: `GET /api/animales` acepta `?nombre=` (contiene, sin distinguir mayúsculas) y `?estado=` (uno de los estados del animal; un valor inexistente devuelve 400 con los valores aceptados), combinables con `?organizacionId=`. Cada animal informa `alertaCastracionVencida`, que el listado muestra como etiqueta.

**Ingreso en un solo paso**: el formulario de rescate carga en una sola operación transaccional los datos del animal, el rescate y, opcionalmente, el tránsito. Si el espacio está lleno (RN-01), no queda un animal registrado a medias.

---

## 11. Alertas de castración vencida (RN-05 / HU-016)

**Qué se decidió**: una tarea programada con `@Scheduled` revisa todos los días a las 06:00 las adopciones que superaron `fecha_limite_castracion` sin una castración registrada, y las marca con `alerta_castracion_vencida`. La tarea también corre al arrancar el backend. La alerta se recalcula en el momento al registrar o dar de baja una castración y al crear o editar una adopción. La página de inicio muestra las alertas de la organización.

**Por qué**: el TFG pide una revisión diaria, y la marca guardada permite mostrar las alertas sin recalcular todo en cada consulta.

**Qué problema resuelve**: la organización ya no depende de acordarse de cada adopción: el sistema le avisa qué castraciones están vencidas.

**Qué alternativa se descartó**: una tabla de alertas separada. Sería más genérica, pero suma una entidad que el TFG no define. Para un solo tipo de alerta alcanza con un campo en la adopción.

**Por qué es adecuada para un proyecto individual**: `@Scheduled` viene incluido en Spring y no requiere infraestructura adicional.

---

## 12. Campañas de castración con cupos

**Qué se decidió**: `campania_castracion` (organización, nombre, fecha, lugar, `cantidad_cupos`, descripción y estado `PROGRAMADA`, `ACTIVA` o `FINALIZADA`) y `asignacion_castracion` (campaña, animal, fecha de asignación, estado `ASIGNADA`, `REALIZADA` o `CANCELADA`, y observaciones), con una restricción única (`id_campania`, `id_animal`). Reglas:

- No se puede asignar un animal si los cupos usados (asignaciones no canceladas) llegaron al máximo.
- No se asigna un animal que ya tiene una castración registrada, ni se asigna dos veces en la misma campaña.
- Cancelar una asignación libera el cupo.
- Marcar una asignación como realizada registra la castración del animal, lo que además apaga su alerta de castración vencida.
- La cantidad de cupos no puede quedar por debajo de los ya asignados.

Al asignar, la fila de la campaña se bloquea durante la transacción (`PESSIMISTIC_WRITE`). Así, dos asignaciones simultáneas no pueden ocupar el mismo último cupo.

**Por qué**: el objetivo específico 3 del TFG pide controlar los cupos e impedir asignaciones que superen el máximo definido.

**Qué problema resuelve**: el control de cupos deja de depender de una planilla aparte, y la castración realizada queda en el historial del animal.

**Qué alternativa se descartó**: contar los cupos sin bloquear la fila. Funciona con un solo usuario a la vez, pero dos asignaciones simultáneas podrían superar el máximo.

**Por qué es adecuada para un proyecto individual**: el bloqueo es una anotación en un método del repositorio, sin infraestructura extra.

---

## 13. Seguridad (capítulo 14 del TFG)

**Qué se decidió**:

- **Contraseñas con BCrypt**, mediante la librería `spring-security-crypto`, que incluye sal y factor de trabajo. Requisitos de la contraseña: al menos 8 caracteres, una mayúscula, una minúscula y un número. Se validan en el backend y en la pantalla.
- **Sesión del servidor** (`HttpSession`, cookie `JSESSIONID` con `HttpOnly`) que vence a los **30 minutos** sin actividad. Un interceptor verifica la sesión en cada pedido a la API.
- **Bloqueo** del usuario después de **5 intentos fallidos** consecutivos (estado `BLOQUEADO`).
- **Contraseña provisoria con cambio obligatorio**: toda contraseña que asigna otra persona (alta o desbloqueo) es provisoria, y hasta cambiarla la API solo permite cambiarla o salir. La contraseña también vence a los **180 días**. Los dos usuarios de demostración se cargan en `schema.sql` con la contraseña ya definida (`cambio_obligatorio = 0` y fecha de cambio del día de la carga) para que la demostración empiece directamente en el inicio; la regla se muestra dando de alta un usuario nuevo desde Usuarios.
- **Permisos entre organizaciones**: cada usuario registra y modifica solo la información de los animales a cargo de su organización (y de sus adopciones, adoptantes, seguimientos, espacios y campañas) y los datos de su propia organización: editar otra organización devuelve 403. Cualquier usuario puede dar de alta una organización nueva. De los animales de la otra organización solo puede consultar el historial. Cualquier intento de escritura sobre datos ajenos devuelve 403 con un mensaje claro. El alta, el desbloqueo y la baja de usuarios los hace solo otro usuario de la misma organización. La regla está centralizada en `ControlAcceso`.
- **CORS restringido** a los orígenes del frontend (`app.cors.origenes`).
- **Errores sin detalles internos**: las respuestas de error nunca incluyen el stack trace del servidor.

**Por qué**: son los requisitos de seguridad que define el capítulo 14 del TFG.

**Qué problema resuelve**: protege las contraseñas guardadas, limita los intentos de adivinarlas, evita que una sesión abierta quede disponible indefinidamente y garantiza que cada organización solo modifique sus propios datos.

**Qué alternativa se descartó**: Spring Security completo, o tokens JWT. Son más potentes, pero agregan una configuración extensa que este prototipo no necesita. La sesión estándar del servidor ya viene en Spring Web, y de Spring Security se usa solo la librería de hashing.

**Por qué es adecuada para un proyecto individual**: cada regla está en un lugar concreto del código (`AutenticacionService`, `SesionInterceptor`, `UsuarioService`, `ControlAcceso`, `PoliticaContrasena`, `GeneradorHashContrasena`) y se puede mostrar en la defensa desde la pantalla de Usuarios.

---

## 14. Verificación

**Qué se decidió**: verificar en dos niveles.

1. **API:** scripts con `curl` que recorren todas las reglas contra MySQL real, incluidos los casos de error: contraseñas débiles, bloqueo, vencimiento a los 180 días, permisos entre organizaciones (403), capacidad de los espacios, requisitos de adopción, cupos de campañas, la tarea programada y la reversión de transacciones.
2. **Interfaz:** el caso completo rescate → tránsito → derivación → adopción → seguimiento → alerta → historial, recorrido en el navegador. También se probaron las campañas, los usuarios, las organizaciones, los espacios de tránsito, la búsqueda de animales y el vencimiento de la sesión (bajando el tiempo a 1 minuto para la prueba).

Todos los textos visibles y los mensajes de error del backend están en castellano con tildes y eñes. Los nombres de clases, campos, columnas y endpoints no llevan tildes.

Además, la eliminación de comentarios del código se verificó comparando el bytecode antes y después: las clases resultaron idénticas, salvo los números de línea.

**Por qué**: cada regla del TFG tiene que poder demostrarse con evidencia concreta.

**Qué problema resuelve**: da confianza de que las reglas funcionan de punta a punta, no solo en el código.

**Qué alternativa se descartó**: pruebas unitarias automatizadas con JUnit integradas al build. Serían el siguiente paso natural (ver Limitaciones).

**Por qué es adecuada para un proyecto individual**: los scripts de prueba se escriben rápido y prueban el sistema tal como lo usa el frontend.

---

## 15. Entorno de desarrollo

**Qué se decidió**: en la computadora de desarrollo, Maven y MySQL se instalaron desde los archivos `.zip`, sin instalador y sin registrar MySQL como servicio de Windows. Los scripts `scripts/start-mysql.ps1` y `scripts/stop-mysql.ps1` levantan y detienen MySQL. Para instalar en otra computadora, `INSTRUCTIVO.md` usa los instaladores oficiales.

**Por qué**: la modalidad `.zip` no necesita permisos de administrador ni asistentes interactivos.

**Qué problema resuelve**: un entorno local completo y reproducible.

**Qué alternativa se descartó**: un contenedor Docker con MySQL, porque agrega una herramienta más para instalar y explicar.

**Por qué es adecuada para un proyecto individual**: depende solo de los programas descargados y es fácil de documentar.

---

## Limitaciones conocidas y trabajo futuro

- No hay pruebas automatizadas con JUnit; la verificación se hizo con scripts `curl` y pruebas en el navegador.
- Las sesiones viven en la memoria del servidor: reiniciar el backend cierra todas las sesiones.
- El sistema funciona sobre HTTP local. En un despliegue real haría falta HTTPS.
- Los listados de consulta muestran registros de ambas organizaciones; la restricción entre organizaciones se aplica a la escritura, tal como lo define el capítulo 14.
- `application.properties` incluye las credenciales del usuario de base de datos de demostración. En un despliegue real se tomarían de variables de entorno.
- Donaciones: fuera del alcance por falta de detalle en el relevamiento.
