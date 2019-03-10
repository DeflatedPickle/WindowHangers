package com.deflatedpickle.hangerchan

// import org.dyn4j.dynamics.World
// import org.dyn4j.geometry.Vector2
import com.sun.jna.Native
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
import org.jbox2d.collision.shapes.PolygonShape
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.BodyDef
import org.jbox2d.dynamics.World
import java.awt.Color
import java.awt.event.ActionListener
import javax.swing.JFrame
import javax.swing.Timer


@Suppress("KDocMissingDocumentation")
fun main(args: Array<String>) {
    val frame = JFrame("Hanger-chan")
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

    frame.isUndecorated = true
    frame.isAlwaysOnTop = true
    frame.background = Color(0, 0, 0, 0)

    frame.extendedState = JFrame.MAXIMIZED_BOTH

    var monitor: WinUser.HMONITOR? = null
    val monitorInfo = WinUser.MONITORINFO()

    val world = World(Vec2(0f, -80f))

    // 1.524
    val hangerchan = Hangerchan(frame, world)
    frame.contentPane.add(hangerchan)

    val timer = Timer(120 / 2, ActionListener {
        world.step(1f / 60f, 6, 2)
        hangerchan.animate()
        hangerchan.repaint()
    })
    timer.start()

    frame.pack()
    frame.isVisible = true

    if (monitor == null) {
        monitor = User32.INSTANCE.MonitorFromWindow(WinDef.HWND(Native.getComponentPointer(frame)), User32.MONITOR_DEFAULTTONEAREST)
        User32.INSTANCE.GetMonitorInfo(monitor, monitorInfo)
    }

    val monitorWidth = monitorInfo.rcWork.right.toFloat() * PhysicsUtil.scaleDown
    val monitorHeight = monitorInfo.rcWork.bottom.toFloat() * PhysicsUtil.scaleDown

    val floor = world.createBody(BodyDef().apply {
        position.set(monitorWidth / 2, -monitorHeight - 1)
    }).apply {
        createFixture(PolygonShape().apply {
            setAsBox(monitorWidth / 2, 1f)
        }, 0f)
    }
    hangerchan.floor = floor
}