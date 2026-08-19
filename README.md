# 💳 NexCard gerenciador de Cartões

## 📖 Sobre o Projeto

O **Gerenciador de Cartões** é uma aplicação Android desenvolvida para simular a gestão de cartões de crédito, proporcionando uma experiência semelhante à oferecida por bancos digitais e fintechs.

A aplicação permite que os usuários visualizem seus cartões, consultem informações detalhadas, realizem compras simuladas, solicitem novos cartões, alterem limites e efetuem bloqueios de forma simples e intuitiva.

Como diferencial, o projeto disponibiliza um **Cartão Virtual Interativo**, com animações e recursos de visualização de CVV, aproximando a experiência dos aplicativos bancários modernos.

---

# 👥 Integrantes da Equipe

Este projeto foi desenvolvido pelos seguintes integrantes:

- **Audrin Lucio**
- **Pyetro Sabaraense**
- **Ernani Ferreira**
- **Italo Rocha**

## 🎯 Objetivo

Desenvolver uma aplicação mobile utilizando **Kotlin** e **Jetpack Compose**, aplicando conceitos de:

- Desenvolvimento Android moderno;
- Consumo de APIs REST;
- Persistência local de dados;
- Gerenciamento de estado;
- Navegação entre telas;
- Arquitetura em camadas;
- Experiência do usuário (UX).

---

# ✨ Funcionalidades

## 🔐 Login

- Autenticação simulada do usuário;
- Validação de acesso;
- Navegação para a área principal do sistema.

## 💳 Lista de Cartões

- Exibição dos cartões disponíveis;
- Informações resumidas do cartão;
- Marcação de cartões favoritos;
- Navegação para tela de detalhes.

## ℹ️ Informações do Cartão

- Número mascarado;
- Nome do titular;
- Bandeira do cartão;
- Data de validade;
- Status do cartão;
- Limite disponível.

## 📝 Solicitar Cartão

- Consulta de produtos disponíveis;
- Simulação de solicitação de cartão;
- Atualização automática da lista de cartões.

## 🚫 Bloquear Cartão

- Bloqueio e desbloqueio simulados;
- Alteração visual imediata do status.

## 📈 Alterar Limite

- Solicitação de alteração de limite;
- Atualização do valor disponível.

## 🛒 Compras

- Registro de compras simuladas;
- Atualização do limite restante;
- Histórico de transações.

## ⭐ Favoritos

- Marcação de cartões favoritos;
- Persistência local dos favoritos.

## 🚀 Cartão Virtual

- Exibição do cartão virtual;
- Mostrar e ocultar CVV;
- Mostrar e ocultar número do cartão;
- Visualização animada;
- Ações rápidas de segurança.

---

# 📱 Telas da Aplicação

## 1. Login

Tela inicial responsável pela autenticação simulada do usuário.

📷 *Inserir print ou GIF da tela Login.*

---

## 2. Lista de Cartões

Exibe todos os cartões cadastrados e permite acessar suas funcionalidades.

📷 *Inserir print ou GIF da tela Lista de Cartões.*

---

## 3. Informações do Cartão

Apresenta detalhes completos do cartão selecionado.

📷 *Inserir print ou GIF da tela Informações do Cartão.*

---

## 4. Solicitar Cartão

Permite consultar produtos disponíveis e solicitar novos cartões.

📷 *Inserir print ou GIF da tela Solicitar Cartão.*

---

## 5. Bloqueio e Alteração de Limite

Gerencia configurações e status do cartão.

📷 *Inserir print ou GIF da tela de Bloqueio e Alteração de Limite.*

---

## 6. Compras

Exibe histórico e simula operações de compra.

📷 *Inserir print ou GIF da tela Compras.*

---

## 7. Cartão Virtual

Visualização interativa do cartão virtual.

📷 *Inserir print ou GIF da tela Cartão Virtual.*

---

# 🔄 Fluxo de Navegação

```text
Login
  │
  ▼
Lista de Cartões
  │
  ├──► Informações do Cartão
  │          │
  │          ├──► Bloquear Cartão
  │          ├──► Alterar Limite
  │          └──► Cartão Virtual
  │
  ├──► Solicitar Cartão
  │
  └──► Compras
```

A navegação será implementada utilizando **Navigation Compose**, garantindo uma navegação moderna, desacoplada e eficiente.

---

