package com.deflatedpickle.windowhangers

class WindowHangerThread : Runnable {
    companion object {
        @Volatile
        var windowHangerList = listOf<WindowHanger>()
    }

    var run = true

    override fun run() {
        while (run) {
            // windowHangerList.toString()
            // println(windowHangerList)
            for (i in windowHangerList) {
                // println(i)
                i.moveChildren()
            }
        }
    }
}