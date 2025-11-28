# Script de Execução do Pet Shop
# PowerShell

Write-Host "====================================" -ForegroundColor Cyan
Write-Host "   PET SHOP - Inicializador" -ForegroundColor Cyan
Write-Host "====================================" -ForegroundColor Cyan
Write-Host ""

# Verificar PostgreSQL
Write-Host "[1/3] Verificando PostgreSQL..." -ForegroundColor Yellow
$pgService = Get-Service -Name "postgresql*" -ErrorAction SilentlyContinue
if ($pgService -and $pgService.Status -eq "Running") {
    Write-Host "[OK] PostgreSQL está rodando" -ForegroundColor Green
} else {
    Write-Host "[ERRO] PostgreSQL não está rodando!" -ForegroundColor Red
    Write-Host "Por favor, inicie o PostgreSQL e tente novamente." -ForegroundColor Red
    pause
    exit 1
}

# Verificar/Criar banco de dados
Write-Host ""
Write-Host "[2/3] Verificando banco de dados..." -ForegroundColor Yellow
$dbExists = & psql -U postgres -lqt | Select-String -Pattern "petshop" -Quiet
if (-not $dbExists) {
    Write-Host "Criando banco de dados 'petshop'..." -ForegroundColor Yellow
    & psql -U postgres -c "CREATE DATABASE petshop;"
    if ($LASTEXITCODE -eq 0) {
        Write-Host "[OK] Banco de dados criado" -ForegroundColor Green
    } else {
        Write-Host "[ERRO] Falha ao criar banco de dados" -ForegroundColor Red
        pause
        exit 1
    }
} else {
    Write-Host "[OK] Banco de dados já existe" -ForegroundColor Green
}

# Iniciar aplicação
Write-Host ""
Write-Host "[3/3] Iniciando aplicação..." -ForegroundColor Yellow
Write-Host ""
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "Acesse: http://localhost:8080" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

& .\mvnw.cmd spring-boot:run
