package com.deflatedpickle.windowhangers

import com.sun.jna.ptr.IntByReference
import org.joml.Vector2i

object WindowUtil {
    val processMap = mutableMapOf<String, IntByReference>()

    val windowPosition = Vector2i()
    val windowSize = Vector2i()

    val movementSpeed = Vector2i()

    init {
        for (ph in ProcessHandle.allProcesses()) {
            // println("ID: ${ph.pid()} | Command: ${ph.info().command()} | Parent: ${ph.parent()}")

            if (ph.info().command().isPresent) {
                processMap[ph.info().command().get().split("\\").last().split(".").first().toLowerCase()] = IntByReference(ph.pid().toInt())
            }
        }
    }

    fun getWindowFromName(name: String) {

    }

    fun getWindowFromProcess(process: IntByReference) {

    }
}