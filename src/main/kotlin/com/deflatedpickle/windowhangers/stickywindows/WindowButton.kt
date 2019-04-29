package com.deflatedpickle.windowhangers.stickywindows

import com.deflatedpickle.windowhangers.Icons
import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Button
import org.eclipse.swt.widgets.Composite

class WindowButton(val composite: Composite, val x: Int, val y: Int, val width: Int, val height: Int) {
    var parent: WindowButton? = null

    val button = Button(composite, SWT.PUSH)

    val edgeWindows = mutableMapOf<String, WindowButton>() // mutableListOf<WindowButton>()
    val edgeKeys = listOf(
            listOf("top_left", "top_centre", "top_right"),
            listOf("middle_left", "middle_centre", "middle_right"),
            listOf("bottom_left", "bottom_centre", "bottom_right"))

    // The window button has been clicked and a window has been selected for it
    var isToggled = false

    var xPadding = 10
    var yPadding = 10

    init {
        WindowRegistry.windowButtons.add(this)

        // TODO: Show a connection made between windows
        button.setBounds(x, y, width, height)

        button.addListener(SWT.Selection) {
            // TODO: Enlarge the button when it's clicked, pushing aside it's edge buttons
            if (!isToggled) {
                isToggled = true

                // TODO: Make the mouse able to select a window

                // TODO: Wait until a window has been selected to show these
                for (yMultiplier in -1..1) {
                    for (xMultiplier in -1..1) {
                        // println("$xMultiplier, $yMultiplier")
                        if (xMultiplier != 0 || yMultiplier != 0) {
                            // TODO: Check if the parent already has a button in this location
                            val button = WindowButton(composite, x + ((width + xPadding) * xMultiplier), y + ((height + yPadding) * yMultiplier), width, height).apply { button.image = Icons.addIcon }
                            button.centre()
                            edgeWindows[edgeKeys[yMultiplier + 1][xMultiplier + 1]] = button
                        }
                    }
                }
            }
        }
    }

    fun centre() {
        button.setBounds(x + (composite.clientArea.width / 2) - (width / 2), y + (composite.clientArea.height / 2) - (height / 2), width, height)
    }
}