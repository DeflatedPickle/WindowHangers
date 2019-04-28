package com.deflatedpickle.windowhangers.stickywindows

import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Button
import org.eclipse.swt.widgets.Composite

class Window(val composite: Composite, val x: Int, val y: Int, val width: Int, val height: Int) {
    var parent: Window? = null

    val button = Button(composite, SWT.PUSH)

    val edgeWindows = mutableListOf<Window>()

    // The window button has been clicked and a window has been selected for it
    var isToggled = false

    var xPadding = 10
    var yPadding = 10

    init {
        button.setBounds(x, y, width, height)

        button.addListener(SWT.Selection) {
            if (!isToggled) {
                isToggled = true

                // TODO: Make the mouse able to select a window

                for (yMultiplier in -1..1) {
                    for (xMultiplier in -1..1) {
                        if (y != 0 && x != 0) {
                            edgeWindows.add(Window(composite, x + ((width + xPadding) * xMultiplier), y + ((height + yPadding) * yMultiplier), width, height))
                        }
                    }
                }
            }
        }
    }
}