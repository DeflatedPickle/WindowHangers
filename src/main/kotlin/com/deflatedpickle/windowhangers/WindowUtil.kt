package com.deflatedpickle.windowhangers

import com.sun.jna.Native
import com.sun.jna.StringArray
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
import com.sun.jna.ptr.IntByReference
import org.joml.Vector2i


/**
 * A utility object for working with windows on-screen
 */
object WindowUtil {
    /**
     * A map of the processes running (not updated during run-time)
     */
    val processMap = mutableMapOf<String, IntByReference>()

    /**
     * The position of the root window
     */
    val windowPosition = Vector2i()
    /**
     * The size of the root window
     */
    val windowSize = Vector2i()

    /**
     * The speed the root window is moving at
     */
    val movementSpeed = Vector2i()

    init {
        for (ph in ProcessHandle.allProcesses()) {
            // println("ID: ${ph.pid()} | Command: ${ph.info().command()} | Parent: ${ph.parent()}")

            if (ph.info().command().isPresent) {
                processMap[ph.info().command().get().split("\\").last().split(".").first().toLowerCase()] = IntByReference(ph.pid().toInt())
            }
        }
    }

    fun getAllWindows(onlyShown: Boolean = false): List<WinDef.HWND> {
        val windows: MutableList<WinDef.HWND> = mutableListOf()

        User32.INSTANCE.EnumWindows({ hwnd, pntr ->
            if (getTitle(hwnd) !in arrayOf("", "Default IME", "MSCTFIME UI")) {
                if (onlyShown) {
                    if (!isIconic(hwnd)) {
                        windows.add(hwnd)
                    }
                }
                else {
                    windows.add(hwnd)
                }
            }

            true
        }, null)

        return windows
    }

    /**
     * Gets a window's HWND from its title
     */
    fun getWindowFromTitle(name: String, partial: Boolean = false): WinDef.HWND? {
        var window: WinDef.HWND? = null

        User32.INSTANCE.EnumWindows({ hwnd, pntr ->
            val wText = getTitle(hwnd)

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

    /**
     * Gets the owner window from a process ID
     */
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

    fun getTitle(hwnd: WinDef.HWND): String {
        val windowText = CharArray(512)
        User32.INSTANCE.GetWindowText(hwnd, windowText, 512)

        return Native.toString(windowText)
    }

    fun isIconic(hwnd: WinDef.HWND): Boolean {
        val info = WinUser.WINDOWINFO()
        User32.INSTANCE.GetWindowInfo(hwnd, info)

        if (info.dwStyle and WinUser.WS_MINIMIZE == WinUser.WS_MINIMIZE) {
            return true
        }
        return false
    }
}