package com.nexcard.nextwallet.ui.screens.cards

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import com.nexcard.nextwallet.ui.components.AppTopBar
import com.nexcard.nextwallet.ui.components.ConfirmationDialog
import com.nexcard.nextwallet.ui.components.EmptyContent
import com.nexcard.nextwallet.ui.components.ErrorContent
import com.nexcard.nextwallet.ui.components.LoadingContent
import com.nexcard.nextwallet.ui.components.NextWalletCard
import com.nexcard.nextwallet.util.ScreenLoadState

@Composable
fun CardsScreen(
    onBack: () -> Unit,
    onCardClick: (String) -> Unit,
    onAddCard: () -> Unit,
    viewModel: CardsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(title = stringResource(R.string.your_cards), onBack = onBack)
        when (val loadState = state.loadState) {
            ScreenLoadState.Loading -> LoadingContent(modifier = Modifier.fillMaxSize())
            is ScreenLoadState.Error -> ErrorContent(loadState.message, viewModel::refresh)
            ScreenLoadState.Empty -> EmptyContent(stringResource(R.string.no_cards), stringResource(R.string.request_card), onAddCard)
            else -> {
                LazyColumn(
                    modifier = Modifier.weight(1f).padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    item {
                        Text(stringResource(R.string.your_cards), style = MaterialTheme.typography.headlineSmall, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                    }
                    items(state.cards, key = { it.id }) { card ->
                        AnimatedVisibility(visible = true) {
                            Column {
                                NextWalletCard(card = card, modifier = Modifier.animateItem())
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    IconButton(onClick = { viewModel.toggleFavorite(card.id, !card.isFavorite) }) {
                                        Icon(
                                            imageVector = if (card.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                            contentDescription = stringResource(R.string.favorite),
                                        )
                                    }
                                    IconButton(onClick = { onCardClick(card.id) }) {
                                        Icon(Icons.Default.ChevronRight, contentDescription = stringResource(R.string.open_details))
                                    }
                                    IconButton(onClick = { viewModel.askDelete(card.id) }) {
                                        Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.remove_card))
                                    }
                                }
                            }
                        }
                    }
                }
                FloatingActionButton(onClick = onAddCard, modifier = Modifier.padding(16.dp)) {
                    Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_card))
                }
            }
        }
    }

    if (state.deletingCardId != null) {
        ConfirmationDialog(
            title = stringResource(R.string.confirm_remove),
            message = stringResource(R.string.remove_card_message),
            onConfirm = viewModel::deleteConfirmed,
            onDismiss = viewModel::clearDeleteRequest,
        )
    }
}
