package com.deflatedpickle.windowhangers.gui.stickywindows

import com.deflatedpickle.windowhangers.HookPoint
import com.deflatedpickle.windowhangers.Icons
import com.deflatedpickle.windowhangers.WindowHanger
import com.deflatedpickle.windowhangers.gui.SelectionWindow
import com.sun.jna.platform.win32.WinDef
import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Button
import org.eclipse.swt.widgets.Composite

class WindowButton(val parent: WindowButton? = null, val composite: Composite, val x: Int, val y: Int) {
    val button = Button(composite, SWT.PUSH)
    var window: WinDef.HWND? = null

    val edgeWindows = mutableMapOf<HookPoint, WindowButton>()
    val edgeWindowsReversed = mutableMapOf<WindowButton, HookPoint>()
    val edgeKeys = listOf(
            listOf(HookPoint.NONE, HookPoint.TOP_CENTRE, HookPoint.NONE),
            listOf(HookPoint.MIDDLE_LEFT, HookPoint.NONE, HookPoint.MIDDLE_RIGHT),
            listOf(HookPoint.NONE, HookPoint.BOTTOM_CENTRE, HookPoint.NONE))

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
                if (yMultiplier != 0 || xMultiplier != 0) {
                    if (edgeKeys[yMultiplier + 1][xMultiplier + 1] != HookPoint.NONE) {
                        // TODO: Check if the parent already has a button in this location
                        val thisX = x + (((parent?.currentWidth ?: currentWidth) + xPadding) * xMultiplier)
                        val thisY = y + (((parent?.currentHeight ?: currentHeight) + yPadding) * yMultiplier)
                        
                        val button = WindowButton(this, composite, thisX, thisY).apply {
                            button.image = Icons.addIcon
                            // button.text = "$xMultiplier, $yMultiplier"
                        }
                        edgeWindows[edgeKeys[yMultiplier + 1][xMultiplier + 1]] = button
                        edgeWindowsReversed[button] = edgeKeys[yMultiplier + 1][xMultiplier + 1]
                    }
                }
            }
        }
        centre()
    }
}