package org.example.project.nbride

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform