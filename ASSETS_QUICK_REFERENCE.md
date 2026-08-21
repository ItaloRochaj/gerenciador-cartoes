# 🎨 Índice Rápido de Assets - NextWallet

Referência rápida dos assets organizados por tela e funcionalidade.

## 📱 TELAS E SEUS ASSETS

### 🔐 Tela 1: Login
```kotlin
// Social Login
GoogleIcon()
FacebookIcon()

// Login Field
LoginIcon()
```

---

### 👤 Tela 2: Cadastro
```kotlin
// Social Registration
GoogleIcon()
FacebookIcon()

// Profile Setup
ProfileIcon()
LoginIcon()
```

---

### 🏠 Tela 3: Home
```kotlin
// User Profile
ProfileAvatar(size = 64.dp)

// Quick Actions
WalletIcon()
SendMoneyIcon()
AddCircleIcon()

// Notifications
NotificationIcon()

// Card Preview
CardDesign(CardDesigns.CART_24)

// Navigation
NavigationIcon()

// Partner Logos
AmazonLogo()
PayPalLogo()
```

---

### 💳 Tela 4: Lista de Cartões
```kotlin
// Card Designs (multiple sizes)
CardDesign(CardDesigns.CART_24)
CardDesign(CardDesigns.CART_25)
CardDesign(CardDesigns.CART_26)
CardDesign(CardDesigns.CART_30)
CardDesign(CardDesigns.CART_GEOMETRIC_32)
CardDesign(CardDesigns.CART_GEOMETRIC_34)

// Card Elements
ChipIcon()
NFCIcon()
CardLogo()

// Navigation
BackIcon()
```

---

### 📊 Tela 5: Limites, Faturas e Compras
```kotlin
// Charts & Indicators
GraphicsImage(GraphicsAssets.GROUP_26944)
GraphicsImage(GraphicsAssets.INTERSECT)

// Card
CardDesign(CardDesigns.CART_30)

// Transaction Icons
SendMoneyIcon()
ConvertIcon()
ChartIcon()
```

---

### ✨ Tela 6: Novo Cartão
```kotlin
// Card Preview
CardDesign(CardDesigns.CART_GEOMETRIC_32)
CardDesign(CardDesigns.CART_GEOMETRIC_34)

// Decorative Elements
CardDesign(CardDesigns.RECTANGLE_594)
CardDesign(CardDesigns.RECTANGLE_595)
CardDesign(CardDesigns.RECTANGLE_596)

// Card Elements
ChipIcon()
CardLogo()
```

---

### 🔍 Tela 7: Detalhes e Cartão Virtual
```kotlin
// Card Designs (Front/Back)
CardDesign(CardDesigns.CART_24)
CardDesign(CardDesigns.CART_25)
CardDesign(CardDesigns.CART_26)
CardDesign(CardDesigns.CART_GEOMETRIC_32)
CardDesign(CardDesigns.CART_GEOMETRIC_34)

// Card Elements
ChipIcon()
NFCIcon()
CardLogo()
SecurityIcon()

// Navigation
BackIcon()
```

---

### ⚙️ Tela 8: Configurações
```kotlin
// Profile
ProfileAvatar()
EditIcon()

// Sections
CallIcon()
SecurityIcon()
NotificationIcon()
ProfileIcon()
WalletIcon()
LoginIcon()

// Navigation
BackIcon()
```

---

## 🎯 ESTRUTURA DE PASTAS

