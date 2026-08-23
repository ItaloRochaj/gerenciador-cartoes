package com.nexcard.nextwallet.ui.screens.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nexcard.nextwallet.R
import com.nexcard.nextwallet.ui.components.AppIcon
import com.nexcard.nextwallet.ui.components.EmptyContent
import com.nexcard.nextwallet.ui.components.ErrorContent
import com.nexcard.nextwallet.ui.components.LoadingContent
import com.nexcard.nextwallet.util.ScreenLoadState

@Composable
fun CardsScreen(
    onBack: () -> Unit,
    onCardClick: (String) -> Unit,
    onAddCard: () -> Unit,
    onGoCards: () -> Unit,
    onGoFinancial: () -> Unit,
    onGoSettings: () -> Unit,
    viewModel: CardsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (val loadState = state.loadState) {
        ScreenLoadState.Loading -> LoadingContent(modifier = Modifier.fillMaxSize())
        is ScreenLoadState.Error -> ErrorContent(loadState.message, viewModel::refresh)
        ScreenLoadState.Empty -> EmptyContent(stringResource(R.string.no_cards), stringResource(R.string.request_card), onAddCard)
        else -> {
            val cardIds = state.cards.map { it.id }
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
                        .padding(horizontal = 24.dp, vertical = 18.dp),
                ) {
                    Spacer(modifier = Modifier.height(14.dp))

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

                    Spacer(modifier = Modifier.height(10.dp))

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Seus Cartões",
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF21195B),
                        fontSize = 36.sp,
                        lineHeight = 36.sp,
                        fontWeight = FontWeight.Bold,
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    CardsImageItem("cards/Cart  Geometric  34.png") { onCardClick(cardIds.getOrElse(0) { "card_01" }) }
                    Spacer(modifier = Modifier.height(14.dp))
                    CardsImageItem("cards/Cart 25.png") { onCardClick(cardIds.getOrElse(1) { "card_02" }) }
                    Spacer(modifier = Modifier.height(14.dp))
                    CardsImageItem("cards/Cart 24.jpg") { onCardClick(cardIds.getOrElse(2) { "card_03" }) }

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
                        BottomBarIcon(iconPath = "icons/transactions/chart-2.png", onClick = onGoFinancial)
                        BottomBarIcon(iconPath = "icons/notification-bing.png", onClick = {})
                        BottomBarIcon(iconPath = "icons/setting.png", onClick = onGoSettings)
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }
}

@Composable
private fun CardsImageItem(
    path: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.86f)
            .clip(RoundedCornerShape(24.dp))
            .clickable(onClick = onClick),
    ) {
        AppIcon(
            iconPath = path,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        )
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
