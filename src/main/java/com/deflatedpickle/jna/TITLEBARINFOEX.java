package com.deflatedpickle.jna;

import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinDef;

// https://docs.microsoft.com/en-us/windows/desktop/api/winuser/ns-winuser-tagtitlebarinfoex
@Structure.FieldOrder({"cbSize", "rcTitleBar", "rgstate", "rgrect"})
public class TITLEBARINFOEX extends TITLEBARINFO {
    public WinDef.RECT[] rgrect;

    public TITLEBARINFOEX() {
        rgstate = new int[CCHILDREN_TITLEBAR + 1];
        rgrect = new WinDef.RECT[CCHILDREN_TITLEBAR + 1];
        cbSize = size();
    }
}
