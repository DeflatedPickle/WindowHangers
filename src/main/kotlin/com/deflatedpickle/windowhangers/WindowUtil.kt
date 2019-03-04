package com.deflatedpickle.windowhangers

import com.sun.jna.ptr.IntByReference

object WindowUtil {
    val processMap = mutableMapOf<String, IntByReference>()

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