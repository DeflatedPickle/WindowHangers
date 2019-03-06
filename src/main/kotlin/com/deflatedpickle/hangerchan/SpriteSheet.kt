package com.deflatedpickle.hangerchan

import java.awt.Component
import java.awt.Graphics
import java.awt.Image
import java.io.File
import javax.imageio.ImageIO
import javax.swing.ImageIcon
import javax.swing.plaf.synth.SynthGraphicsUtils.paintIcon
import javax.swing.Spring.scale
import javax.swing.plaf.synth.SynthGraphicsUtils.getIconWidth
import java.awt.Graphics2D



class SpriteSheet(image: String, spriteNumX: Int, spriteNumY: Int) {
    val spriteMap = mutableMapOf<String, MutableList<ImageIcon>>()

    var spriteSizeX = 0
    var spriteSizeY = 0

    init {
        val sheet = ImageIO.read(File(this::class.java.getResource("$image.png").file))!!
        val animations = File(this::class.java.getResource("$image.txt").file).readText()

        spriteSizeX = sheet.width / spriteNumX
        spriteSizeY = sheet.height / spriteNumY

        var gridX = 0
        var gridY = 0

        for (anim in animations.lines()) {
            spriteMap[anim] = mutableListOf()

            for (frame in 1..spriteNumX) {
                spriteMap[anim]!!.add(ScalableImageIcon(sheet.getSubimage(gridX * spriteSizeX, gridY * spriteSizeY, spriteSizeX, spriteSizeY).getScaledInstance(spriteSizeX / 2, spriteSizeY / 2, Image.SCALE_SMOOTH)))
                gridX++
            }

            gridX = 0
            gridY++
        }
    }
}