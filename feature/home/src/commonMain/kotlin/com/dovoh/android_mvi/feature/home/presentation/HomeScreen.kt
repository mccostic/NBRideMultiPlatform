package com.dovoh.android_mvi.feature.home.presentation

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person

@Composable
fun HomeScreen() {
    var selectedTab by remember { mutableStateOf(0) }

    val tabs = listOf(
        BottomTab("Dashboard", Icons.Default.Home),
        BottomTab("Products", Icons.AutoMirrored.Filled.List),
        BottomTab("Profile", Icons.Default.Person),
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (selectedTab) {
                0 -> Text("📊 Dashboard Screen", style = MaterialTheme.typography.headlineMedium)
                1 -> Text("🛒 Products List Screen", style = MaterialTheme.typography.headlineMedium)
                2 -> Text("👤 Profile Screen", style = MaterialTheme.typography.headlineMedium)
            }
        }
    }
}

private data class BottomTab(val label: String, val icon: ImageVector)
