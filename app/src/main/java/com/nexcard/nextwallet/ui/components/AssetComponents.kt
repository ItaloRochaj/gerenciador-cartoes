package com.nexcard.nextwallet.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

/**
 * Asset Manager - Gerenciador centralizado de assets para toda a aplicação
 *
 * Esta classe fornece funções auxiliares para carregar SVGs e PNGs de forma
 * consistente em toda a aplicação.
 */

// ============================================================================
// ÍCONES GERAIS
// ============================================================================

/**
 * Componente para carregar ícones SVG dos assets
 */
@Composable
fun AppIcon(
    iconPath: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
    size: Dp = 24.dp,
    contentScale: ContentScale = ContentScale.Fit
) {
    AsyncImage(
        model = "file:///android_asset/$iconPath",
        contentDescription = contentDescription,
        modifier = modifier.then(Modifier),
        contentScale = contentScale,
    )
}

// ============================================================================
// ÍCONES DE AÇÃO
// ============================================================================

object ActionIcons {
    const val ADD = "icons/actions/add-circle.svg"
    const val EDIT = "icons/actions/edit-2.svg"
    const val EXPORT = "icons/actions/export.svg"
    const val BACK = "icons/actions/arrow-circle-left.svg"
}

@Composable
fun AddCircleIcon(
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified
) {
    AppIcon(
        iconPath = ActionIcons.ADD,
        contentDescription = "Add",
        modifier = modifier
    )
}

@Composable
fun EditIcon(
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified
) {
    AppIcon(
        iconPath = ActionIcons.EDIT,
        contentDescription = "Edit",
        modifier = modifier
    )
}

@Composable
fun ExportIcon(
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified
) {
    AppIcon(
        iconPath = ActionIcons.EXPORT,
        contentDescription = "Export",
        modifier = modifier
    )
}

@Composable
fun BackIcon(
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified
) {
    AppIcon(
        iconPath = ActionIcons.BACK,
        contentDescription = "Back",
        modifier = modifier
    )
}

// ============================================================================
// ÍCONES DE AUTENTICAÇÃO E PERFIL
// ============================================================================

object AuthIcons {
    const val GOOGLE = "logos/google.png"
    const val FACEBOOK = "logos/Facebook.png"
    const val LOGIN = "icons/login.svg"
    const val PROFILE = "icons/profile.svg"
}

@Composable
fun GoogleIcon(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp
) {
    AppIcon(
        iconPath = AuthIcons.GOOGLE,
        contentDescription = "Google",
        modifier = modifier,
        size = size
    )
}

@Composable
fun FacebookIcon(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp
) {
    AppIcon(
        iconPath = AuthIcons.FACEBOOK,
        contentDescription = "Facebook",
        modifier = modifier,
        size = size
    )
}

@Composable
fun LoginIcon(
    modifier: Modifier = Modifier
) {
    AppIcon(
        iconPath = AuthIcons.LOGIN,
        contentDescription = "Login",
        modifier = modifier
    )
}

@Composable
fun ProfileIcon(
    modifier: Modifier = Modifier
) {
    AppIcon(
        iconPath = AuthIcons.PROFILE,
        contentDescription = "Profile",
        modifier = modifier
    )
}

// ============================================================================
// ÍCONES DE CARTÃO
// ============================================================================

object CardIcons {
    const val CHIP = "icons/card/chip.svg"
    const val NFC = "icons/card/NFC.svg"
}

@Composable
fun ChipIcon(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp
) {
    AppIcon(
        iconPath = CardIcons.CHIP,
        contentDescription = "Chip",
        modifier = modifier,
        size = size
    )
}

@Composable
fun NFCIcon(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp
) {
    AppIcon(
        iconPath = CardIcons.NFC,
        contentDescription = "NFC",
        modifier = modifier,
        size = size
    )
}

// ============================================================================
// ÍCONES DE TRANSAÇÃO
// ============================================================================

object TransactionIcons {
    const val SEND_MONEY = "icons/transactions/money-send.svg"
    const val CONVERT = "icons/transactions/convert.svg"
    const val CHART = "icons/transactions/chart-2.svg"
    const val WALLET = "icons/wallet-2.svg"
}

@Composable
fun SendMoneyIcon(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp
) {
    AppIcon(
        iconPath = TransactionIcons.SEND_MONEY,
        contentDescription = "Send Money",
        modifier = modifier,
        size = size
    )
}

@Composable
fun ConvertIcon(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp
) {
    AppIcon(
        iconPath = TransactionIcons.CONVERT,
        contentDescription = "Convert",
        modifier = modifier,
        size = size
    )
}

@Composable
fun ChartIcon(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp
) {
    AppIcon(
        iconPath = TransactionIcons.CHART,
        contentDescription = "Chart",
        modifier = modifier,
        size = size
    )
}

@Composable
fun WalletIcon(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp
) {
    AppIcon(
        iconPath = TransactionIcons.WALLET,
        contentDescription = "Wallet",
        modifier = modifier,
        size = size
    )
}

// ============================================================================
// ÍCONES DE NAVEGAÇÃO
// ============================================================================

object NavigationIcons {
    const val PRIMARY_NAV = "icons/navigation/Primary Navigation.svg"
    const val PRIMARY_NAV_ALT = "icons/navigation/Primary Navigation (1).svg"
}

