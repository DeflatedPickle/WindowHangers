package com.deflatedpickle.hangerchan

import java.awt.Component
import javax.swing.ImageIcon
import java.awt.Graphics2D
import java.awt.Graphics
import java.awt.Image


/**
 * An <code>ImageIcon</code> that can be scaled
 */
class ScalableImageIcon(image: Image) : ImageIcon(image) {
    /**
     * How much to scale the image by on the X axis
     */
    var xScale: Double = 1.0
    /**
     * How much to scale the image by on the Y axis
     */
    var yScale: Double = 1.0

    /**
     * {@inheritDoc}
     */
    @Synchronized
    override fun paintIcon(c: Component, g: Graphics, x: Int, y: Int) {
        val g2 = g.create() as Graphics2D
        g2.translate(if (xScale < 0.0) iconWidth * -xScale else 0.0, if (yScale < 0.0) iconHeight * -yScale else 0.0)
        g2.scale(xScale, yScale)
        super.paintIcon(c, g2, x, y)
    }
}