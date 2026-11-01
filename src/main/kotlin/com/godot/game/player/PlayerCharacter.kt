package com.godot.game.player

import com.godot.game.components.AnimationComponent
import com.godot.game.components.MovementComponent
import com.godot.game.input.IPlayerInput
import com.godot.game.interfaces.ICharacter
import godot.annotation.Script
import godot.api.CharacterBody3D
import godot.core.Vector3

/**
 * Provides the base logic for controlling the player character.
 */
@Script
class PlayerCharacter : CharacterBody3D(), ICharacter {

    private lateinit var controller: PlayerCharacterController

    override fun _ready() {
        val animationComponent = getNode(AnimationComponent.COMPONENT_NAME) as AnimationComponent
        val movementComponent = getNode(MovementComponent.COMPONENT_NAME) as MovementComponent

        controller = PlayerCharacterController(
            player = this,
            playerInput = IPlayerInput.instance(),
            animationComponent = animationComponent,
            movementComponent = movementComponent
        )
    }

    override fun _physicsProcess(delta: Double) {
        controller.physicsProcess(delta)
    }

    override var characterVelocity: Vector3
        get() = velocity
        set(value) {
            velocity = value
        }
}
