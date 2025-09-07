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

class TokenRepository(private val s: TokenStore) {
    suspend fun access() = s.getAccess()
    suspend fun refresh() = s.getRefresh()
    suspend fun save(a: String?, r: String?) = s.set(a, r)
    suspend fun clear() = s.set(null, null)
}
