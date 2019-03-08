package com.deflatedpickle.hangerchan

import java.awt.Image
import java.io.File
import javax.imageio.ImageIO
import javax.swing.ImageIcon


/**
 * A sprite sheet
 */
class SpriteSheet(image: String, spriteNumX: Int, spriteNumY: Int) {
    /**
     * The map of sprites cut from the sheet
     */
    val spriteMap: MutableMap<String, MutableList<Image>> = mutableMapOf()

    /**
     * The width of the sprites
     */
    @Suppress("MemberVisibilityCanBePrivate")
    var spriteWidth: Int = 0
    /**
     * The height of the sprites
     */
    @Suppress("MemberVisibilityCanBePrivate")
    var spriteHeight: Int = 0

    init {
        val sheet = ImageIO.read(File(this::class.java.getResource("$image.png").file))!!
        val animations = File(this::class.java.getResource("$image.txt").file).readText()

        spriteWidth = sheet.width / spriteNumX
        spriteHeight = sheet.height / spriteNumY

        var gridX = 0
        var gridY = 0

        for (anim in animations.lines()) {
            spriteMap[anim] = mutableListOf()

            for (frame in 1..spriteNumX) {
                spriteMap[anim]!!.add(sheet.getSubimage(gridX * spriteWidth, gridY * spriteHeight, spriteWidth, spriteHeight))
                gridX++
            }

            gridX = 0
            gridY++
        }
    }
}