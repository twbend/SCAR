package com.godot.game.interfaces

import godot.api.CharacterBody3D
import godot.core.Vector3

/**
 * Acts as a wrapper around the [CharacterBody3D] class within the Godot API.
 *
 * This exists because [CharacterBody3D] is not easily testable, therefore it is easier to wrap
 * this interface around implementations of [CharacterBody3D], and use it for testing instead.
 *
 * @property characterVelocity [CharacterBody3D.velocity].
 */
interface ICharacter {
    var characterVelocity: Vector3
}