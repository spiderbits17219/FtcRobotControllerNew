package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="blueFarAuto", group="Autonomous")
public class blueFarAuto extends LinearOpMode {

    private DcMotor frontLeft = null;
    private DcMotor backLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backRight = null;
    private DcMotor Shooter1 = null;
    private DcMotor Shooter2 = null;
    private DcMotor intakeMotor = null;
    private DcMotor transferMotor = null;




    // Motor / wheel constants
    static final double COUNTS_PER_MOTOR_REV = 537.7;   // GoBILDA Yellow Jacket (19.2:1)
    static final double WHEEL_DIAMETER_INCHES = 4.0;    // Wheel diameter
    static final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER_INCHES * Math.PI;
    static final double COUNTS_PER_INCH = COUNTS_PER_MOTOR_REV / WHEEL_CIRCUMFERENCE;

    @Override
    public void runOpMode() throws InterruptedException {
        // Map hardware
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        DcMotor backRight = hardwareMap.get(DcMotor.class, "backRight");
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        DcMotor intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        DcMotor transferMotor = hardwareMap.get(DcMotor.class, "transferMotor");
        DcMotor Shooter1 = hardwareMap.get(DcMotor.class, "Shooter1");
        DcMotor Shooter2 = hardwareMap.get(DcMotor.class, "Shooter2");

        Servo feederServo = hardwareMap.get(Servo.class, "feederServo");
        Servo blockerServo = hardwareMap.get(Servo.class, "blockerServo");
        CRServo liftServo = hardwareMap.get(CRServo.class, "liftServo");
        Servo holdServo1 = hardwareMap.get(Servo.class, "holdServo1");
        Servo holdServo2 = hardwareMap.get(Servo.class, "holdServo2");


        // Reset encoders
        resetEncoders();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        blockerServo.setPosition(0);
        encoderDrive(0.5, 100);    // forward 4.smth tiles
        turnDrive(0.5, 12);      // turn left
        encoderDrive(0.5, 6);   //half a tile


        transferMotor.setPower(-0.8);
        Shooter1.setPower(0.8);
        Shooter2.setPower(0.8);

        telemetry.addData("Path", "Complete");
        telemetry.update();

        wait(1000);
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
