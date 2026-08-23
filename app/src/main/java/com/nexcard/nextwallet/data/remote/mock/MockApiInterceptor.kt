package com.nexcard.nextwallet.data.remote.mock

import com.google.gson.Gson
import com.nexcard.nextwallet.data.remote.dto.CardDto
import com.nexcard.nextwallet.data.remote.dto.CardProductDto
import com.nexcard.nextwallet.data.remote.dto.InvoiceDto
import com.nexcard.nextwallet.data.remote.dto.PatchLimitRequestDto
import com.nexcard.nextwallet.data.remote.dto.PatchStatusRequestDto
import com.nexcard.nextwallet.data.remote.dto.PostCardRequestDto
import com.nexcard.nextwallet.data.remote.dto.PostTransactionRequestDto
import com.nexcard.nextwallet.data.remote.dto.TransactionDto
import java.util.UUID
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockApiInterceptor @Inject constructor(
    private val gson: Gson,
) : Interceptor {

    @Volatile
    var forceError: Boolean = false

    override fun intercept(chain: Interceptor.Chain): Response {
        Thread.sleep(700)
        if (forceError || chain.request().header("X-Force-Error") == "true") {
            return chain.request().toJsonResponse(500, """{"message":"network error"}""")
        }

        val request = chain.request()
        val method = request.method
        val path = request.url.encodedPath.removePrefix("/")

        val body = when {
            method == "GET" && path == "cards" -> gson.toJson(MockApiState.cards)
            method == "GET" && path.startsWith("cards/") && !path.contains("/status") && !path.contains("/limit") -> {
                val id = path.removePrefix("cards/")
                gson.toJson(MockApiState.cards.first { it.id == id })
            }
            method == "GET" && path == "products" -> gson.toJson(MockApiState.products)
            method == "GET" && path == "transactions" -> {
                val cardId = request.url.queryParameter("cardId")
                val list = if (cardId.isNullOrBlank()) MockApiState.transactions else MockApiState.transactions.filter { it.cardId == cardId }
                gson.toJson(list)
            }
            method == "GET" && path == "invoices" -> gson.toJson(MockApiState.invoices)
            method == "POST" && path == "cards" -> {
                val req = gson.fromJson(request.bodyAsString(), PostCardRequestDto::class.java)
                val product = MockApiState.products.first { it.id == req.productId }
                val created = CardDto(
                    id = "card_${UUID.randomUUID().toString().take(8)}",
                    productId = product.id,
                    holderName = "Italo Rocha",
                    maskedNumber = "**** **** **** ${randomLastFour()}",
                    lastFourDigits = randomLastFour(),
                    brand = product.brand,
                    type = "CREDIT",
                    expirationDate = "12/31",
                    totalLimitCents = product.initialLimitCents,
                    usedLimitCents = 0,
                    availableLimitCents = product.initialLimitCents,
                    status = "ACTIVE",
                    isFavorite = false,
                    colorStyle = req.style,
                    isVirtual = true,
                )
                MockApiState.cards.add(created)
                gson.toJson(created)
            }
            method == "POST" && path == "transactions" -> {
                val req = gson.fromJson(request.bodyAsString(), PostTransactionRequestDto::class.java)
                val tx = TransactionDto(
                    id = "tx_${UUID.randomUUID().toString().take(8)}",
                    cardId = req.cardId,
                    description = req.description,
                    amountCents = req.amountCents,
                    category = req.category,
                    dateEpochMillis = req.dateEpochMillis,
                    status = "APPROVED",
                )
                MockApiState.transactions.add(0, tx)
                gson.toJson(tx)
            }
            method == "PATCH" && path.matches(Regex("cards/.+/status")) -> {
                val id = path.split("/")[1]
                val req = gson.fromJson(request.bodyAsString(), PatchStatusRequestDto::class.java)
                val index = MockApiState.cards.indexOfFirst { it.id == id }
                val old = MockApiState.cards[index]
                val updated = old.copy(status = req.status)
                MockApiState.cards[index] = updated
                gson.toJson(updated)
            }
            method == "PATCH" && path.matches(Regex("cards/.+/limit")) -> {
                val id = path.split("/")[1]
                val req = gson.fromJson(request.bodyAsString(), PatchLimitRequestDto::class.java)
                val index = MockApiState.cards.indexOfFirst { it.id == id }
                val old = MockApiState.cards[index]
                val used = old.usedLimitCents
                val updated = old.copy(
                    totalLimitCents = req.newLimitCents,
                    availableLimitCents = (req.newLimitCents - used).coerceAtLeast(0),
                )
                MockApiState.cards[index] = updated
                gson.toJson(updated)
            }
            else -> """{"message":"not found"}"""
        }

        return request.toJsonResponse(200, body)
    }

    private fun okhttp3.Request.toJsonResponse(code: Int, content: String): Response =
        Response.Builder()
            .request(this)
            .protocol(Protocol.HTTP_1_1)
            .code(code)
            .message(if (code in 200..299) "OK" else "Error")
            .body(content.toResponseBody("application/json".toMediaType()))
            .build()

    private fun okhttp3.Request.bodyAsString(): String {
        val copy = newBuilder().build()
        val buffer = okio.Buffer()
        copy.body?.writeTo(buffer)
        return buffer.readUtf8()
    }

    private fun randomLastFour(): String = (1000..9999).random().toString()
}

