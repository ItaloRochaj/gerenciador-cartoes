package com.nexcard.nextwallet.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nexcard.nextwallet.R
import com.nexcard.nextwallet.ui.components.ActionButton
import com.nexcard.nextwallet.ui.components.AvatarInitials
import com.nexcard.nextwallet.ui.components.EmptyContent
import com.nexcard.nextwallet.ui.components.ErrorContent
import com.nexcard.nextwallet.ui.components.LoadingContent
import com.nexcard.nextwallet.ui.components.NextWalletCard
import com.nexcard.nextwallet.ui.components.TransactionItem
import com.nexcard.nextwallet.util.ScreenLoadState

@Composable
fun HomeScreen(
    onGoCards: () -> Unit,
    onGoFinancial: () -> Unit,
    onGoAddCard: () -> Unit,
    onGoDetail: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    when (val loadState = state.loadState) {
        ScreenLoadState.Loading -> LoadingContent(modifier = Modifier.fillMaxSize())
        is ScreenLoadState.Error -> ErrorContent(loadState.message, viewModel::refresh)
        ScreenLoadState.Empty -> EmptyContent(stringResource(R.string.empty_transactions), stringResource(R.string.request_card), onGoAddCard)
        else -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Header com título e avatar
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column {
                            Text(stringResource(R.string.app_name), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                            Text(stringResource(R.string.hello_user, state.userName), style = MaterialTheme.typography.bodyMedium)
                        }
                        AvatarInitials(state.userName.take(1))
                    }
                }

                // Card principal
                item {
                    state.primaryCard?.let {
                        NextWalletCard(card = it, modifier = Modifier.fillMaxWidth())
                    }
                }

                // 4 botões de ação
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        ActionButton(
                            icon = Icons.Default.CreditCard,
                            label = stringResource(R.string.shortcut_cards),
                            onClick = onGoCards,
                        )
                        ActionButton(
                            icon = Icons.AutoMirrored.Filled.TrendingUp,
                            label = stringResource(R.string.shortcut_limits),
                            onClick = onGoFinancial,
                        )
                        ActionButton(
                            icon = Icons.Default.ShoppingCart,
                            label = stringResource(R.string.shortcut_purchases),
                            onClick = onGoFinancial,
                        )
                        ActionButton(
                            icon = Icons.Default.Add,
                            label = stringResource(R.string.shortcut_new_card),
                            onClick = onGoAddCard,
                        )
                    }
                }

                // Últimas transações
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(stringResource(R.string.latest_transactions), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        TextButton(onClick = onGoFinancial) { Text(stringResource(R.string.view_all)) }
                    }
                }

                items(state.recentTransactions.take(3)) { tx ->
                    TransactionItem(tx)
                }
            }
        }
    }
}
