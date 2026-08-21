package com.nexcard.nextwallet.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CardDto(
    val id: String,
    @SerializedName("product_id") val productId: String,
    @SerializedName("holder_name") val holderName: String,
    @SerializedName("masked_number") val maskedNumber: String,
    @SerializedName("last_four_digits") val lastFourDigits: String,
    val brand: String,
    val type: String,
    @SerializedName("expiration_date") val expirationDate: String,
    @SerializedName("total_limit_cents") val totalLimitCents: Long,
    @SerializedName("used_limit_cents") val usedLimitCents: Long,
    @SerializedName("available_limit_cents") val availableLimitCents: Long,
    val status: String,
    @SerializedName("is_favorite") val isFavorite: Boolean,
    @SerializedName("color_style") val colorStyle: String,
    @SerializedName("is_virtual") val isVirtual: Boolean,
)

data class CardProductDto(
    val id: String,
    val name: String,
    val brand: String,
    val category: String,
    @SerializedName("annual_fee_cents") val annualFeeCents: Long,
    @SerializedName("initial_limit_cents") val initialLimitCents: Long,
    val benefits: List<String>,
    val style: String,
)

data class TransactionDto(
    val id: String,
    @SerializedName("card_id") val cardId: String,
    val description: String,
    @SerializedName("amount_cents") val amountCents: Long,
    val category: String,
    @SerializedName("date_epoch_millis") val dateEpochMillis: Long,
    val status: String,
)

data class InvoiceDto(
    val id: String,
    @SerializedName("card_id") val cardId: String,
    @SerializedName("reference_month") val referenceMonth: String,
    @SerializedName("total_amount_cents") val totalAmountCents: Long,
    @SerializedName("due_date") val dueDate: String,
    val status: String,
)

data class PostCardRequestDto(
    @SerializedName("product_id") val productId: String,
    val style: String,
)

data class PostTransactionRequestDto(
    @SerializedName("card_id") val cardId: String,
    val description: String,
    @SerializedName("amount_cents") val amountCents: Long,
    val category: String,
    @SerializedName("date_epoch_millis") val dateEpochMillis: Long,
)

data class PatchStatusRequestDto(val status: String)
data class PatchLimitRequestDto(@SerializedName("new_limit_cents") val newLimitCents: Long)
