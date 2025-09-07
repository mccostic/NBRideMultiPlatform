package com.dovoh.android_mvi.core.common

interface Mapper<I, O> {
    fun map(input: I): O
}
