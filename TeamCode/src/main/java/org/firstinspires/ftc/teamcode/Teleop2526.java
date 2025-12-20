package org.firstinspires.ftc.teamcode;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;



@Config
@TeleOp(name = "Teleop2526", group = "TeleOp")
public class Teleop2526 extends LinearOpMode {
    // --- FTC Dashboard tunable variables ---
    public static double desiredVelocity = 800;
    public static double kP1 = 4;
    public static double kP2 = 8;

    public static double  kI1 = 4;

    public static double kI2 = 4;

    public static double kD1 = 0.05;

    public static double kD2 = 0.05;

    public static double kF1 = 0;

    public static double kF2 = 0;




    public static double shooterVelocity1 = 0;
    public static double shooterVelocity2 = 0;
    private final ElapsedTime runtime = new ElapsedTime();

    // Creates a PIDFController with gains kP, kI, kD, and kF
    PIDFController pidf1 = new PIDFController(kP1, kI1, kD1, kF1);
    PIDFController pidf2 = new PIDFController(kP2, kI2, kD2, kF2);

    @Override
    public void runOpMode() {
        // Initialize hardware
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        DcMotor backRight = hardwareMap.get(DcMotor.class, "backRight");
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        DcMotor intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        DcMotor transferMotor = hardwareMap.get(DcMotor.class, "transferMotor");
        MotorEx shooter1 = new MotorEx(hardwareMap, "shooter1", Motor.GoBILDA.RPM_312);
        MotorEx shooter2 = new MotorEx(hardwareMap, "shooter2", Motor.GoBILDA.RPM_312);
        Servo shooterServo = hardwareMap.get(Servo.class, "shooterServo");
        Servo intakeServo = hardwareMap.get(Servo.class, "intakeServo");
        CRServo liftServo = hardwareMap.get(CRServo.class, "liftServo");
        Servo holdServo1 = hardwareMap.get(Servo.class, "holdServo1");
        Servo holdServo2 = hardwareMap.get(Servo.class, "holdServo2");
        holdServo1.setPosition(0.5);
        holdServo2.setPosition(0);
        liftServo.setPower(0);
        shooterServo.setPosition(0.6);
        shooter1.setRunMode(Motor.RunMode.VelocityControl);
        shooter2.setRunMode(Motor.RunMode.VelocityControl);
//
//        double[] coeffs1 = shooter1.getVeloCoefficients();
//        kP1 = coeffs1[0];
//        kI1 = coeffs1[1];
//        kD1 = coeffs1[2];
//
//        double[] coeffs2 = shooter2.getVeloCoefficients();
//        kP2 = coeffs2[0];
//        kI2 = coeffs2[1];
//        kD2 = coeffs2[2];

        boolean shooterServoOpen = false;
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        shooter1.motor.setDirection(DcMotor.Direction.FORWARD);
        shooter2.motor.setDirection(DcMotor.Direction.REVERSE);
        // Brake behavior
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        // Intake encoder reset
        intakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // --- FTC Dashboard Setup ---
        // --- FTC Dashboard Setup ---
        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());
        waitForStart();
        runtime.reset();
        double lastPos1 = shooter1.motor.getCurrentPosition();
        double lastPos2 = shooter2.motor.getCurrentPosition();
        double lastTime = runtime.time();
        while (opModeIsActive()) {
            // --- Drive control ---
            double leftDrive = gamepad1.left_stick_y;
            double rightDrive = gamepad1.right_stick_y;
            if (gamepad1.y) {
                sleep(1000);
            }
            frontLeft.setPower(leftDrive);
            frontRight.setPower(rightDrive);
            backLeft.setPower(leftDrive);
            backRight.setPower(rightDrive);
            // --- Intake ---
            if (gamepad2.a) {
                intakeMotor.setPower(0.9);
            } else if (gamepad2.y) {
                intakeMotor.setPower(-0.9);
            } else {
                intakeMotor.setPower(0);
            }
            if (gamepad2.dpad_left) {
                transferMotor.setPower(0.5);
                transferMotor.setPower(-0.8);
            }

            // --- Transfer ---
            if (gamepad2.dpad_down) {
                transferMotor.setPower(0.7);
            } else if (gamepad2.dpad_up) {
                transferMotor.setPower(-0.7);
            } else {
                transferMotor.setPower(0);
            }

            if (gamepad1.left_bumper && gamepad1.right_bumper) {
                liftServo.setPower(-1);
                sleep(1950);
                liftServo.setPower(0);
                holdServo1.setPosition(0);
                holdServo2.setPosition(0.5);
                sleep(1000);
                liftServo.setPower(1);
                sleep(1200);
                liftServo.setPower(0);
            }


            if (gamepad2.b){
                intakeServo.setPosition(0.7);
            } else {
                intakeServo.setPosition(1);
            }

            if (gamepad2.left_bumper){
                shooterServo.setPosition(1);

            } else {
                shooterServo.setPosition(0.6);

            }

//            double output1 = pidf1.calculate(
//                    shooter1.getVelocity(),desiredVelocity
//            );
//
//            double output2 = pidf2.calculate(
//                    shooter2.getVelocity(),desiredVelocity
//            );
//
//            shooter1.set(output1);
//            shooter2.set(output2);
            shooter1.setVeloCoefficients(kP1, kI1, kD1);
            shooter2.setVeloCoefficients(kP2, kI2, kD2);

                shooter1.setVelocity(desiredVelocity);
                shooter2.setVelocity(desiredVelocity);


            // --- Telemetry + Dashboard Data ---
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("shooter1.motor Pos", shooter1.motor.getCurrentPosition());
            telemetry.addData("shooter2.motor Pos", shooter2.motor.getCurrentPosition());
            telemetry.addData("Shooter(1-2) Delta", shooter1.motor.getCurrentPosition() - shooter2.motor.getCurrentPosition());
            telemetry.addData("shooter1.motor Power", shooter1.motor.getPower());
            telemetry.addData("shooter2.motor Power", shooter2.motor.getPower());
            telemetry.addData("Left Flywheel Velocity", shooter1.getVelocity());
            telemetry.addData("Right Flywheel Velocity", shooter2.getVelocity());
            telemetry.addData("desired velocity", desiredVelocity);



            telemetry.update();
        }
    }
}

