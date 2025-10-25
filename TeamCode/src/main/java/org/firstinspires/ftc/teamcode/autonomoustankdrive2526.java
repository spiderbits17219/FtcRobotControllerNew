package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="autonomoustankdrive2526", group="Autonomous")
public class autonomoustankdrive2526 extends LinearOpMode {

    private DcMotor leftFront = null;
    private DcMotor leftBack = null;
    private DcMotor rightFront = null;
    private DcMotor rightBack = null;

    // Motor / wheel constants
    static final double COUNTS_PER_MOTOR_REV = 537.7;   // GoBILDA Yellow Jacket (19.2:1)
    static final double WHEEL_DIAMETER_INCHES = 4.0;    // Wheel diameter
    static final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER_INCHES * Math.PI;
    static final double COUNTS_PER_INCH = COUNTS_PER_MOTOR_REV / WHEEL_CIRCUMFERENCE;

    @Override
    public void runOpMode() {
        // Map hardware
        leftFront  = hardwareMap.get(DcMotor.class, "frontLeft");
        leftBack   = hardwareMap.get(DcMotor.class, "backLeft");
        rightFront = hardwareMap.get(DcMotor.class, "frontRight");
        rightBack  = hardwareMap.get(DcMotor.class, "backRight");

        // Reverse one side
        leftFront.setDirection(DcMotor.Direction.FORWARD);
        leftBack.setDirection(DcMotor.Direction.FORWARD);
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightBack.setDirection(DcMotor.Direction.REVERSE);

        // Reset encoders
        resetEncoders();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        // 🔹 Example movements
        encoderDrive(0.5, 24);    // forward 24 inches
        encoderDrive(0.5, -12);   // backward 12 inches
        turnDrive(0.5, 12);       // turn right
        turnDrive(0.5, -12);      // turn left

        telemetry.addData("Path", "Complete");
        telemetry.update();
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

        // Opposite directions for turning
        setTargetPositions(targetTicks, -targetTicks);
        runToPositionWithPower(speed);
    }

    // --- Utility: Set target positions relative to current ---
    private void setTargetPositions(int leftTicks, int rightTicks) {
        leftFront.setTargetPosition(leftFront.getCurrentPosition() + leftTicks);
        leftBack.setTargetPosition(leftBack.getCurrentPosition() + leftTicks);
        rightFront.setTargetPosition(rightFront.getCurrentPosition() + rightTicks);
        rightBack.setTargetPosition(rightBack.getCurrentPosition() + rightTicks);

        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    // --- Utility: Run motors until done ---
    private void runToPositionWithPower(double speed) {
        leftFront.setPower(Math.abs(speed));
        leftBack.setPower(Math.abs(speed));
        rightFront.setPower(Math.abs(speed));
        rightBack.setPower(Math.abs(speed));

        while (opModeIsActive() &&
                (leftFront.isBusy() && rightFront.isBusy() &&
                        leftBack.isBusy() && rightBack.isBusy())) {
            telemetry.addData("LF", leftFront.getCurrentPosition());
            telemetry.addData("RF", rightFront.getCurrentPosition());
            telemetry.addData("LB", leftBack.getCurrentPosition());
            telemetry.addData("RB", rightBack.getCurrentPosition());
            telemetry.update();
        }

        stopAllMotors();
        resetToEncoderMode();
    }

    // --- Reset encoders ---
    private void resetEncoders() {
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        resetToEncoderMode();
    }

    private void resetToEncoderMode() {
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private void stopAllMotors() {
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }
}
