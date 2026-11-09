# SysRefugios

Prototipo de sistema web desarrollado en el marco del Seminario Final de la Licenciatura en Informática. El proyecto surge a partir de un relevamiento realizado con dos organizaciones de protección animal de la provincia de Jujuy, Narices Frías y Hogar San Roque, que trabajan en el rescate, tránsito, derivación, adopción y seguimiento posterior de animales.

El sistema está orientado al uso interno del personal de las organizaciones y no a la publicación de animales en adopción. Su propósito es centralizar la información de cada caso desde el rescate y durante las etapas de tránsito, derivación, adopción y seguimiento, incorporando además el registro de vacunaciones, castraciones y campañas de castración. De esta manera, cualquiera de las dos organizaciones puede consultar los antecedentes de un animal aunque haya pasado por la otra.

## Tecnologías utilizadas

El frontend se desarrolló con HTML, CSS y JavaScript sin frameworks. El backend es una API REST construida con Java 17 y Spring Boot 3.3 (Spring Web y Spring Data JPA), y la información se almacena en una base de datos MySQL 8.4.

El proyecto se organiza en las siguientes carpetas:

```
sysrefugios/
├── backend/     API REST (proyecto Maven de Spring Boot)
├── frontend/    páginas HTML, estilos y scripts que consumen la API
├── database/    script de creación de la base y datos de prueba
├── scripts/     scripts para iniciar y detener MySQL en Windows
└── docs/        decisiones técnicas del prototipo
```

## Requisitos

Para ejecutar el prototipo es necesario contar con Java 17 o superior, Maven (o un IDE que lo incluya, como IntelliJ, Eclipse o VS Code con el Java Extension Pack) y MySQL 8 funcionando en `localhost:3306`. Para servir el frontend se utilizó Python 3, aunque puede reemplazarse por cualquier servidor de archivos estáticos.

## Instalación

Los pasos detallados para Windows, junto con un recorrido sugerido por las pantallas y la solución a problemas frecuentes, se encuentran en [INSTRUCTIVO.md](INSTRUCTIVO.md). A continuación se resume el procedimiento.

En primer lugar se clona el repositorio:

```bash
git clone https://github.com/Analiaperullini/sysrefugios.git
cd sysrefugios
```

Luego se carga la base de datos ejecutando el script como usuario `root` de MySQL. El script crea la base `sysrefugios_db`, sus tablas y los datos de prueba:

```bash
mysql -u root -p --default-character-set=utf8mb4 < database/schema.sql
```

La aplicación no se conecta como `root`, sino con un usuario que solo tiene permisos sobre esa base. Para crearlo, en una sesión de MySQL como `root`:

```sql
CREATE USER IF NOT EXISTS 'sysrefugios'@'localhost' IDENTIFIED BY 'SysRefugios2026';
GRANT ALL PRIVILEGES ON sysrefugios_db.* TO 'sysrefugios'@'localhost';
FLUSH PRIVILEGES;
```

En caso de utilizar otra contraseña, debe modificarse también en `backend/src/main/resources/application.properties`.

A continuación se compila y se inicia el backend, que queda disponible en `http://localhost:8080`. Al arrancar, Hibernate valida que la estructura de la base coincida con el modelo, sin modificarla:

```bash
cd backend
mvn clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

Por último, desde otra terminal se sirve el frontend y se accede a `http://localhost:5500`:

```bash
cd frontend
python -m http.server 5500
```

## Datos de prueba

El script `database/schema.sql` crea la base desde cero e incorpora un escenario de demostración, por lo que volver a ejecutarlo restablece los datos iniciales. Para ingresar se cuenta con un usuario por organización, ambos con la contraseña `Demo2026`:

| Usuario | Organización |
|---|---|
| `demo.narices@sysrefugios.local` | Narices Frías (cachorros) |
| `demo.sanroque@sysrefugios.local` | Hogar San Roque (adultos) |

El escenario incluye seis animales en distintas etapas del proceso. Luna y Milo ya fueron adoptados, Toto ingresó por Narices Frías y fue derivado a Hogar San Roque por ser adulto, Negra y Chispa se encuentran en tránsito y Pelusa está disponible para adopción. Cada uno cuenta con su rescate, sus tránsitos y, según el caso, vacunaciones y castraciones registradas.

Por otra parte, se cargaron tres adoptantes y dos adopciones con sus seguimientos. Una de esas adopciones tiene vencido el plazo de castración, lo que permite observar la alerta que muestra el sistema en la pantalla de inicio. También se incluyen dos campañas de castración con cupos asignados, una de ellas con un único cupo libre. Las fechas se calculan a partir del día en que se ejecuta el script, de modo que las alertas y los plazos se mantienen vigentes en cualquier momento en que se instale.

## API

Todos los endpoints, salvo los de ingreso y salida, requieren una sesión iniciada, que vence después de 30 minutos sin actividad. Cada usuario solo puede registrar o modificar información de los animales a cargo de su organización; sobre los de la otra organización la API responde con el código 403, aunque permite consultar su historial. Las operaciones `DELETE` realizan una baja lógica, por lo que el registro deja de mostrarse en los listados pero se conserva en el historial del animal.

| Recurso | Endpoints |
|---|---|
| Sesión | `POST /api/login`, `POST /api/logout`, `GET /api/sesion`, `POST /api/cambiar-contrasena` |
| Organizaciones | `GET/POST/PUT/DELETE /api/organizaciones[/{id}]` (`PUT` y `DELETE` solo sobre la propia) |
| Usuarios | `GET/POST/PUT/DELETE /api/usuarios[/{id}]` (solo los de la organización del usuario) |
| Animales | `GET/POST/PUT/DELETE /api/animales[/{id}]` (+ `?organizacionId=`, `?estado=`, `?nombre=`) |
| Historial del animal | `GET /api/animales/{id}/historial` |
| Rescates | `GET/POST/PUT/DELETE /api/rescates[/{id}]` |
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

Las decisiones técnicas tomadas durante el desarrollo del prototipo, junto con los motivos que las justifican y las alternativas descartadas, se describen en la carpeta `docs/`.
