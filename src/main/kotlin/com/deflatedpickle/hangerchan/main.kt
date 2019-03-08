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

    // frame.minimumSize = Dimension(220 / 2, 256 / 2)
    // frame.setSize(220 / 2, 256 / 2)

    frame.extendedState = JFrame.MAXIMIZED_BOTH

    frame.contentPane.add(Hangerchan())

    frame.pack()
    frame.isVisible = true
}