package org.example.project.nbride

import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController {
    initKoin()
    AppRoot()
}