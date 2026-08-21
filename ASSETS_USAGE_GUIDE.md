# 📦 Assets - Guia de Uso

Guia prático para utilizar os assets SVG e PNG na aplicação NextWallet com Jetpack Compose.

## 📂 Estrutura de Assets

```
app/src/main/assets/
├── icons/
│   ├── actions/         # add-circle, edit-2, export, arrow-circle-left
│   ├── card/            # chip, NFC
│   ├── navigation/      # Primary Navigation
│   ├── social/          # google, Facebook, PayPal, Amazon
│   ├── transactions/    # money-send, convert, chart-2
│   ├── call-calling.svg
│   ├── key-square.svg
│   ├── login.svg
│   ├── notification.svg
│   ├── notification-bing.svg
│   ├── profile.svg
│   ├── Vector.svg
│   └── wallet-2.svg
├── cards/               # Designs de cartões
│   ├── Cart 24.svg
│   ├── Cart 25.svg
│   ├── Cart 26.svg
│   ├── Cart 30.svg
│   ├── Cart Geometric 32.svg
│   ├── Cart Geometric 34.svg
│   ├── Rectangle 594.svg
│   ├── Rectangle 595.svg
│   └── Rectangle 596.svg
├── logos/               # Logos de bandeiras
│   ├── Logo.svg
│   ├── Logo (1).svg
│   └── Logo (2).svg
├── images/              # Imagens e gráficos
│   ├── Profile Picture.png
│   ├── Group*.svg
│   ├── Intersect.*
│   └── Mask Group.svg
└── ...
```

## 🎨 Como Usar SVG com Compose

### 1. SVG via XML Vector Drawable

Converta SVGs para Android Vector Drawable:

```bash
# Adicionar à pasta res/drawable/
# Arquivo: res/drawable/ic_wallet.xml
```

### 2. SVG via Coil (Recomendado)

```kotlin
import coil.compose.AsyncImage

// Carregar SVG como Image
AsyncImage(
    model = "file:///android_asset/icons/wallet-2.svg",
    contentDescription = "Wallet Icon",
    modifier = Modifier.size(24.dp),
    contentScale = ContentScale.Fit
)
```

### 3. PNG via Assets

```kotlin
import androidx.compose.material3.Icon
import coil.compose.AsyncImage

// Carregar PNG
AsyncImage(
    model = "file:///android_asset/images/Profile Picture.png",
    contentDescription = "Profile Picture",
    modifier = Modifier
        .size(64.dp)
        .clip(CircleShape),
    contentScale = ContentScale.Crop
)
```

## 🔧 Componentes por Tela

### Tela 1 - Login
```kotlin
// Social Login Icons
AsyncImage(
    model = "file:///android_asset/icons/social/google.svg",
    contentDescription = "Google Login"
)

AsyncImage(
    model = "file:///android_asset/icons/social/Facebook.svg",
    contentDescription = "Facebook Login"
)
```

### Tela 2 - Cadastro
```kotlin
// Profile Setup
AsyncImage(
    model = "file:///android_asset/icons/profile.svg",
    contentDescription = "Profile"
)
```

### Tela 3 - Home
```kotlin
// User Avatar
AsyncImage(
    model = "file:///android_asset/images/Profile Picture.png",
    contentDescription = "User Avatar",
    modifier = Modifier
        .size(64.dp)
        .clip(CircleShape),
    contentScale = ContentScale.Crop
)

// Wallet Icon
AsyncImage(
    model = "file:///android_asset/icons/wallet-2.svg",
    contentDescription = "Wallet"
)

// Send Money Icon
AsyncImage(
    model = "file:///android_asset/icons/transactions/money-send.svg",
    contentDescription = "Send Money"
)

// Navigation Bar
AsyncImage(
    model = "file:///android_asset/icons/navigation/Primary Navigation.svg",
    contentDescription = "Navigation"
)
```

