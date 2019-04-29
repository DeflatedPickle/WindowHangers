package com.deflatedpickle.windowhangers.stickywindows

import com.deflatedpickle.windowhangers.Icons
import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Canvas
import org.eclipse.swt.widgets.Composite

class StickyWindows(parent: Composite) : Canvas(parent, SWT.NONE) {
    val width = 30
    val height = 30

    var rootButton: WindowButton? = null

    init {
        this.addListener(SWT.Activate) {
            rootButton = WindowButton(this, 0, 0, width, height).apply { button.image = Icons.addIcon }
            rootButton!!.centre()
        }

        this.addListener(SWT.Resize) {
            for (i in WindowRegistry.windowButtons) {
                i.centre()
            }
        }
    }
}