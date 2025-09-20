package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "TeleopV12526", group = "Linear OpMode")
public class TeleopV12526 extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backLeft = null;
    private DcMotor backRight = null;
    private DcMotor intakeMotor = null;
    private Servo servo;
    // Timer for runtime
    @Override
    public void runOpMode() {
        // Initialize hardware
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");

        servo = hardwareMap.get(Servo.class, "servo");

        // Reverse motors for correct direction
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        // Reset arm motor encoder
        intakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            // Mecanum Drive control (tank drive with joysticks)
            double leftStickX = gamepad1.left_stick_x;
            double leftStickY = -gamepad1.left_stick_y; // Invert Y for natural forward
            double rightStickX = gamepad1.right_stick_x;

            double frontLeftPower = leftStickY + leftStickX + rightStickX;
            double frontRightPower = leftStickY - leftStickX - rightStickX;
            double backLeftPower = leftStickY - leftStickX + rightStickX;
            double backRightPower = leftStickY + leftStickX - rightStickX;

            // Normalize powers so no value exceeds 1.0
            double max = Math.max(1.0, Math.max(
                    Math.abs(frontLeftPower),
                    Math.max(Math.abs(frontRightPower),
                            Math.max(Math.abs(backLeftPower), Math.abs(backRightPower)))
            ));

            frontLeft.setPower(frontLeftPower / max);
            frontRight.setPower(frontRightPower / max);
            backLeft.setPower(backLeftPower / max);
            backRight.setPower(backRightPower / max);

            double intakeMotorPower = 0;
            intakeMotor.setPower(intakeMotorPower / max);

            // Telemetry data for debugging
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Mecanum Drive",
                    "FL (%.2f), FR (%.2f), BL (%.2f), BR (%.2f)",
                    frontLeft.getPower(),
                    frontRight.getPower(),
                    backLeft.getPower(),
                    backRight.getPower());
            telemetry.update();
        }
    }
}
