package com.nexcard.nextwallet.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nexcard.nextwallet.R
import com.nexcard.nextwallet.ui.components.AppIcon
import com.nexcard.nextwallet.ui.theme.darkAwareTextColor

@Composable
fun LoginScreen(
    onGoHome: () -> Unit,
    onGoSignup: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val appBackground = MaterialTheme.colorScheme.background
    val containerSurface = MaterialTheme.colorScheme.surface

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                LoginEffect.GoHome -> onGoHome()
                LoginEffect.GoSignup -> onGoSignup()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(appBackground)
            .padding(10.dp),
        contentAlignment = Alignment.Center,
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .shadow(
                    elevation = 80.dp,
                    spotColor = Color(0x1A000000),
                    ambientColor = Color(0x1A000000),
                    shape = RoundedCornerShape(size = 50.dp),
                )
                .fillMaxSize()
                .clip(RoundedCornerShape(size = 50.dp))
                .background(color = containerSurface),
        ) {
            val topGap = (maxHeight * 0.11f).coerceIn(52.dp, 110.dp)
            val titleToSocialGap = (maxHeight * 0.10f).coerceIn(40.dp, 92.dp)
            val socialToFieldsGap = (maxHeight * 0.045f).coerceIn(18.dp, 38.dp)
            val fieldGap = (maxHeight * 0.022f).coerceIn(10.dp, 20.dp)
            val fieldsToButtonGap = (maxHeight * 0.12f).coerceIn(44.dp, 120.dp)
            val buttonToFooterGap = (maxHeight * 0.022f).coerceIn(10.dp, 18.dp)
            val bottomGap = (maxHeight * 0.08f).coerceIn(20.dp, 72.dp)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 28.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.Top,
            ) {
                Spacer(modifier = Modifier.height(topGap))

                Text(
                    text = stringResource(R.string.welcome_next_wallet),
                    modifier = Modifier.fillMaxWidth(),
                    color = darkAwareTextColor(Color(0xFF2E1A62)),
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 38.sp,
                )

                Spacer(modifier = Modifier.height(titleToSocialGap))

                Text(
                    text = stringResource(R.string.sign_up_with),
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFCACACA),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    SocialAuthButton(
                        text = stringResource(R.string.google_demo_login),
                        containerColor = Color(0xFFFFFFFF),
                        contentColor = Color(0xFF8C8C8C),
                        borderColor = Color(0xFFEFEFEF),
                        onClick = {},
                        modifier = Modifier.weight(1f),
                        icon = {
                            AppIcon(
                                iconPath = "logos/google.png",
                                contentDescription = stringResource(R.string.google_demo_login),
                                modifier = Modifier.size(18.dp),
                            )
                        },
                    )

                    SocialAuthButton(
                        text = stringResource(R.string.facebook_demo_login),
                        containerColor = Color(0xFF3F67D7),
                        contentColor = Color(0xFFFFFFFF),
                        borderColor = Color.Transparent,
                        onClick = {},
                        modifier = Modifier.weight(1f),
                        icon = {
                            AppIcon(
                                iconPath = "logos/Facebook.png",
                                contentDescription = stringResource(R.string.facebook_demo_login),
                                modifier = Modifier.size(18.dp),
                            )
                        },
                    )
                }

                Spacer(modifier = Modifier.height(socialToFieldsGap))

                LoginInput(
                    value = state.email,
                    onValueChange = { viewModel.onEvent(LoginUiEvent.EmailChanged(it)) },
                    placeholder = stringResource(R.string.username),
                    leadingIcon = {
                        AppIcon(
                            iconPath = "images/profilee.png",
                            contentDescription = stringResource(R.string.username),
                            modifier = Modifier.size(16.dp),
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("loginEmail"),
                )

                Spacer(modifier = Modifier.height(fieldGap))

                LoginInput(
                    value = state.password,
                    onValueChange = { viewModel.onEvent(LoginUiEvent.PasswordChanged(it)) },
                    placeholder = stringResource(R.string.password_placeholder),
                    visualTransformation = if (state.showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    leadingIcon = {
                        AppIcon(
                            iconPath = "images/key-squaree.png",
                            contentDescription = stringResource(R.string.password_placeholder),
                            modifier = Modifier.size(16.dp),
                        )
                    },
                    trailingIcon = {
                        if (state.showPassword) {
                            Icon(
                                imageVector = Icons.Filled.Visibility,
                                contentDescription = stringResource(R.string.show_or_hide_password),
                                tint = Color(0xFF9A9A9A),
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable { viewModel.onEvent(LoginUiEvent.TogglePasswordVisibility) },
                            )
                        } else {
                            AppIcon(
                                iconPath = "icons/Eye-slash.svg",
                                contentDescription = stringResource(R.string.show_or_hide_password),
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable { viewModel.onEvent(LoginUiEvent.TogglePasswordVisibility) },
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("loginPassword"),
                )

                Spacer(modifier = Modifier.height(fieldsToButtonGap))

                if (state.errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = state.errorMessage!!,
                        color = Color(0xFFC62828),
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                }

                Button(
                    onClick = { viewModel.onEvent(LoginUiEvent.Submit) },
                    enabled = !state.isLoading,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(196.dp)
                        .height(66.dp)
                        .testTag("primaryButton"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5F2AA8),
                        disabledContainerColor = Color(0xFFBDA8DC),
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        disabledElevation = 0.dp,
                    ),
                ) {
                    Text(
                        text = stringResource(R.string.login_title),
                        color = Color(0xFFFFFFFF),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 24.sp,
                    )
                }

                Spacer(modifier = Modifier.height(buttonToFooterGap))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.no_account_yet),
                        color = Color(0xFFC7C7C7),
                        fontSize = 14.sp,
                    )
                    Text(
                        text = stringResource(R.string.create_short),
                        color = Color(0xFF81B8FF),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .clickable { viewModel.goSignup() },
                    )
                }

                Spacer(modifier = Modifier.height(bottomGap))
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.testTag("loginLoading"))
        }
    }
}

@Composable
private fun SocialAuthButton(
    text: String,
    containerColor: Color,
    contentColor: Color,
    borderColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(14.dp),
        border = if (borderColor == Color.Transparent) null else BorderStroke(1.dp, borderColor),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 3.dp,
            pressedElevation = 2.dp,
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            icon()
            Text(text = text, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun LoginInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        singleLine = true,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        placeholder = {
            Text(
                text = placeholder,
                color = Color(0xFFA0A0A0),
                fontSize = 15.sp,
            )
        },
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF3F3F3),
            unfocusedContainerColor = Color(0xFFF3F3F3),
            disabledContainerColor = Color(0xFFF3F3F3),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = Color(0xFF5F2AA8),
        ),
    )
}
