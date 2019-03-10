package com.deflatedpickle.hangerchan

import com.sun.jna.Native
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
import org.jbox2d.collision.shapes.PolygonShape
import org.jbox2d.dynamics.Body
import org.jbox2d.dynamics.BodyDef
import org.jbox2d.dynamics.BodyType
import org.jbox2d.dynamics.World
import java.awt.*
import java.util.concurrent.ThreadLocalRandom
import javax.swing.JFrame
import javax.swing.JPanel

class Hangerchan(val myFrame: JFrame, val world: World) : JPanel() {
    val sheet = SpriteSheet("/hangerchan/Hangerchan", 8, 10)
    var currentAction = Action.Idle

    val body = world.createBody(BodyDef().apply {
        type = BodyType.DYNAMIC
        position.set(20f, 4f)
    }).apply {
        createFixture(PolygonShape().apply {
            setAsBox((sheet.spriteWidth.toFloat() / 4) * PhysicsUtil.scaleDown, (sheet.spriteHeight.toFloat() / 4) * PhysicsUtil.scaleDown)
        }, 1f).apply {
            m_mass = 80f
        }
    }
    // val windowBody = Body()

    var floor: Body? = null

    // -1 = Left, 1 = Right
    var direction = 1.0

    // A cooldown that happens after the action is changed
    var graceCooldown = 30

    var currentFrame = 0

    var monitor: WinUser.HMONITOR? = null
    val monitorInfo = WinUser.MONITORINFO()

    // val windowShapes = mutableListOf<BodyFixture>()

    init {
        isOpaque = false

        // val shape = Geometry.createRectangle(sheet.spriteWidth / 2.0, sheet.spriteHeight / 2.0)
        // body.addFixture(shape)
        // world.addBody(body)

        // windowBody.gravityScale = 0.0
        // world.addBody(windowBody)
    }

    fun animate() {
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
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val g2D = g as Graphics2D
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        if (monitor == null) {
            monitor = User32.INSTANCE.MonitorFromWindow(WinDef.HWND(Native.getComponentPointer(myFrame)), User32.MONITOR_DEFAULTTONEAREST)
            User32.INSTANCE.GetMonitorInfo(monitor, monitorInfo)
        }

        println("Position: ${body.position}, Velocity: ${body.linearVelocity}")
        g.drawImage(sheet.spriteMap[currentAction.toString()]!![currentFrame], (body.position.x * PhysicsUtil.scaleUp - sheet.spriteWidth / 4).toInt(), (-body.position.y * PhysicsUtil.scaleUp - sheet.spriteHeight / 4).toInt(), sheet.spriteWidth / 2, sheet.spriteHeight / 2, this)

        // Debug prints
        // TODO: Window intersections, stop cluttering up the draw space
        g2D.color = Color.RED
        PhysicsUtil.drawPhysics(g2D, body)
        // g2D.stroke = BasicStroke(4f)
        // for (i in WindowUtil.getAllWindowRects()) {
        //     g2D.drawRect(i.left, i.top, i.right - i.left, i.bottom - i.top)
        // }

        // var body = world.bodyList
        // for (i in 0..world.bodyCount) {
        //     body = body.next
        // }

        g2D.color = Color.GREEN
        // g2D.drawRect(monitorInfo.rcWork.left, monitorInfo.rcWork.top, monitorInfo.rcWork.right - monitorInfo.rcWork.left, monitorInfo.rcWork.bottom - monitorInfo.rcWork.top)
        if (floor != null) {
            PhysicsUtil.drawPhysics(g2D, floor!!)
        }
    }
}