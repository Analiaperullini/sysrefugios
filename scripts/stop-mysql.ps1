param(
    [string]$MysqlHome = $env:MYSQL_HOME
)

if (-not $MysqlHome) {
    Write-Output "Indicar la carpeta de MySQL: .\stop-mysql.ps1 -MysqlHome C:\ruta\mysql-8.4-winx64"
    Write-Output "o definir la variable de entorno MYSQL_HOME."
    exit 1
}

Write-Output "Se pide la contrasena del usuario root de MySQL para apagarlo."
& "$MysqlHome\bin\mysqladmin.exe" --user=root --password shutdown
Write-Output "Orden de apagado enviada a MySQL."
