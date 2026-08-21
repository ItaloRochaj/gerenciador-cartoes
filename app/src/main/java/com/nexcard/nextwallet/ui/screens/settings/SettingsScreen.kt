package com.nexcard.nextwallet.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nexcard.nextwallet.R
import com.nexcard.nextwallet.domain.model.ThemeMode
import com.nexcard.nextwallet.ui.components.AppTopBar
import com.nexcard.nextwallet.ui.theme.PurpleDark

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit,
    onThemeSelected: (ThemeMode) -> Unit,
    onNotificationsSelected: (Boolean) -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
    ) {
        AppTopBar(title = stringResource(R.string.settings), onBack = onBack)

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            // Seção de perfil
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(PurpleDark),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(state.userName.take(1), color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineSmall)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(state.userName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.edit), modifier = Modifier.size(16.dp))
                }
            }

            HorizontalDivider()

            // Menu de configurações
            Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
                SettingMenuItem(
                    icon = Icons.Default.Person,
                    label = stringResource(R.string.profile),
                    onClick = {},
                )
                SettingMenuItem(
                    icon = Icons.Default.Notifications,
                    label = stringResource(R.string.notifications),
                    onClick = {},
                    trailing = {
                        Switch(checked = state.notifications, onCheckedChange = {
                            viewModel.setNotifications(it)
                            onNotificationsSelected(it)
                        })
                    },
                )
                SettingMenuItem(
                    icon = Icons.Default.Settings,
                    label = "Seu Next",
                    onClick = {},
                )
                SettingMenuItem(
                    icon = Icons.Default.Gavel,
                    label = stringResource(R.string.settings),
                    onClick = {},
                )
                SettingMenuItem(
                    icon = Icons.AutoMirrored.Filled.Help,
                    label = stringResource(R.string.support),
                    onClick = {},
                )
            }

            HorizontalDivider()

            // Seção de tema
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(stringResource(R.string.theme), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                ThemeMode.entries.forEach { mode ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.setTheme(mode)
                                onThemeSelected(mode)
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        RadioButton(selected = state.themeMode == mode, onClick = null)
                        Text(mode.name)
                    }
                }
            }

            HorizontalDivider()

            // Sobre
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(stringResource(R.string.about), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Text("Audrin Lucio", style = MaterialTheme.typography.bodySmall)
                Text("Pyetro Sabaraense", style = MaterialTheme.typography.bodySmall)
                Text("Ernani Ferreira", style = MaterialTheme.typography.bodySmall)
                Text("Italo Rocha", style = MaterialTheme.typography.bodySmall)
            }

            HorizontalDivider()

            // Botões de ação
            TextButton(onClick = viewModel::clearCache, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.clear_cache))
            }

            TextButton(
                onClick = { viewModel.logout(onLogout) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.logout), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun SettingMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    trailing: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f),
        ) {
            Icon(icon, contentDescription = label, modifier = Modifier.size(24.dp))
            Text(label, style = MaterialTheme.typography.bodyMedium)
        }
        if (trailing != null) {
            trailing()
        } else {
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
