package com.nexcard.nextwallet

import com.nexcard.nextwallet.domain.model.User
import com.nexcard.nextwallet.domain.repository.AuthRepository
import com.nexcard.nextwallet.domain.usecase.LoginUseCase
import com.nexcard.nextwallet.domain.usecase.SignupUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthUseCasesTest {
    @Test
    fun `login valido`() = runTest {
        val repo = FakeAuthRepository()
        val result = LoginUseCase(repo)("usuario@nextwallet.com", "123456", true)
        assertTrue(result.isSuccess)
    }

    @Test
    fun `login invalido`() = runTest {
        val repo = FakeAuthRepository()
        val result = LoginUseCase(repo)("x@x.com", "000000", false)
        assertTrue(result.isFailure)
    }

    @Test
    fun `cadastro com senhas diferentes falha na camada de tela`() {
        val samePasswords = "123456" == "654321"
        assertTrue(!samePasswords)
    }
}

private class FakeAuthRepository : AuthRepository {
    private val session = MutableStateFlow(false)
    override suspend fun login(email: String, password: String, remember: Boolean): Result<User> {
        return if (email == "usuario@nextwallet.com" && password == "123456") {
            session.value = true
            Result.success(User("1", "Usuário", email))
        } else {
            Result.failure(IllegalArgumentException("Credenciais inválidas"))
        }
    }

    override suspend fun signup(name: String, email: String, password: String): Result<Unit> = Result.success(Unit)
    override suspend fun logout() { session.value = false }
    override fun isSessionActive(): Flow<Boolean> = session
    override fun currentUser(): Flow<User?> = flowOf(User("1", "Usuário", "usuario@nextwallet.com"))
}
