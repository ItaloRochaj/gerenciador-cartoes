package com.nexcard.nextwallet.ui.screens.financial

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nexcard.nextwallet.R
import com.nexcard.nextwallet.domain.model.Card
import com.nexcard.nextwallet.domain.model.Invoice
import com.nexcard.nextwallet.domain.model.InvoiceStatus
import com.nexcard.nextwallet.domain.model.Transaction
import com.nexcard.nextwallet.domain.model.TransactionCategory
import com.nexcard.nextwallet.ui.components.AppIcon
import com.nexcard.nextwallet.ui.components.EmptyContent
import com.nexcard.nextwallet.ui.components.ErrorContent
import com.nexcard.nextwallet.ui.components.LoadingContent
import com.nexcard.nextwallet.ui.components.NextWalletScaffold
import com.nexcard.nextwallet.ui.components.PrimaryButton
import com.nexcard.nextwallet.util.MoneyFormatter
import com.nexcard.nextwallet.util.ScreenLoadState
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialScreen(
    onOpenConsolidated: (String, String) -> Unit,
    viewModel: FinancialViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snack = remember { SnackbarHostState() }

    LaunchedEffect(state.message) {
        if (state.message != null) {
            snack.showSnackbar(state.message!!)
            viewModel.clearMessage()
        }
    }

    NextWalletScaffold(snackbarHostState = snack) {
        when (val load = state.loadState) {
            ScreenLoadState.Loading -> LoadingContent(modifier = Modifier.fillMaxSize())
            is ScreenLoadState.Error -> ErrorContent(message = load.message, onRetry = viewModel::refresh)
            ScreenLoadState.Empty -> EmptyContent(message = stringResource(R.string.no_cards))
            else -> {
                val card = state.cards.firstOrNull { it.id == state.selectedCardId } ?: state.cards.first()
                val invoiceItems = buildInvoiceItems(card, state.invoices, state.selectedReferenceMonth)
                val purchaseItems = buildPurchaseInsights(state.transactions)

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF3F3F6))
                        .padding(10.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .navigationBarsPadding()
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color.White)
                            .padding(horizontal = 14.dp, vertical = 18.dp)
                            .verticalScroll(rememberScrollState()),
                    ) {
                        Text(
                            text = stringResource(R.string.limits),
                            color = Color(0xFF21195B),
                            fontSize = 38.sp,
                            lineHeight = 38.sp,
                            fontWeight = FontWeight.Bold,
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        LimitsCard(
                            cardImagePath = cardImagePath(card),
                            onClick = viewModel::selectNextCard,
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        LimitProgressSection(card = card)

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = stringResource(R.string.invoices),
                            color = Color(0xFF201A53),
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                        ) {
                            invoiceItems.forEach { item ->
                                InvoiceTile(
                                    month = item.monthLabel,
                                    valueCents = item.valueCents,
                                    selected = item.selected,
                                    openLabel = item.isOpen,
                                    onClick = {
                                        viewModel.selectInvoiceMonth(item.referenceMonth)
                                        onOpenConsolidated(card.id, item.referenceMonth)
                                    },
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = stringResource(R.string.purchases),
                            color = Color(0xFF201A53),
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            purchaseItems.forEach { item ->
                                PurchaseCircle(
                                    label = item.label,
                                    valueCents = item.valueCents,
                                    icon = item.icon,
                                    ringAssetPath = item.ringAssetPath,
                                )
                            }
                        }


                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                        ) {
                            TextAction(stringResource(R.string.register_purchase)) { viewModel.togglePurchaseSheet(true) }
                            TextAction(stringResource(R.string.change_limit)) { viewModel.toggleLimitSheet(true) }
                            TextAction(stringResource(R.string.sort)) { viewModel.toggleSort() }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            FilterChip(
                                selected = state.selectedCategory == null,
                                onClick = { viewModel.setCategory(null) },
                                label = { Text(stringResource(R.string.all)) },
                            )
                            TransactionCategory.entries.take(3).forEach { category ->
                                FilterChip(
                                    selected = state.selectedCategory == category,
                                    onClick = { viewModel.setCategory(category) },
                                    label = { Text(category.name) },
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(72.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color(0xFF31105A))
                                .padding(horizontal = 24.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            BottomBarIcon(iconPath = "icons/wallet-2.png")
                            BottomBarIcon(iconPath = "icons/transactions/chart-2.png")
                            BottomBarIcon(iconPath = "icons/notification-bing.png")
                            BottomBarIcon(iconPath = "icons/setting.png")
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }

    if (state.showPurchaseSheet) {
        var description by remember { mutableStateOf("") }
        var amountReais by remember { mutableStateOf("") }
        var category by remember { mutableStateOf(TransactionCategory.OUTROS) }
        ModalBottomSheet(onDismissRequest = { viewModel.togglePurchaseSheet(false) }) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(stringResource(R.string.register_purchase))
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text(stringResource(R.string.description)) })
                OutlinedTextField(
                    value = amountReais,
                    onValueChange = { amountReais = sanitizeCurrencyInput(it) },
                    label = { Text(stringResource(R.string.value_reais_hint)) },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                )
                Row {
                    TransactionCategory.entries.forEach {
                        TextButton(onClick = { category = it }) { Text(it.name) }
                    }
                }
                PrimaryButton(text = stringResource(R.string.save)) {
                    val amountCents = parseReaisToCents(amountReais) ?: 0L
                    viewModel.registerPurchase(description, amountCents, category)
                }
            }
        }
    }

    if (state.showLimitSheet) {
        var limitReais by remember { mutableStateOf("") }
        ModalBottomSheet(onDismissRequest = { viewModel.toggleLimitSheet(false) }) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(stringResource(R.string.new_limit))
                OutlinedTextField(
                    value = limitReais,
                    onValueChange = { limitReais = sanitizeCurrencyInput(it) },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text(stringResource(R.string.value_reais_hint)) },
                    modifier = Modifier.fillMaxWidth(),
                )
                PrimaryButton(text = stringResource(R.string.confirm)) {
                    val newLimitCents = parseReaisToCents(limitReais) ?: 0L
                    viewModel.changeLimit(newLimitCents)
                }
            }
        }
    }
}

