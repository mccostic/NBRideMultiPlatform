package com.dovoh.android_mvi.feature.home.presentation.model

data class RideOption(
    val id: String,
    val name: String,
    val description: String,
    val icon: String,
    val time: String,
    val price: Double,
    val seats: Int,
)
