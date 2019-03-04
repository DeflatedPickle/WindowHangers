package com.deflatedpickle.windowhangers

import com.sun.jna.Pointer
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
import com.sun.jna.ptr.IntByReference
import org.joml.Vector2i


fun main(args: Array<String>) {
    // WindowUtil.processMap.forEach { t, u -> println("$t = $u") }

    // Test window
    val firstWindow = WindowUtil.processMap["notepad++"]
    AttachedWindows.rootWindowProcessID = firstWindow

    val secondWindow = WindowUtil.processMap["gvim"]
    AttachedWindows.attachedWindowProcessIDs["gvim"] = secondWindow

    AttachedWindows.hookPoint = HookPoint.Top

    // TODO: Move to WindowUtil
    User32.INSTANCE.EnumWindows(object : WinUser.WNDENUMPROC {
        override fun callback(hwnd: WinDef.HWND, pntr: Pointer): Boolean {
            val dwProcessId = IntByReference(0)

            User32.INSTANCE.GetWindowThreadProcessId(hwnd, dwProcessId)

            if (AttachedWindows.rootWindowHandleID == null
                    && dwProcessId.value == AttachedWindows.rootWindowProcessID!!.value) {
                AttachedWindows.rootWindowHandleID = User32.INSTANCE.GetWindow(hwnd, WinDef.DWORD(User32.GW_OWNER.toLong()))
            }

            for ((k, v) in AttachedWindows.attachedWindowProcessIDs) {
                if (AttachedWindows.attachedWindowHandleIDs[k] == null
                        && dwProcessId.value == AttachedWindows.attachedWindowProcessIDs[k]!!.value) {
                    AttachedWindows.attachedWindowHandleIDs[k] = User32.INSTANCE.GetWindow(hwnd, WinDef.DWORD(User32.GW_OWNER.toLong()))
                }
            }

            return true
        }

    }, Pointer(1L))

    val rootRect = WinDef.RECT()
    val attachedRect = mutableMapOf<String, WinDef.RECT>()

    for ((k, v) in AttachedWindows.attachedWindowProcessIDs) {
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

        for ((k, v) in attachedRect) {
            val attachedWidth = attachedRect[k]!!.right - attachedRect[k]!!.left
            val attachedHeight = attachedRect[k]!!.bottom - attachedRect[k]!!.top

            when (AttachedWindows.hookPoint) {
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