package com.nexcard.nextwallet.ui.screens.carddetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nexcard.nextwallet.R
import com.nexcard.nextwallet.domain.model.Card
import com.nexcard.nextwallet.ui.components.AppIcon
import com.nexcard.nextwallet.ui.components.ConfirmationDialog
import com.nexcard.nextwallet.ui.components.ErrorContent
import com.nexcard.nextwallet.ui.components.LoadingContent
import com.nexcard.nextwallet.ui.theme.darkAwareTextColor
import com.nexcard.nextwallet.ui.util.resolveCardAssetPath
import com.nexcard.nextwallet.util.MoneyFormatter
import com.nexcard.nextwallet.util.ScreenLoadState

@Composable
fun CardDetailScreen(
    onBack: () -> Unit,
    onDeleted: () -> Unit,
    viewModel: CardDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (val load = state.loadState) {
        ScreenLoadState.Loading -> LoadingContent(modifier = Modifier.fillMaxSize())
        is ScreenLoadState.Error -> ErrorContent(load.message, onBack)
        else -> {
            val card = state.card ?: return
            CardDetailContent(
                card = card,
                onBack = onBack,
                onToggleFavorite = viewModel::toggleFavorite,
                onAskBlockToggle = viewModel::askBlockToggle,
                onToggleVirtual = viewModel::toggleVirtualCard,
                onAskDelete = viewModel::askDelete,
                showVirtualCard = state.showVirtualCard,
                showCardBack = state.showCardBack,
                revealNumber = state.revealNumber,
                revealCvv = state.revealCvv,
                onFlipVirtualCard = viewModel::flipVirtualCard,
                onToggleNumber = viewModel::toggleNumber,
                onToggleCvv = viewModel::toggleCvv,
            )
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

@Composable
private fun CardDetailContent(
    card: Card,
    onBack: () -> Unit,
    onToggleFavorite: () -> Unit,
    onAskBlockToggle: () -> Unit,
    onToggleVirtual: () -> Unit,
    onAskDelete: () -> Unit,
    showVirtualCard: Boolean,
    showCardBack: Boolean,
    revealNumber: Boolean,
    revealCvv: Boolean,
    onFlipVirtualCard: () -> Unit,
    onToggleNumber: () -> Unit,
    onToggleCvv: () -> Unit,
) {
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

            Text(
                text = stringResource(R.string.card_details),
                color = darkAwareTextColor(Color(0xFF21195B)),
                fontSize = 34.sp,
                lineHeight = 36.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.86f)
                    .clip(RoundedCornerShape(24.dp)),
            ) {
                AppIcon(
                    iconPath = cardAssetPath(card),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            DetailRow(label = "Número", value = card.maskedNumber)
            DetailRow(label = stringResource(R.string.name), value = card.holderName)
            DetailRow(label = stringResource(R.string.bank), value = "Italo Bank")
            DetailRow(label = stringResource(R.string.account), value = card.lastFourDigits)
            DetailRow(label = "Bandeira", value = card.brand.name)
            DetailRow(label = "Tipo", value = card.type.name)
            DetailRow(label = "Status", value = card.status.name)
            DetailRow(label = "Validade", value = card.expirationDate)
            DetailRow(label = "Limite total", value = MoneyFormatter.format(card.totalLimitCents))
            DetailRow(label = "Limite usado", value = MoneyFormatter.format(card.usedLimitCents))
            DetailRow(label = "Limite disponível", value = MoneyFormatter.format(card.availableLimitCents))

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                TextAction(text = stringResource(R.string.favorite), onClick = onToggleFavorite)
                TextAction(text = stringResource(R.string.block_or_unblock), onClick = onAskBlockToggle)
                TextAction(text = stringResource(R.string.virtual_card), onClick = onToggleVirtual)
            }

            if (showVirtualCard) {
                Spacer(modifier = Modifier.height(14.dp))

                val rotationAngle by animateFloatAsState(
                    targetValue = if (showCardBack) 180f else 0f,
                    animationSpec = tween(durationMillis = 520),
                    label = "virtual-card-flip",
                )
                val density = LocalDensity.current.density
                val showBackFace = rotationAngle > 90f

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFF4F4F6))
                        .clickable(onClick = onFlipVirtualCard)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer {
                                rotationY = rotationAngle
                                cameraDistance = 12f * density
                            },
                    ) {
                        if (!showBackFace) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = if (revealNumber) virtualCardNumber(card) else "**** **** **** ${virtualCardLastFour(card)}",
                                    color = darkAwareTextColor(Color(0xFF221A56)),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                )
                                Text(
                                    text = "Toque para virar",
                                    color = darkAwareTextColor(Color(0xFF5A27A2)),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                )
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .graphicsLayer { rotationY = 180f },
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(28.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFF2A2A2A)),
                                )
                                Text(
                                    text = if (revealCvv) "CVV: ${virtualCardCvv(card)}" else "CVV: ***",
                                    color = darkAwareTextColor(Color(0xFF221A56)),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        TextAction(
                            text = if (revealNumber) "Ocultar número" else "Mostrar número",
                            onClick = onToggleNumber,
                        )
                        TextAction(
                            text = if (revealCvv) "Ocultar CVV" else "Mostrar CVV",
                            onClick = onToggleCvv,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable(onClick = onAskDelete),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Box(
                    modifier = Modifier.size(52.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    AppIcon(
                        iconPath = "icons/Ellipse 35.png",
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds,
                    )
                    AppIcon(
                        iconPath = "icons/trash.png",
                        contentDescription = stringResource(R.string.delete),
                        modifier = Modifier.size(22.dp),
                    )
                }
                Text(
                    text = stringResource(R.string.delete),
                    color = darkAwareTextColor(Color(0xFF5B259F)),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            color = Color(0xFF666676),
            fontSize = 15.sp,
        )
        Text(
            text = value,
            color = Color(0xFF222033),
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun TextAction(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        color = darkAwareTextColor(Color(0xFF5A27A2)),
        fontSize = 17.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.clickable(onClick = onClick),
    )
}

private fun cardAssetPath(card: Card): String {
    return resolveCardAssetPath(
        colorStyle = card.colorStyle,
        productId = card.productId,
        cardId = card.id,
    )
}

private fun virtualCardCvv(card: Card): String {
    val digits = card.lastFourDigits.filter { it.isDigit() }
    val base = digits.toIntOrNull() ?: 0
    return ((base * 3 + 157) % 1000).toString().padStart(3, '0')
}

private fun virtualCardLastFour(card: Card): String {
    val physical = card.lastFourDigits.filter { it.isDigit() }.takeLast(4).padStart(4, '0')
    val physicalInt = physical.toIntOrNull() ?: 0
    val candidate = ((physicalInt + 1379) % 10_000).toString().padStart(4, '0')
    return if (candidate == physical) ((physicalInt + 1) % 10_000).toString().padStart(4, '0') else candidate
}

private fun virtualCardNumber(card: Card): String {
    val lastFour = virtualCardLastFour(card)
    // Keep a stable virtual PAN format per card while guaranteeing it differs from physical last digits.
    return "4444 8231 5670 $lastFour"
}
