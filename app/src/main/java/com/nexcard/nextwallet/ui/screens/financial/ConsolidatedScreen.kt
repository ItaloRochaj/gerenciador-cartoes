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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
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
import com.nexcard.nextwallet.util.MoneyFormatter
import com.nexcard.nextwallet.util.ScreenLoadState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ConsolidatedScreen(
    onBack: () -> Unit,
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
                cardLabel = currentCard?.maskedNumber.orEmpty(),
                referenceMonth = state.selectedReferenceMonth,
                transactions = state.transactions,
            )
        }
    }
}

@Composable
private fun ConsolidatedContent(
    onBack: () -> Unit,
    cardLabel: String,
    referenceMonth: String,
    transactions: List<Transaction>,
) {
    val monthLabel = monthLabelFromKey(referenceMonth)
    val total = transactions.sumOf { it.amountCents }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F3F6))
            .padding(10.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(50.dp))
                .background(Color.White)
                .padding(horizontal = 18.dp, vertical = 18.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onBack),
                    contentAlignment = Alignment.Center,
                ) {
                    AppIcon(
                        iconPath = "icons/actions/arrow-circle-left.svg",
                        contentDescription = stringResource(R.string.back),
                        modifier = Modifier.size(24.dp),
                    )
                }
                Text(
                    text = "Consolidado",
                    color = Color(0xFF201A53),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$monthLabel  $cardLabel",
                color = Color(0xFF6E6B85),
                fontSize = 14.sp,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = MoneyFormatter.format(total),
                color = Color(0xFF201A53),
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
                return
            }

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
                                color = Color(0xFF21195B),
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
                            color = Color(0xFF201A53),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }
        }
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

