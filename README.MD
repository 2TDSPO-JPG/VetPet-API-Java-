# JavaSPG - Sistema de Gestão Veterinária

## Sobre o Projeto

O JavaSPG é uma API REST desenvolvida em Java utilizando Spring Boot, focada no gerenciamento de tutores, pets e exames veterinários.

O projeto foi construído utilizando boas práticas de desenvolvimento backend, arquitetura em camadas, DTOs, JPA/Hibernate e integração com Oracle Database.

---

# Tecnologias Utilizadas

## Backend

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Hibernate
- Maven
- Lombok
- Oracle Database
- Swagger/OpenAPI

## Ferramentas

- IntelliJ IDEA
- Postman
- Oracle SQL Developer
- Git e GitHub

---

# Arquitetura do Projeto

O sistema segue arquitetura em camadas:

```text
Controller
   ↓
Service
   ↓
Repository
   ↓
Banco de Dados
```

# Estrutura de Pastas
```
  src/main/java/com/challenger/sprint/javaspg
  │
  ├── calcula
  ├── controller
  ├── dto
  │   ├── function
  │   ├── mapper
  │   └── requeste
  ├── entity
  ├── enuns
  ├── exception
  │   ├── exceptions
  │   └── handler
  ├── repository
  ├── service
  └── JavaspgApplication.java
```

# Funcionalidades

- Cadastro de tutores
-  Login de usuários
-  Cadastro de pets
-  Associação entre tutor e pet
-  Cadastro de exames
-  Busca de exames por:
   - Status
   - Data
   - Profissional
   - Código do pet
   - Email do tutor
- Paginação de resultados
- Tratamento global de exceções
- Documentação automática com Swagger

# Configuração do Ambiente

 ### Requisitos
- Java 21
- Maven
- Oracle Database
- IDE Java


# Configuração do application.properties

``` 

spring.application.name=javaspg

spring.datasource.url=jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL
spring.datasource.username=${USER_DB_ORACLE:SEU_USUARIO}
spring.datasource.password=${PASSWORD_DB_ORACLE:SUA_SENHA}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.OracleDialect

```

# Como Executar o Projeto

## Clonar o repositório

```
bash git clone  
```

## Entra na pasta do projeto

```
cd javaspg
```

## Instalar dependências

```
mvn clean install
```

## Executar a aplicação

```
mvn spring-boot:run
```

## Swagger UI

Acesse a documentação da API em: `http://localhost:8080/swagger-ui/index.html`

# Entidade do sistema

## Tutor
Representa o responsável pelo pet

### Principais atributos:
- id: Identificador único do tutor
- nome: Nome do tutor
- cpf: CPF do tutor
- email: Email do tutor
- telefone: Telefone de contato do tutor
- endereco: Endereço do tutor
- credenciais: Senha do tutor para autenticação

---

## Pet
Representa o animal de estimação do tutor

### Principais atributos:
- id: Identificador único do pet
- nome: Nome do pet
- raça: Raça do pet
- dataNascimento: Data de nascimento do pet
- petCode: Código único do pet para identificação

---

## Exame
Representa um exame veterinário realizado no pet

### Principais atributos:
- id: Identificador único do exame
- dataExame: Data em que o exame foi realizado
- status: Status do exame (PENDENTE, CONCLUIDO, CANCELADO)
- profissionalResponsavel: Nome do profissional responsável pelo exame
- pet: Referência ao pet associado ao exame

---

## Endpoints Principais

# JavaSPG - Sistema de Gestão Veterinária

## Sobre o Projeto

O JavaSPG é uma API REST desenvolvida em Java utilizando Spring Boot, focada no gerenciamento de tutores, pets e exames veterinários.

O projeto foi construído utilizando boas práticas de desenvolvimento backend, arquitetura em camadas, DTOs, JPA/Hibernate e integração com Oracle Database.

---

# Tecnologias Utilizadas

## Backend

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Hibernate
- Maven
- Lombok
- Oracle Database
- Swagger/OpenAPI

## Ferramentas

- IntelliJ IDEA
- Postman
- Oracle SQL Developer
- Git e GitHub

---

# Arquitetura do Projeto

O sistema segue arquitetura em camadas:

```text
Controller
   ↓
Service
   ↓
Repository
   ↓
Banco de Dados
```

---

# Estrutura de Pastas

```text
src/main/java/com/challenger/sprint/javaspg
│
├── calcula
├── controller
├── dto
│   ├── function
│   ├── mapper
│   └── requeste
├── entity
├── enuns
├── exception
│   ├── exceptions
│   └── handler
├── repository
├── service
└── JavaspgApplication.java
```

---

# Funcionalidades

- Cadastro de tutores
- Login de usuários
- Cadastro de pets
- Associação entre tutor e pet
- Cadastro de exames
- Busca de exames por:
    - Status
    - Data
    - Profissional
    - Código do pet
    - Email do tutor
