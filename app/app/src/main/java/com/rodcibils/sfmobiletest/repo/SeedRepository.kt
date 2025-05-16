package com.rodcibils.sfmobiletest.repo

import com.rodcibils.sfmobiletest.api.RemoteSeedDataSource

class SeedRepository(
    /**
     * TODO: replace with DI
     */
    private val remoteSeedDataSource: RemoteSeedDataSource = RemoteSeedDataSource(),
) {
    /**
     * @param seed Seed scanned from QR with camera, that we want to know if its a valid seed or not
     * @return true if the seed is valid on backend, false if otherwise.
     * @throws Exception if there is any network error.
     */
    suspend fun isSeedValid(seed: String): Boolean = remoteSeedDataSource.isSeedValid(seed)
}
