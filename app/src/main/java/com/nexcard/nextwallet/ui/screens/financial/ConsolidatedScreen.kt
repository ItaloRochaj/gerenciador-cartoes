package com.nexcard.nextwallet.ui.screens.financial

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.nexcard.nextwallet.ui.theme.darkAwareTextColor
import com.nexcard.nextwallet.util.MoneyFormatter
import com.nexcard.nextwallet.util.ScreenLoadState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ConsolidatedScreen(
    onBack: () -> Unit,
    onGoCards: () -> Unit,
    onGoFinancial: (String, String) -> Unit,
    onGoSettings: () -> Unit,
    viewModel: FinancialViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (val load = state.loadState) {
        ScreenLoadState.Loading -> LoadingContent(modifier = Modifier.fillMaxSize())
        is ScreenLoadState.Error -> ErrorContent(message = load.message, onRetry = viewModel::refresh)
        ScreenLoadState.Empty -> EmptyContent(message = stringResource(R.string.no_cards))
        else -> {
            val currentCard = state.cards.firstOrNull { it.id == state.selectedCardId }
            ConsolidatedContent(
                onBack = onBack,
                onGoCards = onGoCards,
                onGoFinancial = { onGoFinancial(currentCard?.id.orEmpty(), state.selectedReferenceMonth) },
                onGoSettings = onGoSettings,
                cardNumber = currentCard?.maskedNumber.orEmpty(),
                referenceMonth = state.selectedReferenceMonth,
                transactions = state.transactions,
            )
        }
    }
}

@Composable
private fun ConsolidatedContent(
    onBack: () -> Unit,
    onGoCards: () -> Unit,
    onGoFinancial: () -> Unit,
    onGoSettings: () -> Unit,
    cardNumber: String,
    referenceMonth: String,
    transactions: List<Transaction>,
) {
    val monthLabel = monthLabelFromKey(referenceMonth)
    val total = transactions.sumOf { it.amountCents }
    val appBackground = MaterialTheme.colorScheme.background
    val containerSurface = MaterialTheme.colorScheme.surface

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(appBackground)
            .padding(10.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .clip(RoundedCornerShape(50.dp))
                .background(containerSurface)
                .padding(horizontal = 18.dp, vertical = 18.dp),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .clickable(onClick = onBack),
                        contentAlignment = Alignment.Center,
                    ) {
                        AppIcon(
                            iconPath = "images/arrow-circle-left.png",
                            contentDescription = stringResource(R.string.back),
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Consolidado",
                    color = darkAwareTextColor(Color(0xFF201A53)),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = monthLabel,
                    color = Color(0xFF6E6B85),
                    fontSize = 14.sp,
                )

                Text(
                    text = "Cartao: ${cardNumber.ifBlank { "**** **** **** ----" }}",
                    color = Color(0xFF6E6B85),
                    fontSize = 14.sp,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = MoneyFormatter.format(total),
                    color = darkAwareTextColor(Color(0xFF201A53)),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (transactions.isEmpty()) {
                    Text(
                        text = "Sem compras registradas neste mes.",
                        color = Color(0xFF8A8A9A),
                        fontSize = 14.sp,
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        transactions.forEach { tx ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0xFFF7F5FD))
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Column {
                                    Text(
                                        text = tx.description,
                                        color = darkAwareTextColor(Color(0xFF21195B)),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                    )
                                    Text(
                                        text = formatDate(tx.dateEpochMillis),
                                        color = Color(0xFF8A8A9A),
                                        fontSize = 12.sp,
                                    )
                                }
                                Text(
                                    text = MoneyFormatter.format(tx.amountCents),
                                    color = darkAwareTextColor(Color(0xFF201A53)),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

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
                BottomBarIcon(iconPath = "icons/transactions/chart-2.png", onClick = onGoFinancial)
                BottomBarIcon(iconPath = "icons/notification-bing.png", onClick = {})
                BottomBarIcon(iconPath = "icons/setting.png", onClick = onGoSettings)
            }

            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

@Composable
private fun BottomBarIcon(
    iconPath: String,
    onClick: () -> Unit,
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
            modifier = Modifier.size(30.dp),
        )
    }
}

private fun formatDate(epochMillis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
    return formatter.format(Date(epochMillis))
}

private fun monthLabelFromKey(referenceMonth: String): String {
    val monthNumber = referenceMonth.substringAfter('-', "0").toIntOrNull() ?: return referenceMonth
    return when (monthNumber) {
        1 -> "Janeiro"
        2 -> "Fevereiro"
        3 -> "Marco"
        4 -> "Abril"
        5 -> "Maio"
        6 -> "Junho"
        7 -> "Julho"
        8 -> "Agosto"
        9 -> "Setembro"
        10 -> "Outubro"
        11 -> "Novembro"
        12 -> "Dezembro"
        else -> referenceMonth
    }
}

