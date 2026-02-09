package com.threeapps.getmystuff

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Get My Stuff"
    ) {
        App()
    }
}
