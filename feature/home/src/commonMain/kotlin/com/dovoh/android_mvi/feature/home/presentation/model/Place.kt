package com.dovoh.android_mvi.feature.home.presentation.model

data class Place(
    val id: Int,
    val name: String,
    val address: String,
    val icon: String,
    val lat: Double = 0.0,
    val lng: Double = 0.0,
)
