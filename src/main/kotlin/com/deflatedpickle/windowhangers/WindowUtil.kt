package com.deflatedpickle.windowhangers

import com.sun.jna.Native
import com.sun.jna.StringArray
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.ptr.IntByReference
import org.joml.Vector2i


object WindowUtil {
    val processMap = mutableMapOf<String, IntByReference>()

    val windowPosition = Vector2i()
    val windowSize = Vector2i()

    val movementSpeed = Vector2i()

    init {
        for (ph in ProcessHandle.allProcesses()) {
            // println("ID: ${ph.pid()} | Command: ${ph.info().command()} | Parent: ${ph.parent()}")

            if (ph.info().command().isPresent) {
                processMap[ph.info().command().get().split("\\").last().split(".").first().toLowerCase()] = IntByReference(ph.pid().toInt())
            }
        }
    }

    fun getWindowFromTitle(name: String, partial: Boolean = false): WinDef.HWND? {
        var window: WinDef.HWND? = null

        User32.INSTANCE.EnumWindows({ hwnd, pntr ->
            val windowText = CharArray(512)
            User32.INSTANCE.GetWindowText(hwnd, windowText, 512)
            val wText = Native.toString(windowText)

            if (wText == pntr.getStringArray(0L)[0]) {
                window = hwnd
                return@EnumWindows false
            }
            else {
                if (partial) {
                    if (wText.contains(pntr.getStringArray(0L)[0])) {
                        window = hwnd
                        return@EnumWindows false
                    }
                }
            }

            true
        }, StringArray(arrayOf(name)))

        return window
    }

    fun getWindowFromProcess(process: IntByReference): WinDef.HWND? {
        var window: WinDef.HWND? = null

        User32.INSTANCE.EnumWindows({ hwnd, pntr ->
            val currentProcess = IntByReference(0)
            User32.INSTANCE.GetWindowThreadProcessId(hwnd, currentProcess)

            if (currentProcess.value == process.value) {
                window = hwnd
                return@EnumWindows false
            }

            true
        }, null)

        return User32.INSTANCE.GetWindow(window, WinDef.DWORD(User32.GW_OWNER.toLong()))
    }
}