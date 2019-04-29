package com.deflatedpickle.windowhangers.stickywindows

enum class ButtonEdge(val x: Int, val y: Int) {
    TOP_LEFT(-1, -1),
    TOP_CENTRE(0, -1),
    TOP_RIGHT(1, -1),
    MIDDLE_LEFT(-1, 0),
    MIDDLE_CENTRE(0, 0),
    MIDDLE_RIGHT(1, 0),
    BOTTOM_LEFT(-1, 1),
    BOTTOM_CENTRE(0, 1),
    BOTTOM_RIGHT(1, 1)
}