@Composable
private fun LimitsCard(cardImagePath: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.74f)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF29242E))
            .clickable(onClick = onClick),
    ) {
        AppIcon(
            iconPath = cardImagePath,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        )

        Text(
            text = "Toque no cartao para trocar",
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp),
        )
    }
}

private fun cardImagePath(card: Card): String {
    return when (card.colorStyle) {
        "purple_black" -> "cards/Cart  Geometric  34.png"
        "graphite" -> "cards/Cart 25.png"
        "blue_dark" -> "cards/Cart 24.jpg"
        else -> when (card.id) {
            "card_01" -> "cards/Cart  Geometric  34.png"
            "card_02" -> "cards/Cart 25.png"
            else -> "cards/Cart 24.jpg"
        }
    }
}

@Composable
private fun LimitProgressSection(card: Card) {
    val progress = if (card.totalLimitCents == 0L) 0f else (card.usedLimitCents.toFloat() / card.totalLimitCents.toFloat()).coerceIn(0f, 1f)

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = "Limite ${MoneyFormatter.format(card.totalLimitCents)}",
            color = Color(0xFF21195B),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFE9E4F7)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Brush.horizontalGradient(listOf(Color(0xFF5E27A0), Color(0xFFD34BFF)))),
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Usado ${MoneyFormatter.format(card.usedLimitCents)}",
                color = Color(0xFF201A53),
                fontSize = 12.sp,
            )
            Text(
                text = "Disponível ${MoneyFormatter.format(card.availableLimitCents)}",
                color = Color(0xFF201A53),
                fontSize = 12.sp,
            )
        }
    }
}

