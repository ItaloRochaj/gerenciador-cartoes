# 🎨 NextWallet - Sistema de Assets Organizado

Documentação completa da estrutura, organização e uso dos assets do projeto NextWallet.

## 📋 Visão Geral

Este projeto organiza todos os assets do Figma (ícones SVG, imagens PNG, designs de cartão, logos, etc.) de forma estruturada e pronta para integração com Jetpack Compose.

## 🎯 O Que Foi Feito

### ✅ Organização de Assets
- **Classificados por categoria**: icons, cards, logos, images, avatars, fonts, illustrations
- **Subcategorias lógicas**: actions, card, navigation, social, transactions
- **Todos os 65+ arquivos** organizados em suas respectivas pastas
- **Sincronizados** em duas localidades:
  - `assets_figma/` - Referência principal
  - `app/src/main/assets/` - Integrado à aplicação

### 📄 Documentação Criada

1. **ASSETS_MAPPING.md** 
   - Mapeamento completo de assets por tela (8 telas)
   - Possíveis usos de cada arquivo
   - Caminho exato dos arquivos

2. **ASSETS_USAGE_GUIDE.md**
   - Guia prático com exemplos de Compose
   - Como carregar SVG e PNG
   - Componentes por tela
   - Checklist de integração

3. **ASSETS_QUICK_REFERENCE.md**
   - Referência rápida para desenvolvimento
   - Código de exemplo completo
   - Índice visual dos assets
   - Estatísticas de organização

4. **AssetComponents.kt**
   - Componentes Kotlin reutilizáveis
   - Funções auxiliares para cada ícone
   - Organizados por categoria
   - Pronto para usar em screens

## 📂 Estrutura de Pastas

```
NextWallet/
├── assets_figma/                 # Referência principal de assets
│   ├── icons/                    # Ícones (28 arquivos)
│   │   ├── actions/              # Editar, adicionar, exportar, voltar
│   │   ├── card/                 # Chip, NFC
│   │   ├── navigation/           # Navegação inferior
│   │   ├── social/               # Google, Facebook, PayPal, Amazon
│   │   ├── transactions/         # Enviar, converter, gráfico
│   │   └── ...
│   ├── cards/                    # Designs de cartão (13 arquivos)
│   │   ├── backgrounds/
│   │   ├── brands/
│   │   ├── elements/
│   │   ├── textures/
│   │   └── ...
│   ├── logos/                    # Logos de bandeiras (4 arquivos)
│   ├── images/                   # Imagens e gráficos (15+ arquivos)
│   ├── avatars/
│   ├── fonts/
│   ├── illustrations/
│   └── incoming/
│
├── app/src/main/assets/          # Assets integrados à app
│   └── [Mesma estrutura de assets_figma/]
│
├── app/src/main/java/com/nexcard/nextwallet/
│   └── ui/components/
│       └── AssetComponents.kt    # Componentes Kotlin reutilizáveis
│
├── ASSETS_MAPPING.md             # Mapeamento por tela
├── ASSETS_USAGE_GUIDE.md         # Guia de uso
├── ASSETS_QUICK_REFERENCE.md     # Referência rápida
└── README.md                     # Este arquivo
```

## 🎨 Mapeamento de Telas

| Tela | Assets Principais | Status |
|------|-------------------|--------|
| 🔐 Login | Google, Facebook, Login | ✅ Mapeado |
| 👤 Cadastro | Google, Facebook, Profile | ✅ Mapeado |
| 🏠 Home | Avatar, Wallet, Send Money, Cart, Navigation | ✅ Mapeado |
| 💳 Cards | Cart designs, Chip, NFC, Logo | ✅ Mapeado |
| 📊 Limits | Charts, Progress, Card, Transaction icons | ✅ Mapeado |
| ✨ New Card | Card designs, Chip, Decorative shapes | ✅ Mapeado |
| 🔍 Details | Card designs, Chip, NFC, Security | ✅ Mapeado |
| ⚙️ Settings | Avatar, Edit, Call, Security, Notifications | ✅ Mapeado |

## 🚀 Como Usar

### 1. Adicionar Coil ao build.gradle.kts

```kotlin
dependencies {
    implementation("io.coil-kt:coil-compose:2.4.0")
}
```

### 2. Importar Componentes

```kotlin
import com.nexcard.nextwallet.ui.components.*
```

### 3. Usar em Suas Screens

```kotlin
@Composable
fun HomeScreen() {
    Column {
        // Avatar
        ProfileAvatar(size = 64.dp)
        
        // Ícones
        WalletIcon()
        SendMoneyIcon()
        
        // Navegação
        NavigationIcon()
    }
}
```

