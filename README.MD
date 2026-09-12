# 🐾 VetPet API

API REST desenvolvida em **Java 21 + Spring Boot**, criada para dar suporte à gestão de uma clínica veterinária. A aplicação centraliza o cadastro de tutores e pets, a associação entre eles e o controle de exames veterinários, oferecendo endpoints REST documentados via Swagger/OpenAPI e autenticação via Spring Security.

Este documento descreve a arquitetura do projeto, suas dependências e, principalmente, **o passo a passo completo para realizar o deploy da aplicação no Microsoft Azure** (App Service + Azure SQL Database).

---

## Sumário

- [O que a aplicação faz](#o-que-a-aplicação-faz)
- [Arquitetura](#arquitetura)
- [Stack e dependências](#stack-e-dependências)
- [Executando localmente](#executando-localmente)
- [Deploy no Azure — passo a passo](#deploy-no-azure--passo-a-passo)
- [Variáveis de ambiente](#variáveis-de-ambiente)
- [Verificando o deploy](#verificando-o-deploy)
- [Encerrando os recursos na Azure](#encerrando-os-recursos-na-azure)
- [Segurança e boas práticas para produção](#segurança-e-boas-práticas-para-produção)

---

## O que a aplicação faz

O **VetPet** é uma API para gestão veterinária que permite:

- **Cadastro e autenticação de tutores**, incluindo busca por CPF/telefone e alteração de senha.
- **Cadastro de pets** e afiliação de um pet a um tutor, com atualização de dados.
- **Cadastro e consulta de exames veterinários**, com filtros por status, data, profissional responsável, código do pet e e-mail do tutor.
- **Paginação** nas listagens de exames.
- **Tratamento global de exceções** (`@ControllerAdvice`) para respostas de erro padronizadas.
- **Documentação automática da API** via Swagger/OpenAPI.
- Autenticação básica das requisições via **Spring Security**.

A aplicação segue uma arquitetura em camadas clássica:

```
Controller → Service → Repository → Banco de Dados
```

com uso de DTOs e mappers para desacoplar as entidades JPA da camada de exposição HTTP.

## Arquitetura

```
src/main/java/com/challenger/sprint/javaspg
│
├── controller        # Endpoints REST (Tutor, Pet, Exames, Veterinario, Prontuario, Perguntas)
├── service            # Regras de negócio
├── repository         # Interfaces Spring Data JPA
├── entity             # Entidades JPA mapeadas para o banco relacional
├── dto                # Objetos de transferência (request/response) e mappers
├── security           # Configuração do Spring Security
├── config             # Configurações gerais (CORS, etc.)
├── exception           # Exceções customizadas e handler global
├── enuns              # Enumeradores do domínio (ex.: Status do exame)
└── JavaspgApplication.java
```

## Stack e dependências

| Categoria           | Tecnologia                                             |
|----------------------|---------------------------------------------------------|
| Linguagem            | Java 21                                                 |
| Framework            | Spring Boot (`spring-boot-starter-parent` 4.0.6)        |
| Camada Web           | Spring Web MVC                                          |
| Persistência         | Spring Data JPA / Hibernate                              |
| Banco de dados        | Microsoft SQL Server (driver `mssql-jdbc` 12.8.1.jre11) — compatível com **Azure SQL Database** |
| Segurança            | Spring Security                                         |
| Validação            | Spring Boot Validation (Jakarta Bean Validation)        |
| Templates            | Thymeleaf (páginas HTML auxiliares)                     |
| Documentação de API   | SpringDoc OpenAPI (Swagger UI)                           |
| Redução de boilerplate | Lombok                                                 |
| Build                | Maven (via Maven Wrapper `mvnw` / `mvnw.cmd`)            |
| Containerização       | Docker (multi-stage build) e Docker Compose             |
| Testes                | Spring Boot Test, Spring Data JPA Test, Spring Web MVC Test |

Essas dependências estão declaradas no `pom.xml` e são resolvidas automaticamente pelo Maven durante o build.

### Pré-requisitos para deploy

- Conta ativa no **Microsoft Azure** com permissão para criar recursos.
- **Azure CLI** instalado e autenticado (`az login`).
- **JDK 21** e **Maven** (ou apenas o Maven Wrapper incluso no projeto) instalados localmente.
- **Git** (opcional, para clonar o repositório).

---

## Executando localmente

```bash
# Clonar o repositório
git clone <URL_DO_REPOSITORIO>
cd VetPet-API-Java-

# Definir as variáveis de ambiente exigidas pelo application.properties
export SPRING_DATASOURCE_URL="jdbc:sqlserver://<host>:1433;databaseName=<db>;encrypt=true;"
export SPRING_DATASOURCE_USERNAME="<usuario>"
export SPRING_DATASOURCE_PASSWORD="<senha>"
export APP_ADMIN_USER="<usuario_admin>"
export APP_ADMIN_PASSWORD="<senha_admin>"

# Build e execução
./mvnw clean package -DskipTests
./mvnw spring-boot:run
```

A API sobe por padrão na porta `8080` e a documentação Swagger fica disponível em:

```
http://localhost:8080/swagger-ui/index.html
```

Também é possível subir a aplicação via Docker:

```bash
docker build -t vetpet-api .
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL="jdbc:sqlserver://<host>:1433;databaseName=<db>;encrypt=true;" \
  -e SPRING_DATASOURCE_USERNAME="<usuario>" \
  -e SPRING_DATASOURCE_PASSWORD="<senha>" \
  -e APP_ADMIN_USER="<usuario_admin>" \
  -e APP_ADMIN_PASSWORD="<senha_admin>" \
  vetpet-api
```

---

## Deploy no Azure — passo a passo

A estratégia de deploy utiliza:

- **Azure App Service (Linux, runtime Java 21)** para hospedar o `.jar` da aplicação.
- **Azure SQL Database** como banco de dados relacional (compatível com o driver `mssql-jdbc` já presente no `pom.xml`).
- **Azure CLI** para provisionar toda a infraestrutura via linha de comando.

> Substitua os nomes de recursos (`rg-javaspg-vetpet`, `sqlserver-vetpet-rm565824`, `webapp-vetpet-rm565824`) pelos identificadores desejados no seu ambiente. Nomes de servidor SQL e de Web App precisam ser **globalmente únicos** na Azure.

### 1. Autenticar na Azure

```bash
az login
```

### 2. Criar o Resource Group

Agrupa todos os recursos do projeto para facilitar gestão e remoção posterior.

```bash
az group create -n rg-javaspg-vetpet -l brazilsouth
```

### 3. Criar o servidor Azure SQL

```bash
az sql server create \
  -n sqlserver-vetpet-rm565824 \
  -g rg-javaspg-vetpet \
  --admin-user fiapadmin \
  --admin-password "<SENHA_FORTE_AQUI>"
```

> ⚠️ Use uma senha forte e única. Não reutilize a mesma senha em múltiplos ambientes.

### 4. Criar o banco de dados

```bash
az sql db create \
  -g rg-javaspg-vetpet \
  --server sqlserver-vetpet-rm565824 \
  -n bd_vetpet \
  --service-objective Basic
```

O tier `Basic` é suficiente para ambientes de teste/demonstração. Para produção, avalie tiers como `Standard` ou `GeneralPurpose`.

### 5. Liberar acesso de serviços Azure ao banco

Libera o firewall do Azure SQL para que o App Service consiga se conectar.

```bash
az sql server firewall-rule create \
  -g rg-javaspg-vetpet \
  --server sqlserver-vetpet-rm565824 \
  -n AllowAzureServices \
  --start-ip-address 0.0.0.0 \
  --end-ip-address 0.0.0.0
```

> A faixa `0.0.0.0-0.0.0.0` é a regra especial reconhecida pela Azure para liberar recursos internos do Azure. Evite liberar a faixa completa `0.0.0.0-255.255.255.255`, pois isso abre o banco para qualquer IP da internet.

### 6. Criar o App Service Plan

```bash
az appservice plan create \
  -n plan-javaspg-vetpet \
  -g rg-javaspg-vetpet \
  --is-linux \
  --sku F1
```

O SKU `F1` (Free) é adequado para demonstrações. Para cargas reais, utilize no mínimo `B1` (Basic) ou superior.

### 7. Criar o Web App com runtime Java 21

```bash
az webapp create \
  -g rg-javaspg-vetpet \
  --plan plan-javaspg-vetpet \
  -n webapp-vetpet-rm565824 \
  --runtime "JAVA:21-java21"
```

### 8. Configurar a conexão com o Azure SQL

```bash
az webapp config appsettings set \
  -g rg-javaspg-vetpet \
  -n webapp-vetpet-rm565824 \
  --settings \
    "SPRING_DATASOURCE_URL=jdbc:sqlserver://sqlserver-vetpet-rm565824.database.windows.net:1433;databaseName=bd_vetpet;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;" \
    "SPRING_DATASOURCE_USERNAME=fiapadmin" \
    "SPRING_DATASOURCE_PASSWORD=<SENHA_FORTE_AQUI>"
```

### 9. Configurar o usuário do Spring Security

Essas variáveis alimentam `spring.security.user.name` e `spring.security.user.password` no `application.properties`.

```bash
az webapp config appsettings set \
  -g rg-javaspg-vetpet \
  -n webapp-vetpet-rm565824 \
  --settings \
    "APP_ADMIN_USER=adminVetClyo" \
    "APP_ADMIN_PASSWORD=adminVetPet123456"
```

Obs: peço que utilize exatamente essas credenciais para que outros professores utilizem, por favor. 

### 10. Configurar o comando de inicialização (startup)

Define como o App Service deve iniciar o `.jar` do Spring Boot.

```bash
az webapp config set \
  -g rg-javaspg-vetpet \
  -n webapp-vetpet-rm565824 \
  --startup-file "java -jar /home/site/wwwroot/app.jar --server.port=80"
```

### 11. Habilitar logs da aplicação

```bash
az webapp log config \
  -g rg-javaspg-vetpet \
  -n webapp-vetpet-rm565824 \
  --application-logging filesystem \
  --level information
```

### 12. Conferir as variáveis de ambiente configuradas

```bash
az webapp config appsettings list \
  -g rg-javaspg-vetpet \
  -n webapp-vetpet-rm565824 \
  --query "[].name" -o table
```

### 13. Compilar o projeto

**Windows (PowerShell):**

```powershell
.\mvnw.cmd clean package -DskipTests
```

**Linux/macOS:**

```bash
./mvnw clean package -DskipTests
```

### 14. Conferir se o `.jar` foi gerado

**Windows (PowerShell):**

```powershell
Get-ChildItem .\target\*.jar
```

**Linux/macOS:**

```bash
ls target/*.jar
```

### 15. Fazer o deploy do artefato

**Windows (PowerShell):**

```powershell
az webapp deploy `
  -g rg-javaspg-vetpet `
  -n webapp-vetpet-rm565824 `
  --src-path .\target\javaspg-0.0.1-SNAPSHOT.jar `
  --type jar
```

**Linux/macOS:**

```bash
az webapp deploy \
  -g rg-javaspg-vetpet \
  -n webapp-vetpet-rm565824 \
  --src-path ./target/javaspg-0.0.1-SNAPSHOT.jar \
  --type jar
```

---

## Verificando o deploy

### Status da aplicação

```bash
az webapp show \
  -g rg-javaspg-vetpet \
  -n webapp-vetpet-rm565824 \
  --query "{state:state,enabled:enabled,host:defaultHostName}" -o table
```

### Acompanhar logs em tempo real

```bash
az webapp log tail -g rg-javaspg-vetpet -n webapp-vetpet-rm565824
```

### Obter a URL pública da aplicação

```bash
az webapp show \
  -g rg-javaspg-vetpet \
  -n webapp-vetpet-rm565824 \
  --query defaultHostName -o tsv
```

Com a URL retornada, a documentação da API estará disponível em:

```
https://<url-retornada>/swagger-ui/index.html
```

O acesso aos endpoints protegidos deverá usar as credenciais configuradas em `APP_ADMIN_USER` / `APP_ADMIN_PASSWORD` (autenticação básica do Spring Security).

---

## Variáveis de ambiente

A aplicação lê as seguintes variáveis via `application.properties`:

| Variável                     | Descrição                                                            |
|-------------------------------|------------------------------------------------------------------------|
| `SPRING_DATASOURCE_URL`       | URL JDBC de conexão com o Azure SQL Database                          |
| `SPRING_DATASOURCE_USERNAME`  | Usuário administrador do servidor SQL                                  |
| `SPRING_DATASOURCE_PASSWORD`  | Senha do usuário administrador do servidor SQL                         |
| `APP_ADMIN_USER`              | Usuário utilizado na autenticação básica do Spring Security            |
| `APP_ADMIN_PASSWORD`          | Senha utilizada na autenticação básica do Spring Security              |

Nenhuma credencial deve ser versionada em texto puro no repositório — utilize sempre os *App Settings* do Azure App Service (ou um cofre de segredos, como o **Azure Key Vault**) para armazená-las.

---

## Encerrando os recursos na Azure

Para evitar custos após testes/apresentações, remova todo o Resource Group (isso apaga o App Service, o App Service Plan e o Azure SQL Server/Database associados):

```bash
az group delete --name rg-javaspg-vetpet --yes
```

Para confirmar que os recursos foram removidos:

```bash
az group exists --name rg-javaspg-vetpet
```

Resultado esperado:

```
false
```

---

## Segurança e boas práticas para produção

O passo a passo acima é adequado para ambientes de demonstração/estudo. Para um ambiente produtivo, considere:

- Armazenar segredos (senhas, connection strings) no **Azure Key Vault** em vez de *App Settings* em texto puro.
- Restringir a regra de firewall do Azure SQL a IPs específicos, em vez de liberar todos os serviços Azure.
- Utilizar um SKU pago (`B1` ou superior) no App Service Plan, já que o tier `F1` possui limitações de CPU, memória e não garante SLA.
- Ativar HTTPS obrigatório (`az webapp update --https-only true`).
- Substituir a autenticação básica por um mecanismo mais robusto, como **JWT**, conforme já indicado nas melhorias futuras do projeto.
- Configurar um pipeline de **CI/CD** (Azure DevOps ou GitHub Actions) para automatizar build e deploy a cada alteração no repositório.
- Habilitar backups automáticos do Azure SQL Database.

---

## Links 

 - Link do repositório: [Vet Pet](https://github.com/2TDSPO-JPG/VetPet-API-Java-.git)

 - Link do Vídeo : [Youtube](https://youtu.be/TKsj_6xIfmI)

## Autor

**Pedro Ferreira**

## Licença

Projeto desenvolvido para fins acadêmicos e educacionais.