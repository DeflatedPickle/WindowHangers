package com.deflatedpickle.windowhangers

import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
import com.sun.jna.ptr.IntByReference
import java.lang.ProcessHandle
import org.eclipse.swt.internal.win32.OS.GetWindowTextA




object AttachedWindows {
    var firstWindowProcessID: IntByReference? = null
    var secondWindowProcessID: IntByReference? = null

    var firstWindowHandleID: WinDef.HWND? = null
    var secondWindowHandleID: WinDef.HWND? = null
}

fun main(args: Array<String>) {
    val processMap = mutableMapOf<String, Long>()

    for (ph in ProcessHandle.allProcesses()) {
        // println("ID: ${ph.pid()} | Command: ${ph.info().command()} | Parent: ${ph.parent()}")

        if (ph.info().command().isPresent) {
            processMap[ph.info().command().get().split("\\").last().split(".").first()] = ph.pid()
        }
    }

    // processMap.forEach { t, u -> println("$t = $u") }

    // Test window
    val window = IntByReference(20916)
    AttachedWindows.firstWindowProcessID = window

    val attachedWindow = IntByReference(21600)
    AttachedWindows.secondWindowProcessID = attachedWindow

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

    val rect = WinDef.RECT()
    User32.INSTANCE.GetWindowRect(AttachedWindows.firstWindowHandleID, rect)

    User32.INSTANCE.SetWindowPlacement(AttachedWindows.secondWindowHandleID, placement)

    User32.INSTANCE.MoveWindow(AttachedWindows.secondWindowHandleID, rect.left, rect.top, rect.right, rect.bottom, true)
}