@Composable
private fun InvoiceTile(
    month: String,
    valueCents: Long,
    selected: Boolean,
    openLabel: Boolean,
    onClick: () -> Unit,
) {
    val bg = if (selected) Color(0xFF2D2D2D) else Color(0xFFF2F2F2)
    val fg = if (selected) Color.White else Color(0xFF30303A)

    Column(
        modifier = Modifier
            .width(108.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(text = month, color = fg, fontSize = 14.sp)
        Text(text = MoneyFormatter.format(valueCents), color = fg, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        val status = if (openLabel) stringResource(R.string.invoice_open) else stringResource(R.string.invoice_closed)
        Text(text = status, color = fg, fontSize = 12.sp)
    }
}

@Composable
private fun PurchaseCircle(label: String, valueCents: Long, icon: ImageVector, ringAssetPath: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(
            modifier = Modifier
                .size(84.dp),
            contentAlignment = Alignment.Center,
        ) {
            AppIcon(
                iconPath = ringAssetPath,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                androidx.compose.material3.Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = Color(0xFF1F1F1F),
                    modifier = Modifier.size(28.dp),
                )
                Text(text = label, color = Color(0xFF1F1F1F), fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
        Text(text = MoneyFormatter.format(valueCents), color = Color(0xFF1F1F1F), fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

private data class InvoiceUiItem(
    val referenceMonth: String,
    val monthLabel: String,
    val valueCents: Long,
    val selected: Boolean,
    val isOpen: Boolean,
)

private data class PurchaseInsightUiItem(
    val label: String,
    val valueCents: Long,
    val icon: ImageVector,
    val ringAssetPath: String,
)

private fun buildInvoiceItems(card: Card, invoices: List<Invoice>, selectedReferenceMonth: String): List<InvoiceUiItem> {
    val currentMonthKey = currentYearMonthKey()
    val effectiveMonth = selectedReferenceMonth.ifBlank { currentMonthKey }
    val currentMonth = InvoiceUiItem(
        referenceMonth = currentMonthKey,
        monthLabel = monthLabelFromKey(currentMonthKey),
        valueCents = card.usedLimitCents,
        selected = effectiveMonth == currentMonthKey,
        isOpen = true,
    )

    val historicalReal = invoices
        .asSequence()
        .filter { it.referenceMonth != currentMonthKey }
        .sortedByDescending { it.referenceMonth }
        .map {
            InvoiceUiItem(
                referenceMonth = it.referenceMonth,
                monthLabel = monthLabelFromKey(it.referenceMonth),
                valueCents = it.totalAmountCents,
                selected = effectiveMonth == it.referenceMonth,
                isOpen = it.status == InvoiceStatus.OPEN,
            )
        }
        .take(2)
        .toList()

    if (historicalReal.size == 2) return listOf(currentMonth) + historicalReal

    val usedMonths = mutableSetOf(currentMonthKey)
    usedMonths += historicalReal.map { it.referenceMonth }
    val paddedHistorical = historicalReal.toMutableList()
    var seedMonth = historicalReal.lastOrNull()?.referenceMonth ?: currentMonthKey

    while (paddedHistorical.size < 2) {
        val candidate = previousMonthKey(seedMonth)
        seedMonth = candidate
        if (!usedMonths.add(candidate)) continue

        paddedHistorical += InvoiceUiItem(
            referenceMonth = candidate,
            monthLabel = monthLabelFromKey(candidate),
            valueCents = mockInvoiceValueCents(card, candidate),
            selected = effectiveMonth == candidate,
            isOpen = false,
        )
    }

    return (paddedHistorical + currentMonth).sortedBy { it.referenceMonth }
}

private fun previousMonthKey(referenceMonth: String): String {
    val year = referenceMonth.substringBefore('-').toIntOrNull() ?: return referenceMonth
    val month = referenceMonth.substringAfter('-', "1").toIntOrNull() ?: return referenceMonth
    val newMonth = if (month == 1) 12 else month - 1
    val newYear = if (month == 1) year - 1 else year
    return String.format(Locale.US, "%04d-%02d", newYear, newMonth)
}

private fun mockInvoiceValueCents(card: Card, referenceMonth: String): Long {
    val month = referenceMonth.substringAfter('-', "1").toIntOrNull() ?: 1
    val base = card.totalLimitCents / 10
    val variation = (month * 31_37L) % 95_000L
    return (base + variation).coerceAtMost(card.totalLimitCents)
}

private fun buildPurchaseInsights(transactions: List<Transaction>): List<PurchaseInsightUiItem> {
    fun sumByPredicate(predicate: (Transaction) -> Boolean): Long =
        transactions.filter(predicate).sumOf { it.amountCents }

    val mercado = 60_200L + sumByPredicate {
        it.category == TransactionCategory.MERCADO || it.description.contains("mercado", ignoreCase = true)
    }
    val uber = 8_956L + sumByPredicate {
        it.category == TransactionCategory.TRANSPORTE || it.description.contains("uber", ignoreCase = true)
    }
    val passagem = 55_835L + sumByPredicate {
        it.category == TransactionCategory.VIAGEM || it.description.contains("passagem", ignoreCase = true)
    }

    return listOf(
        PurchaseInsightUiItem(
            label = "MERCADO",
            valueCents = mercado,
            icon = Icons.Default.ShoppingCart,
            ringAssetPath = "images/Group 26943.png",
        ),
        PurchaseInsightUiItem(
            label = "UBER",
            valueCents = uber,
            icon = Icons.Default.DirectionsCar,
            ringAssetPath = "images/Group 26944.png",
        ),
        PurchaseInsightUiItem(
            label = "PASSAGEM",
            valueCents = passagem,
            icon = Icons.Default.FlightTakeoff,
            ringAssetPath = "images/Group 26945.png",
        ),
    )
}

private fun sanitizeCurrencyInput(value: String): String =
    value.filter { it.isDigit() || it == ',' || it == '.' }

private fun parseReaisToCents(value: String): Long? {
    if (value.isBlank()) return null
    val normalized = value
        .replace("R$", "", ignoreCase = true)
        .replace(" ", "")
        .replace(".", "")
        .replace(',', '.')

    return normalized
        .toBigDecimalOrNull()
        ?.setScale(2, RoundingMode.HALF_UP)
        ?.multiply(BigDecimal(100))
        ?.toLong()
}

private fun currentYearMonthKey(): String {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    return String.format(Locale.US, "%04d-%02d", year, month)
}


private fun monthLabelFromKey(referenceMonth: String): String {
    val monthNumber = referenceMonth.substringAfter('-', "0").toIntOrNull() ?: return referenceMonth
    return when (monthNumber) {
        1 -> "Janeiro"
        2 -> "Fevereiro"
        3 -> "Março"
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

@Composable
private fun BottomBarIcon(iconPath: String) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center,
    ) {
        AppIcon(
            iconPath = iconPath,
            contentDescription = null,
            modifier = Modifier.size(28.dp),
        )
    }
}

@Composable
private fun TextAction(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        color = Color(0xFF5A27A2),
        fontSize = 24.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.clickable(onClick = onClick),
    )
}
