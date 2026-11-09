# Instructivo de instalación y ejecución en Windows

Este instructivo explica cómo instalar SysRefugios en una computadora con Windows 10 u 11, cargar los datos de demostración y recorrer el sistema. Lleva unos 30 minutos si hay que instalar todas las herramientas.

El sistema tiene tres partes que se levantan por separado:

| Parte | Tecnología | Dónde queda |
|---|---|---|
| Base de datos | MySQL 8 | `localhost:3306` |
| Backend (API) | Java 17 + Spring Boot | `http://localhost:8080` |
| Frontend | HTML, CSS y JavaScript | `http://localhost:5500` |

---

## 1. Herramientas necesarias

Instalar lo que falte. En cada caso, la columna "Verificación" es el comando que hay que escribir en **PowerShell** para confirmar que quedó instalado.

| Herramienta | Versión | Descarga | Verificación |
|---|---|---|---|
| Java (JDK) | 17 o superior | https://adoptium.net (Temurin 17, instalador `.msi`) | `java -version` |
| Maven | 3.9 o superior | https://maven.apache.org/download.cgi | `mvn -v` |
| MySQL Server | 8.0 u 8.4 | https://dev.mysql.com/downloads/installer/ | `mysql --version` |
| Python | 3.10 o superior | https://www.python.org/downloads/ | `python --version` |

Notas:

- **Java:** en el instalador de Temurin, activar la opción *Set JAVA_HOME variable*.
- **Maven:** es un `.zip`. Descomprimirlo, por ejemplo en `C:\herramientas\apache-maven-3.9.9`, y agregar la carpeta `bin` a la variable de entorno `Path` (Inicio → "Editar las variables de entorno del sistema" → Variables de entorno → `Path` → Nuevo). Cerrar y volver a abrir PowerShell.
- **MySQL:** en el instalador elegir *Server only*, dejar el puerto **3306** y anotar la contraseña que se le asigna a `root`. Si `mysql --version` no responde, agregar al `Path` la carpeta `C:\Program Files\MySQL\MySQL Server 8.4\bin` (o la de la versión instalada).
- **Python:** solo se usa para servir el frontend. En el instalador marcar *Add python.exe to PATH*. Si se trabaja con Visual Studio Code, la extensión *Live Server* sirve igual (también usa el puerto 5500).

---

## 2. Base de datos

### 2.1. Crear la base y cargar los datos de demostración

El archivo `database\schema.sql` crea la base `sysrefugios_db`, sus 14 tablas y los datos de demostración.

> **Atención:** el script borra y vuelve a crear todas las tablas. Si se ejecuta sobre una base con datos cargados, esos datos se pierden.

Abrir PowerShell en la carpeta del proyecto y entrar a MySQL como `root`:

```powershell
mysql -u root -p --default-character-set=utf8mb4
```

Dentro de MySQL, ejecutar el script con `SOURCE`, usando la ruta completa con barras `/`:

```sql
SOURCE C:/ruta/al/proyecto/sysrefugios/database/schema.sql;
```

### 2.2. Crear el usuario que usa la aplicación

El backend no se conecta como `root`, sino con un usuario propio que solo tiene permisos sobre `sysrefugios_db`. En la misma sesión de MySQL:

```sql
CREATE USER IF NOT EXISTS 'sysrefugios'@'localhost' IDENTIFIED BY 'SysRefugios2026';
GRANT ALL PRIVILEGES ON sysrefugios_db.* TO 'sysrefugios'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

Si se elige otra contraseña, hay que cambiarla también en `backend\src\main\resources\application.properties` (propiedad `spring.datasource.password`).

### 2.3. Comprobar la carga

```powershell
mysql -u sysrefugios -p sysrefugios_db -e "SELECT nombre, estado FROM animal;"
```

Tienen que aparecer 6 animales: Luna, Toto, Milo, Negra, Pelusa y Chispa.

---

## 3. Backend

Desde la carpeta del proyecto:

```powershell
cd backend
mvn clean package
java -jar target\backend-0.0.1-SNAPSHOT.jar
```

La primera vez Maven descarga dependencias y tarda unos minutos. El backend está listo cuando la consola muestra:

```
Started SysrefugiosApplication in ... seconds
Revisión diaria de castraciones: 1 adopciones con el plazo vencido sin castración registrada
```

La segunda línea confirma que la tarea de alertas de castración (RN-05) corrió al arrancar. Después se repite todos los días a las 06:00.

Dejar esa ventana abierta mientras se usa el sistema. Para detener el backend: `Ctrl + C`.

Alternativa sin generar el `.jar`: `mvn spring-boot:run` desde la carpeta `backend`.

---

## 4. Frontend

En **otra** ventana de PowerShell, desde la carpeta del proyecto:

```powershell
cd frontend
python -m http.server 5500
```

Abrir en el navegador **http://localhost:5500**.

Dos cosas importantes:

- Usar exactamente el puerto **5500** y la dirección `localhost` (o `127.0.0.1`). El backend solo acepta pedidos desde esos orígenes (CORS). Para usar otro puerto hay que agregarlo en `app.cors.origenes` dentro de `application.properties` y reiniciar el backend.
- No abrir `index.html` con doble clic: desde `file://` el navegador no envía la cookie de sesión.

