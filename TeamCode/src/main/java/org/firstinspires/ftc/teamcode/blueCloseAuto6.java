package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="blueCloseAuto6", group="Autonomous")
public class blueCloseAuto6 extends LinearOpMode {

    private DcMotor frontLeft, backLeft, frontRight, backRight;
    private DcMotor shooter1, shooter2, intakeMotor, transferMotor;

//    public double desiredVelocity = 0.001;
//    public double kP1 = 0.002, kP2 = 0.002;
//    public double shooterPower1 = 0.8, shooterPower2 = -0.8;

    private final ElapsedTime runtime = new ElapsedTime();

    static final double COUNTS_PER_MOTOR_REV = 537.7;
    static final double WHEEL_DIAMETER_INCHES = 4.0;
    static final double COUNTS_PER_INCH =
            COUNTS_PER_MOTOR_REV / (WHEEL_DIAMETER_INCHES * Math.PI);

    @Override
    public void runOpMode() throws InterruptedException {

        // ---------------------------
        // Hardware Mapping
        // ---------------------------
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        shooter1 = hardwareMap.get(DcMotor.class, "Shooter1");
        shooter2 = hardwareMap.get(DcMotor.class, "Shooter2");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        transferMotor = hardwareMap.get(DcMotor.class, "transferMotor");

        Servo intakeServo = hardwareMap.get(Servo.class, "intakeServo");
        Servo shooterServo = hardwareMap.get(Servo.class, "shooterServo");
        CRServo liftServo = hardwareMap.get(CRServo.class, "liftServo");
        Servo holdServo1 = hardwareMap.get(Servo.class, "holdServo1");
        Servo holdServo2 = hardwareMap.get(Servo.class, "holdServo2");

        // ---------------------------
        // Motor Config
        // ---------------------------
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        shooter1.setDirection(DcMotor.Direction.FORWARD);
        shooter2.setDirection(DcMotor.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        shooter1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        intakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        resetEncoders();
        sleep(10);

        telemetry.addData("Status", "Initialized");
        telemetry.update();


        // ---------------------------
        // WAIT FOR START
        // ---------------------------
        waitForStart();
        runtime.reset();

        shooterServo.setPosition(0.6);
        intakeServo.setPosition(0.6);
        shooter1.setPower(0.36);
        shooter2.setPower(0.36);
        encoderDrive(0.5, -5); // move backwards
        turnDrive(0.5, -10);      // turn right
        encoderDrive(0.5, -30);   //move backwards
        intakeServo.setPosition(1);

        shooter1.setPower(0.35);
        shooter2.setPower(0.35);
        intakeMotor.setPower(-0.8);
        transferMotor.setPower(-0.8);
        shooterServo.setPosition(1);
        sleep(4000);

        turnDrive(0.5, -10); //turn right
        encoderDrive(0.5, -20); //move backwards
        turnDrive(0.5, -20); // turn right
        shooterServo.setPosition(0.6);
        intakeMotor.setPower(-0.8);
        transferMotor.setPower(-0.8);

        encoderDrive(0.3, -25);
        encoderDrive(0.5,38 );
        turnDrive(0.5,  30); // turn left
        encoderDrive(0.5,5 );
        shooterServo.setPosition(0.6);

        shooter1.setPower(0.4);
        shooter2.setPower(0.4);
        intakeMotor.setPower(-0.8);
        transferMotor.setPower(-1);
        shooterServo.setPosition(1);

        sleep(4000);


        turnDrive(0.5,  -10);
        encoderDrive(0.5, -25);



        sleep(1000);

        // ---------------------------
        // MOVEMENT
        //---------------------------

        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(2000);
    }

    // ------------------------------------------------------------------------
    // ENCODER DRIVE FUNCTIONS
    // ------------------------------------------------------------------------
    public void encoderDrive(double speed, double inches) {
        int targetTicks = (int) (inches * COUNTS_PER_INCH);
        setTargetPositions(targetTicks, targetTicks);
        runToPositionWithPower(speed);
    }

    public void turnDrive(double speed, double inches) {
        int targetTicks = (int) (inches * COUNTS_PER_INCH);
        setTargetPositions(targetTicks, -targetTicks);
        runToPositionWithPower(speed);
    }

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

    private void runToPositionWithPower(double speed) {
        frontLeft.setPower(speed);
        backLeft.setPower(speed);
        frontRight.setPower(speed);
        backRight.setPower(speed);

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