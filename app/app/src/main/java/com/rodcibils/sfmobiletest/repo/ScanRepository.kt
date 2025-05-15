package com.rodcibils.sfmobiletest.repo

import kotlinx.coroutines.delay

class ScanRepository {
    suspend fun isSeedValid(seed: String): Boolean {
        delay(2000)
        return false
    }
}
