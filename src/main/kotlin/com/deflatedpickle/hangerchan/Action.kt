package com.deflatedpickle.hangerchan

/**
 * Actions that Hanger-chan can carry out
 */
@Suppress("KDocMissingDocumentation")
enum class Action {
    Idle,
    Walking,

    Grabbed,
    Pulled,
    Thrown,

    Falling,
}