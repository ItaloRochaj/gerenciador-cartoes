package com.nexcard.nextwallet.ui.screens.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nexcard.nextwallet.R
import com.nexcard.nextwallet.ui.components.PrimaryButton

@Composable
fun SignupScreen(
    onBackLogin: () -> Unit,
    viewModel: SignupViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.goLogin.collect { onBackLogin() }
    }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(stringResource(R.string.signup_title))
        OutlinedTextField(
            value = state.name,
            onValueChange = { viewModel.onEvent(SignupEvent.NameChanged(it)) },
            label = { Text(stringResource(R.string.name)) },
            isError = state.errorName != null,
            supportingText = { if (state.errorName != null) Text(state.errorName!!) },
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = state.email,
            onValueChange = { viewModel.onEvent(SignupEvent.EmailChanged(it)) },
            label = { Text(stringResource(R.string.email)) },
            isError = state.errorEmail != null,
            supportingText = { if (state.errorEmail != null) Text(state.errorEmail!!) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = state.password,
            onValueChange = { viewModel.onEvent(SignupEvent.PasswordChanged(it)) },
            label = { Text(stringResource(R.string.password)) },
            isError = state.errorPassword != null,
            supportingText = { if (state.errorPassword != null) Text(state.errorPassword!!) },
            visualTransformation = if (state.showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { viewModel.onEvent(SignupEvent.TogglePassword) }) {
                    Icon(
                        imageVector = if (state.showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = stringResource(R.string.show_or_hide_password),
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = state.confirmPassword,
            onValueChange = { viewModel.onEvent(SignupEvent.ConfirmChanged(it)) },
            label = { Text(stringResource(R.string.confirm_password)) },
            isError = state.errorConfirm != null,
            supportingText = { if (state.errorConfirm != null) Text(state.errorConfirm!!) },
            visualTransformation = if (state.showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
        )
        if (state.message != null) Text(state.message!!, modifier = Modifier.testTag("signupMessage"))
        PrimaryButton(
            text = stringResource(R.string.signup),
            enabled = !state.isLoading,
            onClick = { viewModel.onEvent(SignupEvent.Submit) },
        )
        if (state.isLoading) CircularProgressIndicator()
        TextButton(onClick = viewModel::onGoLoginClick, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.already_have_account))
        }
    }
}
