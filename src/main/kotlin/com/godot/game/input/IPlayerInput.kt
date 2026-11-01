package com.godot.game.input

import godot.api.Input
import godot.api.InputEventJoypadMotion
import godot.api.InputEventKey
import godot.api.InputEventMouseButton

/**
 * Acts as an intermediary for the [Input] singleton.
 *
 * Adding this abstract allows for higher testability.
 */
interface IPlayerInput {
    companion object {
        /**
         * Provides an instance of [IPlayerInput].
         *
         * @return An instance of [IPlayerInput].
         */
        fun instance(): IPlayerInput {
            return PlayerInput()
        }
    }

    /**
     * Returns true if you are pressing the action event.
     * If [exactMatch] is false, it ignores additional input modifiers for [InputEventKey] and [InputEventMouseButton]
     * events, and the direction for [InputEventJoypadMotion] events.
     *
     * Note: Due to keyboard ghosting, isActionPressed may return false even if one of the action's keys is pressed.
     * See url=$DOCS_URL/tutorials/inputs/input_examples.html#keyboard-eventsInput examples/url in the documentation
     * for more information.
     *
     * @param action The [String] which identifies the input action.
     * @param exactMatch Indicates whether additional input modifiers should be considered (true) or not (false).
     *
     * @return True if the player is pressing the specified action event.
     */
    fun isActionPressed(action: String, exactMatch: Boolean = false): Boolean
}

/**
 * Implements the [IPlayerInput] interface.
 */
private class PlayerInput : IPlayerInput {
    override fun isActionPressed(action: String, exactMatch: Boolean): Boolean {
        return Input.isActionPressed(action, exactMatch)
    }
}