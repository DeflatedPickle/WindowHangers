package com.deflatedpickle.windowhangers

import com.deflatedpickle.windowhangers.gui.stickywindows.StickyWindows
import org.eclipse.jface.window.ApplicationWindow
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Control

@Suppress("KDocMissingDocumentation")
fun main(args: Array<String>) {
    // Test window
    // Grabs a window by it's process ID
    // val rootWindow = WindowUtil.getWindowFromProcess(WindowUtil.processMap["notepad++"]!!)!!

    // val attachedWindows = mutableMapOf<String, WinDef.HWND>()
    // val hookPoints = mutableMapOf<String, HookPoint>()

    // Grabs a window by it's name
    // attachedWindows["notepad"] = WindowUtil.getWindowFromTitle("Untitled - Notepad")!!
    // hookPoints["notepad"] = HookPoint.Left

    // Grabs a window by part of it's name
    // attachedWindows["gvim"] = WindowUtil.getWindowFromTitle("GVIM", true)!!
    // hookPoints["gvim"] = HookPoint.Top

    // val windowHanger = WindowHanger(rootWindow, attachedWindows, hookPoints)
    // windowHanger.run()

    val windowHangerThread = WindowHangerThread()
    Thread(windowHangerThread).start()

    val window = object : ApplicationWindow(null) {
        override fun create() {
            super.create()

            shell.text = "Window Hangers"
            shell.setMinimumSize(600, 400)
        }

        override fun createContents(parent: Composite): Control {
            Icons.createIcons()
            StickyWindows(parent)

            return super.createContents(parent)
        }

        override fun close(): Boolean {
            windowHangerThread.run = false
            return super.close()
        }
    }
    window.setBlockOnOpen(true)

    window.open()
}