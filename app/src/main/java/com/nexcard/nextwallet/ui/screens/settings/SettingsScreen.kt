package com.nexcard.nextwallet.ui.screens.settings

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nexcard.nextwallet.R
import com.nexcard.nextwallet.domain.model.ThemeMode
import com.nexcard.nextwallet.ui.components.AppIcon
import com.nexcard.nextwallet.ui.components.ProfileAvatar
import com.nexcard.nextwallet.ui.theme.darkAwareTextColor

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit,
    onGoCards: () -> Unit,
    onGoFinancial: () -> Unit,
    onGoSettings: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isDarkMode = state.themeMode == ThemeMode.DARK
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
                .navigationBarsPadding()
                .clip(RoundedCornerShape(50.dp))
                .background(containerSurface)
                .padding(horizontal = 20.dp, vertical = 18.dp),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
            ) {
                Spacer(modifier = Modifier.height(6.dp))

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

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.settings),
                    color = darkAwareTextColor(Color(0xFF231A57)),
                    fontSize = 36.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    ProfileAvatar(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape),
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(
                            text = state.userName,
                            color = darkAwareTextColor(Color(0xFF231A57)),
                            fontSize = 34.sp,
                            lineHeight = 34.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        AppIcon(
                            iconPath = "images/edit-2.png",
                            contentDescription = stringResource(R.string.edit),
                            modifier = Modifier.size(18.dp),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SettingMenuItemAsset(iconPath = "images/profile.png", label = stringResource(R.string.profile), onClick = {})
                    SettingMenuItemAsset(
                        iconPath = "images/notification.png",
                        label = stringResource(R.string.notifications),
                        onClick = {},
                        trailing = {
                            Switch(
                                checked = state.notifications,
                                onCheckedChange = {
                                    viewModel.setNotifications(it)
                                },
                            )
                        },
                    )
                    SettingMenuItemAsset(iconPath = "images/wallet-2.png", label = "Seu Next", onClick = {})
                    SettingMenuItemAsset(
                        iconPath = "images/key-square.png",
                        label = stringResource(R.string.theme),
                        onClick = {},
                        trailing = {
                            Switch(
                                checked = isDarkMode,
                                onCheckedChange = { enabled ->
                                    val mode = if (enabled) ThemeMode.DARK else ThemeMode.LIGHT
                                    viewModel.setTheme(mode)
                                },
                            )
                        },
                    )
                    SettingMenuItemAsset(iconPath = "images/call-calling.png", label = stringResource(R.string.support), onClick = {})
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .clip(CircleShape)
                        .shadow(10.dp, CircleShape, ambientColor = Color(0x22000000), spotColor = Color(0x22000000))
                        .background(Color.White)
                        .clickable { viewModel.logout(onLogout) },
                    contentAlignment = Alignment.Center,
                ) {
                    AppIcon(
                        iconPath = "images/login.png",
                        contentDescription = stringResource(R.string.logout),
                        modifier = Modifier.size(30.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

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

@Composable
private fun SettingMenuItemAsset(
    iconPath: String,
    label: String,
    onClick: () -> Unit,
    trailing: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f),
        ) {
            Box(
                modifier = Modifier
                    .size(62.dp)
                    .clip(CircleShape)
                    .shadow(8.dp, CircleShape, ambientColor = Color(0x22000000), spotColor = Color(0x22000000))
                    .background(Color.White),
                contentAlignment = Alignment.Center,
            ) {
                AppIcon(
                    iconPath = iconPath,
                    contentDescription = label,
                    modifier = Modifier
                        .size(52.dp)
                        .padding(2.dp),
                )
            }
            Text(
                text = label,
                color = darkAwareTextColor(Color(0xFF261F58)),
                fontSize = 18.sp,
            )
        }
        if (trailing != null) {
            trailing()
        } else {
            Text(
                text = "›",
                color = darkAwareTextColor(Color(0xFF261F58)),
                fontSize = 24.sp,
                modifier = Modifier.padding(end = 4.dp),
            )
        }
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
