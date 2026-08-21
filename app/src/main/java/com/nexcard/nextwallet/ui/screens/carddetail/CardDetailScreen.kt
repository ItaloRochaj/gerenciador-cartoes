package com.nexcard.nextwallet.ui.screens.carddetail

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nexcard.nextwallet.R
import com.nexcard.nextwallet.ui.components.AppTopBar
import com.nexcard.nextwallet.ui.components.CardDetailInfo
import com.nexcard.nextwallet.ui.components.ConfirmationDialog
import com.nexcard.nextwallet.ui.components.ErrorContent
import com.nexcard.nextwallet.ui.components.LoadingContent
import com.nexcard.nextwallet.ui.components.NextWalletCard
import com.nexcard.nextwallet.ui.components.NextWalletScaffold
import com.nexcard.nextwallet.util.ScreenLoadState

@Composable
fun CardDetailScreen(
    onBack: () -> Unit,
    onDeleted: () -> Unit,
    viewModel: CardDetailViewModel = hiltViewModel(),
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
        Column(Modifier.fillMaxSize()) {
            AppTopBar(title = stringResource(R.string.card_details), onBack = onBack)
            when (val load = state.loadState) {
                ScreenLoadState.Loading -> LoadingContent(modifier = Modifier.fillMaxSize())
                is ScreenLoadState.Error -> ErrorContent(load.message, onBack)
                else -> {
                    val card = state.card ?: return@NextWalletScaffold
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        NextWalletCard(card)
                        
                        // Informações do cartão em layout de label/valor
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            CardDetailInfo(stringResource(R.string.name), card.holderName)
                            CardDetailInfo(stringResource(R.string.bank), "Italo Bank")
                            CardDetailInfo(stringResource(R.string.account), card.lastFourDigits)
                            CardDetailInfo(stringResource(R.string.status), card.status.name)
                            CardDetailInfo(stringResource(R.string.expiration), card.expirationDate)
                        }
                        
                        // Ações
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            TextButton(onClick = viewModel::toggleFavorite) { Text(stringResource(R.string.favorite)) }
                            TextButton(onClick = viewModel::askBlockToggle) { Text(stringResource(R.string.block_or_unblock)) }
                            TextButton(onClick = viewModel::toggleVirtualCard, enabled = card.status.name == "ACTIVE") { Text(stringResource(R.string.virtual_card)) }
                        }
                        
                        // Virtual Card
                        if (state.showVirtualCard) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.flipVirtualCard() },
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                AnimatedContent(targetState = state.showCardBack, label = "card_flip") { back ->
                                    Text(
                                        if (!back) {
                                            if (state.revealNumber) "4444 1234 5678 ${card.lastFourDigits}" else "**** **** **** ${card.lastFourDigits}"
                                        } else {
                                            if (state.revealCvv) "CVV: 907" else "CVV: ***"
                                        },
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                }
                                Row {
                                    TextButton(onClick = viewModel::toggleNumber) { Text(stringResource(R.string.show_hide_number)) }
                                    TextButton(onClick = viewModel::toggleCvv) { Text(stringResource(R.string.show_cvv)) }
                                    TextButton(onClick = {}) { Text(stringResource(R.string.copy_number)) }
                                }
                            }
                        }
                    }
                    
                    // Botão Deletar ao final
                    Column(modifier = Modifier.padding(16.dp)) {
                        TextButton(
                            onClick = viewModel::askDelete,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(stringResource(R.string.delete), color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    if (state.showBlockConfirm) {
        ConfirmationDialog(
            title = stringResource(R.string.confirm_block_title),
            message = stringResource(R.string.confirm_block_message),
            onConfirm = viewModel::confirmToggleBlock,
            onDismiss = viewModel::dismissBlockToggle,
        )
    }
    if (state.showDeleteConfirm) {
        ConfirmationDialog(
            title = stringResource(R.string.confirm_remove),
            message = stringResource(R.string.remove_card_message),
            onConfirm = { viewModel.confirmDelete(onDeleted) },
            onDismiss = viewModel::dismissDelete,
        )
    }
}
