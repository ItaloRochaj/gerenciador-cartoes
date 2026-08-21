package com.nexcard.nextwallet.data.local.database

import androidx.room.TypeConverter
import com.nexcard.nextwallet.domain.model.CardBrand
import com.nexcard.nextwallet.domain.model.CardStatus
import com.nexcard.nextwallet.domain.model.CardType
import com.nexcard.nextwallet.domain.model.InvoiceStatus
import com.nexcard.nextwallet.domain.model.TransactionCategory
import com.nexcard.nextwallet.domain.model.TransactionStatus

class Converters {
    @TypeConverter
    fun cardBrandToString(value: CardBrand): String = value.name

    @TypeConverter
    fun stringToCardBrand(value: String): CardBrand = CardBrand.valueOf(value)

    @TypeConverter
    fun cardStatusToString(value: CardStatus): String = value.name

    @TypeConverter
    fun stringToCardStatus(value: String): CardStatus = CardStatus.valueOf(value)

    @TypeConverter
    fun cardTypeToString(value: CardType): String = value.name

    @TypeConverter
    fun stringToCardType(value: String): CardType = CardType.valueOf(value)

    @TypeConverter
    fun invoiceStatusToString(value: InvoiceStatus): String = value.name

    @TypeConverter
    fun stringToInvoiceStatus(value: String): InvoiceStatus = InvoiceStatus.valueOf(value)

    @TypeConverter
    fun categoryToString(value: TransactionCategory): String = value.name

    @TypeConverter
    fun stringToCategory(value: String): TransactionCategory = TransactionCategory.valueOf(value)

    @TypeConverter
    fun transactionStatusToString(value: TransactionStatus): String = value.name

    @TypeConverter
    fun stringToTransactionStatus(value: String): TransactionStatus = TransactionStatus.valueOf(value)
}
