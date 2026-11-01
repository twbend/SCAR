package com.godot.game.components

import com.godot.game.input.InputActions
import godot.annotation.*
import godot.api.CharacterBody3D
import godot.api.Input
import godot.api.Node
import godot.api.Node3D
import godot.core.NodePath
import godot.core.Vector3
import godot.global.GD.lerpAngle
import godot.global.GD.moveToward
import kotlin.math.atan2

/**
 * Handles the movement for a character/entity.
 *
 * @property aimSpeed The speed the entity moves at when aiming.
 * @property walkSpeed The speed the entity moves at when walking.
 * @property runSpeed The speed the entity moves at when running.
 * @property acceleration The acceleration of the entity.
 * @property deceleration The deceleration of the entity's movement.
 * @property gravity The amount of gravity applied to the entity.
 * @property rotationSpeed The speed at which the entity rotates.
 * @property cameraPivotPath The [NodePath] pointing the [Node3D] for the camera's pivot.
 * @property visualRootPath The [NodePath] pointing to the visual root of the entity.
 */
@Script
class MovementComponent : Node(), IMovementComponent {

    companion object {
        const val COMPONENT_NAME = "MovementComponent"
    }

    @Export
    var aimSpeed: Float = 0.5f

    @Export
    var walkSpeed: Float = 3.5f

    @Export
    var runSpeed: Float = 5.5f


    @Export
    var acceleration: Float = 18.0f

    @Export
    var deceleration: Float = 22.0f

    @Export
    var gravity: Float = 24.0f

    @Export
    var rotationSpeed: Float = 12.0f

    @Export
    var cameraPivotPath: NodePath = NodePath("../CameraPivot")

    @Export
    var visualRootPath: NodePath = NodePath("../VisualRoot")

    private lateinit var character: CharacterBody3D
    private lateinit var cameraPivot: Node3D
    private lateinit var visualRoot: Node3D


    override var movementDirection: Float = 0f
        private set

    override fun _ready() {
        character = getParent() as CharacterBody3D

        cameraPivot = getNode(cameraPivotPath) as Node3D

        visualRoot = getNode(visualRootPath) as Node3D
    }

    /**
     * Processes the entity's movement.
     *
     * @param delta The logical time between physics ticks in seconds.
     * @param isSprinting Whether the entity is sprinting or not.
     * @param isAiming Whether the entity is aiming or not.
     */
    override fun processMovement(delta: Double, isSprinting: Boolean, isAiming: Boolean) {
        val dt = delta.toFloat()

        handleMovement(dt, isSprinting, isAiming)
        handleGravity(dt)

        character.moveAndSlide()
    }

    private fun handleMovement(
        delta: Float,
        isSprinting: Boolean,
        isAiming: Boolean
    ) {
        val input = Input.getVector(
            InputActions.MOVE_LEFT,
            InputActions.MOVE_RIGHT,
            InputActions.MOVE_FORWARD,
            InputActions.MOVE_BACKWARDS
        )

        // Get camera directions.
        val cameraForward =
            -cameraPivot.globalBasis.z

        val cameraRight =
            cameraPivot.globalBasis.x

        // Remove vertical camera influence.
        val forward = Vector3(
            cameraForward.x,
            0.0f,
            cameraForward.z
        ).normalized()

        val right = Vector3(
            cameraRight.x,
            0.0f,
            cameraRight.z
        ).normalized()

        // Calculate camera-relative movement.
        var direction = (right * input.x + forward * -input.y)

        if (direction.lengthSquared() > 1.0f) {
            direction = direction.normalized()
        }

        // Determine movement speed.
        val targetSpeed = when {
            isSprinting ->
                runSpeed

            isAiming ->
                aimSpeed

            input.lengthSquared() > 0.0f ->
                walkSpeed

            else -> 0.0f
        }

        val targetVelocity = direction * targetSpeed

        // Smooth acceleration/deceleration.
        val currentVelocity = character.velocity

        val rate = if (direction.lengthSquared() > 0.0f) {
            acceleration
        } else {
            deceleration
        }

        val newX = moveToward(
            currentVelocity.x,
            targetVelocity.x,
            (rate * delta).toDouble()
        )

        val newZ = moveToward(
            currentVelocity.z,
            targetVelocity.z,
            (rate * delta).toDouble()
        )

        character.velocity = Vector3(
            newX,
            currentVelocity.y,
            newZ
        )

        // Rotate character toward movement direction.
        if (direction.lengthSquared() > 0.01f) {

            val targetRotation = getTargetRotation(direction)

            visualRoot.rotation = Vector3(
                visualRoot.rotation.x,
                lerpAngle(
                    visualRoot.rotation.y,
                    targetRotation,
                    (rotationSpeed * delta).toDouble()
                ),
                visualRoot.rotation.z
            )
        }
    }

    private fun getTargetRotation(direction: Vector3): Double {
        // TODO: SCAR-8 Make character face different direction dependent on if we are walking, running, or aiming.
        return atan2(
            -direction.x,
            -direction.z
        )
    }

    private fun handleGravity(delta: Float) {
        if (!character.isOnFloor()) {

            character.velocity = Vector3(
                character.velocity.x,
                character.velocity.y - gravity * delta,
                character.velocity.z
            )
        }
    }
}
