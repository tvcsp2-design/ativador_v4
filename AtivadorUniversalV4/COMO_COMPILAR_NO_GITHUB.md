# 🚀 Compilar o APK automaticamente no GitHub Actions

Sem PC, sem Android Studio. Cada `push` gera um APK novo na nuvem.

## Passo a passo (uma vez só)

### 1. Criar repositório no GitHub
1. Entre em https://github.com/new
2. Nome: `ativador-universal-v4` (ou o que quiser)
3. Deixe **Private** (recomendado)
4. **NÃO** marque "Add README" — deixe vazio
5. Clique **Create repository**

### 2. Subir o código
No seu PC, dentro da pasta `AtivadorUniversalV4/`, abra o terminal e rode:

```bash
git init
git add .
git commit -m "primeiro commit"
git branch -M main
git remote add origin https://github.com/SEU_USUARIO/ativador-universal-v4.git
git push -u origin main
```

Troque `SEU_USUARIO` pelo seu nome de usuário do GitHub.

> Sem git no PC? Instale em https://git-scm.com/downloads
> Ou use o botão **"uploading an existing file"** na página do repo e arraste todos os arquivos.

### 3. Aguardar a compilação
1. Vá na aba **Actions** do repositório no GitHub
2. Você verá "Build APK" rodando (~5 minutos na primeira vez)
3. Quando terminar (✅ verde), clique nele
4. Role até o final e baixe em **Artifacts**:
   - `AtivadorUniversal-debug` → APK pronto pra instalar e testar
   - `AtivadorUniversal-release-unsigned` → APK para assinar depois com sua chave (Play Store)

### 4. Instalar no celular
1. Baixa o `.zip`, descompacta e pega o `.apk` dentro
2. Envia pro celular (WhatsApp, Drive, USB)
3. Abre o `.apk` no Android → autoriza "instalar de fontes desconhecidas"
4. Pronto! ✅

## Mudanças futuras

Quer trocar cor, logo ou textos padrão do APK?
1. Edita o arquivo no GitHub (pode ser direto pela web)
2. Dá `commit` → o Actions compila sozinho um APK novo
3. Baixa o novo APK em **Actions → último run → Artifacts**

**Arquivos úteis pra editar:**
- `app/src/main/res/values/strings.xml` → textos padrão
- `app/src/main/res/values/themes.xml` → cores padrão
- `app/src/main/res/drawable/` → logo/ícones
- `app/src/main/java/com/ativador/universal/SettingsActivity.kt` → menu secreto (5 toques na logo)

## Assinar o APK release (opcional, só pra Play Store)

O `release-unsigned` não instala direto — precisa de chave. Se for só uso interno, use o **debug APK**, que já está assinado e funciona normalmente.
