package com.deflatedpickle.windowhangers

import com.sun.jna.Pointer
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
import com.sun.jna.ptr.IntByReference


fun main(args: Array<String>) {
    val processMap = mutableMapOf<String, IntByReference>()

    for (ph in ProcessHandle.allProcesses()) {
        // println("ID: ${ph.pid()} | Command: ${ph.info().command()} | Parent: ${ph.parent()}")

        if (ph.info().command().isPresent) {
            processMap[ph.info().command().get().split("\\").last().split(".").first()] = IntByReference(ph.pid().toInt())
        }
    }

    // processMap.forEach { t, u -> println("$t = $u") }

    // Test window
    val firstWindow = processMap["notepad++"]
    AttachedWindows.firstWindowProcessID = firstWindow

    val secondWindow = processMap["Notepad2"]
    AttachedWindows.secondWindowProcessID = secondWindow

    AttachedWindows.hookPoint = HookPoint.Top

    User32.INSTANCE.EnumWindows(object : WinUser.WNDENUMPROC {
        override fun callback(hwnd: WinDef.HWND, pntr: Pointer): Boolean {
            val dwProcessId = IntByReference(0)

            User32.INSTANCE.GetWindowThreadProcessId(hwnd, dwProcessId)

            if (AttachedWindows.firstWindowHandleID == null
                    && dwProcessId.value == AttachedWindows.firstWindowProcessID!!.value) {
                AttachedWindows.firstWindowHandleID = User32.INSTANCE.GetWindow(hwnd, WinDef.DWORD(User32.GW_OWNER.toLong()))
            }

            if (AttachedWindows.secondWindowHandleID == null
                    && dwProcessId.value == AttachedWindows.secondWindowProcessID!!.value) {
                AttachedWindows.secondWindowHandleID = User32.INSTANCE.GetWindow(hwnd, WinDef.DWORD(User32.GW_OWNER.toLong()))
            }

            return true
        }

    }, Pointer(1L))

    val placement = WinUser.WINDOWPLACEMENT()
    User32.INSTANCE.GetWindowPlacement(AttachedWindows.firstWindowHandleID, placement)
    User32.INSTANCE.SetWindowPlacement(AttachedWindows.secondWindowHandleID, placement)

    val firstRect = WinDef.RECT()
    val secondRect = WinDef.RECT()

    while (true) {
        User32.INSTANCE.GetWindowRect(AttachedWindows.firstWindowHandleID, firstRect)
        User32.INSTANCE.GetClientRect(AttachedWindows.secondWindowHandleID, secondRect)

        val firstWidth = firstRect.right - firstRect.left
        val firstHeight = firstRect.bottom - firstRect.top

        val secondWidth = secondRect.right - secondRect.left
        val secondHeight = secondRect.bottom - secondRect.top

        // TODO: Implement hook points
        User32.INSTANCE.MoveWindow(AttachedWindows.secondWindowHandleID,
                firstRect.left,
                firstRect.top - secondHeight - 51,
                firstWidth,
                240,
                true)
    }
}