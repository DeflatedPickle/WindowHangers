package com.deflatedpickle.windowhangers.gui.stickywindows

import com.deflatedpickle.windowhangers.Icons
import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Canvas
import org.eclipse.swt.widgets.Composite

class StickyWindows(parent: Composite) : Canvas(parent, SWT.NONE) {
    var rootButton: WindowButton? = null

    init {
        this.addListener(SWT.Activate) {
            rootButton = WindowButton(null, this, 0, 0).apply { button.image = Icons.addIcon }
            rootButton!!.centre()
        }

        this.addListener(SWT.Resize) {
            rootButton?.centre()
        }
    }
}