---

## 5. Usuarios de demostración

Se ingresa con el **email** y la contraseña.

| Email | Organización | Contraseña |
|---|---|---|
| `demo.narices@sysrefugios.local` | Narices Frías (cachorros) | `Demo2026` |
| `demo.sanroque@sysrefugios.local` | Hogar San Roque (adultos) | `Demo2026` |

Los usuarios de demostración entran directo, sin cambiar la contraseña. El botón **Mostrar** de cada campo de contraseña permite ver lo que se escribió. Toda contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número.

Reglas de seguridad que se pueden observar durante la demostración:

- 5 intentos fallidos seguidos bloquean al usuario.
- La sesión se cierra sola después de 30 minutos sin actividad.
- La contraseña vence a los 180 días y hay que cambiarla.
- Cada organización solo registra y modifica los datos de sus propios animales; de los animales de la otra organización solo puede consultar el historial.
- En **Usuarios** se dan de alta usuarios de la propia organización con contraseña provisoria, se desbloquean y se dan de baja.
- En **Organizaciones** cada organización edita solo sus propios datos; la otra aparece en el listado, pero sin el botón Editar.

---

## 6. Datos de demostración

Las fechas se calculan a partir del día en que se carga `schema.sql`, así que la situación es la misma cualquier día que se haga la demostración.

| Animal | Organización | Situación |
|---|---|---|
| Luna | Narices Frías | Cachorra adoptada hace unos 8 meses **sin castración registrada: aparece la alerta** en el inicio de Narices Frías |
| Milo | Narices Frías | Cachorro adoptado hace un mes; el plazo de castración todavía no venció |
| Chispa | Narices Frías | Cachorro rescatado hace 10 días, en tránsito |
| Toto | Hogar San Roque | Adulto que ingresó por Narices Frías y **se derivó** al Hogar San Roque; en tránsito |
| Negra | Hogar San Roque | Adulta rescatada hace 25 días, en tránsito |
| Pelusa | Hogar San Roque | Adulta castrada y vacunada, en adopción |

Campañas de castración:

| Campaña | Organización | Cupos |
|---|---|---|
| Campaña de castración Alto Comedero | Narices Frías | 1 / 2 (Chispa asignado) |
| Jornada de castración Palpalá | Hogar San Roque | 2 / 3 (Toto y Negra asignados) |

---

## 7. Recorrido sugerido para la demostración

El menú lateral tiene estas pantallas:

| Pantalla | Para qué sirve |
|---|---|
| Inicio | Resumen de la organización y alertas de castración vencida |
| Animales | Listado con buscador por nombre, filtro por estado y etiquetas de aviso; alta y edición |
| Historial | Ficha del animal con todos sus eventos en orden cronológico (HU-014) |
| Rescates | Ingreso de un animal rescatado: animal, rescate y tránsito en un solo paso |
| Tránsitos | Tarjetas de los espacios (ocupados, capacidad y lugares disponibles), alta y edición de espacios, y tránsitos |
| Derivaciones | Paso de un animal a la otra organización, con la propuesta de destino según su etapa de vida (RN-03) |
| Adoptantes | Datos del adoptante con su consentimiento (RN-07) |
| Adopciones | Adopción con requisitos y compromisos (RN-04) y fecha límite de castración (RN-05) |
| Seguimientos | Seguimientos posteriores a la adopción, con filtro por adopción |
| Vacunaciones y Castraciones | Control sanitario |
| Campañas | Campañas de castración con cupos |
| Usuarios | Usuarios de la propia organización |
| Organizaciones | Listado de organizaciones, alta y edición de los datos propios |

Recorrido:

