package com.nexcard.nextwallet.ui.theme

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont

private fun googleSansFlexFamily(context: Context): FontFamily {
	val certs = loadGooglePlayCertificates(context)
	if (certs.isEmpty()) return FontFamily.SansSerif

	val provider = GoogleFont.Provider(
		providerAuthority = "com.google.android.gms.fonts",
		providerPackage = "com.google.android.gms",
		certificates = listOf(certs),
	)

	return FontFamily(
		Font(googleFont = GoogleFont("Google Sans Flex"), fontProvider = provider, weight = FontWeight.Normal),
		Font(googleFont = GoogleFont("Google Sans Flex"), fontProvider = provider, weight = FontWeight.Medium),
		Font(googleFont = GoogleFont("Google Sans Flex"), fontProvider = provider, weight = FontWeight.SemiBold),
		Font(googleFont = GoogleFont("Google Sans Flex"), fontProvider = provider, weight = FontWeight.Bold),
	)
}

private fun loadGooglePlayCertificates(context: Context): List<ByteArray> {
	return runCatching {
		val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
			context.packageManager.getPackageInfo("com.google.android.gms", PackageManager.GET_SIGNING_CERTIFICATES)
		} else {
			@Suppress("DEPRECATION")
			context.packageManager.getPackageInfo("com.google.android.gms", PackageManager.GET_SIGNATURES)
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
			packageInfo.signingInfo?.apkContentsSigners?.map { it.toByteArray() }.orEmpty()
		} else {
			@Suppress("DEPRECATION")
			packageInfo.signatures?.map { it.toByteArray() }.orEmpty()
		}
	}.getOrDefault(emptyList())
}

private fun Typography.withFamily(fontFamily: FontFamily): Typography = copy(
	displayLarge = displayLarge.copy(fontFamily = fontFamily),
	displayMedium = displayMedium.copy(fontFamily = fontFamily),
	displaySmall = displaySmall.copy(fontFamily = fontFamily),
	headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
	headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
	headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
	titleLarge = titleLarge.copy(fontFamily = fontFamily),
	titleMedium = titleMedium.copy(fontFamily = fontFamily),
	titleSmall = titleSmall.copy(fontFamily = fontFamily),
	bodyLarge = bodyLarge.copy(fontFamily = fontFamily),
	bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
	bodySmall = bodySmall.copy(fontFamily = fontFamily),
	labelLarge = labelLarge.copy(fontFamily = fontFamily),
	labelMedium = labelMedium.copy(fontFamily = fontFamily),
	labelSmall = labelSmall.copy(fontFamily = fontFamily),
)

fun appTypography(context: Context): Typography = Typography().withFamily(googleSansFlexFamily(context))
