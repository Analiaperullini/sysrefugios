param(
    [string]$MysqlHome = $env:MYSQL_HOME,
    [string]$DataDir = $env:MYSQL_DATA
)

if (-not $MysqlHome -or -not $DataDir) {
    Write-Output "Indicar la carpeta de MySQL y la carpeta de datos:"
    Write-Output "  .\start-mysql.ps1 -MysqlHome C:\ruta\mysql-8.4-winx64 -DataDir C:\ruta\mysql-data"
    Write-Output "o definir las variables de entorno MYSQL_HOME y MYSQL_DATA."
    exit 1
}

$yaCorriendo = Get-Process mysqld -ErrorAction SilentlyContinue
if ($yaCorriendo) {
    Write-Output "MySQL ya esta corriendo (PID $($yaCorriendo.Id -join ', '))."
    exit 0
}

Start-Process -FilePath "$MysqlHome\bin\mysqld.exe" `
    -ArgumentList "--basedir=`"$MysqlHome`"", "--datadir=`"$DataDir`"", "--port=3306" `
    -RedirectStandardOutput "$DataDir\stdout.log" -RedirectStandardError "$DataDir\stderr.log" `
    -WindowStyle Hidden

Start-Sleep -Seconds 4

if (Get-NetTCPConnection -LocalPort 3306 -State Listen -ErrorAction SilentlyContinue) {
    Write-Output "MySQL arrancado correctamente en el puerto 3306."
} else {
    Write-Output "No se pudo confirmar que MySQL arranco. Revisar $DataDir\stderr.log"
}
