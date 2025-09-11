package com.dovoh.android_mvi.core.common

fun interface Mapper<I, O> {
    fun map(input: I): O
}
