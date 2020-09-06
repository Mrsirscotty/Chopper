/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="TeleOpMain", group="Linear Opmode")

public class TeleOpMain extends LinearOpMode {

    // Declare OpMode members.
    HardwareChopper chopper   = new HardwareChopper();   // Use a Chopper's hardware
    private ElapsedTime runtime = new ElapsedTime();

    static final double INCREMENT   = 0.002;     // amount to slow servo 
    static final double MAX_POS     =  0.70;     // Maximum rotational position
    static final double MIN_POS     =  0.30;     // Minimum rotational position

    double  left_position = (MIN_POS); 
    double  right_position = (MAX_POS);
    int drivePowerAdjustment = 1;
    double leftPower = 0;
    double rightPower = 0;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        chopper.init(hardwareMap);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            if (0<gamepad1.right_trigger) {
                leftPower = rightPower = gamepad1.right_trigger ;
            }
            else if (0<gamepad1.left_trigger) {
                leftPower = rightPower = -gamepad1.left_trigger ;
            }
            else {
                leftPower  = -gamepad1.left_stick_y ;
                rightPower = -gamepad1.right_stick_y ; 
            }

            if (gamepad1.a)
                drivePowerAdjustment = 1;
            if (gamepad1.b)
                drivePowerAdjustment = 2;
            if (gamepad1.x)
                drivePowerAdjustment = 3;
            if (gamepad1.y)
                drivePowerAdjustment = 4;

            if (gamepad1.dpad_up) {
                left_position = (.60); 
                right_position = (.40);
            }
            if (gamepad1.dpad_down) {
                left_position = (MIN_POS); 
                right_position = (MAX_POS);
            }

            if (gamepad1.left_bumper) {
                // Keep stepping up until we hit the max value.
                left_position += INCREMENT ;
                right_position -= INCREMENT ;
                if (left_position >= MAX_POS ) {
                    left_position = MAX_POS;
                }
                if (right_position <= MIN_POS ) {
                    right_position = MIN_POS;
                }
            }

            if (gamepad1.right_bumper) {
                // Keep stepping down until we hit the min value.
                left_position -= INCREMENT ;
                right_position += INCREMENT ;
                if (left_position <= MIN_POS ) {
                    left_position = MIN_POS;
                }
                if (right_position >= MAX_POS ) {
                    right_position = MAX_POS;
                }
            }

            // Set the servo to the new position and pause;
            chopper.leftClaw.setPosition(left_position);
            chopper.rightClaw.setPosition(right_position);

            // Send calculated power to wheels
            chopper.leftDrive.setPower(leftPower/drivePowerAdjustment);
            chopper.rightDrive.setPower(rightPower/drivePowerAdjustment);

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Servo Position", "leftCLaw (%5.2f), rightClaw (%5.2f)", left_position, right_position);
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower/drivePowerAdjustment, rightPower/drivePowerAdjustment);
            telemetry.addData("Power Adjustment", "(%d) ", drivePowerAdjustment);
            telemetry.update();
        }
    }
}
