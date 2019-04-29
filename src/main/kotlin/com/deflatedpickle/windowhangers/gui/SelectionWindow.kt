package com.deflatedpickle.windowhangers.gui

import com.deflatedpickle.jna.User32Extended
import com.deflatedpickle.windowhangers.WindowUtil
import com.deflatedpickle.windowhangers.gui.stickywindows.StickyWindowsUtil
import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.GDI32
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import org.eclipse.swt.SWT
import org.eclipse.swt.graphics.GC
import org.eclipse.swt.graphics.Image
import org.eclipse.swt.internal.win32.OS
import org.eclipse.swt.internal.win32.POINT
import org.eclipse.swt.widgets.Display
import java.awt.Color
import java.awt.Rectangle
import java.awt.Window
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.UIManager


class SelectionWindow : JFrame("Window Hangers - Selection") {
    init {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        SwingUtilities.updateComponentTreeUI(this)

        this.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        this.isAlwaysOnTop = true
        this.isUndecorated = true
        this.background = Color(0, 0, 0, 255 / 4)
        this.type = Window.Type.UTILITY
        this.extendedState = JFrame.MAXIMIZED_BOTH

        this.addMouseListener(object : MouseAdapter() {
            override fun mouseMoved(e: MouseEvent) {
                // TODO: Draw a box around the hovered window, using SystemColor#WINDOW_BORDER
                // TODO: Find the most forward window under the mouse
                // val point = longArrayOf(1)
                // User32Extended.INSTANCE.GetCursorPos(point)

                // for (i in WindowUtil.getAllWindows(true)) {
                //     val rect = WinDef.RECT()
                //     User32.INSTANCE.GetWindowRect(i, rect)

                //     val x = User32Extended.POINT_X(point[0])
                //     val y = User32Extended.POINT_Y(point[0])
                //     // println("$x, $y")
                //     if (Rectangle(rect.left, rect.top, rect.right, rect.bottom).contains(x, y)) {
                //         if (User32Extended.INSTANCE.GetWindow(i, WinDef.DWORD(User32Extended.GW_HWNDNEXT.toLong())) != null) {
                //             println("$point, $i, ${WindowUtil.getTitle(i)}")
                //         }
                //     }
                // }
            }

            override fun mouseClicked(e: MouseEvent) {
                this@SelectionWindow.isVisible = false

                val point = longArrayOf(1)
                User32Extended.INSTANCE.GetCursorPos(point)

                val window = User32Extended.INSTANCE.WindowFromPoint(point[0])
                val windowRect = WinDef.RECT()
                User32.INSTANCE.GetWindowRect(window, windowRect)
                val clientRect = WinDef.RECT()
                User32.INSTANCE.GetClientRect(window, clientRect)
                // println("$point, $window, ${WindowUtil.getTitle(window)}")
                User32.INSTANCE.SetForegroundWindow(window)

                Display.getDefault().asyncExec {
                    val windowButton = StickyWindowsUtil.currentButton!!
                    windowButton.addEdgeButtons()
                    windowButton.button.image = captureRegion(windowRect.left, windowRect.top, clientRect.right, clientRect.bottom)
                }
            }
        }.also { this.addMouseMotionListener(it) })
    }

    fun captureRegion(x: Int, y: Int, width: Int, height: Int): Image? {
        val display = Display.getDefault()

        // println("Capturing $x, $y to $width, $height")

        val gc = GC(display)
        gc.antialias = SWT.ON
        gc.interpolation = SWT.HIGH

        val image = Image(display, width, height)
        gc.copyArea(image, x, y)
        gc.dispose()

        // TODO: Remap the width and height to a specified range (it just stretches it to fit the button)
        val button = StickyWindowsUtil.currentButton!!.button
        return Image(display, image.imageData.scaledTo(button.bounds.width, button.bounds.height))
    }
}