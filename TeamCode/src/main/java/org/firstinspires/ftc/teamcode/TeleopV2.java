package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "TeleopV2")
public class TeleopV2 extends LinearOpMode {

    // Declare motors
    private DcMotorEx frontRight;
    private DcMotorEx frontLeft;
    private DcMotorEx backRight;
    private DcMotorEx backLeft;
    private DcMotor intakeMotor;
    private DcMotor rampPusher;
    private DcMotor shooter1;

    private DcMotor shooter2;

    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        // Initialize motors
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        rampPusher = hardwareMap.get(DcMotor.class, "RampPusher");
        shooter1 = hardwareMap.get(DcMotor.class, "Shooter1");
        shooter2 = hardwareMap.get(DcMotor.class, "Shooter2");

        // Set motor directions
        frontRight.setDirection(DcMotorEx.Direction.REVERSE);
        backRight.setDirection(DcMotorEx.Direction.REVERSE);
        frontLeft.setDirection(DcMotorEx.Direction.FORWARD);
        backLeft.setDirection(DcMotorEx.Direction.FORWARD);

        // Reset encoders for intake
        intakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start
        waitForStart();
        runtime.reset();

        // Run until the end of the match
        while (opModeIsActive()) {

            // --- Mecanum Drive Control ---
            double leftStickX = gamepad1.left_stick_x;
            double leftStickY = -gamepad1.left_stick_y;  // invert for natural forward
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

            // --- Mechanisms (example placeholders) ---
            // Intake control (A = forward, B = reverse)
            if (gamepad1.a) {
                intakeMotor.setPower(1.0);
            } else if (gamepad1.b) {
                intakeMotor.setPower(-1.0);
            } else {
                intakeMotor.setPower(0);
            }

            // Ramp pusher (Y = push, X = pull)
            if (gamepad1.y) {
                rampPusher.setPower(1.0);
            } else if (gamepad1.x) {
                rampPusher.setPower(-1.0);
            } else {
                rampPusher.setPower(0);
            }

            // Shooter (Right trigger = spin up)
            shooter1.setPower(gamepad1.right_trigger);
            shooter2.setPower(gamepad1.right_trigger);

            // --- Telemetry ---
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Drive Power",
                    "FL: %.2f | FR: %.2f | BL: %.2f | BR: %.2f",
                    frontLeft.getPower(), frontRight.getPower(),
                    backLeft.getPower(), backRight.getPower());
            telemetry.addData("Intake", intakeMotor.getPower());
            telemetry.addData("Shooter1", shooter1.getPower());
            telemetry.addData("Shooter2", shooter2.getPower());
            telemetry.update();
        }
    }
}