### Tela 4 - Lista de Cartões
```kotlin
// Card Designs
AsyncImage(
    model = "file:///android_asset/cards/Cart 24.svg",
    contentDescription = "Card Design"
)

// Chip Icon
AsyncImage(
    model = "file:///android_asset/icons/card/chip.svg",
    contentDescription = "Chip"
)

// Card Logo
AsyncImage(
    model = "file:///android_asset/logos/Logo.svg",
    contentDescription = "Card Brand"
)

// Back Button
AsyncImage(
    model = "file:///android_asset/icons/actions/arrow-circle-left.svg",
    contentDescription = "Back"
)
```

### Tela 5 - Limites e Faturas
```kotlin
// Charts and Graphs
AsyncImage(
    model = "file:///android_asset/images/Group 26944.svg",
    contentDescription = "Spending Chart"
)

// Progress Arc
AsyncImage(
    model = "file:///android_asset/images/Intersect.svg",
    contentDescription = "Progress"
)

// Transaction Icon
AsyncImage(
    model = "file:///android_asset/icons/transactions/money-send.svg",
    contentDescription = "Transaction"
)
```

### Tela 6 - Novo Cartão
```kotlin
// Card Design Preview
AsyncImage(
    model = "file:///android_asset/cards/Cart Geometric 32.svg",
    contentDescription = "Card Preview"
)

// Decorative Shapes
AsyncImage(
    model = "file:///android_asset/cards/Rectangle 594.svg",
    contentDescription = "Decoration"
)
```

### Tela 7 - Detalhes do Cartão
```kotlin
// Card Front
AsyncImage(
    model = "file:///android_asset/cards/Cart 24.svg",
    contentDescription = "Card Front"
)

// NFC Icon
AsyncImage(
    model = "file:///android_asset/icons/card/NFC.svg",
    contentDescription = "Contactless"
)

// Security Icon
AsyncImage(
    model = "file:///android_asset/icons/key-square.svg",
    contentDescription = "Security"
)
```

### Tela 8 - Configurações
```kotlin
// Settings Icons
AsyncImage(
    model = "file:///android_asset/images/Profile Picture.png",
    contentDescription = "Profile Picture"
)

AsyncImage(
    model = "file:///android_asset/icons/actions/edit-2.svg",
    contentDescription = "Edit Profile"
)

AsyncImage(
    model = "file:///android_asset/icons/call-calling.svg",
    contentDescription = "Support"
)

AsyncImage(
    model = "file:///android_asset/icons/key-square.svg",
    contentDescription = "Security"
)

AsyncImage(
    model = "file:///android_asset/icons/notification.svg",
    contentDescription = "Notifications"
)
```

## 📋 Checklist de Integração

- [ ] Adicionar dependência Coil no build.gradle.kts
- [ ] Criar função auxiliar para carregar assets
- [ ] Implementar componentes reutilizáveis
- [ ] Testar carregamento de SVGs e PNGs
- [ ] Implementar tema de cores
- [ ] Adicionar suporte a tema claro/escuro

## 🎯 Próximas Etapas

1. **Criar Componentes Reutilizáveis**
   - `AppIcon()` para ícones
   - `ProfileAvatar()` para avatares
   - `CardPreview()` para cartões

2. **Temas e Cores**
   - Mapear cores dos SVGs
   - Criar variações de temas

3. **Documentação Visual**
   - Screenshots de cada tela
   - Guia de padrões visuais

4. **Performance**
   - Cachear assets frequentemente usados
   - Otimizar tamanho de arquivos

## 🔗 Referências

- [Jetpack Compose Image](https://developer.android.com/compose/graphics/images)
- [Coil Documentation](https://coil-kt.github.io/coil/)
- [SVG on Android](https://developer.android.com/guide/topics/graphics/vector-drawable-resources)

---

**Última atualização:** 21/08/2026  
**Versão:** 1.0

