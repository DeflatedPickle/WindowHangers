package com.deflatedpickle.hangerchan

import org.jbox2d.collision.shapes.PolygonShape
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.Body
import java.awt.Graphics2D

object PhysicsUtil {
    // *claw* RAWR *claw* UWU
    val scaleUp = 20f
    val scaleDown = 1 / scaleUp

    fun drawPhysicsShape(graphics2D: Graphics2D, body: Body) {
        val shape = (body.fixtureList.shape as PolygonShape)
        val vertices = shape.vertices
        for (i in vertices) {
            val next: Vec2 = if (vertices.indexOf(i) < shape.vertexCount - 1) {
                vertices[vertices.indexOf(i) + 1]
            }
            else {
                vertices[0]
            }

            val x = body.transform.p.x
            val y = body.transform.p.y
            graphics2D.drawLine(
                    ((i.x + x) * scaleUp).toInt(),
                    ((i.y - y) * scaleUp).toInt(),
                    ((next.x + x) * scaleUp).toInt(),
                    ((next.y - y) * scaleUp).toInt())

            if (next == vertices[0]) {
                break
            }
        }
    }
}