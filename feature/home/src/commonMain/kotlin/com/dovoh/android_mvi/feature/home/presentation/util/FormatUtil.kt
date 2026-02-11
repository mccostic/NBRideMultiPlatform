package com.dovoh.android_mvi.feature.home.presentation.util

fun Double.formatPrice(): String {
    val cents = (this * 100).toLong()
    val dollars = cents / 100
    val remainder = (cents % 100).toString().padStart(2, '0')
    return "$dollars.$remainder"
}