## 📊 Estatísticas de Assets

| Categoria | Quantidade |
|-----------|-----------|
| Ícones (icons) | 28 |
| Cartões (cards) | 13 |
| Logos | 4 |
| Imagens/Gráficos (images) | 15+ |
| Avatares | 1 |
| Fontes | 1 |
| Ilustrações | 1 |
| **Total** | **~63** |

## 🎯 Subcategorias de Ícones

- **actions/** (5) - add-circle, edit-2, export, arrow-circle-left, .gitkeep
- **card/** (2) - chip, NFC
- **navigation/** (3) - Primary Navigation (2 variações), .gitkeep
- **social/** (5) - google, Facebook, PayPal, Amazon
- **transactions/** (4) - money-send, convert, chart-2
- **Gerais** (9) - wallet-2, login, profile, notification, call-calling, key-square, notification-bing, Vector, .gitkeep

## 📚 Documentação Relacionada

| Arquivo | Descrição |
|---------|-----------|
| ASSETS_MAPPING.md | Mapeamento detalhado por tela |
| ASSETS_USAGE_GUIDE.md | Exemplos de código Compose |
| ASSETS_QUICK_REFERENCE.md | Referência rápida com índice |
| AssetComponents.kt | Componentes Kotlin prontos |

## ⚙️ Próximos Passos

### Curto Prazo (Esta Sprint)
- [ ] Adicionar Coil dependency
- [ ] Implementar telas com assets
- [ ] Testar carregamento de SVG/PNG

### Médio Prazo
- [ ] Criar sistema de temas (claro/escuro)
- [ ] Adicionar animações de ícones
- [ ] Implementar cache de imagens

### Longo Prazo
- [ ] Visualizar e classificar Group* ambíguos
- [ ] Otimizar tamanhos de arquivo
- [ ] Sistema de ícones customizáveis

## 🔍 Arquivos Ambíguos a Revisar

Estes arquivos precisam ser visualizados para melhor classificação:

- `Group 12.svg`
- `Group 26907.svg`
- `Group 26939.svg`
- `Group 26944.svg / Group 26944.png`
- `Group 26969.svg`
- `Group 26970.svg`
- `Group 26971.svg / Group 26971.png`
- `Group 33525.svg`
- `Mask Group.svg`
- `Vector.svg`

## 💡 Dicas Úteis

### Carregar SVG
```kotlin
AsyncImage(
    model = "file:///android_asset/icons/wallet-2.svg",
    contentDescription = "Wallet",
    modifier = Modifier.size(24.dp),
    contentScale = ContentScale.Fit
)
```

### Carregar PNG
```kotlin
AsyncImage(
    model = "file:///android_asset/images/Profile Picture.png",
    contentDescription = "Profile",
    modifier = Modifier
        .size(64.dp)
        .clip(CircleShape),
    contentScale = ContentScale.Crop
)
```

### Usar Componente Pronto
```kotlin
WalletIcon()
SendMoneyIcon()
ProfileAvatar()
CardDesign(CardDesigns.CART_24)
```

## 🔗 Referências

- [Jetpack Compose Image Loading](https://developer.android.com/compose/graphics/images)
- [Coil Documentation](https://coil-kt.github.io/coil/compose/)
- [Android Asset Management](https://developer.android.com/guide/topics/resources/providing-resources)

## 📞 Suporte

Para dúvidas sobre a estrutura de assets:
1. Consulte ASSETS_QUICK_REFERENCE.md
2. Verifique ASSETS_USAGE_GUIDE.md
3. Revise AssetComponents.kt para componentes prontos

## 📝 Atualizações

| Data | Versão | Descrição |
|------|--------|-----------|
| 21/08/2026 | 1.0 | Organização completa e documentação |
| - | 1.1 | Pendente: Revisão de ambíguos |

## ✅ Checklist de Conclusão

- [x] ✅ Organizar todos os assets em categorias
- [x] ✅ Sincronizar com app/src/main/assets/
- [x] ✅ Criar ASSETS_MAPPING.md
- [x] ✅ Criar ASSETS_USAGE_GUIDE.md
- [x] ✅ Criar ASSETS_QUICK_REFERENCE.md
- [x] ✅ Criar AssetComponents.kt
- [x] ✅ Documentar estrutura
- [ ] ⏳ Adicionar Coil dependency
- [ ] ⏳ Implementar em screens
- [ ] ⏳ Criar temas claro/escuro

---

**Status:** 🚀 **Pronto para Desenvolvimento**  
**Última Atualização:** 21/08/2026  
**Versão:** 1.0

