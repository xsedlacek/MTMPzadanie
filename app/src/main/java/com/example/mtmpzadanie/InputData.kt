package com.example.mtmpzadanie

import kotlinx.serialization.Serializable

@Serializable
data class InputData(
    val angle: Double,
    val power: Double
)
