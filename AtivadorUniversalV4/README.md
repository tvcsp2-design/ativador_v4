# Ativador Universal v4 — Multi-Tenant Branding

APK único que puxa **logo, nome, cor, telefone, whatsapp e suporte** direto do painel cPanel.
Cada revendedor (admin / master / revenda) tem sua própria aparência — o mesmo APK vira "cara" da revenda que criou o código.

## Como o APK descobre o painel
1. Primeira execução → tela pede a **URL do painel** (ex: `https://seusite.com/painel`).
2. Após ativar um código, o APK grava a URL e o token localmente.
3. **Menu secreto:** toque **5 vezes na logo** da tela inicial para reabrir configurações.

## Como o branding flui
```
Cliente digita código  →  validate.php responde com branding do dono do código
                       →  APK aplica logo/cor/nome/whatsapp na hora
Splash seguinte        →  api.php?action=getBranding&code=XXXX atualiza tudo
```

Hierarquia de fallback (campos vazios herdam):
`revenda → master → superadmin (global)`

## Painel (novas telas)
- Sidebar → **Aparência**
  - **Superadmin:** edita o branding global (padrão para tudo)
  - **Master:** edita seu próprio (aparece pros clientes da master e das revendas filhas)
  - **Revenda:** só edita se o master liberar `canEditBranding = true`
- Novos endpoints em `api.php`:
  - `GET  ?action=getBranding&code=XXX`  (público, usado pelo APK)
  - `POST ?action=saveBranding`          (usuário logado)
  - `POST ?action=uploadLogo`            (multipart, upload em /uploads/)
  - `POST ?action=setBrandingPermission` (master libera/bloqueia revenda)

## Como compilar
1. Abra `AtivadorUniversalV4/` no **Android Studio Hedgehog+**
2. Aguarde o Gradle sync
3. `Build > Generate Signed Bundle / APK > APK`
4. Distribua o APK único — nada de recompilar por cliente

## Estrutura
- `SplashActivity` — busca branding e roteia
- `ActivationActivity` — tela de código
- `SuccessActivity` — pós-ativação (whatsapp/suporte)
- `SettingsActivity` — URL do painel + reset (acessada com 5 toques na logo)
