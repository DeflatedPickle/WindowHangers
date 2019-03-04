package com.deflatedpickle.windowhangers

import com.sun.jna.platform.win32.WinDef
import com.sun.jna.ptr.IntByReference

object AttachedWindows {
    var firstWindowProcessID: IntByReference? = null
    var secondWindowProcessID: IntByReference? = null

    var firstWindowHandleID: WinDef.HWND? = null
    var secondWindowHandleID: WinDef.HWND? = null

    var hookPoint = HookPoint.Centre
}