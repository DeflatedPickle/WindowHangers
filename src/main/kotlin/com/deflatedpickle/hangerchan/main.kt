package com.deflatedpickle.hangerchan

import org.dyn4j.dynamics.World
import org.dyn4j.geometry.Vector2
import java.awt.Color
import javax.swing.JFrame


@Suppress("KDocMissingDocumentation")
fun main(args: Array<String>) {
    val frame = JFrame("Hanger-chan")
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

    frame.isUndecorated = true
    frame.isAlwaysOnTop = true
    frame.background = Color(0, 0, 0, 0)

    frame.extendedState = JFrame.MAXIMIZED_BOTH

    val world = World()
    world.gravity = Vector2(0.0, -300.0)

    val hangerchan = Hangerchan(frame, world)
    frame.contentPane.add(hangerchan)

    frame.pack()
    frame.isVisible = true
}