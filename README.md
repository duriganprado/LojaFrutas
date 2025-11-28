# 🐾 Pet Shop - Sistema de Gestão e Adoção

Sistema completo de gerenciamento de Pet Shop com funcionalidades de CRUD de animais, autenticação de usuários e formulários de adoção. Desenvolvido com Spring Boot e design glassmorphism moderno.

## ✨ Características

- 🔐 **Autenticação e Autorização** com Spring Security
- 🐕 **CRUD Completo de Animais** (Criar, Ler, Atualizar, Deletar)
- 👥 **Sistema de Usuários** com roles (USER e ADMIN)
- ❤️ **Formulários de Adoção** com controle de status
- 🎨 **Design Glassmorphism** moderno e responsivo
- 📱 **Interface em Abas** para navegação intuitiva
- 🗄️ **PostgreSQL** como banco de dados
- 🔄 **API RESTful** completa

## 🛠️ Tecnologias Utilizadas

### Backend
- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- Spring Security
- PostgreSQL
- Lombok
- Maven

### Frontend
- HTML5
- CSS3 (Glassmorphism)
- JavaScript (Vanilla)
- Thymeleaf

## 📋 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- **Java JDK 17** ou superior
- **PostgreSQL 12** ou superior
- **Maven 3.8** ou superior

## 🚀 Instalação e Configuração

### 1. Clone o repositório

```bash
cd C:\Project\CrudSpringInacio
```

### 2. Configure o PostgreSQL

Crie o banco de dados PostgreSQL:

```sql
CREATE DATABASE petshop;
```

### 3. Configure as credenciais do banco

Edite o arquivo `src/main/resources/application.properties` se necessário:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/petshop
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### 4. Compile e execute o projeto

```bash
mvn clean install
mvn spring-boot:run
```

Ou use o comando PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

### 5. Acesse a aplicação

Abra seu navegador e acesse:

```
http://localhost:8080
```

## 👤 Usuários Padrão

O sistema cria automaticamente dois usuários para teste:

### Administrador
- **Email:** admin@petshop.com
- **Senha:** admin123
- **Perfil:** ADMIN + USER

### Usuário Teste
- **Email:** joao@email.com
- **Senha:** 123456
- **Perfil:** USER

## 📚 Funcionalidades Detalhadas

### 🔐 Autenticação
- Login e logout seguros
- Registro de novos usuários
- Validação de senhas
- Sessão persistente

### 🐕 Gerenciamento de Animais
- Cadastrar novos animais
- Editar informações
- Excluir registros
- Buscar por nome
- Filtrar por espécie
- Marcar como disponível/adotado
- Campos: nome, espécie, raça, idade, sexo, cor, porte, descrição, vacinado, castrado

### ❤️ Sistema de Adoção
- Visualizar animais disponíveis
- Preencher formulário de adoção
- Acompanhar status (Pendente, Aprovado, Recusado, Cancelado)
- Histórico de formulários

### 📋 Formulário de Adoção
Informações solicitadas:
- Endereço completo
- Tipo de residência (Casa/Apartamento)
- Possui quintal
- Possui outros pets
- Motivo da adoção
- Experiência com pets

## 🎨 Design

O sistema utiliza o conceito de **Glassmorphism** com:

- Efeito de vidro fosco (backdrop-filter)
- Gradientes animados no background
- Transições suaves
- Cards flutuantes
- Interface em abas
- Responsivo para mobile
- Paleta de cores moderna
- Ícones emoji para melhor UX

## 📁 Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/petshop/
│   │   ├── config/
│   │   │   ├── SecurityConfig.java
│   │   │   └── DataInitializer.java
│   │   ├── controller/
│   │   │   ├── HomeController.java
│   │   │   ├── UsuarioController.java
│   │   │   ├── AnimalController.java
│   │   │   └── FormularioAdocaoController.java
│   │   ├── model/
│   │   │   ├── Usuario.java
│   │   │   ├── Animal.java
│   │   │   └── FormularioAdocao.java
│   │   ├── repository/
│   │   │   ├── UsuarioRepository.java
│   │   │   ├── AnimalRepository.java
│   │   │   └── FormularioAdocaoRepository.java
│   │   ├── security/
│   │   │   └── CustomUserDetailsService.java
│   │   ├── service/
│   │   │   ├── UsuarioService.java
│   │   │   ├── AnimalService.java
│   │   │   └── FormularioAdocaoService.java
│   │   └── PetShopApplication.java
│   └── resources/
│       ├── static/
│       │   ├── css/
│       │   │   └── style.css
│       │   └── js/
│       │       ├── login.js
│       │       ├── register.js
│       │       └── dashboard.js
│       ├── templates/
│       │   ├── index.html
│       │   ├── login.html
│       │   ├── register.html
│       │   └── dashboard.html
│       └── application.properties
```

## 🔌 API Endpoints

### Animais
- `GET /api/animals` - Listar todos os animais
- `GET /api/animals/disponiveis` - Listar animais disponíveis
- `GET /api/animals/{id}` - Buscar animal por ID
- `POST /api/animals` - Criar novo animal
- `PUT /api/animals/{id}` - Atualizar animal
- `DELETE /api/animals/{id}` - Deletar animal

### Usuários
- `POST /api/usuarios/register` - Registrar novo usuário
- `GET /api/usuarios/me` - Buscar usuário logado
- `GET /api/usuarios` - Listar todos os usuários
- `GET /api/usuarios/{id}` - Buscar usuário por ID

### Formulários de Adoção
- `GET /api/formularios` - Listar todos os formulários
- `GET /api/formularios/meus` - Listar formulários do usuário logado
- `POST /api/formularios` - Criar novo formulário
- `PATCH /api/formularios/{id}/aprovar` - Aprovar formulário
- `PATCH /api/formularios/{id}/recusar` - Recusar formulário

## 🐛 Troubleshooting

### Erro de conexão com PostgreSQL
Verifique se o PostgreSQL está rodando:
```bash
# Windows
pg_ctl status
```

### Porta 8080 já em uso
Altere a porta no `application.properties`:
```properties
server.port=8081
```

### Lombok não funciona
Certifique-se de ter o Lombok plugin instalado na sua IDE.

## 🤝 Contribuindo

Contribuições são bem-vindas! Sinta-se à vontade para:

1. Fazer um fork do projeto
2. Criar uma branch para sua feature (`git checkout -b feature/NovaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/NovaFeature`)
5. Abrir um Pull Request

## 📄 Licença

Este projeto é livre para uso educacional e pessoal.

## ✨ Funcionalidades Futuras

- [ ] Upload de fotos dos animais
- [ ] Sistema de notificações
- [ ] Dashboard administrativo
- [ ] Relatórios e estatísticas
- [ ] Integração com email
- [ ] Chat em tempo real
- [ ] Sistema de agendamento de visitas
- [ ] Histórico médico dos animais

## 📞 Suporte

Para dúvidas ou problemas, abra uma issue no repositório.

---

Desenvolvido com ❤️ para ajudar animais a encontrarem um lar! 🐾
