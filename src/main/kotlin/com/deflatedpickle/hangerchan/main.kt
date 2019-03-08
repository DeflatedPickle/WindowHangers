package com.deflatedpickle.hangerchan

import com.deflatedpickle.windowhangers.HookPoint
import com.deflatedpickle.windowhangers.WindowHanger
import com.deflatedpickle.windowhangers.WindowUtil
import com.sun.jna.Native
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
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

    var window: WinDef.HWND? = null
    val windowInfo = WinUser.WINDOWINFO()

    var monitor: WinUser.HMONITOR? = null
    val monitorInfo = WinUser.MONITORINFO()

    while (monitorInfo.rcWork.top == windowInfo.rcWindow.top) {
        window = WindowUtil.getAllWindows().shuffled()[0]
        User32.INSTANCE.GetWindowInfo(window, windowInfo)

        println(WindowUtil.getTitle(window))

        User32.INSTANCE.MonitorFromWindow(window, User32.MONITOR_DEFAULTTONEAREST)
        User32.INSTANCE.GetMonitorInfo(monitor, monitorInfo)
    }

    val windowHanger = WindowHanger(
            // TODO: Check if the height of the window is the same as the monitor
            window!!,
            mutableMapOf("hangerchan" to WinDef.HWND(Native.getComponentPointer(frame))),
            mutableMapOf("hangerchan" to HookPoint.Top)
    )
    windowHanger.run()

    // -1 = Left, 1 = Right
    var direction = 1.0
    // A cooldown that happens after the action is changed
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
                        graceCooldown = ThreadLocalRandom.current().nextInt(25, 36)
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
                        graceCooldown = ThreadLocalRandom.current().nextInt(25, 36)
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