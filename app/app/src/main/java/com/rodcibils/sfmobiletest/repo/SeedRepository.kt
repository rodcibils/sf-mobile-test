package com.rodcibils.sfmobiletest.repo

import android.content.Context
import com.rodcibils.sfmobiletest.api.RemoteSeedDataSource
import com.rodcibils.sfmobiletest.local.LocalSeedDataSource
import com.rodcibils.sfmobiletest.model.QRCodeSeed

/**
 * Repository responsible for managing QR code seed retrieval and validation,
 * including fallback to cached values and remote fetching.
 *
 * @param context Application context used to initialize local storage.
 * @param remoteSeedDataSource API client for remote seed operations.
 */
class SeedRepository(
    /**
     * TODO: replace with proper DI
     */
    context: Context,
    /**
     * TODO: replace with proper DI
     */
    private val remoteSeedDataSource: RemoteSeedDataSource = RemoteSeedDataSource(),
    /**
     * TODO: replace with proper DI
     */
    private val localSeedDataSource: LocalSeedDataSource = LocalSeedDataSource(context),
) {
    /**
     * Checks if the provided seed is valid according to the backend.
     *
     * @param seed Seed scanned from QR with camera, that we want to validate.
     * @return `true` if valid, `false` if rejected by the server.
     * @throws Exception if the request fails or response is invalid.
     */
    suspend fun isSeedValid(seed: String): Boolean = remoteSeedDataSource.isSeedValid(seed)

    /**
     * Retrieves a fresh or locally cached QR code seed.
     *
     * 1. Checks for a valid (non-expired) seed in local storage.
     * 2. If none found, fetches a new seed from the backend.
     * 3. Caches the fetched seed for future retrievals.
     *
     * @return A non-expired [QRCodeSeed] from local or remote source.
     * @throws Exception if the remote fetch fails and no local seed is available.
     */
    suspend fun retrieveSeed(): QRCodeSeed {
        localSeedDataSource.getSeed()?.let { return it }
        val freshSeed = remoteSeedDataSource.getSeed()
        localSeedDataSource.saveSeed(freshSeed)
        return freshSeed
    }
}
