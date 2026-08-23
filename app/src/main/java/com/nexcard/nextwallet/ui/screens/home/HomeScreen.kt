package com.nexcard.nextwallet.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nexcard.nextwallet.R
import com.nexcard.nextwallet.domain.model.Transaction
import com.nexcard.nextwallet.ui.components.AppIcon
import com.nexcard.nextwallet.ui.components.EmptyContent
import com.nexcard.nextwallet.ui.components.ErrorContent
import com.nexcard.nextwallet.ui.components.LoadingContent
import com.nexcard.nextwallet.ui.components.ProfileAvatar
import com.nexcard.nextwallet.util.ScreenLoadState
import java.util.Calendar
import java.util.Locale

@Composable
fun HomeScreen(
    onGoCards: () -> Unit,
    onGoFinancial: (String, String) -> Unit,
    onGoConsolidated: (String, String) -> Unit,
    onGoSettings: () -> Unit,
    onGoAddCard: () -> Unit,
    onGoDetail: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    when (val loadState = state.loadState) {
        ScreenLoadState.Loading -> LoadingContent(modifier = Modifier.fillMaxSize())
        is ScreenLoadState.Error -> ErrorContent(loadState.message, viewModel::refresh)
        ScreenLoadState.Empty -> EmptyContent(stringResource(R.string.empty_transactions), stringResource(R.string.request_card), onGoAddCard)
        else -> HomeContent(
            state = state,
            onGoCards = onGoCards,
            onGoFinancial = onGoFinancial,
            onGoConsolidated = onGoConsolidated,
            onGoSettings = onGoSettings,
            onGoAddCard = onGoAddCard,
            onGoDetail = onGoDetail,
        )
    }
}

@Composable
private fun HomeContent(
    state: HomeUiState,
    onGoCards: () -> Unit,
    onGoFinancial: (String, String) -> Unit,
    onGoConsolidated: (String, String) -> Unit,
    onGoSettings: () -> Unit,
    onGoAddCard: () -> Unit,
    onGoDetail: (String) -> Unit,
) {
    val primaryCard = state.primaryCard
    val goFinancialWithCurrentCard = {
        onGoFinancial(primaryCard?.id.orEmpty(), currentYearMonthKey())
    }
    val goConsolidatedWithCurrentCard = {
        onGoConsolidated(primaryCard?.id.orEmpty(), currentYearMonthKey())
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F3F6))
            .padding(10.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .shadow(80.dp, RoundedCornerShape(50.dp), spotColor = Color(0x1A000000), ambientColor = Color(0x1A000000))
                .clip(RoundedCornerShape(50.dp))
                .background(Color.White)
                .padding(horizontal = 22.dp, vertical = 20.dp),
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.app_name),
                        color = Color(0xFF231A57),
                        fontSize = 42.sp,
                        lineHeight = 42.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = stringResource(R.string.active_status),
                        color = Color(0xFFB6B6B6),
                        fontSize = 14.sp,
                    )
                }

                ProfileAvatar(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape),
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(color = Color(0xFF5B259F), shape = RoundedCornerShape(size = 50.dp))
                    .padding(horizontal = 24.dp, vertical = 24.dp),
            ) {
                // Decorative overlay exported from design assets.
                AppIcon(
                    iconPath = "images/info-section.png",
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                )

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "Limite diponivel",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                        )
                        Text(
                            text = "R$ ${(primaryCard?.availableLimitCents ?: 40000L) / 100}",
                            color = Color.White,
                            fontSize = 30.sp,
                            lineHeight = 32.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Text(
                        text = stringResource(R.string.mastercard_label),
                        color = Color.White,
                        fontSize = 17.sp,
                        lineHeight = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 6.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                HomeAction(iconPath = "icons/transactions/convert.png", label = stringResource(R.string.shortcut_cards), onClick = onGoCards)
                HomeAction(iconPath = "icons/actions/export.png", label = stringResource(R.string.shortcut_limits), onClick = goFinancialWithCurrentCard)
                HomeAction(iconPath = "icons/transactions/money-send.png", label = stringResource(R.string.shortcut_purchases), onClick = goFinancialWithCurrentCard)
                HomeAction(iconPath = "icons/actions/add-circle.png", label = stringResource(R.string.shortcut_new_card), onClick = onGoAddCard)
            }

            Spacer(modifier = Modifier.height(22.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.latest_transactions_clean),
                    color = Color(0xFF221A56),
                    fontSize = 32.sp,
                    lineHeight = 32.sp,
                    fontWeight = FontWeight.Bold,
                )
                TextButton(onClick = goConsolidatedWithCurrentCard) {
                    Text(
                        text = stringResource(R.string.view_all),
                        color = Color(0xFF7A52FF),
                        fontSize = 18.sp,
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                state.recentTransactions.take(6).forEach { tx ->
                    TransactionRow(
                        transaction = tx,
                        onClick = { onGoDetail(tx.cardId) },
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF31105A))
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BottomBarIcon(iconPath = "icons/wallet-2.png", onClick = onGoCards)
                BottomBarIcon(iconPath = "icons/transactions/chart-2.png", onClick = goFinancialWithCurrentCard)
                BottomBarIcon(iconPath = "icons/notification-bing.png", onClick = {})
                BottomBarIcon(iconPath = "icons/setting.png", onClick = onGoSettings)
            }

            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

@Composable
private fun HomeAction(
    iconPath: String,
    label: String,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Card(
            onClick = onClick,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.size(76.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                contentAlignment = Alignment.Center,
            ) {
                AppIcon(
                    iconPath = iconPath,
                    contentDescription = label,
                    modifier = Modifier.size(38.dp),
                )
            }
        }
        Text(
            text = label,
            color = Color(0xFF6A3BFF),
            fontSize = 14.sp,
        )
    }
}

@Composable
private fun TransactionRow(
    transaction: Transaction,
    onClick: () -> Unit,
) {
    val lower = transaction.description.lowercase()
    val logoPath = when {
        "aurora" in lower -> "logos/amazon.png"
        "netflix" in lower -> "logos/netflix.png"
        "paypal" in lower -> "images/paypal.png"
        "madero" in lower -> "images/Logo_Madero.jpg"
        "zara" in lower -> "images/4zara-logo-inditex-1.png"
        else -> "logos/amazon.png"
    }
    val subtitle = when {
        "aurora" in lower -> "Loja online"
        "netflix" in lower -> "Mês atual"
        "paypal" in lower -> "Uber"
        "madero" in lower -> "Restaurante"
        "zara" in lower -> "Compras"
        else -> "PS5"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF171717)),
                contentAlignment = Alignment.Center,
            ) {
                AppIcon(
                    iconPath = logoPath,
                    contentDescription = transaction.description,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
            }

            Column {
                Text(
                    text = transaction.description,
                    color = Color(0xFF231A57),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = subtitle,
                    color = Color(0xFFC1C1C1),
                    fontSize = 13.sp,
                )
            }
        }

        Text(
            text = formatAmountCompact(transaction.amountCents),
            color = Color(0xFF221A56),
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun BottomBarIcon(
    iconPath: String,
    onClick: () -> Unit,
    iconSize: Dp = 30.dp,
) {
    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Transparent)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        AppIcon(
            iconPath = iconPath,
            contentDescription = null,
            modifier = Modifier
                .size(iconSize),
        )
    }
}

private fun formatAmountCompact(cents: Long): String {
    val value = cents / 100
    return if (value >= 1000) "R$ ${"%,d".format(value).replace(',', '.')}" else "R$$value"
}

private fun currentYearMonthKey(): String {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    return String.format(Locale.US, "%04d-%02d", year, month)
}

