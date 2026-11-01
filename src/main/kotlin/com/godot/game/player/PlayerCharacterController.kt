package com.godot.game.player

import com.godot.game.components.IAnimationComponent
import com.godot.game.components.IMovementComponent
import com.godot.game.input.InputActions
import com.godot.game.input.IPlayerInput
import com.godot.game.interfaces.ICharacter

/**
 * Controls the [PlayerCharacter].
 *
 * @param player The [PlayerCharacter] to control.
 * @param playerInput Provides the current values for player input.
 * @param movementComponent The [player]'s [IMovementComponent].
 * @param animationComponent The [player]'s [IAnimationComponent].
 */
class PlayerCharacterController(
    private val player: ICharacter,
    private val playerInput: IPlayerInput,
    private val movementComponent: IMovementComponent,
    private val animationComponent: IAnimationComponent
) {
    /**
     * Implements the logic for [PlayerCharacter._physicsProcess].
     *
     * @param delta The logical time between physics ticks in seconds
     */
    fun physicsProcess(delta: Double) {
        with(player) {
            val speed = characterVelocity.length()
            movementComponent.processMovement(
                delta = delta,
                isSprinting = playerInput.isActionPressed(InputActions.SPRINT),
                isAiming = playerInput.isActionPressed(InputActions.AIM)
            )

            animationComponent.updateAnimation(speed.toFloat(), movementComponent.movementDirection)
        }
    }
}