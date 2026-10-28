# CLYVO VET — API Java — Sprint 4

API Spring Boot 4 / Java 21+, mantida pela VitalPet Tech (Gabriel Muller RM561995, Jonas Santos RM563234 e Pedro Ferreira RM565824). Atende o aplicativo React Native com cadastros, pets, formulários, agenda, prontuários e integração com a IA Python.

## Executar localmente

Instalar JDK 21 ou superior. Usar `iniciar_backend_local.bat` ou:

```powershell
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local
```

O perfil local usa H2 persistente no diretório `runtime/clyvo-v4`, separado do SQL Server. A base começa sem cadastro de pacientes. Criar tutor/veterinário pela interface. As credenciais administrativas locais são configuráveis por `CLYVO_LOCAL_ADMIN_EMAIL`/`CLYVO_LOCAL_ADMIN_PASSWORD` em `.env.local`; não usar os padrões locais em hospedagem pública.

Copiar `.env.example` para `.env.local` e manter `IOT_API_URL=http://127.0.0.1:8000`. Definir `IOT_INTERNAL_TOKEN` igual no Java e no Python. O perfil local importa `.env.local` como properties; outras implantações devem definir as variáveis no processo de execução. Não versionar credenciais.

Swagger: `http://localhost:8080/swagger-ui/index.html`. O app físico usa o IP do computador na mesma rede, porta 8080. O Java encaminha análises ao Python na porta 8000; a chave Gemini fica no Python.

## Perfis e segurança

O perfil padrão usa SQL Server com `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` e `SPRING_DATASOURCE_PASSWORD`. Administrador por `APP_ADMIN_USER`/`APP_ADMIN_PASSWORD`. Configure essas variáveis antes de iniciar sem o perfil local. O ambiente SQL Server hospedado não foi testado nesta preparação.

Login tutor/veterinário/administrador retorna um token opaco Bearer, que deve ser enviado em `Authorization`. Sessões expiram em oito horas e são revogadas no logout. As sessões ficam em memória: reiniciar exige novo login; usar uma instância na implantação atual. Senhas novas e migradas no login usam BCrypt. O tutor só acessa seus dados, pets e prontuários. A consulta de horários ocupados não revela os dados do outro tutor.

Recuperação de senha: código de seis dígitos, expiração, limite de tentativas e envio por SMTP. Configure `MAIL_HOST`, `MAIL_USERNAME`, `MAIL_PASSWORD`, `MAIL_FROM` e as opções do `.env.example`. Sem SMTP o endpoint retorna 503 e não afirma envio. Registro de veterinários é permitido pelo fluxo acadêmico existente; uma implantação operacional exige aprovação de profissionais pela clínica.

## Rotas do aplicativo

| Fluxo | Rotas principais |
| --- | --- |
| Conta | POST `/tutores`, `/veterinarios`, `/tutores/login`, `/veterinarios/login`, `/auth/admin/login`, `/auth/sair` |
| Recuperação | POST `/tutores/recuperar-senha`, `/tutores/confirmar-recuperacao` |
| Pets | POST `/pets/cadastrar/pet`, PUT `/pets/atualizar-pet`, GET `/pets/buscar-por-tutor?email=...` |
| Formulário | GET `/perguntas/pet/{id}`, POST `/perguntas`, PUT `/perguntas/{id}` |
| Agenda | POST `/exames/cadastrar`, GET `/exames/veterinario/{id}/data?data=AAAA-MM-DD`, PUT `/exames/{id}/cancelar` |
| Prontuário | POST `/prontuarios`, GET `/prontuarios/exame/{id}` |
| IA | GET `/cuidados/questionario`, POST `/pets/{id}/cuidados` |

O gateway de IA verifica o vínculo com o pet e encaminha um formulário canônico ao Python, com `X-Internal-Token` apenas entre servidores. Erros de integração geram resposta explícita; nenhum alerta fictício é usado para esconder indisponibilidade.

## Verificação

```powershell
.\mvnw.cmd test
.\mvnw.cmd package
```

Três testes aprovados: inicialização isolada, fluxo HTTP completo e recuperação de senha. O fluxo HTTP cobre cadastro, login, pet/formulário, agenda com conflito 409, cancelamento, prontuário e negação entre contas. Os testes usam H2 em memória, sem dados clínicos reais. O envio SMTP é validado com um emissor controlado em teste; a entrega real de e-mail exige provedor configurado.

A integração visual com o aplicativo e o Python foi verificada separadamente com perfis fictícios. Hospedagem pública, instalação do APK e recepção nativa de notificações são verificações externas restantes. O Dockerfile usa Java 21; a imagem Docker não foi executada nesta máquina.
