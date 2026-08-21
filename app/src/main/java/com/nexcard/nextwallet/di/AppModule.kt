package com.nexcard.nextwallet.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nexcard.nextwallet.data.local.dao.ActionHistoryDao
import com.nexcard.nextwallet.data.local.dao.CardDao
import com.nexcard.nextwallet.data.local.dao.InvoiceDao
import com.nexcard.nextwallet.data.local.dao.ProductDao
import com.nexcard.nextwallet.data.local.dao.TransactionDao
import com.nexcard.nextwallet.data.local.dao.UserDao
import com.nexcard.nextwallet.data.local.database.NextWalletDatabase
import com.nexcard.nextwallet.data.remote.api.NextWalletApi
import com.nexcard.nextwallet.data.remote.mock.MockApiInterceptor
import com.nexcard.nextwallet.data.repository.AuthRepositoryImpl
import com.nexcard.nextwallet.data.repository.SettingsRepositoryImpl
import com.nexcard.nextwallet.data.repository.WalletRepositoryImpl
import com.nexcard.nextwallet.domain.repository.AuthRepository
import com.nexcard.nextwallet.domain.repository.SettingsRepository
import com.nexcard.nextwallet.domain.repository.WalletRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppProvidesModule {
    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context): NextWalletDatabase =
        Room.databaseBuilder(context, NextWalletDatabase::class.java, "next_wallet.db").build()

    @Provides fun provideUserDao(db: NextWalletDatabase): UserDao = db.userDao()
    @Provides fun provideCardDao(db: NextWalletDatabase): CardDao = db.cardDao()
    @Provides fun provideTransactionDao(db: NextWalletDatabase): TransactionDao = db.transactionDao()
    @Provides fun provideInvoiceDao(db: NextWalletDatabase): InvoiceDao = db.invoiceDao()
    @Provides fun provideProductDao(db: NextWalletDatabase): ProductDao = db.productDao()
    @Provides fun provideActionHistoryDao(db: NextWalletDatabase): ActionHistoryDao = db.actionHistoryDao()

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideOkHttp(interceptor: MockApiInterceptor): OkHttpClient {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(logger)
            .build()
    }

    @Provides
    @Singleton
    fun provideApi(okHttpClient: OkHttpClient, gson: Gson): NextWalletApi =
        Retrofit.Builder()
            .baseUrl("https://nextwallet.mock/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(NextWalletApi::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppBindsModule {
    @Binds abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
    @Binds abstract fun bindWalletRepository(impl: WalletRepositoryImpl): WalletRepository
    @Binds abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}
