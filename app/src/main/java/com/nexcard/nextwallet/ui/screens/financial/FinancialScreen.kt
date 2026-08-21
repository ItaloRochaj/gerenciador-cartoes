package com.nexcard.nextwallet.ui.screens.financial

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nexcard.nextwallet.R
import com.nexcard.nextwallet.domain.model.TransactionCategory
import com.nexcard.nextwallet.ui.components.CategoryCircle
import com.nexcard.nextwallet.ui.components.EmptyContent
import com.nexcard.nextwallet.ui.components.ErrorContent
import com.nexcard.nextwallet.ui.components.InvoiceMonthButton
import com.nexcard.nextwallet.ui.components.LimitProgress
import com.nexcard.nextwallet.ui.components.LoadingContent
import com.nexcard.nextwallet.ui.components.NextWalletCard
import com.nexcard.nextwallet.ui.components.NextWalletScaffold
import com.nexcard.nextwallet.ui.components.PrimaryButton
import com.nexcard.nextwallet.ui.components.TransactionItem
import com.nexcard.nextwallet.util.ScreenLoadState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialScreen(viewModel: FinancialViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snack = remember { SnackbarHostState() }
    LaunchedEffect(state.message) {
        if (state.message != null) {
            snack.showSnackbar(state.message!!)
            viewModel.clearMessage()
        }
    }

    NextWalletScaffold(snackbarHostState = snack) { inner ->
        when (val load = state.loadState) {
            ScreenLoadState.Loading -> LoadingContent(modifier = Modifier.fillMaxSize())
            is ScreenLoadState.Error -> ErrorContent(message = load.message, onRetry = viewModel::refresh)
            ScreenLoadState.Empty -> EmptyContent(message = stringResource(R.string.no_cards))
            else -> {
                val card = state.cards.firstOrNull { it.id == state.selectedCardId } ?: state.cards.first()
                val filtered = state.transactions
                    .filter { state.selectedCategory == null || it.category == state.selectedCategory }
                    .let { list -> if (state.sortByValue) list.sortedByDescending { it.amountCents } else list.sortedByDescending { it.dateEpochMillis } }

                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(inner).padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    item { 
                        Text(stringResource(R.string.limits), style = androidx.compose.material3.MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    }
                    item { NextWalletCard(card = card) }
                    item {
                        LimitProgress(total = card.totalLimitCents, used = card.usedLimitCents)
                    }
                    
                    // Seção de Faturas
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(stringResource(R.string.invoices), style = androidx.compose.material3.MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                InvoiceMonthButton("Junho", "R$ 700")
                                InvoiceMonthButton("Julho", "R$ 850", selected = true)
                                InvoiceMonthButton("Agosto", "R$ 600")
                                InvoiceMonthButton("Setembro", "R$ 740")
                            }
                        }
                    }
                    
                    // Seção de Compras
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(stringResource(R.string.purchases), style = androidx.compose.material3.MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                            ) {
                                CategoryCircle("MERCADO", "R$ 600,00")
                                CategoryCircle("UBER", "R$ 85,50")
                                CategoryCircle("PASSAGEM", "R$ 258,35")
                            }
                        }
                    }
                    
                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TextButton(onClick = { viewModel.togglePurchaseSheet(true) }) { Text(stringResource(R.string.register_purchase)) }
                            TextButton(onClick = { viewModel.toggleLimitSheet(true) }) { Text(stringResource(R.string.change_limit)) }
                            TextButton(onClick = viewModel::toggleSort) { Text(stringResource(R.string.sort)) }
                        }
                    }
                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilterChip(selected = state.selectedCategory == null, onClick = { viewModel.setCategory(null) }, label = { Text(stringResource(R.string.all)) })
                            TransactionCategory.entries.forEach { category ->
                                FilterChip(
                                    selected = state.selectedCategory == category,
                                    onClick = { viewModel.setCategory(category) },
                                    label = { Text(category.name) },
                                )
                            }
                        }
                    }
                    item { Text(stringResource(R.string.latest_transactions)) }
                    items(filtered) { tx -> TransactionItem(tx) }
                }
            }
        }
    }

    if (state.showPurchaseSheet) {
        var description by remember { mutableStateOf("") }
        var amount by remember { mutableStateOf("") }
        var category by remember { mutableStateOf(TransactionCategory.OUTROS) }
        ModalBottomSheet(onDismissRequest = { viewModel.togglePurchaseSheet(false) }) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(stringResource(R.string.register_purchase))
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text(stringResource(R.string.description)) })
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it.filter { c -> c.isDigit() } },
                    label = { Text(stringResource(R.string.value_cents_hint)) },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                )
                Row {
                    TransactionCategory.entries.forEach {
                        TextButton(onClick = { category = it }) { Text(it.name) }
                    }
                }
                PrimaryButton(text = stringResource(R.string.save)) {
                    viewModel.registerPurchase(description, amount.toLongOrNull() ?: 0L, category)
                }
            }
        }
    }

    if (state.showLimitSheet) {
        var limit by remember { mutableLongStateOf(0L) }
        ModalBottomSheet(onDismissRequest = { viewModel.toggleLimitSheet(false) }) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(stringResource(R.string.new_limit))
                OutlinedTextField(
                    value = if (limit == 0L) "" else limit.toString(),
                    onValueChange = { limit = it.filter { c -> c.isDigit() }.toLongOrNull() ?: 0L },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text(stringResource(R.string.value_cents_hint)) },
                    modifier = Modifier.fillMaxWidth(),
                )
                PrimaryButton(text = stringResource(R.string.confirm)) { viewModel.changeLimit(limit) }
            }
        }
    }
}
