# Lastro — Android App

App Android do Lastro, gerenciador financeiro a dois.

---

## Estrutura do projeto

Projeto multimodule com separação por camada e por feature.

```
lastro-app/
├── app/                     # Entry point, NavHost, DI root
├── build-logic/             # Convention plugins Gradle
├── core/
│   ├── common/              # Result, navegação (Route), utilitários
│   ├── database/            # Room, DAOs
│   ├── domain/              # Interfaces de repositório e implementações
│   ├── network/             # Retrofit, interceptors, DTOs, serviços
│   ├── testing/             # Fakes para testes (FakeAuthRepository, etc.)
│   └── ui/                  # Design system: componentes, tema, tokens
└── feature/
    ├── auth/                # Login, registro, recuperação de senha, splash
    ├── couple/              # Conexão de casal
    ├── dashboard/           # Dashboard financeiro
    ├── profile/             # Perfil do usuário
    └── transactions/        # Transações e recorrentes
```

---

## Módulo `core:ui` — Design System

Componentes disponíveis em `core/ui/src/main/kotlin/com/lastro/core/ui/components/`:

| Componente | Uso |
|---|---|
| `LastroButton` | Botões primário, outlined e ghost; suporta `loading` |
| `LastroTextField` | Campos de texto; tipos `Default`, `Password`, `Money` |
| `LastroTopBar` | Barra superior com título e ação de voltar opcional |
| `ErrorBanner` | Banner animado de erro com ação de retry opcional |
| `LastroCard` | Card com elevação e padding interno |
| `LastroChip` | Chip selecionável |
| `Avatar` | Avatar circular com iniciais ou imagem |
| `BottomNavigation` | Barra de navegação inferior |
| `BottomSheet` | Modal bottom sheet |
| `EmptyState` | Tela vazia com ícone e mensagem |
| `ListItem` | Item de lista com leading/trailing |
| `ProgressBar` | Barra de progresso linear |
| `SegmentedControl` | Controle segmentado de seleção |
| `StepIndicator` | Indicador de etapas |
| `ToggleRow` | Linha com switch |

**Regra:** toda tela deve usar exclusivamente os componentes acima. Não usar componentes Material3 diretamente nas telas.

---

## Autenticação (`feature:auth`)

### Fluxo

```
Splash
  ├── token ausente → Login
  ├── token presente + refresh OK → Home
  └── token presente + refresh falhou → Login

Login
  ├── sucesso → Home
  ├── "Esqueci minha senha" → ForgotPassword
  └── "Criar conta" → Register

ForgotPassword
  └── envia email de recuperação (resposta genérica, sem confirmar existência de conta)

Register
  └── sucesso → Home
```

### Segurança

- `FLAG_SECURE` em `LoginScreen` e `ForgotPasswordScreen` (bloqueia screenshots e app switcher)
- Tokens salvos em `EncryptedSharedPreferences` com chave no Android Keystore
- Refresh automático em 401: `AuthInterceptor` renova o access token e retenta a request original
- Nunca salvar ou logar senha do usuário

### API — contratos auth

| Endpoint | Body | Resposta |
|---|---|---|
| `POST /auth/login` | `{email, password}` | `{user, access_token, refresh_token}` |
| `POST /auth/register` | `{name, email, password}` | `{user, access_token, refresh_token}` |
| `POST /auth/refresh` | `{refresh_token}` | `{user, access_token, refresh_token}` |
| `POST /auth/logout` | `{refresh_token}` | 204 |
| `POST /auth/logout/all` | — | 204 |
| `POST /auth/forgot-password` | `{email}` | `{message}` (sempre 200) |

---

## Testes

### Unitários

Rodam na JVM, sem emulador:

```bash
./gradlew :feature:auth:test
```

Cobrem ViewModels (`AuthViewModel`, `ForgotPasswordViewModel`, `SplashViewModel`) com `FakeAuthRepository`.

### Screenshot tests (Roborazzi)

Roborazzi + Robolectric: capturam PNG das telas em estados-chave (default, loading, erro, sucesso) direto na JVM.

```bash
# Gerar/atualizar screenshots de referência
./gradlew :feature:auth:recordRoborazziDebug

# Verificar regressões (falha se algo mudou)
./gradlew :feature:auth:verifyRoborazziDebug

# Rodar como teste normal (sem comparação)
./gradlew :feature:auth:test
```

Screenshots são salvas em `feature/auth/build/outputs/roborazzi/`. As imagens de referência ficam em `feature/auth/src/test/snapshots/` e devem ser commitadas.

Estados cobertos por tela:

| Tela | Estados |
|---|---|
| `LoginScreen` | default, preenchido, loading, erro 401 |
| `RegisterScreen` | default, erro de email duplicado |
| `ForgotPasswordScreen` | default, loading, sucesso, erro 429 |

### Previews

Cada tela tem `@Preview` com estados representativos, visíveis no Android Studio via painel de preview.

---

## Convenções

- Commits seguem [Conventional Commits](https://www.conventionalcommits.org/): `feat(LASTRO-XXX): ...`, `fix(...)`, `ci(...)`, etc.
- Sem comentários no código. Se necessário, em inglês.
- PRs abrem da branch `feat/LASTRO-XXX` para `main`.
