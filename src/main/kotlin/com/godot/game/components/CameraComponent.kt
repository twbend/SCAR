package com.godot.game.components

import com.godot.game.input.InputActions
import godot.annotation.Export
import godot.annotation.Script
import godot.api.Input
import godot.api.InputEvent
import godot.api.InputEventKey
import godot.api.InputEventMouseMotion
import godot.api.Node3D
import godot.api.SpringArm3D
import godot.core.Key
import godot.core.NodePath
import godot.core.Vector3
import godot.global.GD

/**
 * Controls the camera for a player character.
 *
 * @property mouseSensitivity The sensitivity of mouse movement when looking around.
 * @property controllerSensitivity The sensitivity for the controller joystick when looking around.
 * @property minPitch The minimum pitch of the camera.
 * @property maxPitch The maximum pitch of the camera.
 * @property shoulderOffset The offset location from the camera to the player's shoulder.
 * @property springArmPath The [NodePath] that points to the [SpringArm3D].
 */
@Script
class CameraComponent : Node3D() {

    @Export
    var mouseSensitivity: Float = 0.0025f

    @Export
    var controllerSensitivity: Float = 2.5f

    @Export
    var minPitch: Float = -60.0f

    @Export
    var maxPitch: Float = 45.0f

    @Export
    var shoulderOffset: Float = 0.55f

    @Export
    var springArmPath: NodePath = NodePath("./SpringArm3D")


    private lateinit var springArm: SpringArm3D

    private var pitch: Float = 0.0f

    override fun _ready() {
        springArm = getNode(springArmPath) as SpringArm3D

        springArm.position = Vector3(
            shoulderOffset,
            0.0f,
            0.0f
        )

        Input.mouseMode = Input.MouseMode.CAPTURED
    }

    override fun _unhandledInput(event: InputEvent) {

        if (event is InputEventMouseMotion) {
            rotateCamera(
                (event.relative.x * mouseSensitivity).toFloat(),
                (event.relative.y * mouseSensitivity).toFloat()
            )
        }

        if (event is InputEventKey && event.pressed &&
            event.keycode == Key.ESCAPE
        ) {
            Input.mouseMode = Input.MouseMode.VISIBLE
        }
    }

    override fun _process(delta: Double) {

        // Controller right stick.
        val lookInput = Input.getVector(
            InputActions.LOOK_LEFT,
            InputActions.LOOK_RIGHT,
            InputActions.LOOK_UP,
            InputActions.LOOK_DOWN
        )

        if (lookInput.lengthSquared() > 0.0f) {
            rotateCamera(
                (lookInput.x * controllerSensitivity * delta).toFloat(),
                (lookInput.y * controllerSensitivity * delta).toFloat()
            )
        }

        // TODO: [SCAR-7] Likely handle aiming here.
    }

    private fun rotateCamera(
        horizontal: Float,
        vertical: Float
    ) {

        // Rotate camera horizontally.
        rotateY(-horizontal)

        // Rotate camera vertically.
        pitch -= vertical

        pitch = GD.clamp(
            pitch,
            Math.toRadians(minPitch.toDouble()).toFloat(),
            Math.toRadians(maxPitch.toDouble()).toFloat()
        )

        springArm.rotation = Vector3(
            pitch,
            0.0f,
            0.0f
        )
    }
}
