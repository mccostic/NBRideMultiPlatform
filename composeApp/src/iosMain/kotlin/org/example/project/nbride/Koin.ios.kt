package org.example.project.nbride

import com.dovoh.android_mvi.di.appModule
import org.koin.core.context.startKoin


fun initKoin() {
    startKoin { modules(appModule) }
}
