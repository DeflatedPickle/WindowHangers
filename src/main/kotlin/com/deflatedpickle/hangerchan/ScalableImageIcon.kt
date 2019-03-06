package com.deflatedpickle.hangerchan

import java.awt.Component
import javax.swing.ImageIcon
import java.awt.Graphics2D
import java.awt.Graphics
import java.awt.Image


class ScalableImageIcon(image: Image) : ImageIcon(image) {
    var xScale = 1.0
    var yScale = 1.0

    @Synchronized
    override fun paintIcon(c: Component, g: Graphics, x: Int, y: Int) {
        val g2 = g.create() as Graphics2D
        g2.translate(if (xScale < 0.0) iconWidth * -xScale else 0.0, if (yScale < 0.0) iconHeight * -yScale else 0.0)
        g2.scale(xScale, yScale)
        super.paintIcon(c, g2, x, y)
    }
}