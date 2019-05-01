package com.deflatedpickle.windowhangers

import com.deflatedpickle.jna.TITLEBARINFO
import com.deflatedpickle.jna.User32Extended
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

    init {
        for (ph in ProcessHandle.allProcesses()) {
            // println("ID: ${ph.pid()} | Command: ${ph.info().command()} | Parent: ${ph.parent()}")

            if (ph.info().command().isPresent) {
                processMap[ph.info().command().get().split("\\").last().split(".").first().toLowerCase()] = IntByReference(ph.pid().toInt())
            }
        }
    }

    /**
     * Returns a list of all the current windows that are shown
     */
    fun getAllWindows(onlyShown: Boolean = false): List<WinDef.HWND> {
        val windows: MutableList<WinDef.HWND> = mutableListOf()

        User32.INSTANCE.EnumWindows({ hwnd, pntr ->
            if (isWindow(hwnd)) {
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

    /**
     * Gets the title of a window as a string
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun getTitle(hwnd: WinDef.HWND): String {
        val length = User32.INSTANCE.GetWindowTextLength(hwnd) + 1
        val windowText = CharArray(length)
        User32.INSTANCE.GetWindowText(hwnd, windowText, length)

        return Native.toString(windowText)
    }

    /**
     * Gets the class of the window
     */
    fun getClass(hwnd: WinDef.HWND): String {
        val className = CharArray(80)
        User32.INSTANCE.GetClassName(hwnd, className, 80)

        return Native.toString(className)
    }

    /**
     * Determines whether or not a window is minimized
     */
    @Suppress("MemberVisibilityCanBePrivate")
    // https://stackoverflow.com/a/7292674
    fun isIconic(hwnd: WinDef.HWND): Boolean {
        val info = WinUser.WINDOWINFO()
        User32.INSTANCE.GetWindowInfo(hwnd, info)

        if (info.dwStyle and WinUser.WS_MINIMIZE == WinUser.WS_MINIMIZE) {
            return true
        }
        return false
    }

    /**
     * Determines whether or not the given HWND is a window
     */
    // TODO: Check if the program is running in the background, if so, return false
    // TODO: Move visibility checks to a different function
    fun isWindow(hwnd: WinDef.HWND): Boolean {
        if (getTitle(hwnd).isEmpty()
                || !User32.INSTANCE.IsWindowVisible(hwnd)
                || !User32.INSTANCE.IsWindowEnabled(hwnd)
                || User32.INSTANCE.GetWindowLong(hwnd, User32.GWL_EXSTYLE) and User32Extended.WS_EX_TOOLWINDOW != 0) {
            return false
        }

        var hwndTry = User32.INSTANCE.GetAncestor(hwnd, User32.GA_ROOTOWNER)
        var hwndWalk: WinDef.HWND? = null
        while (hwndTry != hwndWalk) {
            hwndWalk = hwndTry
            hwndTry = User32Extended.INSTANCE.GetLastActivePopup(hwndWalk)

            if (User32.INSTANCE.IsWindowVisible(hwndTry)) {
                break
            }
        }
        if (hwndWalk != hwnd) {
            return false
        }

        val titleBarInfo = TITLEBARINFO()
        User32Extended.INSTANCE.GetTitleBarInfo(hwnd, titleBarInfo)

        if (titleBarInfo.rgstate[TITLEBARINFO.TITLE_BAR] and User32Extended.STATE_SYSTEM_INVISIBLE != 0) {
            return false
        }

        if (User32Extended.INSTANCE.IsIconic(hwnd)) {
            return false
        }

        return true
    }
}