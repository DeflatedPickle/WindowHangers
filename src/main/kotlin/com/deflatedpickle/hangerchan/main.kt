package com.deflatedpickle.hangerchan

import java.awt.Color
import java.awt.Dimension
import java.awt.event.ActionListener
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.Timer

fun main(args: Array<String>) {
    val frame = JFrame("Hanger-chan")

    frame.isUndecorated = true
    frame.isAlwaysOnTop = true
    frame.background = Color(0, 0, 0, 0)

    frame.minimumSize = Dimension(220 / 2, 256 / 2)
    frame.setSize(220 / 2, 256 / 2)

    val label = JLabel()
    frame.add(label)

    val sheet = SpriteSheet("/hangerchan/Dance", 8, 10)

    frame.pack()
    frame.isVisible = true

    var animFrame = 0
    val timer = Timer(120, ActionListener {
        animFrame++

        if (animFrame >= 8) {
            animFrame = 0
        }

        label.icon = sheet.spriteMap["Hula"]!![animFrame]
    })
    timer.start()

    // TODO: Add proper calls to hook windows together
    // AttachedWindows.attachedWindowHandleIDs["hangerchan"] = WinDef.HWND(Native.getComponentPointer(frame))
    // AttachedWindows.hookPoints["hangerchan"] = HookPoint.Top
}