```
app/src/main/assets/
├── icons/
│   ├── actions/              [4 arquivos]
│   │   ├── add-circle.svg
│   │   ├── edit-2.svg
│   │   ├── export.svg
│   │   └── arrow-circle-left.svg
│   │
│   ├── card/                 [2 arquivos]
│   │   ├── chip.svg
│   │   └── NFC.svg
│   │
│   ├── navigation/           [3 arquivos]
│   │   ├── Primary Navigation.svg
│   │   ├── Primary Navigation (1).svg
│   │   └── .gitkeep
│   │
│   ├── social/               [4 arquivos]
│   │   ├── google.svg
│   │   ├── Facebook.svg
│   │   ├── Paypal logo.svg
│   │   └── amazon-round-circle-logo-symbol-button-19641_128 1.svg
│   │
│   ├── transactions/         [3 arquivos]
│   │   ├── money-send.svg
│   │   ├── convert.svg
│   │   └── chart-2.svg
│   │
│   ├── actions/
│   ├── call-calling.svg
│   ├── key-square.svg
│   ├── login.svg
│   ├── notification.svg
│   ├── notification-bing.svg
│   ├── profile.svg
│   ├── wallet-2.svg
│   └── Vector.svg
│
├── cards/                    [13 arquivos]
│   ├── Cart 24.svg
│   ├── Cart 25.svg
│   ├── Cart 26.svg
│   ├── Cart 30.svg
│   ├── Cart Geometric 32.svg
│   ├── Cart Geometric 34.svg
│   ├── Rectangle 594.svg
│   ├── Rectangle 595.svg
│   ├── Rectangle 596.svg
│   ├── backgrounds/
│   ├── brands/
│   ├── elements/
│   ├── textures/
│   └── .gitkeep
│
├── logos/                    [4 arquivos]
│   ├── Logo.svg
│   ├── Logo (1).svg
│   ├── Logo (2).svg
│   └── .gitkeep
│
├── images/                   [15+ arquivos]
│   ├── Profile Picture.png
│   ├── Group 12.svg
│   ├── Group 26907.svg
│   ├── Group 26939.svg
│   ├── Group 26944.svg
│   ├── Group 26944.png
│   ├── Group 26969.svg
│   ├── Group 26970.svg
│   ├── Group 26971.svg
│   ├── Group 26971.png
│   ├── Group 33525.svg
│   ├── Intersect.svg
│   ├── Intersect.png
│   ├── Mask Group.svg
│   └── .gitkeep
│
├── avatars/
├── fonts/
├── illustrations/
├── incoming/
├── original/
└── .gitkeep
```

---

## ✅ CHECKLIST DE COMPONENTES

### Implementados
- [x] AssetComponents.kt com todos os ícones
- [x] ActionIcons
- [x] AuthIcons
- [x] CardIcons
- [x] TransactionIcons
- [x] NavigationIcons
- [x] NotificationIcons
- [x] CardLogos
- [x] SocialLogos
- [x] CardDesigns
- [x] GraphicsAssets

### A Implementar
- [ ] Testes de componentes
- [ ] Documentação de uso em screens
- [ ] Temas (claro/escuro)
- [ ] Animações de ícones
- [ ] Cache de imagens

---

## 📝 EXEMPLO DE USO COMPLETO

```kotlin
// HomeScreen.kt
@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header com avatar
        ProfileAvatar(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
        )

        // Quick Actions Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = {}) {
                WalletIcon()
            }
            IconButton(onClick = {}) {
                SendMoneyIcon()
            }
            IconButton(onClick = {}) {
                AddCircleIcon()
            }
        }

        // Card Preview
        CardDesign(
            designPath = CardDesigns.CART_24,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        // Notifications
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NotificationIcon(modifier = Modifier.size(20.dp))
            Text("You have new notifications")
        }

        // Navigation Bar (Bottom)
        NavigationIcon()
    }
}
```

---

## 🔗 REFERÊNCIAS RÁPIDAS

### Caminhos de Assets
- Icons: `icons/`
- Cards: `cards/`
- Logos: `logos/`
- Images: `images/`

### Componentes Principais
- `AppIcon()` - Componente base para ícones
- `ProfileAvatar()` - Avatar do usuário
- `CardDesign()` - Designs de cartão
- `GraphicsImage()` - Gráficos e imagens

### Padrões
- Tamanho padrão de ícone: 24.dp
- Tamanho de avatar: 64.dp
- ContentScale para SVG: Fit
- ContentScale para PNG: Crop

---

## 📊 ESTATÍSTICAS

| Categoria | Quantidade |
|-----------|-----------|
| Ícones gerais | 28 |
| Designs de cartão | 9 |
| Logos | 3 |
| Imagens/Gráficos | 15+ |
| **Total** | **55+** |

---

**Última atualização:** 21/08/2026  
**Versão:** 1.0  
**Status:** Pronto para desenvolvimento 🚀

