package com.deflatedpickle.windowhangers.stickywindows

import com.deflatedpickle.windowhangers.Icons
import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Button
import org.eclipse.swt.widgets.Composite

class WindowButton(val parent: WindowButton? = null, val composite: Composite, val x: Int, val y: Int) {
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

    val untoggledWidth = 30
    val untoggledHeight = 30

    val toggledWidth = untoggledWidth * 6
    val toggledHeight = untoggledHeight * 4

    var currentWidth = untoggledWidth
    var currentHeight = untoggledHeight

    init {
        // TODO: Show a connection made between windows
        currentWidth = untoggledWidth
        currentHeight = untoggledHeight
        button.setBounds(x, y, currentWidth, currentHeight)

        button.addListener(SWT.Selection) {
            // TODO: Enlarge the button when it's clicked, pushing aside it's edge buttons
            if (!isToggled) {
                isToggled = true

                currentWidth = toggledWidth
                currentHeight = toggledHeight

                // TODO: Make the mouse able to select a window

                // TODO: Wait until a window has been selected to show these
                for (yMultiplier in -1..1) {
                    for (xMultiplier in -1..1) {
                        // println("$xMultiplier, $yMultiplier")
                        if (xMultiplier != 0 || yMultiplier != 0) {
                            // TODO: Check if the parent already has a button in this location
                            edgeWindows[edgeKeys[yMultiplier + 1][xMultiplier + 1]] =
                                    WindowButton(this, composite,
                                            x + (((parent?.currentWidth ?: currentWidth) + xPadding) * xMultiplier),
                                            y + (((parent?.currentHeight ?: currentHeight) + yPadding) * yMultiplier)
                                    ).apply { button.image = Icons.addIcon }
                        }
                    }
                }
                centre()
            }
        }
    }

    fun centre() {
        button.setBounds(x + (composite.clientArea.width / 2) - (currentWidth / 2), y + (composite.clientArea.height / 2) - (currentHeight / 2), currentWidth, currentHeight)

        for (i in edgeWindows.values) {
            i.centre()
        }
    }
}