@Composable
fun NavigationIcon(
    modifier: Modifier = Modifier,
    variant: Int = 1
) {
    val path = if (variant == 2) NavigationIcons.PRIMARY_NAV_ALT else NavigationIcons.PRIMARY_NAV
    AppIcon(
        iconPath = path,
        contentDescription = "Navigation",
        modifier = modifier
    )
}

// ============================================================================
// ÍCONES DE NOTIFICAÇÃO E SUPORTE
// ============================================================================

object NotificationIcons {
    const val NOTIFICATION = "icons/notification.svg"
    const val NOTIFICATION_BING = "icons/notification-bing.svg"
    const val CALL = "icons/call-calling.svg"
    const val SECURITY = "icons/key-square.svg"
}

@Composable
fun NotificationIcon(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp
) {
    AppIcon(
        iconPath = NotificationIcons.NOTIFICATION,
        contentDescription = "Notifications",
        modifier = modifier,
        size = size
    )
}

@Composable
fun CallIcon(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp
) {
    AppIcon(
        iconPath = NotificationIcons.CALL,
        contentDescription = "Call",
        modifier = modifier,
        size = size
    )
}

@Composable
fun SecurityIcon(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp
) {
    AppIcon(
        iconPath = NotificationIcons.SECURITY,
        contentDescription = "Security",
        modifier = modifier,
        size = size
    )
}

// ============================================================================
// LOGOS DE CARTÕES
// ============================================================================

object CardLogos {
    const val LOGO = "logos/Logo.svg"
    const val LOGO_ALT1 = "logos/Logo (1).svg"
    const val LOGO_ALT2 = "logos/Logo (2).svg"
}

@Composable
fun CardLogo(
    modifier: Modifier = Modifier,
    variant: Int = 1,
    size: Dp = 32.dp
) {
    val path = when (variant) {
        2 -> CardLogos.LOGO_ALT1
        3 -> CardLogos.LOGO_ALT2
        else -> CardLogos.LOGO
    }
    AppIcon(
        iconPath = path,
        contentDescription = "Card Logo",
        modifier = modifier,
        size = size
    )
}

// ============================================================================
// LOGOS DE REDES SOCIAIS
// ============================================================================

object SocialLogos {
    const val AMAZON = "icons/social/amazon-round-circle-logo-symbol-button-19641_128 1.svg"
    const val PAYPAL = "icons/social/Paypal logo.svg"
}

@Composable
fun AmazonLogo(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp
) {
    AppIcon(
        iconPath = SocialLogos.AMAZON,
        contentDescription = "Amazon",
        modifier = modifier,
        size = size
    )
}

@Composable
fun PayPalLogo(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp
) {
    AppIcon(
        iconPath = SocialLogos.PAYPAL,
        contentDescription = "PayPal",
        modifier = modifier,
        size = size
    )
}

// ============================================================================
// IMAGENS (PNG)
// ============================================================================

object AppImages {
    const val PROFILE_PICTURE = "images/Profile Picture.png"
    const val MASK_GROUP = "images/Mask Group.svg"
}

@Composable
fun ProfileAvatar(
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    contentScale: ContentScale = ContentScale.Crop
) {
    AsyncImage(
        model = "file:///android_asset/${AppImages.PROFILE_PICTURE}",
        contentDescription = "Profile Avatar",
        modifier = modifier.then(Modifier),
        contentScale = contentScale
    )
}

// ============================================================================
// DESIGNS DE CARTÃO
// ============================================================================

object CardDesigns {
    const val CART_24 = "cards/Cart 24.svg"
    const val CART_25 = "cards/Cart 25.svg"
    const val CART_26 = "cards/Cart 26.svg"
    const val CART_30 = "cards/Cart 30.svg"
    const val CART_GEOMETRIC_32 = "cards/Cart Geometric 32.svg"
    const val CART_GEOMETRIC_34 = "cards/Cart Geometric 34.svg"
    const val RECTANGLE_594 = "cards/Rectangle 594.svg"
    const val RECTANGLE_595 = "cards/Rectangle 595.svg"
    const val RECTANGLE_596 = "cards/Rectangle 596.svg"
}

@Composable
fun CardDesign(
    designPath: String,
    modifier: Modifier = Modifier,
    contentDescription: String = "Card Design"
) {
    AsyncImage(
        model = "file:///android_asset/$designPath",
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = ContentScale.Fit
    )
}

// ============================================================================
// GRÁFICOS E IMAGENS
// ============================================================================

object GraphicsAssets {
    const val GROUP_12 = "images/Group 12.svg"
    const val GROUP_26907 = "images/Group 26907.svg"
    const val GROUP_26939 = "images/Group 26939.svg"
    const val GROUP_26944 = "images/Group 26944.svg"
    const val GROUP_26944_PNG = "images/Group 26944.png"
    const val GROUP_26969 = "images/Group 26969.svg"
    const val GROUP_26970 = "images/Group 26970.svg"
    const val GROUP_26971 = "images/Group 26971.svg"
    const val GROUP_26971_PNG = "images/Group 26971.png"
    const val GROUP_33525 = "images/Group 33525.svg"
    const val INTERSECT = "images/Intersect.svg"
    const val INTERSECT_PNG = "images/Intersect.png"
}

@Composable
fun GraphicsImage(
    imagePath: String,
    modifier: Modifier = Modifier,
    contentDescription: String = "Graphics"
) {
    AsyncImage(
        model = "file:///android_asset/$imagePath",
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = ContentScale.Fit
    )
}

