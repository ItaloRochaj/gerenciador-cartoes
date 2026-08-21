package com.nexcard.nextwallet.ui.screens.addcard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nexcard.nextwallet.R
import com.nexcard.nextwallet.ui.components.AppTopBar
import com.nexcard.nextwallet.ui.components.ErrorContent
import com.nexcard.nextwallet.ui.components.LoadingContent
import com.nexcard.nextwallet.ui.components.NextWalletScaffold
import com.nexcard.nextwallet.ui.components.PrimaryButton
import com.nexcard.nextwallet.ui.theme.BlackCard
import com.nexcard.nextwallet.ui.theme.PurpleDark
import com.nexcard.nextwallet.ui.theme.PurpleLight
import com.nexcard.nextwallet.util.ScreenLoadState

@Composable
fun AddCardScreen(
    onBack: () -> Unit,
    onRequested: () -> Unit,
    viewModel: AddCardViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snack = remember { SnackbarHostState() }
    var confirm by remember { mutableStateOf(false) }

    LaunchedEffect(state.message) {
        if (state.message != null) {
            snack.showSnackbar(state.message!!)
            viewModel.clearMessage()
        }
    }

    NextWalletScaffold(snackbarHostState = snack) {
        Column(modifier = Modifier.fillMaxSize()) {
            AppTopBar(title = stringResource(R.string.new_card), onBack = onBack)
            when (val load = state.loadState) {
                ScreenLoadState.Loading -> LoadingContent(modifier = Modifier.fillMaxSize())
                is ScreenLoadState.Error -> ErrorContent(load.message, viewModel::refresh)
                else -> {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        // Card ilustrativo
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Brush.horizontalGradient(listOf(PurpleDark, PurpleLight, BlackCard))),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                stringResource(R.string.new_card),
                                color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                            )
                        }

                        // Texto descritivo
                        Text(
                            stringResource(R.string.add_new_card_description),
                            textAlign = TextAlign.Center,
                            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                        )
                    }

                    // Botão de solicitação
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        PrimaryButton(
                            text = stringResource(R.string.request_card),
                            enabled = !state.isRequesting,
                            onClick = { confirm = true },
                        )
                        if (state.isRequesting) {
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }

    if (confirm) {
        AlertDialog(
            onDismissRequest = { confirm = false },
            title = { Text(stringResource(R.string.confirm_request_card)) },
            text = { Text(stringResource(R.string.confirm_request_card_msg)) },
            confirmButton = {
                TextButton(onClick = {
                    confirm = false
                    viewModel.requestCard(onRequested)
                }) { Text(stringResource(R.string.confirm)) }
            },
            dismissButton = { TextButton(onClick = { confirm = false }) { Text(stringResource(R.string.cancel)) } },
        )
    }
}
