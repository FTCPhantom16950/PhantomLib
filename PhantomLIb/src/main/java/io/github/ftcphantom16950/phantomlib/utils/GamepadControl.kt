package io.github.ftcphantom16950.phantomlib.utils

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Gamepad

class GamepadControl() {
    companion object {
        lateinit var opMode: LinearOpMode
        lateinit var gamepadDriver: Gamepad
        lateinit var gamepadOperator: Gamepad


        fun init(): Boolean {
            gamepadDriver = opMode.gamepad1
            gamepadOperator = opMode.gamepad2

            return true
        }
    }
}
