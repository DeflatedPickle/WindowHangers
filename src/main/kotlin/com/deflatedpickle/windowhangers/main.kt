package com.deflatedpickle.windowhangers

import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import org.joml.Vector2i


fun main(args: Array<String>) {
    // WindowUtil.processMap.forEach { t, u -> println("$t = $u") }

    // Test window
    // Grabs a window by it's process ID
    AttachedWindows.rootWindowHandleID = WindowUtil.getWindowFromProcess(WindowUtil.processMap["notepad++"]!!)

    // Grabs a window by it's name
    AttachedWindows.attachedWindowHandleIDs["notepad"] = WindowUtil.getWindowFromTitle("Untitled - Notepad")
    AttachedWindows.hookPoints["notepad"] = HookPoint.Left

    // Grabs a window by part of it's name
    AttachedWindows.attachedWindowHandleIDs["gvim"] = WindowUtil.getWindowFromTitle("GVIM", true)
    AttachedWindows.hookPoints["gvim"] = HookPoint.Top

    val rootRect = WinDef.RECT()
    val attachedRect = mutableMapOf<String, WinDef.RECT>()

    for ((k, v) in AttachedWindows.attachedWindowHandleIDs) {
        attachedRect[k] = WinDef.RECT()
        User32.INSTANCE.GetWindowRect(AttachedWindows.attachedWindowHandleIDs[k], attachedRect[k])
    }

    // val placement = WinUser.WINDOWPLACEMENT()
    // User32.INSTANCE.GetWindowPlacement(AttachedWindows.rootWindowHandleID, placement)
    // User32.INSTANCE.SetWindowPlacement(AttachedWindows.secondWindowHandleID, placement)

    val oldPosition = Vector2i()
    val newPosition = Vector2i()

    while (true) {
        oldPosition.set(rootRect.left, rootRect.top)
        User32.INSTANCE.GetWindowRect(AttachedWindows.rootWindowHandleID, rootRect)
        newPosition.set(rootRect.left, rootRect.top)

        WindowUtil.movementSpeed.set(oldPosition.x - newPosition.x, oldPosition.y - newPosition.y)
        WindowUtil.windowPosition.set(newPosition.x, newPosition.y)

        val rootWidth = rootRect.right - rootRect.left
        val rootHeight = rootRect.bottom - rootRect.top

        WindowUtil.windowSize.set(rootWidth, rootHeight)

        for ((k, _) in attachedRect) {
            val attachedWidth = attachedRect[k]!!.right - attachedRect[k]!!.left
            val attachedHeight = attachedRect[k]!!.bottom - attachedRect[k]!!.top

            when (AttachedWindows.hookPoints[k]) {
                HookPoint.Top -> {
                    User32.INSTANCE.MoveWindow(AttachedWindows.attachedWindowHandleIDs[k],
                            rootRect.left,
                            rootRect.top - attachedHeight + 8,
                            rootWidth,
                            attachedHeight,
                            true)
                }
                HookPoint.Right -> {
                    User32.INSTANCE.MoveWindow(AttachedWindows.attachedWindowHandleIDs[k],
                            rootRect.left + rootWidth - 15,
                            rootRect.top,
                            attachedWidth,
                            rootHeight,
                            true)
                }
                HookPoint.Bottom -> {
                    User32.INSTANCE.MoveWindow(AttachedWindows.attachedWindowHandleIDs[k],
                            rootRect.left,
                            rootRect.top + rootHeight - 8,
                            rootWidth,
                            attachedHeight,
                            true)
                }
                HookPoint.Left -> {
                    User32.INSTANCE.MoveWindow(AttachedWindows.attachedWindowHandleIDs[k],
                            rootRect.left - attachedWidth + 15,
                            rootRect.top,
                            attachedWidth,
                            rootHeight,
                            true)
                }
                HookPoint.Centre -> {
                    User32.INSTANCE.MoveWindow(AttachedWindows.attachedWindowHandleIDs[k],
                            rootRect.left + (rootWidth / 2) - (attachedWidth / 2),
                            rootRect.top + (rootHeight / 2) - (attachedHeight / 2),
                            attachedWidth,
                            attachedHeight,
                            true)
                }
            }
        }
    }
}