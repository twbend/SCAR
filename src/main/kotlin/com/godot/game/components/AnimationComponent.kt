package com.godot.game.components

import godot.annotation.Export
import godot.api.Node
import godot.annotation.Script
import godot.api.AnimationTree
import godot.core.NodePath
import godot.core.Vector2

/**
 * Controls animations for a given character.
 *
 * @property animationTreePath The [NodePath] that points to the [AnimationTree].
 */
@Script
class AnimationComponent : Node(), IAnimationComponent {

	companion object {
		private const val LOCOMOTION_BLEND_POSITION_PATH = "parameters/Locomotion/blend_position"
		const val COMPONENT_NAME = "AnimationComponent"
	}

	@Export
	var animationTreePath: NodePath = NodePath("./AnimationTree")

	private lateinit var animTree: AnimationTree

	override fun _ready() {
		animTree = getNode(animationTreePath) as AnimationTree
		animTree.setActive(true)
	}

	/**
	 * Updates the animation state.
	 *
	 * @param speed The player's current speed.
	 * @param direction The direction that the player is moving.
	 */
    override fun updateAnimation(speed: Float, direction: Float) {
		val weightedSpeed = speed / 10.0
		val weightedDirection = direction / 10.0
		animTree.set(LOCOMOTION_BLEND_POSITION_PATH, Vector2(weightedDirection, weightedSpeed))
	}
}
