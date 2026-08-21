package com.nexcard.nextwallet.data.remote.api

import com.nexcard.nextwallet.data.remote.dto.CardDto
import com.nexcard.nextwallet.data.remote.dto.CardProductDto
import com.nexcard.nextwallet.data.remote.dto.InvoiceDto
import com.nexcard.nextwallet.data.remote.dto.PatchLimitRequestDto
import com.nexcard.nextwallet.data.remote.dto.PatchStatusRequestDto
import com.nexcard.nextwallet.data.remote.dto.PostCardRequestDto
import com.nexcard.nextwallet.data.remote.dto.PostTransactionRequestDto
import com.nexcard.nextwallet.data.remote.dto.TransactionDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NextWalletApi {
    @GET("cards")
    suspend fun getCards(): List<CardDto>

    @GET("cards/{id}")
    suspend fun getCard(@Path("id") id: String): CardDto

    @GET("products")
    suspend fun getProducts(): List<CardProductDto>

    @GET("transactions")
    suspend fun getTransactions(@Query("cardId") cardId: String? = null): List<TransactionDto>

    @GET("invoices")
    suspend fun getInvoices(): List<InvoiceDto>

    @POST("cards")
    suspend fun createCard(@Body body: PostCardRequestDto): CardDto

    @POST("transactions")
    suspend fun createTransaction(@Body body: PostTransactionRequestDto): TransactionDto

    @PATCH("cards/{id}/status")
    suspend fun patchCardStatus(@Path("id") id: String, @Body body: PatchStatusRequestDto): CardDto

    @PATCH("cards/{id}/limit")
    suspend fun patchCardLimit(@Path("id") id: String, @Body body: PatchLimitRequestDto): CardDto
}
