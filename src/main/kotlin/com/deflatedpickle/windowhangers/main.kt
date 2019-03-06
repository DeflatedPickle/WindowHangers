package com.deflatedpickle.windowhangers

import com.sun.jna.platform.win32.WinDef


@Suppress("KDocMissingDocumentation")
fun main(args: Array<String>) {
    // Test window
    // Grabs a window by it's process ID
    val rootWindow = WindowUtil.getWindowFromProcess(WindowUtil.processMap["notepad++"]!!)!!

    val attachedWindows = mutableMapOf<String, WinDef.HWND>()
    val hookPoints = mutableMapOf<String, HookPoint>()

    // Grabs a window by it's name
    attachedWindows["notepad"] = WindowUtil.getWindowFromTitle("Untitled - Notepad")!!
    hookPoints["notepad"] = HookPoint.Left

    // Grabs a window by part of it's name
    attachedWindows["gvim"] = WindowUtil.getWindowFromTitle("GVIM", true)!!
    hookPoints["gvim"] = HookPoint.Top

    val windowHanger = WindowHanger(rootWindow, attachedWindows, hookPoints)
    windowHanger.run()
}