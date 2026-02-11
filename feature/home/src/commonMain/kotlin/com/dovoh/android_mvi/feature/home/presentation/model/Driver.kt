package com.dovoh.android_mvi.feature.home.presentation.model

data class Driver(
    val name: String,
    val rating: Double,
    val trips: Int,
    val car: String,
    val plate: String,
    val avatar: String,
)
