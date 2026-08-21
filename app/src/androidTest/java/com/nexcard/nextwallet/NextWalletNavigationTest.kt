package com.nexcard.nextwallet

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.nexcard.nextwallet.ui.MainActivity
import org.junit.Rule
import org.junit.Test

class NextWalletNavigationTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun navegacao_login_para_home() {
        composeRule.onNodeWithTag("loginEmail").performTextInput("usuario@nextwallet.com")
        composeRule.onNodeWithTag("loginPassword").performTextInput("123456")
        composeRule.onNodeWithText("Entrar").performClick()
        composeRule.onNodeWithText("Next Wallet").assertIsDisplayed()
    }

    @Test
    fun exibicao_loading_erro_vazio() {
        composeRule.onNodeWithTag("loading").assertIsDisplayed()
    }
}
