package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="autonomoustankdrive2526", group="Autonomous")
public class autonomoustankdrive2526 extends LinearOpMode {

    private DcMotor frontLeft = null;
    private DcMotor backLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backRight = null;
    private DcMotor Shooter1 = null;
    private DcMotor Shooter2 = null;

    // Motor / wheel constants
    static final double COUNTS_PER_MOTOR_REV = 537.7;   // GoBILDA Yellow Jacket (19.2:1)
    static final double WHEEL_DIAMETER_INCHES = 4.0;    // Wheel diameter
    static final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER_INCHES * Math.PI;
    static final double COUNTS_PER_INCH = COUNTS_PER_MOTOR_REV / WHEEL_CIRCUMFERENCE;

    @Override
    public void runOpMode() {
        // Map hardware
        frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft   = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight  = hardwareMap.get(DcMotor.class, "backRight");

        Shooter1 = hardwareMap.get(DcMotor.class, "Shooter1");
        Shooter2 = hardwareMap.get(DcMotor.class, "Shooter2");

        // Reverse one side of drivetrain
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        // Set shooter directions (reverse one if they spin opposite)
        Shooter1.setDirection(DcMotor.Direction.FORWARD);
        Shooter2.setDirection(DcMotor.Direction.REVERSE);

        // Reset and set modes
        resetEncoders();

        Shooter1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Shooter2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Shooter1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Shooter2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            // Example drivetrain movements
            encoderDrive(0.5, 24);    // forward 24 inches
            encoderDrive(0.5, -12);   // backward 12 inches
            turnDrive(0.5, 12);       // turn right
            turnDrive(0.5, -12);      // turn left

            // --- 🔹 Spin shooter motors for 5 seconds ---
            telemetry.addData("Action", "Starting Shooters");
            telemetry.update();

            Shooter1.setPower(1.0);
            Shooter2.setPower(1.0);

            sleep(5000);  // run for 5 seconds

            Shooter1.setPower(0);
            Shooter2.setPower(0);

            telemetry.addData("Action", "Shooters Stopped");
            telemetry.update();

            telemetry.addData("Path", "Complete");
            telemetry.update();
        }
    }

    // --- Forward/backward drive ---
    public void encoderDrive(double speed, double inches) {
        int targetTicks = (int)(inches * COUNTS_PER_INCH);

        setTargetPositions(targetTicks, targetTicks);
        runToPositionWithPower(speed);
    }

    // --- Turning drive (right = positive inches, left = negative inches) ---
    public void turnDrive(double speed, double inches) {
        int targetTicks = (int)(inches * COUNTS_PER_INCH);

        setTargetPositions(targetTicks, -targetTicks);
        runToPositionWithPower(speed);
    }

    // --- Utility: Set target positions relative to current ---
    private void setTargetPositions(int leftTicks, int rightTicks) {
        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() + leftTicks);
        backLeft.setTargetPosition(backLeft.getCurrentPosition() + leftTicks);
        frontRight.setTargetPosition(frontRight.getCurrentPosition() + rightTicks);
        backRight.setTargetPosition(backRight.getCurrentPosition() + rightTicks);

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    // --- Utility: Run motors until done ---
    private void runToPositionWithPower(double speed) {
        frontLeft.setPower(Math.abs(speed));
        backLeft.setPower(Math.abs(speed));
        frontRight.setPower(Math.abs(speed));
        backRight.setPower(Math.abs(speed));

        while (opModeIsActive() &&
                (frontLeft.isBusy() && frontRight.isBusy() &&
                        backLeft.isBusy() && backRight.isBusy())) {
            telemetry.addData("LF", frontLeft.getCurrentPosition());
            telemetry.addData("RF", frontRight.getCurrentPosition());
            telemetry.addData("LB", backLeft.getCurrentPosition());
            telemetry.addData("RB", backRight.getCurrentPosition());
            telemetry.update();
        }

        stopAllMotors();
        resetToEncoderMode();
    }

    // --- Reset encoders ---
    private void resetEncoders() {
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        resetToEncoderMode();
    }

    private void resetToEncoderMode() {
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private void stopAllMotors() {
        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);
    }
}
