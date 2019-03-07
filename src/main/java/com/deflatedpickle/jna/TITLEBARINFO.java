package com.deflatedpickle.jna;

import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinDef;

// https://docs.microsoft.com/en-us/windows/desktop/api/winuser/ns-winuser-tagtitlebarinfo
@Structure.FieldOrder({"cbSize", "rcTitleBar", "rgstate"})
public class TITLEBARINFO extends Structure {
    public int cbSize;
    public WinDef.RECT rcTitleBar;
    public int[] rgstate;

    public TITLEBARINFO() {
        rgstate = new int[CCHILDREN_TITLEBAR + 1];
        cbSize = size();
    }

    // Index constants
    public static final int TITLE_BAR = 0;
    public static final int RESERVED = 1;
    public static final int MINIMIZE_BUTTON = 2;
    public static final int MAXIMIZE_BUTTON = 3;
    public static final int HELP_BUTTON = 4;
    public static final int CLOSE_BUTTON = 5;

    // Child amount constant
    public static final int CCHILDREN_TITLEBAR = 5;
}
