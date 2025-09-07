import SwiftUI

@main
struct iOSApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

/*

import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        KoinKt.initKoin()
        //koinKt.initAppDI(loggingDebug: true)

    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}*/
