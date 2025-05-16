package com.rodcibils.sfmobiletest.repo

import com.rodcibils.sfmobiletest.api.RemoteSeedDataSource
import com.rodcibils.sfmobiletest.model.QRCodeSeed
import kotlinx.coroutines.delay

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

    suspend fun retrieveSeed(): QRCodeSeed {
        delay(2000)
        return QRCodeSeed(
            seed = "a7c5459eb33d08fbcd87e7de1591bc8a",
            expiresAt = "2025-05-16T12:51:36.774Z",
        )
    }
}
