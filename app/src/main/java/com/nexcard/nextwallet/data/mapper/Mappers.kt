package com.nexcard.nextwallet.data.mapper

import com.nexcard.nextwallet.data.local.entity.CardEntity
import com.nexcard.nextwallet.data.local.entity.CardProductEntity
import com.nexcard.nextwallet.data.local.entity.InvoiceEntity
import com.nexcard.nextwallet.data.local.entity.TransactionEntity
import com.nexcard.nextwallet.data.local.entity.UserEntity
import com.nexcard.nextwallet.data.remote.dto.CardDto
import com.nexcard.nextwallet.data.remote.dto.CardProductDto
import com.nexcard.nextwallet.data.remote.dto.InvoiceDto
import com.nexcard.nextwallet.data.remote.dto.TransactionDto
import com.nexcard.nextwallet.domain.model.Card
import com.nexcard.nextwallet.domain.model.CardBrand
import com.nexcard.nextwallet.domain.model.CardProduct
import com.nexcard.nextwallet.domain.model.CardStatus
import com.nexcard.nextwallet.domain.model.CardType
import com.nexcard.nextwallet.domain.model.Invoice
import com.nexcard.nextwallet.domain.model.InvoiceStatus
import com.nexcard.nextwallet.domain.model.Transaction
import com.nexcard.nextwallet.domain.model.TransactionCategory
import com.nexcard.nextwallet.domain.model.TransactionStatus
import com.nexcard.nextwallet.domain.model.User

fun UserEntity.toDomain() = User(id = id, name = name, email = email, avatarUrl = avatarUrl)

fun CardEntity.toDomain() = Card(
    id, productId, holderName, maskedNumber, lastFourDigits, brand, type, expirationDate,
    totalLimitCents, usedLimitCents, availableLimitCents, status, isFavorite, colorStyle, isVirtual,
)

fun Card.toEntity() = CardEntity(
    id, productId, holderName, maskedNumber, lastFourDigits, brand, type, expirationDate,
    totalLimitCents, usedLimitCents, availableLimitCents, status, isFavorite, colorStyle, isVirtual,
)

fun TransactionEntity.toDomain() = Transaction(id, cardId, description, amountCents, category, dateEpochMillis, status)

fun Transaction.toEntity() = TransactionEntity(id, cardId, description, amountCents, category, dateEpochMillis, status)

fun InvoiceEntity.toDomain() = Invoice(id, cardId, referenceMonth, totalAmountCents, dueDate, status)

fun CardProductEntity.toDomain() = CardProduct(
    id = id,
    name = name,
    brand = brand,
    category = category,
    annualFeeCents = annualFeeCents,
    initialLimitCents = initialLimitCents,
    benefits = benefits.split("|").filter { it.isNotBlank() },
    style = style,
)

fun CardDto.toEntity() = CardEntity(
    id = id,
    productId = productId,
    holderName = holderName,
    maskedNumber = maskedNumber,
    lastFourDigits = lastFourDigits,
    brand = CardBrand.valueOf(brand),
    type = CardType.valueOf(type),
    expirationDate = expirationDate,
    totalLimitCents = totalLimitCents,
    usedLimitCents = usedLimitCents,
    availableLimitCents = availableLimitCents,
    status = CardStatus.valueOf(status),
    isFavorite = isFavorite,
    colorStyle = colorStyle,
    isVirtual = isVirtual,
)

fun TransactionDto.toEntity() = TransactionEntity(
    id = id,
    cardId = cardId,
    description = description,
    amountCents = amountCents,
    category = TransactionCategory.valueOf(category),
    dateEpochMillis = dateEpochMillis,
    status = TransactionStatus.valueOf(status),
)

fun InvoiceDto.toEntity() = InvoiceEntity(id, cardId, referenceMonth, totalAmountCents, dueDate, InvoiceStatus.valueOf(status))

fun CardProductDto.toEntity() = CardProductEntity(
    id = id,
    name = name,
    brand = CardBrand.valueOf(brand),
    category = category,
    annualFeeCents = annualFeeCents,
    initialLimitCents = initialLimitCents,
    benefits = benefits.joinToString("|"),
    style = style,
)
