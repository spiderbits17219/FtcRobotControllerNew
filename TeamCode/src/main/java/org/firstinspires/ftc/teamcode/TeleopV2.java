package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "TeleopV2")
public class TeleopV2 extends LinearOpMode {

    // Declare motors
    private DcMotorEx frontRight;
    private DcMotorEx frontLeft;
    private DcMotorEx backRight;
    private DcMotorEx backLeft;

    @Override
    public void runOpMode() {

        // Initialize motors
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");

        // Set motor directions
        frontRight.setDirection(DcMotorEx.Direction.REVERSE);
        backRight.setDirection(DcMotorEx.Direction.REVERSE);
        frontLeft.setDirection(DcMotorEx.Direction.FORWARD);
        backLeft.setDirection(DcMotorEx.Direction.FORWARD);

        // Wait for the game to start
        waitForStart();

        // Run until the end of the match
        while (opModeIsActive()) {

            // Tank drive control: left stick controls left side, right stick controls right side
            double leftPower = -gamepad1.left_stick_y;
            double rightPower = -gamepad1.right_stick_y;

            // Apply power to motors
            frontLeft.setPower(leftPower);
            backLeft.setPower(leftPower);
            frontRight.setPower(rightPower);
            backRight.setPower(rightPower);

            // Telemetry for debugging
            telemetry.addData("Left Power", leftPower);
            telemetry.addData("Right Power", rightPower);
            telemetry.update();
        }
    }
}