# 🏗️ Arquitetura da Aplicação

A aplicação seguirá uma arquitetura em camadas para facilitar manutenção, escalabilidade e organização do código.

```text
┌─────────────────────────┐
│      UI (Compose)       │
│         Screens         │
└────────────┬────────────┘
             │
             ▼
┌─────────────────────────┐
│       ViewModel         │
│ Estado e Regras Negócio │
└────────────┬────────────┘
             │
             ▼
┌─────────────────────────┐
│       Repository        │
│   Acesso aos Dados      │
└────────────┬────────────┘
             │
      ┌──────┴──────┐
      ▼             ▼
┌──────────┐ ┌──────────┐
│    API   │ │DataStore │
└──────────┘ └──────────┘
```

## Camadas

### UI

Responsável pelas telas desenvolvidas com Jetpack Compose.

### ViewModel

Gerencia estados da interface e regras de negócio.

### Repository

Intermedia o acesso entre API e armazenamento local.

### API

Responsável pela comunicação com serviços externos.

### Persistência

Armazena dados locais como favoritos e preferências.

---

# 🛠️ Stack Tecnológica

| Tecnologia | Finalidade |
|------------|------------|
| Kotlin | Linguagem principal |
| Jetpack Compose | Construção da interface |
| Material Design 3 | Componentes visuais |
| Navigation Compose | Navegação entre telas |
| ViewModel | Gerenciamento de estado |
| Retrofit | Consumo de APIs REST |
| Coroutines | Programação assíncrona |
| DataStore | Persistência local |
| Coil | Carregamento de imagens |

---

# 🔌 APIs Utilizadas

## API de Cartões

Responsável por:

- Consulta de cartões;
- Informações detalhadas;
- Solicitação de cartões;
- Alteração de status;
- Limites disponíveis.

## API de Produtos

Responsável por:

- Listagem de produtos;
- Tipos de cartões;
- Benefícios e categorias.

---

# 💾 Persistência de Dados

Os dados locais serão armazenados utilizando **DataStore**, permitindo manter informações entre execuções da aplicação.

### Dados Persistidos

- Cartões favoritos;
- Histórico de ações;
- Último cartão acessado;
- Preferências da aplicação;
- Configurações de usuário.

---

# 📦 Dependências

```kotlin
// Jetpack Compose
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")

// Navigation Compose
implementation("androidx.navigation:navigation-compose")

// ViewModel
implementation("androidx.lifecycle:lifecycle-viewmodel-compose")

// Retrofit
implementation("com.squareup.retrofit2:retrofit")
implementation("com.squareup.retrofit2:converter-gson")

// Coil
implementation("io.coil-kt:coil-compose")

// DataStore
implementation("androidx.datastore:datastore-preferences")
```

---

# ▶️ Como Executar o Projeto

## Pré-requisitos

- Android Studio Hedgehog ou superior;
- JDK 17+;
- Android SDK atualizado;
- Emulador Android ou dispositivo físico.

## 1. Clonar o Repositório

```bash
git clone https://github.com/seu-repositorio/gerenciador-cartoes.git
```

## 2. Entrar na Pasta do Projeto

```bash
cd gerenciador-cartoes
```

## 3. Abrir no Android Studio

```text
File > Open > Selecionar pasta do projeto
```

## 4. Sincronizar Dependências

```text
Sync Project with Gradle Files
```

## 5. Executar Aplicação

Clique no botão:

```text
Run ▶ app
```

Ou utilize o atalho:

```text
Shift + F10
```

---

# 🧪 Cenários de Teste

### Login

- Login válido;
- Login inválido.

### Cartões

- Consulta de cartões;
- Visualização de detalhes;
- Marcação de favoritos.

### Operações

- Solicitar cartão;
- Alterar limite;
- Bloquear cartão;
- Desbloquear cartão.

### Compras

- Registrar compra;
- Atualizar limite disponível;
- Consultar histórico.

### Cartão Virtual

- Exibir cartão virtual;
- Mostrar e ocultar CVV;
- Mostrar e ocultar número do cartão.

---

# 🚀 Diferenciais

## Cartão Virtual Interativo

Funcionalidade inspirada em aplicativos bancários modernos.

### Recursos

- Animação do cartão;
- Exibição temporária do CVV;
- Mascaramento de informações sensíveis;
- Ações rápidas de segurança;
- Atualização em tempo real utilizando
