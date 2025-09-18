package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "TeleopV12526", group = "Linear OpMode")
public class TeleopV12526 extends LinearOpMode {

    // Mecanum drive motors
    private DcMotor frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor;
    // Arm motor with encoder
    private DcMotor armMotor;
    // Intake motors (continuous rotation servos)
    private CRServo intakeServoLeft, intakeServoRight;
    // Servo for additional control
    private Servo servo;
    // Timer for runtime
    private final ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        // Initialize hardware
        frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRightMotor = hardwareMap.get(DcMotor.class, "frontRight");
        backLeftMotor = hardwareMap.get(DcMotor.class, "backLeft");
        backRightMotor = hardwareMap.get(DcMotor.class, "backRight");
        armMotor = hardwareMap.get(DcMotor.class, "Arm Motor");
        intakeServoLeft = hardwareMap.get(CRServo.class, "intake_servo_left");
        intakeServoRight = hardwareMap.get(CRServo.class, "intake_servo_right");
        servo = hardwareMap.get(Servo.class, "servo");

        // Reverse motors for correct direction
        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);

        // Reset arm motor encoder
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

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

            frontLeftMotor.setPower(frontLeftPower / max);
            frontRightMotor.setPower(frontRightPower / max);
            backLeftMotor.setPower(backLeftPower / max);
            backRightMotor.setPower(backRightPower / max);

            // Arm control (with encoder feedback)
            double armPower = gamepad2.left_stick_y; // Arm controlled by left stick on gamepad2
            armMotor.setPower(armPower);

            // Control arm position with telemetry for debugging
            int armPosition = armMotor.getCurrentPosition();
            telemetry.addData("Arm Encoder Position", armPosition);

            // Rolling intake control for left and right intake servos
            if (gamepad1.right_trigger > 0.1) {
                intakeServoLeft.setPower(1.0); // Full power forward
                intakeServoRight.setPower(1.0);
            } else if (gamepad1.left_trigger > 0.1) {
                intakeServoLeft.setPower(-1.0); // Full power reverse
                intakeServoRight.setPower(-1.0);
            } else {
                intakeServoLeft.setPower(0.0);
                intakeServoRight.setPower(0.0); // Stop intake
            }

            // Servo control for additional actions
            if (gamepad1.a) {
                servo.setPosition(0.5); // Example action for servo
            } else if (gamepad1.b) {
                servo.setPosition(1.0); // Example action for servo
            }

            // Telemetry data for debugging
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Mecanum Drive",
                    "FL (%.2f), FR (%.2f), BL (%.2f), BR (%.2f)",
                    frontLeftMotor.getPower(),
                    frontRightMotor.getPower(),
                    backLeftMotor.getPower(),
                    backRightMotor.getPower());
            telemetry.addData("Intake Power", intakeServoLeft.getPower());
            telemetry.update();
        }
    }
}
