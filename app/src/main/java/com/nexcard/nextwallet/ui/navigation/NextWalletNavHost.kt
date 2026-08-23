package com.nexcard.nextwallet.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nexcard.nextwallet.R
import com.nexcard.nextwallet.domain.model.ThemeMode
import com.nexcard.nextwallet.ui.components.BottomNavItem
import com.nexcard.nextwallet.ui.components.BottomNavigationBar
import com.nexcard.nextwallet.ui.components.NextWalletScaffold
import com.nexcard.nextwallet.ui.screens.addcard.AddCardScreen
import com.nexcard.nextwallet.ui.screens.carddetail.CardDetailScreen
import com.nexcard.nextwallet.ui.screens.cards.CardsScreen
import com.nexcard.nextwallet.ui.screens.financial.ConsolidatedScreen
import com.nexcard.nextwallet.ui.screens.financial.FinancialScreen
import com.nexcard.nextwallet.ui.screens.home.HomeScreen
import com.nexcard.nextwallet.ui.screens.login.LoginScreen
import com.nexcard.nextwallet.ui.screens.settings.SettingsScreen
import com.nexcard.nextwallet.ui.screens.signup.SignupScreen

@Composable
fun NextWalletNavHost(
    isSessionActive: Boolean,
    themeMode: ThemeMode,
    notificationsEnabled: Boolean,
    onThemeChanged: (ThemeMode) -> Unit,
    onNotificationsChanged: (Boolean) -> Unit,
) {
    val navController = rememberNavController()
    val snack = remember { SnackbarHostState() }
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val current = navBackStackEntry?.destination?.route
    val startDestination = if (isSessionActive) AppRoute.Home.route else AppRoute.Login.route

    val bottomItems = listOf(
        BottomNavItem(AppRoute.Home.route, stringResource(R.string.home), Icons.Default.Home),
        BottomNavItem(AppRoute.Cards.route, stringResource(R.string.cards), Icons.Default.CreditCard),
        BottomNavItem(AppRoute.Financial.route, stringResource(R.string.financial), Icons.Default.PieChart),
        BottomNavItem(AppRoute.Settings.route, stringResource(R.string.settings), Icons.Default.Settings),
    )

    val showBottomBar = current in setOf(AppRoute.Settings.route)

    NextWalletScaffold(
        snackbarHostState = snack,
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(items = bottomItems, currentRoute = current ?: AppRoute.Home.route) { route ->
                    navController.navigate(route) {
                        popUpTo(AppRoute.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        },
    ) { inner ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.fillMaxSize(),
        ) {
            composable(AppRoute.Login.route) {
                LoginScreen(
                    onGoHome = {
                        navController.navigate(AppRoute.Home.route) {
                            popUpTo(AppRoute.Login.route) { inclusive = true }
                        }
                    },
                    onGoSignup = { navController.navigate(AppRoute.Signup.route) },
                )
            }
            composable(AppRoute.Signup.route) {
                SignupScreen(
                    onBackLogin = {
                        navController.popBackStack()
                    },
                )
            }
            composable(AppRoute.Home.route) {
                HomeScreen(
                    onGoCards = { navController.navigate(AppRoute.Cards.route) },
                    onGoFinancial = { cardId, month ->
                        navController.navigate(AppRoute.Financial.create(cardId = cardId, month = month))
                    },
                    onGoConsolidated = { cardId, month ->
                        navController.navigate(AppRoute.Consolidated.create(cardId = cardId, month = month))
                    },
                    onGoSettings = { navController.navigate(AppRoute.Settings.route) },
                    onGoAddCard = { navController.navigate(AppRoute.AddCard.route) },
                    onGoDetail = { navController.navigate(AppRoute.CardDetail.create(it)) },
                )
            }
            composable(AppRoute.Cards.route) {
                CardsScreen(
                    onBack = {
                        val didPopToHome = navController.popBackStack(AppRoute.Home.route, inclusive = false)
                        if (!didPopToHome) {
                            navController.navigate(AppRoute.Home.route) {
                                popUpTo(0) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    },
                    onCardClick = { navController.navigate(AppRoute.CardDetail.create(it)) },
                    onAddCard = { navController.navigate(AppRoute.AddCard.route) },
                    onGoCards = { navController.navigate(AppRoute.Cards.route) },
                    onGoFinancial = { navController.navigate(AppRoute.Financial.route) },
                    onGoSettings = { navController.navigate(AppRoute.Settings.route) },
                )
            }
            composable(
                route = AppRoute.Financial.routeWithArgs,
                arguments = listOf(
                    navArgument(AppRoute.Financial.ARG_CARD_ID) {
                        type = NavType.StringType
                        defaultValue = ""
                    },
                    navArgument(AppRoute.Financial.ARG_MONTH) {
                        type = NavType.StringType
                        defaultValue = ""
                    },
                ),
            ) {
                FinancialScreen(
                    onOpenConsolidated = { cardId, month ->
                        navController.navigate(AppRoute.Consolidated.create(cardId = cardId, month = month))
                    },
                )
            }
            composable(
                route = AppRoute.Consolidated.routeWithArgs,
                arguments = listOf(
                    navArgument(AppRoute.Consolidated.ARG_CARD_ID) {
                        type = NavType.StringType
                        defaultValue = ""
                    },
                    navArgument(AppRoute.Consolidated.ARG_MONTH) {
                        type = NavType.StringType
                        defaultValue = ""
                    },
                ),
            ) {
                ConsolidatedScreen(onBack = { navController.popBackStack() })
            }
            composable(AppRoute.AddCard.route) {
                AddCardScreen(
                    onBack = { navController.popBackStack() },
                    onRequested = {
                        navController.navigate(AppRoute.Cards.route) {
                            popUpTo(AppRoute.AddCard.route) { inclusive = true }
                        }
                    },
                )
            }
            composable(
                route = AppRoute.CardDetail.route,
                arguments = listOf(navArgument("cardId") { type = NavType.StringType }),
            ) {
                CardDetailScreen(
                    onBack = { navController.popBackStack() },
                    onDeleted = {
                        navController.navigate(AppRoute.Cards.route) {
                            popUpTo(AppRoute.Cards.route) { inclusive = false }
                        }
                    },
                )
            }
            composable(AppRoute.Settings.route) {
                SettingsScreen(
                    onBack = { navController.popBackStack() },
                    onLogout = {
                        navController.navigate(AppRoute.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onThemeSelected = onThemeChanged,
                    onNotificationsSelected = onNotificationsChanged,
                )
            }
        }
    }
}
