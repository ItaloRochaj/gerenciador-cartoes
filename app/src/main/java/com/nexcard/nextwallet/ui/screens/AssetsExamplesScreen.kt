package com.nexcard.nextwallet.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexcard.nextwallet.ui.components.*

/**
 * EXEMPLOS DE IMPLEMENTAÇÃO COM ASSETS
 *
 * Este arquivo contém exemplos práticos de como usar os componentes de assets
 * nas diferentes telas da aplicação.
 */

// ============================================================================
// EXEMPLO 1: HomeScreen
// ============================================================================

@Composable
fun HomeScreenExample() {
    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Header
            item {
                HomeHeader()
            }

            // Quick Actions
            item {
                Spacer(modifier = Modifier.height(24.dp))
                QuickActionsSection()
            }

            // Card Preview
            item {
                Spacer(modifier = Modifier.height(24.dp))
                CardPreviewSection()
            }

            // Recent Transactions
            item {
                Spacer(modifier = Modifier.height(24.dp))
                RecentTransactionsSection()
            }
        }
    }

    // Bottom Navigation
    BottomNavigationBar()
}

@Composable
private fun HomeHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Bem-vindo!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Seu saldo disponível",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        // Avatar
        ProfileAvatar(
            modifier = Modifier.size(56.dp)
        )
    }
}

@Composable
private fun QuickActionsSection() {
    Column {
        Text(
            text = "Ações Rápidas",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            QuickActionCard(
                icon = { WalletIcon(modifier = Modifier.size(32.dp)) },
                label = "Carteira",
                onClick = {}
            )

            QuickActionCard(
                icon = { SendMoneyIcon(modifier = Modifier.size(32.dp)) },
                label = "Enviar",
                onClick = {}
            )

            QuickActionCard(
                icon = { AddCircleIcon(modifier = Modifier.size(32.dp)) },
                label = "Adicionar",
                onClick = {}
            )

            QuickActionCard(
                icon = { ConvertIcon(modifier = Modifier.size(32.dp)) },
                label = "Converter",
                onClick = {}
            )
        }
    }
}

@Composable
private fun QuickActionCard(
    icon: @Composable () -> Unit,
    label: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF5F5F5))
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color(0xFF6C5FD5), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun CardPreviewSection() {
    Column {
        Text(
            text = "Seu Cartão",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        CardDesign(
            designPath = CardDesigns.CART_24,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
        )
    }
}

@Composable
private fun RecentTransactionsSection() {
    Column {
        Text(
            text = "Transações Recentes",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Example Transaction Item
        repeat(3) {
            TransactionItem(
                icon = { SendMoneyIcon(size = 24.dp) },
                title = "Envio de Dinheiro",
                description = "Para João Silva",
                amount = "-R$ 150,00",
                isExpense = true
            )
        }
    }
}

@Composable
private fun TransactionItem(
    icon: @Composable () -> Unit,
    title: String,
    description: String,
    amount: String,
    isExpense: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFF0F0F0), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }

            Column {
                Text(title, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Text(description, fontSize = 12.sp, color = Color.Gray)
            }
        }

        Text(
            text = amount,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = if (isExpense) Color.Red else Color.Green
        )
    }
}

@Composable
private fun BottomNavigationBar() {
    BottomAppBar(
        modifier = Modifier.height(64.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
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
            IconButton(onClick = {}) {
                ProfileIcon()
            }
        }
    }
}

// ============================================================================
// EXEMPLO 2: LoginScreen
// ============================================================================

@Composable
fun LoginScreenExample() {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            CardLogo(size = 64.dp)

            Spacer(modifier = Modifier.height(32.dp))

            // Title
            Text(
                text = "Faça Login",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Social Login Buttons
            SocialLoginButton(
                icon = { GoogleIcon(size = 24.dp) },
                text = "Continuar com Google",
                onClick = {}
            )

            Spacer(modifier = Modifier.height(12.dp))

            SocialLoginButton(
                icon = { FacebookIcon(size = 24.dp) },
                text = "Continuar com Facebook",
                onClick = {}
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Divider
            Text("ou", fontSize = 12.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(24.dp))

            // Login Button
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                LoginIcon()
                Spacer(modifier = Modifier.width(8.dp))
                Text("Fazer Login com Email")
            }
        }
    }
}

@Composable
private fun SocialLoginButton(
    icon: @Composable () -> Unit,
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.outlinedButtonColors()
    ) {
        icon()
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
    }
}

// ============================================================================
// EXEMPLO 3: CardsListScreen
// ============================================================================

@Composable
fun CardsListScreenExample() {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Header with Back Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}) {
                    BackIcon()
                }
                Text(
                    text = "Meus Cartões",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(48.dp))
            }

            // Cards List
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(3) { index ->
                    CardItem(
                        cardDesign = when (index) {
                            0 -> CardDesigns.CART_24
                            1 -> CardDesigns.CART_25
                            else -> CardDesigns.CART_26
                        }
                    )
                }
            }

            // Add New Card Button
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                AddCircleIcon()
                Spacer(modifier = Modifier.width(8.dp))
                Text("Adicionar Novo Cartão")
            }
        }
    }
}

@Composable
private fun CardItem(cardDesign: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            CardDesign(
                designPath = cardDesign,
                modifier = Modifier.fillMaxSize()
            )

            // Card Info Overlay
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CardLogo(size = 32.dp)
                    ChipIcon(size = 32.dp)
                }

                Column {
                    Text("**** **** **** 1234", color = Color.White)
                    Text("João Silva", color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}

// ============================================================================
// EXEMPLO 4: SettingsScreen
// ============================================================================

@Composable
fun SettingsScreenExample() {
    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Profile Section
            item {
                SettingsProfileSection()
            }

            // Settings Items
            item {
                Spacer(modifier = Modifier.height(24.dp))
                SettingsItemCategory("Conta")

                SettingsItem(
                    icon = { EditIcon() },
                    title = "Editar Perfil",
                    onClick = {}
                )

                SettingsItem(
                    icon = { SecurityIcon() },
                    title = "Segurança",
                    onClick = {}
                )

                SettingsItem(
                    icon = { KeySquareIcon() },
                    title = "Senha",
                    onClick = {}
                )
            }

            // Notifications Section
            item {
                Spacer(modifier = Modifier.height(24.dp))
                SettingsItemCategory("Notificações")

                SettingsItem(
                    icon = { NotificationIcon() },
                    title = "Notificações Push",
                    onClick = {}
                )

                SettingsItem(
                    icon = { NotificationIcon() },
                    title = "Preferências",
                    onClick = {}
                )
            }

            // Support Section
            item {
                Spacer(modifier = Modifier.height(24.dp))
                SettingsItemCategory("Suporte")

                SettingsItem(
                    icon = { CallIcon() },
                    title = "Contate-nos",
                    onClick = {}
                )

                SettingsItem(
                    icon = { ExportIcon() },
                    title = "Sobre",
                    onClick = {}
                )
            }

            // Logout
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    LoginIcon()
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sair")
                }
            }
        }
    }
}

@Composable
private fun SettingsProfileSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileAvatar(size = 64.dp)

            Column(modifier = Modifier.weight(1f)) {
                Text("João Silva", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("joao@example.com", fontSize = 12.sp, color = Color.Gray)
            }

            IconButton(onClick = {}) {
                EditIcon()
            }
        }
    }
}

@Composable
private fun SettingsItemCategory(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun SettingsItem(
    icon: @Composable () -> Unit,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Text(title, fontSize = 14.sp, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun KeySquareIcon() {
    SecurityIcon()
}

