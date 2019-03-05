package com.deflatedpickle.hangerchan

import java.awt.Image
import java.io.File
import javax.imageio.ImageIO
import javax.swing.ImageIcon

class SpriteSheet(val image: String, val spriteNumX: Int, val spriteNumY: Int) {
    val spriteMap = mutableMapOf<String, MutableList<ImageIcon>>()

    init {
        val sheet = ImageIO.read(File(this::class.java.getResource("$image.png").file))!!
        val animations = File(this::class.java.getResource("$image.txt").file).readText()

        val spriteSizeX = sheet.width / spriteNumX
        val spriteSizeY = sheet.height / spriteNumY

        var gridX = 0
        var gridY = 0

        for (anim in animations.lines()) {
            spriteMap[anim] = mutableListOf()

            for (frame in 1..spriteNumX) {
                spriteMap[anim]!!.add(ImageIcon(sheet.getSubimage(gridX * spriteSizeX, gridY * spriteSizeY, spriteSizeX, spriteSizeY).getScaledInstance(spriteSizeX / 2, spriteSizeY / 2, Image.SCALE_SMOOTH)))
                gridX++
            }

            gridX = 0
            gridY++
        }
    }
}