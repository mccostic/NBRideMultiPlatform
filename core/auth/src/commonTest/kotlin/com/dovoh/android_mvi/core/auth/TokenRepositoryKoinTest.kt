package com.dovoh.android_mvi.core.auth

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.*

class TokenRepositoryKoinTest {

    private lateinit var koinApp: KoinApplication

    // App-like module used by most tests
    private val coreModule = module {
        single<TokenStore> { InMemoryTokenStore() }
        single<TokenRepository> { TokenRepositoryImpl(get()) }
    }

    @BeforeTest
    fun setup() {
        koinApp = startKoin { modules(coreModule) }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    // --- Helpers -------------------------------------------------------------------------------

    private fun repo(): TokenRepository = koinApp.koin.get()


    // --- Access token tests ---------------------------------------------------------------------

    @Test
    fun `access token successful retrieval`() = runTest {
        val repo = repo()
        repo.save("accessX", "refreshX")
        assertEquals("accessX", repo.access())
    }

    @Test
    fun `access token not found`() = runTest {
        val repo = repo()
        assertNull(repo.access())
    }

    @Test
    fun `access token retrieval after save`() = runTest {
        val repo = repo()
        repo.save("a1", null)
        assertEquals("a1", repo.access())
    }

    @Test
    fun `access token retrieval after clear`() = runTest {
        val repo = repo()
        repo.save("a", "r")
        repo.clear()
        assertNull(repo.access())
    }

    @Test
    fun `access token concurrent access`() = runTest {
        val repo = repo()
        repo.save("a0", "r0")

        // Stress: many simultaneous reads while writes happen.
        val readers = List(20) {
            launch { repeat(5) { repo.access() } }
        }
        readers.forEach { it.join() }
        // Sanity: repository still returns *some* coherent value (no crash).
        assertNotNull(repo.access())
    }

    // --- Refresh token tests --------------------------------------------------------------------

    @Test
    fun `refresh token successful retrieval`() = runTest {
        val repo = repo()
        repo.save("a", "refreshX")
        assertEquals("refreshX", repo.refresh())
    }

    @Test
    fun `refresh token not found`() = runTest {
        val repo = repo()
        assertNull(repo.refresh())
    }

    @Test
    fun `refresh token retrieval after save`() = runTest {
        val repo = repo()
        repo.save(null, "r1")
        assertEquals("r1", repo.refresh())
    }

    @Test
    fun `refresh token retrieval after clear`() = runTest {
        val repo = repo()
        repo.save("a", "r")
        repo.clear()
        assertNull(repo.refresh())
    }

    @Test
    fun `refresh token concurrent access`() = runTest {
        val repo = repo()
        repo.save("a0", "r0")
        val readers = List(20) {
            launch { repeat(5) { repo.refresh() } }
        }
        readers.forEach { it.join() }
        assertNotNull(repo.refresh())
    }

    // --- Save behavior --------------------------------------------------------------------------

    @Test
    fun `save new tokens`() = runTest {
        val repo = repo()
        repo.save("a", "r")
        assertEquals("a", repo.access())
        assertEquals("r", repo.refresh())
    }

    @Test
    fun `save null access token`() = runTest {
        val repo = repo()
        repo.save(null, "r")
        assertNull(repo.access())
        assertEquals("r", repo.refresh())
    }

    @Test
    fun `save null refresh token`() = runTest {
        val repo = repo()
        repo.save("a", null)
        assertEquals("a", repo.access())
        assertNull(repo.refresh())
    }

    @Test
    fun `save both tokens as null`() = runTest {
        val repo = repo()
        repo.save(null, null)
        assertNull(repo.access())
        assertNull(repo.refresh())
    }

    @Test
    fun `save empty string access token`() = runTest {
        val repo = repo()
        repo.save("", "r")
        assertEquals("", repo.access())
        assertEquals("r", repo.refresh())
    }

    @Test
    fun `save empty string refresh token`() = runTest {
        val repo = repo()
        repo.save("a", "")
        assertEquals("a", repo.access())
        assertEquals("", repo.refresh())
    }

    @Test
    fun `save overwriting existing tokens`() = runTest {
        val repo = repo()
        repo.save("a1", "r1")
        repo.save("a2", "r2")
        assertEquals("a2", repo.access())
        assertEquals("r2", repo.refresh())
    }

    @Test
    fun `save concurrent calls`() = runTest {
        val repo = repo()
        val jobs = mutableListOf<Job>()
        repeat(10) { i ->
            jobs += launch {
                // tiny jitter to increase interleaving
                delay((i % 3).toLong())
                repo.save("a$i", "r$i")
            }
        }
        jobs.forEach { it.join() }
        // InMemoryTokenStore is "last writer wins"
        assertEquals("a8", repo.access())
        assertEquals("r8", repo.refresh())
    }

    // --- Clear behavior -------------------------------------------------------------------------

    @Test
    fun `clear when tokens exist`() = runTest {
        val repo = repo()
        repo.save("a", "r")
        repo.clear()
        assertNull(repo.access())
        assertNull(repo.refresh())
    }

    @Test
    fun `clear when no tokens exist`() = runTest {
        val repo = repo()
        // Should not throw
        repo.clear()
        assertNull(repo.access())
        assertNull(repo.refresh())
    }

    @Test
    fun `clear idempotency`() = runTest {
        val repo = repo()
        repo.save("a", "r")
        repo.clear()
        repo.clear()
        assertNull(repo.access())
        assertNull(repo.refresh())
    }

    @Test
    fun `clear concurrent calls`() = runTest {
        val repo = repo()
        repo.save("a", "r")

        val jobs = List(10) { launch { repo.clear() } }
        jobs.forEach { it.join() }

        assertNull(repo.access())
        assertNull(repo.refresh())
    }

    // --- Interaction tests ----------------------------------------------------------------------

    @Test
    fun `interaction between save and clear`() = runTest {
        val repo = repo()
        repo.save("a", "r")
        repo.clear()
        assertNull(repo.access())
        assertNull(repo.refresh())
    }

    @Test
    fun `interaction between clear and save`() = runTest {
        val repo = repo()
        repo.clear()
        repo.save("A2", "R2")
        assertEquals("A2", repo.access())
        assertEquals("R2", repo.refresh())
    }

}
