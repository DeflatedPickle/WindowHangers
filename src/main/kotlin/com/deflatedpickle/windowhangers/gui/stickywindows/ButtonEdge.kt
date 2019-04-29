package com.deflatedpickle.windowhangers.gui.stickywindows

enum class ButtonEdge(val pair: Pair<Int, Int>) {
    TOP_LEFT(Pair(-1, -1)),
    TOP_CENTRE(Pair(0, -1)),
    TOP_RIGHT(Pair(1, -1)),
    MIDDLE_LEFT(Pair(-1, 0)),
    MIDDLE_CENTRE(Pair(0, 0)),
    MIDDLE_RIGHT(Pair(1, 0)),
    BOTTOM_LEFT(Pair(-1, 1)),
    BOTTOM_CENTRE(Pair(0, 1)),
    BOTTOM_RIGHT(Pair(1, 1));

    companion object {
        private val map = ButtonEdge.values().associateBy(ButtonEdge::pair)
        fun fromPair(pair: Pair<Int, Int>) = map[pair]
    }
}