object MockApiState {
    private const val DEFAULT_CARD_LIMIT_CENTS = 2_000_000L

    val cards = mutableListOf(
        CardDto("card_01", "product_black", "Italo Rocha", "**** **** **** 9981", "9981", "VISA", "CREDIT", "12/30", DEFAULT_CARD_LIMIT_CENTS, 243900, DEFAULT_CARD_LIMIT_CENTS - 243900, "ACTIVE", true, "purple_black", true),
        CardDto("card_02", "product_graphite", "Italo Rocha", "**** **** **** 1234", "1234", "MASTERCARD", "CREDIT", "11/29", DEFAULT_CARD_LIMIT_CENTS, 89500, DEFAULT_CARD_LIMIT_CENTS - 89500, "ACTIVE", false, "graphite", true),
        CardDto("card_03", "product_blue", "Italo Rocha", "**** **** **** 4509", "4509", "ELO", "DEBIT_CREDIT", "07/28", DEFAULT_CARD_LIMIT_CENTS, 6200, DEFAULT_CARD_LIMIT_CENTS - 6200, "BLOCKED", false, "blue_dark", true),
    )

    val products = mutableListOf(
        CardProductDto("product_wave26", "Next Wave", "VISA", "Premium", 0, DEFAULT_CARD_LIMIT_CENTS, listOf("Sem anuidade", "Limite inicial de R$ 20.000"), "purple_wave_26"),
        CardProductDto("product_black", "Next Black", "VISA", "Premium", 0, DEFAULT_CARD_LIMIT_CENTS, listOf("Cashback 1.5%", "Sala VIP"), "purple_black"),
        CardProductDto("product_graphite", "Next Graphite", "MASTERCARD", "Intermediário", 9900, DEFAULT_CARD_LIMIT_CENTS, listOf("Cartão virtual", "Parcelamento"), "graphite"),
        CardProductDto("product_blue", "Next Blue", "ELO", "Essencial", 0, DEFAULT_CARD_LIMIT_CENTS, listOf("Sem anuidade", "Notificações em tempo real"), "blue_dark"),
    )

    val transactions = mutableListOf(
        TransactionDto("tx_08", "card_01", "Aurora Store", 21990, "OUTROS", 1724187600000, "APPROVED"),
        TransactionDto("tx_01", "card_01", "Netflix", 6200, "ASSINATURAS", 1724101200000, "APPROVED"),
        TransactionDto("tx_02", "card_01", "PayPal", 8000, "OUTROS", 1724014800000, "APPROVED"),
        TransactionDto("tx_03", "card_01", "Amazon", 48000, "OUTROS", 1723928400000, "APPROVED"),
        TransactionDto("tx_04", "card_01", "Madero", 15900, "OUTROS", 1723842000000, "APPROVED"),
        TransactionDto("tx_05", "card_01", "Zara", 32990, "OUTROS", 1723755600000, "APPROVED"),
        TransactionDto("tx_06", "card_02", "Mercado", 24390, "MERCADO", 1723669200000, "APPROVED"),
        TransactionDto("tx_07", "card_02", "Transporte", 8950, "TRANSPORTE", 1723582800000, "APPROVED"),
    )

    val invoices = mutableListOf(
        InvoiceDto("inv_01", "card_01", "2026-08", 158300, "2026-09-10", "OPEN"),
        InvoiceDto("inv_02", "card_01", "2026-07", 121700, "2026-08-10", "PAID"),
        InvoiceDto("inv_03", "card_02", "2026-08", 98700, "2026-09-12", "OPEN"),
    )
}
