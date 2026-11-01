package com.godot.game.player

import com.godot.game.components.AnimationComponent
import com.godot.game.components.MovementComponent
import com.godot.game.input.InputActions
import godot.annotation.Script
import godot.api.CharacterBody3D
import godot.api.Input
import godot.global.GD

/**
 * Provides the base logic for controlling the player character.
 */
@Script
class PlayerCharacter : CharacterBody3D() {

	private lateinit var animationComponent: AnimationComponent
	private lateinit var movementComponent: MovementComponent

	override fun _ready() {
		animationComponent = getNode(AnimationComponent.COMPONENT_NAME) as AnimationComponent

		movementComponent = getNode(MovementComponent.COMPONENT_NAME) as MovementComponent
	}

	override fun _physicsProcess(delta: Double) {
		val speed = velocity.length()
		movementComponent.processMovement(
			delta = delta,
			isSprinting = Input.isActionPressed(InputActions.SPRINT),
			isAiming = Input.isActionPressed(InputActions.AIM)
		)

		GD.print("Direction: ${movementComponent.movementDirection}")

		animationComponent.updateAnimation(speed.toFloat(), movementComponent.movementDirection)
	}
}
