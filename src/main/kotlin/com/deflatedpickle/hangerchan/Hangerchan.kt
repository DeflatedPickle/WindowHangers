package com.deflatedpickle.hangerchan

import com.deflatedpickle.windowhangers.WindowUtil
import java.awt.*
import java.awt.event.ActionListener
import java.util.concurrent.ThreadLocalRandom
import javax.swing.JPanel
import javax.swing.Timer

class Hangerchan : JPanel() {
    val sheet = SpriteSheet("/hangerchan/Hangerchan", 8, 10)
    var currentAction = Action.Idle

    // -1 = Left, 1 = Right
    var direction = 1.0

    // A cooldown that happens after the action is changed
    var graceCooldown = 30

    var currentFrame = 0

    init {
        isOpaque = false

        val timer = Timer(120, ActionListener {
            currentFrame++

            if (currentFrame >= 8) {
                currentFrame = 0
            }

            // if (label.x > frame.width - 86) {
            //     direction = -1.0
            // }
            // else if (label.x < 0) {
            //     direction = 1.0
            // }

            when (currentAction) {
                Action.Idle -> {
                    val directionRandom = ThreadLocalRandom.current().nextInt(0, 7)

                    if (directionRandom == 0) {
                        direction *= -1
                    }

                    if (graceCooldown == 0) {
                        val random = ThreadLocalRandom.current().nextInt(0, 11)

                        if (random == 0) {
                            currentAction = Action.Walking
                            graceCooldown = ThreadLocalRandom.current().nextInt(25, 36)
                        }
                    }
                    else {
                        graceCooldown--
                    }
                }
                Action.Walking -> {
                    // label.location = Point(label.x + (3 * direction.toInt()), label.y)

                    if (graceCooldown == 0) {
                        val random = ThreadLocalRandom.current().nextInt(0, 11)

                        if (random == 0) {
                            currentAction = Action.Idle
                            graceCooldown = ThreadLocalRandom.current().nextInt(25, 36)
                        }
                    }
                    else {
                        graceCooldown--
                    }
                }
            }

            this.repaint()
        })
        timer.start()
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val g2D = g as Graphics2D
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        g.drawImage(sheet.spriteMap[currentAction.toString()]!![currentFrame], 0, 0, sheet.spriteWidth / 2, sheet.spriteHeight / 2, this)

        // Debug prints
        g2D.color = Color.RED
        g2D.stroke = BasicStroke(4f)
        for (i in WindowUtil.getAllWindowRects()) {
            g2D.drawRect(i.left, i.top, i.right - i.left, i.bottom - i.top)
        }
    }
}