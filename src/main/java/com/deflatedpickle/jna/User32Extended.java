package com.deflatedpickle.jna;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.W32APIOptions;

public interface User32Extended extends User32 {
    User32Extended INSTANCE = Native.load("user32", User32Extended.class, W32APIOptions.DEFAULT_OPTIONS);

    // States
    int STATE_SYSTEM_FOCUSABLE = 0x00100000;
    int STATE_SYSTEM_INVISIBLE = 0x00008000;
    int STATE_SYSTEM_OFFSCREEN = 0x00010000;
    int STATE_SYSTEM_UNAVAILABLE = 0x00000001;
    int STATE_SYSTEM_PRESSED = 0x00000008;

    // Extended Styles
    // https://docs.microsoft.com/en-gb/windows/desktop/winmsg/extended-window-styles
    int WS_EX_ACCEPTFILES = 0x00000010;
    int WS_EX_APPWINDOW = 0x00040000;
    int WS_EX_CLIENTEDGE = 0x00000200;
    int WS_EX_COMPOSITED = 0x02000000;
    int WS_EX_CONTEXTHELP = 0x00000400;
    int WS_EX_CONTROLPARENT = 0x00010000;
    int WS_EX_DLGMODALFRAME = 0x00000001;
    int WS_EX_LAYERED = 0x00080000;
    int WS_EX_LAYOUTRTL = 0x00400000;
    int WS_EX_LEFT = 0x00000000;
    int WS_EX_LEFTSCROLLBAR = 0x00004000;
    int WS_EX_LTRREADING = 0x00000000;
    int WS_EX_MDICHILD = 0x00000040;
    int WS_EX_NOACTIVATE = 0x08000000;
    int WS_EX_NOINHERITLAYOUT = 0x00100000;
    int WS_EX_NOPARENTNOTIFY = 0x00000004;
    int WS_EX_NOREDIRECTIONBITMAP = 0x00200000;
    int WS_EX_RIGHT = 0x00001000;
    int WS_EX_RIGHTSCROLLBAR = 0x00000000;
    int WS_EX_RTLREADING = 0x00002000;
    int WS_EX_STATICEDGE = 0x00020000;
    int WS_EX_TOOLWINDOW = 0x00000080;
    int WS_EX_TOPMOST = 0x00000008;
    int WS_EX_TRANSPARENT = 0x00000020;
    int WS_EX_WINDOWEDGE = 0x00000100;

    long WS_EX_OVERLAPPEDWINDOW = (WS_EX_WINDOWEDGE | WS_EX_CLIENTEDGE);
    long WS_EX_PALETTEWINDOW = (WS_EX_WINDOWEDGE | WS_EX_TOOLWINDOW | WS_EX_TOPMOST);

    // https://docs.microsoft.com/en-us/windows/desktop/api/winuser/nf-winuser-gettitlebarinfo
    boolean GetTitleBarInfo(WinDef.HWND hwnd, TITLEBARINFO titlebarinfo);

    // https://docs.microsoft.com/en-us/windows/desktop/api/winuser/nf-winuser-getlastactivepopup
    WinDef.HWND GetLastActivePopup(WinDef.HWND hwnd);

    // https://docs.microsoft.com/en-us/windows/desktop/api/winuser/nf-winuser-isiconic
    boolean IsIconic(WinDef.HWND hwnd);
}
