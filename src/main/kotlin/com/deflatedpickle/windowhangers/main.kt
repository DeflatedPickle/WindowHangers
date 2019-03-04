package com.deflatedpickle.windowhangers

import com.sun.jna.Pointer
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
import com.sun.jna.ptr.IntByReference


fun main(args: Array<String>) {
    // WindowUtil.processMap.forEach { t, u -> println("$t = $u") }

    // Test window
    val firstWindow = WindowUtil.processMap["notepad++"]
    AttachedWindows.firstWindowProcessID = firstWindow

    val secondWindow = WindowUtil.processMap["gvim"]
    AttachedWindows.secondWindowProcessID = secondWindow

    AttachedWindows.hookPoint = HookPoint.Top

    // TODO: Move to WindowUtil
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

    val firstRect = WinDef.RECT()
    val secondRect = WinDef.RECT()

    User32.INSTANCE.GetWindowRect(AttachedWindows.secondWindowHandleID, secondRect)

    val placement = WinUser.WINDOWPLACEMENT()
    User32.INSTANCE.GetWindowPlacement(AttachedWindows.firstWindowHandleID, placement)
    // User32.INSTANCE.SetWindowPlacement(AttachedWindows.secondWindowHandleID, placement)

    while (true) {
        User32.INSTANCE.GetWindowRect(AttachedWindows.firstWindowHandleID, firstRect)

        val firstWidth = firstRect.right - firstRect.left
        val firstHeight = firstRect.bottom - firstRect.top

        val secondWidth = secondRect.right - secondRect.left
        val secondHeight = secondRect.bottom - secondRect.top

        when (AttachedWindows.hookPoint) {
            HookPoint.Top -> {
                User32.INSTANCE.MoveWindow(AttachedWindows.secondWindowHandleID,
                        firstRect.left,
                        firstRect.top - secondHeight + 8,
                        firstWidth,
                        secondHeight,
                        true)
            }
            HookPoint.Right -> {
                User32.INSTANCE.MoveWindow(AttachedWindows.secondWindowHandleID,
                        firstRect.left + firstWidth - 15,
                        firstRect.top,
                        secondWidth,
                        firstHeight,
                        true)
            }
            HookPoint.Bottom -> {
                User32.INSTANCE.MoveWindow(AttachedWindows.secondWindowHandleID,
                        firstRect.left,
                        firstRect.top + firstHeight - 8,
                        firstWidth,
                        secondHeight,
                        true)
            }
            HookPoint.Left -> {
                User32.INSTANCE.MoveWindow(AttachedWindows.secondWindowHandleID,
                        firstRect.left - secondWidth + 15,
                        firstRect.top,
                        secondWidth,
                        firstHeight,
                        true)
            }
            HookPoint.Centre -> {
                User32.INSTANCE.MoveWindow(AttachedWindows.secondWindowHandleID,
                        firstRect.left + (firstWidth / 2) - (secondWidth / 2),
                        firstRect.top + (firstHeight / 2) - (secondHeight / 2),
                        secondWidth,
                        secondHeight,
                        true)
            }
        }
    }
}