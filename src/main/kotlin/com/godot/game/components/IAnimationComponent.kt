package com.godot.game.components

/**
 * Defines a testable component to handle a character's animations.
 */
interface IAnimationComponent {
    /**
     * Updates the animation state.
     *
     * @param speed The player's current speed.
     * @param direction The direction that the player is moving.
     */
    fun updateAnimation(speed: Float, direction: Float)
}