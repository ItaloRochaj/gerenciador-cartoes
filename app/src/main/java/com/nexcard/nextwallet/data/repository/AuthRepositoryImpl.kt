package com.nexcard.nextwallet.data.repository

import com.nexcard.nextwallet.data.local.dao.UserDao
import com.nexcard.nextwallet.data.local.datastore.PreferencesDataStore
import com.nexcard.nextwallet.data.local.entity.UserEntity
import com.nexcard.nextwallet.data.mapper.toDomain
import com.nexcard.nextwallet.domain.model.User
import com.nexcard.nextwallet.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val preferencesDataStore: PreferencesDataStore,
) : AuthRepository {

    override suspend fun login(email: String, password: String, remember: Boolean): Result<User> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Preencha e-mail e senha."))
        }
        val user = userDao.findByEmail(email)
            ?: if (email == DEMO_EMAIL && password == DEMO_PASSWORD) defaultUser() else null
        if (user == null || user.password != password) {
            return Result.failure(IllegalArgumentException("Credenciais inválidas."))
        }
        userDao.upsert(user)
        preferencesDataStore.saveUserSession(user.name, user.email, remember)
        return Result.success(user.toDomain())
    }

    override suspend fun signup(name: String, email: String, password: String): Result<Unit> {
        if (name.isBlank()) return Result.failure(IllegalArgumentException("Nome obrigatório."))
        if (!Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$").matches(email)) {
            return Result.failure(IllegalArgumentException("E-mail inválido."))
        }
        if (password.length < 6) {
            return Result.failure(IllegalArgumentException("A senha precisa de 6 caracteres ou mais."))
        }
        if (userDao.findByEmail(email) != null) {
            return Result.failure(IllegalArgumentException("Este e-mail já está cadastrado localmente."))
        }
        userDao.upsert(
            UserEntity(
                id = "user_${email.hashCode()}",
                name = name,
                email = email,
                avatarUrl = "",
                password = password,
            ),
        )
        return Result.success(Unit)
    }

    override suspend fun logout() {
        preferencesDataStore.clearSession()
    }

    override fun isSessionActive(): Flow<Boolean> = preferencesDataStore.sessionActive

    override fun currentUser(): Flow<User?> {
        return combine(userDao.observeCurrent(), preferencesDataStore.userEmail) { entity, email ->
            entity?.toDomain() ?: User("demo_user", "Italo Rocha", email.ifBlank { DEMO_EMAIL })
        }
    }

    private fun defaultUser() = UserEntity(
        id = "demo_user",
        name = "Italo Rocha",
        email = DEMO_EMAIL,
        avatarUrl = "",
        password = DEMO_PASSWORD,
    )

    companion object {
        const val DEMO_EMAIL = "usuario@nextwallet.com"
        const val DEMO_PASSWORD = "123456"
    }
}
