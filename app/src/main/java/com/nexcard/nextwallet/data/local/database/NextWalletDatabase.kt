package com.nexcard.nextwallet.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nexcard.nextwallet.data.local.dao.ActionHistoryDao
import com.nexcard.nextwallet.data.local.dao.CardDao
import com.nexcard.nextwallet.data.local.dao.InvoiceDao
import com.nexcard.nextwallet.data.local.dao.ProductDao
import com.nexcard.nextwallet.data.local.dao.TransactionDao
import com.nexcard.nextwallet.data.local.dao.UserDao
import com.nexcard.nextwallet.data.local.entity.ActionHistoryEntity
import com.nexcard.nextwallet.data.local.entity.CardEntity
import com.nexcard.nextwallet.data.local.entity.CardProductEntity
import com.nexcard.nextwallet.data.local.entity.InvoiceEntity
import com.nexcard.nextwallet.data.local.entity.TransactionEntity
import com.nexcard.nextwallet.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        CardEntity::class,
        TransactionEntity::class,
        InvoiceEntity::class,
        CardProductEntity::class,
        ActionHistoryEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class NextWalletDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun cardDao(): CardDao
    abstract fun transactionDao(): TransactionDao
    abstract fun invoiceDao(): InvoiceDao
    abstract fun productDao(): ProductDao
    abstract fun actionHistoryDao(): ActionHistoryDao
}