- Paginação de resultados
- Tratamento global de exceções
- Documentação automática com Swagger

---

# Configuração do Ambiente

## Requisitos

- Java 21
- Maven
- Oracle Database
- IDE Java

---

# Configuração do application.properties

```properties
spring.application.name=javaspg

spring.datasource.url=jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL
spring.datasource.username=${USER_DB_ORACLE:SEU_USUARIO}
spring.datasource.password=${PASSWORD_DB_ORACLE:SUA_SENHA}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.OracleDialect
```

---

# Como Executar o Projeto

## Clonar repositório

```bash
git clone URL_DO_REPOSITORIO
```

## Entrar na pasta

```bash
cd javaspg
```

## Instalar dependências

```bash
mvn clean install
```

## Executar aplicação

```bash
mvn spring-boot:run
```

---

# Swagger

Após iniciar a aplicação:

```text
http://localhost:8080/swagger-ui/index.html
```

---

# Entidades do Sistema

## Tutor

Representa o responsável pelo pet.

### Principais atributos

- id
- nome
- cpf
- telefone
- email
- endereço
- credenciais

---

## Pet

Representa o animal cadastrado no sistema.

### Principais atributos

- id
- nome
- raça
- dataNascimento
- petCode

---

## Exames

Representa exames veterinários realizados.

### Principais atributos

- id
- dataExame
- status
- nomeDoProfissional
- pet

---

# Endpoints Principais

## TutorController

### Cadastro de tutor

```http
POST /tutores
```

### Login

```http
POST /tutores/login
```

### Buscar tutor por CPF

```http
GET /tutores/buscar-por-cpf?cpf=
```

### Buscar tutor por telefone

```http
GET /tutores/buscar-por-telefone?telefone=
```

### Alterar senha

```http
PUT /tutores/alterar-senha/{email}-{novaSenha}
```

---

## PetController

### Criar pet

```http
POST /pets/cadastrar/pet
```

### Buscar pet por código

```http
GET /pets/buscar/pet-code-?petCode=
```

### Afiliar pet ao tutor

```http
POST /pets/afiliar-pet
```

### Atualizar pet

```http
PUT /pets/atualizar-pet
```

---

## ExamesController

### Criar exame

```http
POST /exames/cadastrar
```

### Buscar exames por data

```http
GET /exames/data-exame?dataExame=
```

### Buscar exames por status

```http
GET /exames/status?status=
```

### Buscar exames por profissional

```http
GET /exames/profissional?nomeDoProfissional=
```

---

# Paginação

## Exemplo

```http
GET /exames/pet-code?petCode=ABC123&page=0&size=5
```

---

# Exemplo de Requisições

## Cadastro de Tutor

```json
{
  "nome": "Pedro Ferreira",
  "cpf": "12345678900",
  "telefone": "11999999999",
  "email": "pedro@email.com",
  "senha": "123456",
  "endereco": {
    "logradouro": "Rua Teste",
    "numero": "100",
    "bairro": "Centro",
    "cidade": "São Paulo",
    "estado": "SP"
  }
}
```

---

## Cadastro de Pet

```json
{
  "name": "Negão",
  "dataNascimento": "2026-01-18",
  "raca": "Labrador"
}
```

---

## Cadastro de Exame

```json
{
  "nomeDoProfissional": "Dr. Carlos",
  "status": "PENDENTE",
  "dataExame": "2026-05-14T10:00:00",
  "petCode": "ABC123"
}
```

---

# Tratamento de Exceções

O projeto possui tratamento global de exceções utilizando:

```java
@ControllerAdvice
```

## Exceções personalizadas

- CadastroException
- DadoExistenteException
- EntidadeNaoPersistidaException
- LoginException
- SenhaException

---

# Diferenciais Técnicos

- Arquitetura em camadas
- Uso de DTOs
- Paginação
- Tratamento global de erros
- Integração com Oracle
- Documentação Swagger
- Organização profissional de pacotes
- Separação de responsabilidades
- Uso de boas práticas REST

---

# Melhorias Futuras

## Técnicas

- Spring Security
- JWT Authentication
- Docker
- Testes unitários
- Testes de integração
- CI/CD
- Deploy em Cloud
- Logs estruturados

## Funcionais

- Agenda veterinária
- Histórico clínico
- Vacinação
- Dashboard administrativo
- Upload de exames em PDF
- Notificações por email

---

# Autor

## Pedro Ferreira

Desenvolvedor Backend Java especializado em:

- Java
- Spring Boot
- APIs REST
- Oracle Database
- Hibernate/JPA
- Arquitetura Backend

---

# Licença

Projeto desenvolvido para fins acadêmicos e educacionais.
