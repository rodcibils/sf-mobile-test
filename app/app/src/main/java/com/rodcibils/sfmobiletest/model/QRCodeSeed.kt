package com.rodcibils.sfmobiletest.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @param seed Random String of the seed
 * @param expiresAt Date string in ISO format
 */
@Serializable
data class QRCodeSeed(
    val seed: String,
    @SerialName("expires_at") val expiresAt: String,
)