1. Ingresar con `demo.narices@sysrefugios.local` y `Demo2026` y ver en **Inicio** la alerta de castración vencida de Luna.
2. **Animales:** escribir "lu" en el buscador y elegir el estado "Adoptado". Luna aparece con la etiqueta **Castración vencida**. Hacer clic en su nombre: en el **Historial**, el renglón "ALERTA" de la adopción se ve destacado en rojo, junto con el rescate, el tránsito, la vacunación y los seguimientos.
3. **Organizaciones:** ver las dos organizaciones con la etapa de vida que atiende cada una. Editar el teléfono de Narices Frías. El Hogar San Roque no tiene botón Editar.
4. **Tránsitos → Nuevo espacio:** crear "Casa de Marta" con capacidad 1. Aparece una tarjeta nueva con 0 ocupados y 1 lugar disponible.
5. **Rescates → Registrar ingreso** de un **cachorro** sin elegir espacio: aparece la casilla "Confirmo que el cachorro ya puede alimentarse solo" y sin marcarla no se registra (RN-02).
6. **Tránsitos → Nuevo tránsito** de ese cachorro en "Casa de Marta": el formulario pide la misma confirmación. Con la casilla marcada se registra, y la tarjeta del espacio queda **en rojo** ("Completo", 0 disponibles). Un segundo ingreso en ese espacio se rechaza (RN-01).
7. **Rescates → Registrar ingreso** de un perro **adulto** en el espacio de Narices Frías: aparece el aviso de que corresponde al Hogar San Roque (RN-03).
8. **Derivaciones → Nueva derivación** de ese adulto: el sistema propone como destino al Hogar San Roque y muestra el aviso de RN-03. Si se elige otra organización, aparece una advertencia. Al guardar, su tránsito en Narices Frías se cierra solo y libera el lugar.
9. Salir e ingresar con `demo.sanroque@sysrefugios.local`. En **Tránsitos**, ubicar al animal derivado en el espacio del Hogar San Roque.
10. **Adoptantes:** registrar un adoptante. Sin la casilla de consentimiento, el sistema no lo guarda (RN-07). Los adoptantes de Narices Frías no se ven.
11. **Adopciones:** registrar la adopción. Si falta uno de los tres requisitos, se rechaza (RN-04). La fecha límite de castración se calcula sola.
12. **Seguimientos:** el selector del formulario muestra solo las adopciones del Hogar San Roque. Registrar un seguimiento y usar el filtro "Ver los seguimientos de" para ver solo los de esa adopción. Después, abrir el **Historial** del animal para ver el caso completo entre las dos organizaciones.
13. Como `demo.sanroque`, intentar editar o vacunar a Luna (de Narices Frías): el sistema lo rechaza con un mensaje claro (solo puede consultar su historial).
14. Como `demo.narices`, en **Campañas → Asignaciones** de la campaña Alto Comedero: asignar a Luna (ocupa el último cupo, 2 / 2) y después intentar asignar a Milo: se rechaza por falta de cupos. **Marcar realizada** la castración de Luna (con el nombre del veterinario) registra la castración, y su alerta desaparece del inicio y del listado de animales.
15. **Usuarios:** dar de alta un usuario con su email y una contraseña provisoria. Al ingresar con ese usuario, el sistema le pide cambiarla. Para mostrar el bloqueo, intentar ingresar con ese usuario y una contraseña incorrecta 5 veces; después, desde **Usuarios**, desbloquearlo asignándole una contraseña provisoria nueva.

---

## 8. Volver a los datos de demostración

Repetir el paso 2.1 (`SOURCE .../schema.sql;`). No hace falta volver a crear el usuario `sysrefugios`. Los usuarios de demostración vuelven a tener la contraseña `Demo2026`. Si el navegador tenía una sesión abierta, vuelve al login con el aviso de sesión vencida.

---

## 9. Problemas frecuentes

| Síntoma | Causa y solución |
|---|---|
| El backend no arranca y menciona `Schema-validation` | La base no tiene las tablas esperadas. Cargar `database\schema.sql` (paso 2.1). El backend valida la estructura, no la crea. |
| `Access denied for user 'sysrefugios'` | Falta el paso 2.2, o la contraseña no coincide con `application.properties`. |
| `Public Key Retrieval is not allowed` | Ya está contemplado en la URL de conexión (`allowPublicKeyRetrieval=true`). Si se cambió la URL, volver a agregarlo. |
| `Port 8080 was already in use` | Hay otro backend abierto. Cerrar esa ventana o buscar el proceso con `Get-NetTCPConnection -LocalPort 8080`. |
| La pantalla dice "No se pudo conectar con el servidor" | El backend no está corriendo (paso 3). |
| Después de ingresar, vuelve al login con "La sesión se cerró por 30 minutos sin actividad" | El backend se reinició o pasaron 30 minutos sin uso. Volver a ingresar. |
| El frontend muestra una versión vieja de una página | El navegador guardó la versión anterior. Recargar con `Ctrl + F5`. |
| Un usuario quedó bloqueado por intentos fallidos | Otro usuario de su organización lo desbloquea desde **Usuarios → Desbloquear**, asignándole una contraseña provisoria nueva. Si no hay otro usuario disponible, se puede volver a cargar `schema.sql` (paso 8). |
| "... está a cargo de ...: solo esa organización puede registrar o modificar su información" | Es la regla de permisos entre organizaciones: ingresar con un usuario de la organización que tiene al animal. |

---

## 10. En la computadora de desarrollo

En la computadora donde se desarrolló el proyecto, MySQL está instalado sin instalador (archivo `.zip`) y no corre como servicio de Windows. Para levantarlo y detenerlo, indicando la carpeta de MySQL y la de datos (o definiendo las variables de entorno `MYSQL_HOME` y `MYSQL_DATA`):

```powershell
.\scripts\start-mysql.ps1 -MysqlHome C:\ruta\mysql-8.4-winx64 -DataDir C:\ruta\mysql-data
.\scripts\stop-mysql.ps1 -MysqlHome C:\ruta\mysql-8.4-winx64
```

`stop-mysql.ps1` pide la contraseña de `root` de MySQL.

En una instalación con el instalador oficial (paso 1), MySQL queda como servicio de Windows y arranca solo: esos scripts no hacen falta.
