package com.deflatedpickle.windowhangers

import com.sun.jna.platform.win32.WinDef
import com.sun.jna.ptr.IntByReference

object AttachedWindows {
    var rootWindowProcessID: IntByReference? = null
    var attachedWindowProcessIDs: MutableMap<String, IntByReference?> = mutableMapOf()

    var rootWindowHandleID: WinDef.HWND? = null
    var attachedWindowHandleIDs: MutableMap<String, WinDef.HWND?> = mutableMapOf()

    var hookPoints: MutableMap<String, HookPoint> = mutableMapOf()
}