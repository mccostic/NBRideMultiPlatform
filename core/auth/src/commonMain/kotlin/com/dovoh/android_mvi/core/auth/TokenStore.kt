package com.dovoh.android_mvi.core.auth

interface TokenStore {
    suspend fun getAccess(): String?
    suspend fun getRefresh(): String?
    suspend fun set(access: String?, refresh: String?)
}

class InMemoryTokenStore : TokenStore {
    private var a: String? = null
    private var r: String? = null
    override suspend fun getAccess() = a
    override suspend fun getRefresh() = r
    override suspend fun set(access: String?, refresh: String?) { a = access; r = refresh }
}


interface TokenRepository {
    suspend fun access(): String?
    suspend fun refresh(): String?
    suspend fun save(a: String?, r: String?)
    suspend fun clear()
}

class TokenRepositoryImpl(private val store: TokenStore) : TokenRepository {
    override suspend fun access() = store.getAccess()
    override suspend fun refresh() = store.getRefresh()
    override suspend fun save(a: String?, r: String?) = store.set(a, r)
    override suspend fun clear() = store.set(null, null)
}
