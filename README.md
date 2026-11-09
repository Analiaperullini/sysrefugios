# SysRefugios

Prototipo de sistema web de gestión interna para organizaciones de protección
y rescate animal de Jujuy. Desarrollado como parte del Seminario Final de la
Licenciatura en Informática.

Gestiona el ciclo **rescate → tránsito → adopción → seguimiento**, y permite
**derivar animales entre dos organizaciones** cuando una no tiene espacio
disponible. No es un catálogo público de adopción: es una herramienta interna
para el personal de ambas organizaciones.

## Stack tecnológico

- **Frontend**: HTML + CSS + JavaScript (sin frameworks).
- **Backend**: Java 17 + Spring Boot 3.3 (Spring Web + Spring Data JPA).
- **Base de datos**: MySQL 8.4.

## Estructura del proyecto

```
sysrefugios/
├── backend/          Proyecto Maven de Spring Boot (API REST)
├── frontend/         Páginas HTML/CSS/JS que consumen la API
├── database/          Esquema y datos de demostración (schema.sql)
├── scripts/           Scripts para levantar/parar MySQL local
└── docs/              Documentación del Seminario
```

## Requisitos previos

- **Java 17** (o superior).
- **Maven** (o usar un IDE con Maven embebido: IntelliJ, Eclipse/STS, VS Code
  + Java Extension Pack).
- **MySQL 8.x** corriendo en `localhost:3306`.
- **Python 3** (opcional, solo para servir el frontend con
  `python -m http.server`; cualquier otro servidor estático simple sirve
  igual).

## Puesta en marcha

Los pasos completos para Windows (instalación de herramientas, recorrido sugerido y problemas frecuentes) están en [INSTRUCTIVO.md](INSTRUCTIVO.md). Resumen:

1. Clonar el repositorio:

   ```bash
   git clone https://github.com/Analiaperullini/sysrefugios.git
   cd sysrefugios
   ```

2. Cargar la base de datos y los datos de prueba. Desde la carpeta del proyecto, como `root` de MySQL:

   ```bash
   mysql -u root -p --default-character-set=utf8mb4 < database/schema.sql
   ```

   Después, en una sesión de MySQL como `root`, crear el usuario que usa la aplicación:

   ```sql
   CREATE USER IF NOT EXISTS 'sysrefugios'@'localhost' IDENTIFIED BY 'SysRefugios2026';
   GRANT ALL PRIVILEGES ON sysrefugios_db.* TO 'sysrefugios'@'localhost';
   FLUSH PRIVILEGES;
   ```

   Si se usa otra contraseña, hay que cambiarla en `backend/src/main/resources/application.properties`.

3. Levantar el backend (queda en `http://localhost:8080`):

   ```bash
   cd backend
   mvn clean package
   java -jar target/backend-0.0.1-SNAPSHOT.jar
   ```

   Hibernate valida la estructura de la base al arrancar (`ddl-auto=validate`), no la modifica.

4. En otra terminal, servir el frontend:

   ```bash
   cd frontend
   python -m http.server 5500
   ```

5. Abrir `http://localhost:5500` e ingresar con uno de los usuarios de prueba.

## Datos de prueba

`database/schema.sql` crea la base desde cero y carga un escenario de demostración. Volver a ejecutarlo deja los datos como al principio.

| Usuario (email) | Contraseña | Organización |
|---|---|---|
| `demo.narices@sysrefugios.local` | `Demo2026` | Narices Frías (cachorros) |
| `demo.sanroque@sysrefugios.local` | `Demo2026` | Hogar San Roque (adultos) |

El escenario incluye:

- 6 animales en distintos estados: Luna y Milo adoptados, Toto derivado de Narices Frías a Hogar San Roque, Negra y Chispa en tránsito, y Pelusa en adopción.
- Rescates, tránsitos, vacunaciones y una castración asociados a esos animales.
- 3 adoptantes y 2 adopciones, una de ellas con la castración vencida, que aparece como alerta en la pantalla de inicio.
- Seguimientos de las adopciones.
- 2 campañas de castración con cupos asignados; la de Narices Frías tiene un solo cupo libre.

Las fechas se calculan a partir del día en que se carga el script, así que las alertas y los plazos siempre se ven igual.

## Endpoints principales

Todos los endpoints, salvo `login` y `logout`, requieren una sesión iniciada (cookie `JSESSIONID`, vence a los 30 minutos sin actividad). Cada usuario solo puede registrar o modificar datos de los animales a cargo de su organización; sobre los de la otra organización la API responde 403 (puede consultar su historial). Los `DELETE` hacen una baja lógica: el registro deja de aparecer en los listados pero se conserva en el historial del animal.

| Recurso | Endpoints |
|---|---|
| Sesión | `POST /api/login`, `POST /api/logout`, `GET /api/sesion`, `POST /api/cambiar-contrasena` |
| Organizaciones | `GET/POST/PUT/DELETE /api/organizaciones[/{id}]` (`PUT` y `DELETE` solo sobre la propia) |
| Usuarios | `GET/POST/PUT/DELETE /api/usuarios[/{id}]` (solo los de la organización del usuario) |
| Animales | `GET/POST/PUT/DELETE /api/animales[/{id}]` (+ `?organizacionId=`, `?estado=`, `?nombre=`) |
| Historial del animal | `GET /api/animales/{id}/historial` |
| Rescates (ingreso) | `GET/POST/PUT/DELETE /api/rescates[/{id}]` |
| Espacios de tránsito | `GET/POST/PUT/DELETE /api/espacios-transito[/{id}]` (+ `?organizacionId=`) |
| Tránsitos | `GET/POST/PUT/DELETE /api/transitos[/{id}]` (+ `?animalId=`) |
| Derivaciones | `GET/POST/PUT/DELETE /api/derivaciones[/{id}]` (+ `?animalId=`) |
| Adoptantes | `GET/POST/PUT/DELETE /api/adoptantes[/{id}]` (solo los de la organización del usuario) |
| Adopciones | `GET/POST/PUT/DELETE /api/adopciones[/{id}]` (+ `?animalId=`), `GET /api/adopciones/alertas-castracion` |
| Seguimientos | `GET/POST/PUT/DELETE /api/seguimientos[/{id}]` (+ `?adopcionId=`) |
| Vacunaciones | `GET/POST/PUT/DELETE /api/vacunaciones[/{id}]` (+ `?animalId=`) |
| Castraciones | `GET/POST/PUT/DELETE /api/castraciones[/{id}]` (+ `?animalId=`) |
| Campañas de castración | `GET/POST/PUT/DELETE /api/campanias-castracion[/{id}]`, `GET/POST /api/campanias-castracion/{id}/asignaciones`, `PUT /api/asignaciones-castracion/{id}` |

## Documentación

El detalle de cada decisión técnica (qué se decidió, por qué, qué alternativa
se descartó) está en `docs/`.
