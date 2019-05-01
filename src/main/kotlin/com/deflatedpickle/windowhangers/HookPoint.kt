package com.deflatedpickle.windowhangers

/**
 * The sides a window can be attached to
 */
@Suppress("KDocMissingDocumentation")
enum class HookPoint(val pair: Pair<Int, Int>) {
    NONE(Pair(0, 0)),
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
        private val map = HookPoint.values().associateBy(HookPoint::pair)
        fun fromPair(pair: Pair<Int, Int>) = map[pair]
        fun fromValues(x: Int, y: Int) = map[Pair(x, y)]
    }
}