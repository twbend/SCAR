package com.godot.game.player

import com.godot.game.components.IAnimationComponent
import com.godot.game.components.IMovementComponent
import com.godot.game.input.IPlayerInput
import com.godot.game.input.InputActions
import com.godot.game.interfaces.ICharacter
import godot.core.Vector3
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * Tests [PlayerCharacterController].
 */
class PlayerCharacterControllerTest {
    private val testSpeed: Double = 100.0
    private val testMovementDirection = 90f
    private val testVelocity = mockk<Vector3>().also { velocity ->
        every { velocity.length() } returns testSpeed
    }
    private val mockPlayer = mockk<ICharacter>().also { player ->
        every { player.characterVelocity } returns testVelocity
    }
    private val captureSpeed = slot<Float>()
    private val captureMovementDirection = slot<Float>()
    private val mockAnimationComponent = mockk<IAnimationComponent>().also { component ->
        every { component.updateAnimation(capture(captureSpeed), capture(captureMovementDirection)) } just Runs
    }

    private val captureDelta = slot<Double>()
    private val captureIsSprinting = slot<Boolean>()
    private val captureIsAiming = slot<Boolean>()
    private val mockMovementComponent = mockk<IMovementComponent>().also { component ->
        every {
            component.processMovement(
                capture(captureDelta),
                capture(captureIsSprinting),
                capture(captureIsAiming)
            )
        } just Runs
        every { component.movementDirection } returns testMovementDirection
    }
    private val isSprinting = true
    private val isAiming = false
    private val mockPlayerInput = mockk<IPlayerInput>().also { input ->
        every { input.isActionPressed(InputActions.SPRINT) } returns isSprinting
        every { input.isActionPressed(InputActions.AIM) } returns isAiming
    }

    /**
     * Tests [PlayerCharacterController.physicsProcess].
     */
    @Test
    fun verifyPhysicsProcess() {
        // Given the player controller
        val controller = givenController()

        // When physics is being processed
        val delta = 0.0
        controller.physicsProcess(delta)

        // Verify the correct delta was given to the movement component
        Assertions.assertTrue(
            captureDelta.isCaptured,
            "No delta was given to the movement component."
        )
        Assertions.assertEquals(
            delta,
            captureDelta.captured,
            "An incorrect delta was given to the movement component."
        )

        // Verify the correct sprinting value was given to the movement component
        Assertions.assertTrue(
            captureIsSprinting.isCaptured,
            "No value was given to say whether the player is sprinting or not."
        )
        Assertions.assertEquals(
            isSprinting,
            captureIsSprinting.captured,
            "The incorrect value for sprinting was given to the movement component"
        )

        // Verify the correct aiming value was given to the movement component
        Assertions.assertTrue(
            captureIsAiming.isCaptured,
            "No value was given to say whether the player is aiming or not."
        )
        Assertions.assertEquals(
            isAiming,
            captureIsAiming.captured,
            "The incorrect value was given for aiming to the movement component."
        )

        // Verify the animation component was given the correct speed
        Assertions.assertTrue(
            captureSpeed.isCaptured,
            "The speed was not given to the animation component."
        )
        Assertions.assertEquals(
            testSpeed.toFloat(),
            captureSpeed.captured,
            "The incorrect speed was given to the animation component"
        )

        // And verify the correct movement direction was given
        Assertions.assertTrue(
            captureMovementDirection.isCaptured,
            "No movement direction was given to the animation component."
        )
        Assertions.assertEquals(
            testMovementDirection,
            captureMovementDirection.captured,
            "The incorrect movement direction was given to the animation component."
        )
    }


    private fun givenController(): PlayerCharacterController {
        return PlayerCharacterController(
            player = mockPlayer,
            playerInput = mockPlayerInput,
            movementComponent = mockMovementComponent,
            animationComponent = mockAnimationComponent
        )
    }
}