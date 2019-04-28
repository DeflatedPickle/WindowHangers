package com.deflatedpickle.windowhangers.stickywindows

import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Canvas
import org.eclipse.swt.widgets.Composite

class StickyWindows(parent: Composite) : Canvas(parent, SWT.NONE) {
    init {
        this.addListener(SWT.FocusIn) {
            val width = 200
            val height = 100
            // TODO: Center all the window buttons on resize
            val window = Window(this, (this.clientArea.width / 2) - (width / 2), (this.clientArea.height / 2) - (height / 2), width, height)
        }
    }
}