package com.deflatedpickle.hangerchan

import com.deflatedpickle.windowhangers.HookPoint
import com.deflatedpickle.windowhangers.WindowHanger
import com.deflatedpickle.windowhangers.WindowUtil
import com.sun.jna.Native
import com.sun.jna.platform.win32.WinDef
import java.awt.Color
import java.awt.Dimension
import java.awt.Point
import java.awt.event.ActionListener
import java.util.concurrent.ThreadLocalRandom
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.Timer


@Suppress("KDocMissingDocumentation")
fun main(args: Array<String>) {
    val frame = JFrame("Hanger-chan")

    frame.isUndecorated = true
    frame.isAlwaysOnTop = true
    frame.background = Color(0, 0, 0, 0)

    frame.minimumSize = Dimension(220 / 2, 256 / 2)
    frame.setSize(220 / 2, 256 / 2)

    val label = JLabel()
    frame.add(label)

    val sheet = SpriteSheet("/hangerchan/Hangerchan", 8, 10)
    var currentAction = Action.Idle

    frame.pack()
    frame.isVisible = true

    val windowHanger = WindowHanger(
            // TODO: Change this window to be not hard-coded
            WindowUtil.getWindowFromProcess(WindowUtil.processMap["notepad++"]!!)!!,
            mutableMapOf("hangerchan" to WinDef.HWND(Native.getComponentPointer(frame))),
            mutableMapOf("hangerchan" to HookPoint.Top)
    )
    windowHanger.run()

    // -1 = Left, 1 = Right
    var direction = 1.0
    var graceCooldown = 30
    var animFrame = 0

    val timer = Timer(120, ActionListener {
        animFrame++

        if (animFrame >= 8) {
            animFrame = 0
        }

        if (label.x > frame.width - 86) {
            direction = -1.0
        }
        else if (label.x < 0) {
            direction = 1.0
        }

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
                        graceCooldown = 30
                    }
                }
                else {
                    graceCooldown--
                }
            }
            Action.Walking -> {
                label.location = Point(label.x + (3 * direction.toInt()), label.y)

                if (graceCooldown == 0) {
                    val random = ThreadLocalRandom.current().nextInt(0, 11)

                    if (random == 0) {
                        currentAction = Action.Idle
                        graceCooldown = 30
                    }
                }
                else {
                    graceCooldown--
                }
            }
        }

        label.icon = sheet.spriteMap[currentAction.toString()]!![animFrame]
        if (label.icon != null) {
            (label.icon as ScalableImageIcon).xScale = direction
        }
    })
    timer.start()
}