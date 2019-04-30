package com.deflatedpickle.windowhangers.gui.stickywindows

import com.sun.jna.platform.win32.WinDef

object StickyWindowsUtil {
    var currentButton: WindowButton? = null

    var registeredHandles = mutableListOf<WinDef.HWND>()
}