package com.deflatedpickle.windowhangers.gui.stickywindows

import com.deflatedpickle.windowhangers.Icons
import com.deflatedpickle.windowhangers.gui.SelectionWindow
import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Button
import org.eclipse.swt.widgets.Composite

class WindowButton(val parent: WindowButton? = null, val composite: Composite, val x: Int, val y: Int) {
    val button = Button(composite, SWT.PUSH)

    val edgeWindows = mutableMapOf<ButtonEdge, WindowButton>()
    val edgeKeys = listOf(
            listOf(ButtonEdge.TOP_LEFT, ButtonEdge.TOP_CENTRE, ButtonEdge.TOP_RIGHT),
            listOf(ButtonEdge.MIDDLE_LEFT, ButtonEdge.MIDDLE_CENTRE, ButtonEdge.MIDDLE_RIGHT),
            listOf(ButtonEdge.BOTTOM_LEFT, ButtonEdge.BOTTOM_CENTRE, ButtonEdge.BOTTOM_RIGHT))

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
            if (!isToggled) {
                isToggled = true

                currentWidth = toggledWidth
                currentHeight = toggledHeight

                StickyWindowsUtil.currentButton = this

                val selectionWindow = SelectionWindow()
                selectionWindow.isVisible = true
            }
        }
    }

    fun centre() {
        button.setBounds(x + (composite.clientArea.width / 2) - (currentWidth / 2), y + (composite.clientArea.height / 2) - (currentHeight / 2), currentWidth, currentHeight)

        for (i in edgeWindows.values) {
            i.centre()
        }
    }

    fun addEdgeButtons() {
        for (yMultiplier in -1..1) {
            for (xMultiplier in -1..1) {
                // println("$xMultiplier, $yMultiplier [${ButtonEdge.fromPair(Pair(xMultiplier, yMultiplier))} <=> ${ButtonEdge.fromPair(Pair(xMultiplier * -1, yMultiplier))} \\/ ${ButtonEdge.fromPair(Pair(xMultiplier, yMultiplier * -1))}]")
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