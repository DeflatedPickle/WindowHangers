package com.deflatedpickle.windowhangers

class WindowHangerThread : Runnable {
    val run = true

    override fun run() {
        while (run) {
            for (i in windowHangerList) {
                i.moveChildren()
            }
        }
    }

    companion object {
        val windowHangerList = mutableListOf<WindowHanger>()
    }
}