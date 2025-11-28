@echo off
echo ====================================
echo    PET SHOP - Inicializador
echo ====================================
echo.

echo [1/3] Verificando PostgreSQL...
pg_isready -h localhost -p 5432 >nul 2>&1
if errorlevel 1 (
    echo [ERRO] PostgreSQL nao esta rodando!
    echo Por favor, inicie o PostgreSQL e tente novamente.
    pause
    exit /b 1
)
echo [OK] PostgreSQL esta rodando

echo.
echo [2/3] Verificando banco de dados...
psql -U postgres -lqt | find /i "petshop" >nul 2>&1
if errorlevel 1 (
    echo Criando banco de dados 'petshop'...
    psql -U postgres -c "CREATE DATABASE petshop;"
    if errorlevel 1 (
        echo [ERRO] Falha ao criar banco de dados
        pause
        exit /b 1
    )
    echo [OK] Banco de dados criado
) else (
    echo [OK] Banco de dados ja existe
)

echo.
echo [3/3] Iniciando aplicacao...
echo.
call mvnw.cmd spring-boot:run

pause
