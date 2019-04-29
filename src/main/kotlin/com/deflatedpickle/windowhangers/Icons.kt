package com.deflatedpickle.windowhangers

import org.eclipse.swt.graphics.Image
import org.eclipse.swt.widgets.Display

object Icons {
    var addIcon: Image? = null

    fun createIcons() {
        addIcon = Image(Display.getCurrent(), ClassLoader.getSystemResource("Bincons/icons/add_element.png").openStream())
    }
}