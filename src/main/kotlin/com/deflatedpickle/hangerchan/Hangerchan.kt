package com.deflatedpickle.hangerchan

import org.jbox2d.collision.shapes.PolygonShape
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.Body
import org.jbox2d.dynamics.BodyDef
import org.jbox2d.dynamics.BodyType
import org.jbox2d.dynamics.World
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.concurrent.ThreadLocalRandom
import javax.swing.JFrame
import javax.swing.JPanel

class Hangerchan(val myFrame: JFrame, val world: World) : JPanel() {
    val sheet = SpriteSheet("/hangerchan/Hangerchan", 8, 10)
    var currentAction = Action.Idle

    val body = world.createBody(BodyDef().apply {
        type = BodyType.DYNAMIC
        position.set(20f, -10f)
        fixedRotation = true
    }).apply {
        createFixture(PolygonShape().apply {
            setAsBox((sheet.spriteWidth.toFloat() / 4) * PhysicsUtil.scaleDown, (sheet.spriteHeight.toFloat() / 4) * PhysicsUtil.scaleDown)
        }, 1f).apply {
            m_mass = 1f
            friction = 0.3f
            density = 1f
        }
    }

    var borders: MutableList<Body> = mutableListOf()
    var windows: MutableList<Body> = mutableListOf()

    // -1 = Left, 1 = Right
    var direction = -1
    // A cooldown that happens after the action is changed
    var graceCooldown = 30
    var currentFrame = 0

    var collisionSide: Vec2? = null

    // Mouse events
    var isGrabbed = false
    var isBeingPulled = false

    var justFell = false

    // Changes when the mouse is clicked -- used to determine thrown force
    var clickedX = 0f
    var clickedY = 0f
    var releasedX = 0f
    var releasedY = 0f

    // Changes when the mouse is moved
    var mouseX = 0f
    var mouseY = 0f

    init {
        isOpaque = false

        myFrame.addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                // If inside Hanger-chan
                if (mouseX > body.position.x - sheet.spriteWidth / 2 && mouseX < body.position.x + sheet.spriteWidth / 2
                        && mouseY > body.position.y - sheet.spriteHeight / 2 && mouseY < body.position.y + sheet.spriteHeight / 2) {
                    isGrabbed = true

                    clickedX = e.xOnScreen * PhysicsUtil.scaleDown
                    clickedY = e.yOnScreen * PhysicsUtil.scaleDown
                }
            }

            override fun mouseReleased(e: MouseEvent) {
                if (isGrabbed) {
                    releasedX = e.xOnScreen * PhysicsUtil.scaleDown
                    releasedY = e.yOnScreen * PhysicsUtil.scaleDown
                }

                isGrabbed = false
                isBeingPulled = false
            }

            override fun mouseDragged(e: MouseEvent) {
                mouseX = e.xOnScreen * PhysicsUtil.scaleDown
                mouseY = e.yOnScreen * PhysicsUtil.scaleDown

                if (isGrabbed) {

                    isBeingPulled = true
                }
            }
        }.apply { myFrame.addMouseMotionListener(this) })
    }

    fun animate() {
        currentFrame++

        if (currentFrame >= 8) {
            currentFrame = 0
        }

        when (collisionSide?.x) {
            // Left
            -1f -> {
                direction = 1
                collisionSide = null
            }
            // Right
            1f -> {
                direction = -1
                collisionSide = null
            }
        }

        if (body.linearVelocity.y < -1 && clickedY == 0f || currentAction == Action.Thrown) {
            currentAction = Action.Falling
            justFell = true
        }
        else if (clickedY != 0f) {
            currentAction = Action.Thrown
        }
        else {
            if (justFell) {
                currentAction = Action.Idle
                justFell = false
            }
        }

        if (isGrabbed) {
            currentAction = Action.Grabbed
        }

        // println(currentAction)

        when (currentAction) {
            Action.Idle -> {
                // If this is set lower, her velocity stays at 0 forever. Bug?
                body.linearVelocity.x = 0.4f

                if (graceCooldown == 0) {
                    val random = ThreadLocalRandom.current().nextInt(0, 11)

                    if (random == 0) {
                        currentAction = Action.Walking
                        graceCooldown = ThreadLocalRandom.current().nextInt(25, 36)
                    }
                    else if (random == 1) {
                        direction *= -1
                    }
                }
                else {
                    graceCooldown--
                }
            }
            Action.Walking -> {
                body.linearVelocity.x = 12f * direction

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
            Action.Grabbed -> {
                if (isBeingPulled) {
                    body.linearVelocity.x = 0f
                    body.setTransform(Vec2(mouseX, -mouseY), 0f)
                }
            }
            // TODO: Add some force when she's thrown
            Action.Thrown -> {
                // val force = Vec2(0f, clickedY + releasedY)
                // println(force)
                // body.linearVelocity = force
                // currentAction = Action.Falling

                clickedX = 0f
                clickedY = 0f
                releasedX = 0f
                releasedY = 0f
            }
        }
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val g2D = g as Graphics2D
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        // println("Position: ${body.position}, Velocity: ${body.linearVelocity}")
        g.drawImage(sheet.spriteMap[currentAction.toString()]!![currentFrame], (body.position.x * PhysicsUtil.scaleUp - sheet.spriteWidth / 4 + if (direction == -1) sheet.spriteWidth / 2 else 0).toInt(), (-body.position.y * PhysicsUtil.scaleUp - sheet.spriteHeight / 4).toInt(), (sheet.spriteWidth / 2) * direction, sheet.spriteHeight / 2, this)

        // Debug prints
        g2D.stroke = BasicStroke(2f)

        g2D.color = Color.RED
        PhysicsUtil.drawPhysicsShape(g2D, body)

        g2D.color = Color.GREEN
        if (!borders.isEmpty()) {
            for (b in borders) {
                PhysicsUtil.drawPhysicsShape(g2D, b)
            }
        }

        g2D.color = Color.MAGENTA
        if (!windows.isEmpty()) {
            for (w in windows) {
                PhysicsUtil.drawPhysicsShape(g2D, w)
            }
        }
    }
}