package com.godot.game.components

/**
 * Defines a testable component that handles a character's movement.
 *
 * @property movementDirection The current direction in degrees that the character is moving towards compared to where
 * they are looking.
 */
interface IMovementComponent {
    val movementDirection: Float

    /**
     * Processes the entity's movement.
     *
     * @param delta The logical time between physics ticks in seconds.
     * @param isSprinting Whether the entity is sprinting or not.
     * @param isAiming Whether the entity is aiming or not.
     */
    fun processMovement(delta: Double, isSprinting: Boolean, isAiming: Boolean)
}