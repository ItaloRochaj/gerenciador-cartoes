package com.nexcard.nextwallet.ui.screens.addcard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nexcard.nextwallet.R
import com.nexcard.nextwallet.ui.components.AppIcon
import com.nexcard.nextwallet.ui.components.ErrorContent
import com.nexcard.nextwallet.ui.components.LoadingContent
import com.nexcard.nextwallet.ui.components.NextWalletScaffold
import com.nexcard.nextwallet.ui.theme.darkAwareTextColor
import com.nexcard.nextwallet.util.ScreenLoadState
import kotlinx.coroutines.delay

@Composable
fun AddCardScreen(
    onBack: () -> Unit,
    onGoCards: () -> Unit,
    onGoFinancial: () -> Unit,
    onGoSettings: () -> Unit,
    viewModel: AddCardViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val appBackground = MaterialTheme.colorScheme.background
    val containerSurface = MaterialTheme.colorScheme.surface
    val snack = remember { SnackbarHostState() }
    var requestedThisVisit by remember { mutableStateOf(false) }
    var bannerMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(state.message) {
        if (state.message != null) {
            bannerMessage = state.message
            viewModel.clearMessage()
            delay(2200)
            if (bannerMessage == state.message) {
                bannerMessage = null
            }
        }
    }

    // Each time this screen is opened from Home, request one new default card.
    LaunchedEffect(state.loadState, state.selectedProductId, requestedThisVisit) {
        if (!requestedThisVisit && state.loadState == ScreenLoadState.Success && state.selectedProductId.isNotBlank()) {
            requestedThisVisit = true
            viewModel.requestCard(onSuccess = {})
        }
    }

    NextWalletScaffold(snackbarHostState = snack) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(appBackground)
                .padding(10.dp),
        ) {
            when (val load = state.loadState) {
                ScreenLoadState.Loading -> LoadingContent(modifier = Modifier.fillMaxSize())
                is ScreenLoadState.Error -> ErrorContent(load.message, viewModel::refresh)
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .navigationBarsPadding()
                            .clip(RoundedCornerShape(50.dp))
                            .background(containerSurface)
                            .padding(horizontal = 24.dp, vertical = 18.dp),
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

                        Spacer(modifier = Modifier.height(6.dp))

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = stringResource(R.string.new_card),
                                    color = darkAwareTextColor(Color(0xFF21195B)),
                                    fontSize = 38.sp,
                                    lineHeight = 38.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                )

                                Spacer(modifier = Modifier.height(22.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1.86f)
                                        .clip(RoundedCornerShape(24.dp)),
                                ) {
                                    AppIcon(
                                        iconPath = "cards/Cart 26.png",
                                        contentDescription = stringResource(R.string.new_card),
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.FillBounds,
                                    )
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                Text(
                                    text = stringResource(R.string.add_new_card_description),
                                    color = Color(0xFF1F1F2A),
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 22.sp,
                                    fontWeight = FontWeight.Medium,
                                )

                                Spacer(modifier = Modifier.height(18.dp))
                            }
                        }

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

                    bannerMessage?.let { message ->
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(top = 34.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0xFF5B259F))
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                        ) {
                            Text(
                                text = message,
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomBarIcon(iconPath: String, onClick: () -> Unit) {
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

