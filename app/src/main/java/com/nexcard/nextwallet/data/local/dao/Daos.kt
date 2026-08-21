package com.nexcard.nextwallet.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.nexcard.nextwallet.data.local.entity.ActionHistoryEntity
import com.nexcard.nextwallet.data.local.entity.CardEntity
import com.nexcard.nextwallet.data.local.entity.CardProductEntity
import com.nexcard.nextwallet.data.local.entity.InvoiceEntity
import com.nexcard.nextwallet.data.local.entity.TransactionEntity
import com.nexcard.nextwallet.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun findByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users LIMIT 1")
    fun observeCurrent(): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(user: UserEntity)
}

@Dao
interface CardDao {
    @Query("SELECT * FROM cards ORDER BY isFavorite DESC, id ASC")
    fun observeAll(): Flow<List<CardEntity>>

    @Query("SELECT * FROM cards WHERE id = :id LIMIT 1")
    suspend fun findById(id: String): CardEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(cards: List<CardEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(card: CardEntity)

    @Query("DELETE FROM cards WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT COUNT(*) FROM cards")
    suspend fun count(): Int
}

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY dateEpochMillis DESC")
    fun observeAll(): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(transactions: List<TransactionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(transaction: TransactionEntity)
}

@Dao
interface InvoiceDao {
    @Query("SELECT * FROM invoices ORDER BY referenceMonth DESC")
    fun observeAll(): Flow<List<InvoiceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(invoices: List<InvoiceEntity>)
}

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun observeAll(): Flow<List<CardProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(products: List<CardProductEntity>)
}

@Dao
interface ActionHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(actionHistoryEntity: ActionHistoryEntity)
}

@Dao
interface CleanupDao {
    @Transaction
    @Query("DELETE FROM action_history")
    suspend fun clearCache()
}
