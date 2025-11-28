# Instruções para Executar o Projeto Pet Shop

## Passo a Passo Rápido

### 1. Verificar PostgreSQL
Certifique-se de que o PostgreSQL está instalado e rodando:
```powershell
# Verifique o status do serviço
Get-Service postgresql*
```

### 2. Criar o Banco de Dados
Abra o pgAdmin ou psql e execute:
```sql
CREATE DATABASE petshop;
```

Ou via linha de comando:
```powershell
psql -U postgres -c "CREATE DATABASE petshop;"
```

### 3. Ajustar Credenciais (se necessário)
Edite `src/main/resources/application.properties`:
```properties
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### 4. Compilar e Executar

#### Opção 1: Usando Maven (se instalado)
```powershell
cd C:\Project\CrudSpringInacio
mvn clean install
mvn spring-boot:run
```

#### Opção 2: Usando Maven Wrapper (recomendado)
```powershell
cd C:\Project\CrudSpringInacio
.\mvnw.cmd clean install
.\mvnw.cmd spring-boot:run
```

#### Opção 3: Via IDE (Eclipse, IntelliJ, VS Code)
1. Importe o projeto como Maven Project
2. Aguarde o download das dependências
3. Execute a classe `PetShopApplication.java`

### 5. Acessar a Aplicação
Abra o navegador em: **http://localhost:8080**

## Usuários de Teste

### Admin
- Email: `admin@petshop.com`
- Senha: `admin123`

### Usuário
- Email: `joao@email.com`
- Senha: `123456`

## Verificações Importantes

### Se o banco não criar automaticamente:
O Hibernate está configurado para criar as tabelas automaticamente com:
```properties
spring.jpa.hibernate.ddl-auto=update
```

### Se houver erro de Lombok:
Certifique-se de ter o plugin do Lombok instalado na sua IDE.

### Verificar se está rodando:
```powershell
# Verificar processos Java
Get-Process java

# Testar endpoint
curl http://localhost:8080
```

## Estrutura do Banco de Dados

O sistema criará automaticamente:
- Tabela `usuarios`
- Tabela `usuario_roles`
- Tabela `animais`
- Tabela `formularios_adocao`

## Dados Iniciais

O sistema popula automaticamente:
- 2 usuários (admin e teste)
- 6 animais de exemplo
  - Rex (Cachorro Labrador)
  - Mia (Gato Persa)
  - Bob (Cachorro Vira-lata)
  - Nina (Gato Siamês)
  - Thor (Pastor Alemão)
  - Piu (Canário)

## Portas Utilizadas

- **Aplicação:** 8080
- **PostgreSQL:** 5432

Se a porta 8080 estiver ocupada, altere em `application.properties`:
```properties
server.port=8081
```

## Logs

Para visualizar logs detalhados:
```properties
logging.level.com.petshop=DEBUG
```

## Parar a Aplicação

No terminal onde está rodando, pressione: **Ctrl + C**

Ou via PowerShell:
```powershell
Stop-Process -Name java
```

---

🐾 **Boa sorte com o projeto!**
