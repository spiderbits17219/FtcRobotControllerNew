package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp(name = "Teleop2526", group = "TeleOp")
public class Teleop2526 extends LinearOpMode {

    // --- FTC Dashboard tunable variables ---
    public static double desiredVelocity = 100;
    public static double kP1 = 0.001;
    public static double kP2 = 0.001;
    public static double shooterPower1 = 0.4;

   public static double shooterPower2 = 0.8;

    private final ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = dashboard.getTelemetry();

        // Initialize hardware
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        DcMotor backRight = hardwareMap.get(DcMotor.class, "backRight");
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        DcMotor intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        DcMotor transferMotor = hardwareMap.get(DcMotor.class, "transferMotor");
        DcMotor shooter1 = hardwareMap.get(DcMotor.class, "Shooter1");
        DcMotor shooter2 = hardwareMap.get(DcMotor.class, "Shooter2");

        Servo feederServo = hardwareMap.get(Servo.class, "feederServo");
        Servo blockerServo = hardwareMap.get(Servo.class, "blockerServo");
        CRServo liftServo = hardwareMap.get(CRServo.class, "liftServo");
        Servo holdServo1 = hardwareMap.get(Servo.class, "holdServo1");
        Servo holdServo2 = hardwareMap.get(Servo.class, "holdServo2");

        holdServo1.setPosition(0.5);
        holdServo2.setPosition(0);
        liftServo.setPower(0);
        feederServo.setPosition(1);
        double velocity1 = 0;
        double velocity2 = 0;

        boolean BlockerservoOpen = false;

//        Servo feederServo = hardwareMap.get(Servo.class, "feederServo");
//        Servo blockerServo = hardwareMap.get(Servo.class, "blockerServo");
//        Servo liftServo = (Servo) hardwareMap.get(CRServo.class, "liftServo");
//        Servo holdServo1 = hardwareMap.get(Servo.class, "holdServo1");
//        Servo holdServo2 = hardwareMap.get(Servo.class, "holdServo2");


        // Motor directions
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        // Brake behavior
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Shooter encoders
        shooter1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Intake encoder reset
        intakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Status", "Initialized");
        telemetry.update();


        waitForStart();
        runtime.reset();

        double lastPos1 = shooter1.getCurrentPosition();
        double lastPos2 = shooter2.getCurrentPosition();
        double lastTime = runtime.time();

        while (opModeIsActive()) {

            // --- Drive control ---
            double leftDrive = -gamepad1.left_stick_y;
            double rightDrive = -gamepad1.right_stick_y;

            frontLeft.setPower(leftDrive);
            frontRight.setPower(rightDrive);
            backLeft.setPower(leftDrive);
            backRight.setPower(rightDrive);

            // --- Intake ---


            if (gamepad2.a) {
                intakeMotor.setPower(1.0);
            } else if (gamepad2.y) {
                intakeMotor.setPower(-1.0);
            } else {
                intakeMotor.setPower(0);
            }

            // --- Transfer ---
            if (gamepad2.dpad_down) {
                transferMotor.setPower(1.0);
            } else if (gamepad2.dpad_up) {
                transferMotor.setPower(-1.0);
            } else {
                transferMotor.setPower(0);
            }

            if (gamepad1.right_bumper) {
                int pos1 = shooter1.getCurrentPosition();
                int pos2 = shooter2.getCurrentPosition();
                double timeNow = runtime.time();

                velocity1 = (pos1 - lastPos1) / (timeNow - lastTime);
                velocity2 = (pos2 - lastPos2) / (timeNow - lastTime);

                double error1 = desiredVelocity - velocity1;
                double error2 = desiredVelocity - velocity2;

                double syncAdjust1 = kP1 * error1;
                double syncAdjust2 = kP2 * error2;

                shooter1.setPower(shooterPower1 + syncAdjust1);
                shooter2.setPower(shooterPower2 + syncAdjust2);

                lastPos1 = pos1;
                lastPos2 = pos2;
                lastTime = timeNow;
            } else {
                shooter1.setPower(0);
                shooter2.setPower(0);
            }

            if (gamepad2.left_bumper) {
                liftServo.setPower(-1);
                sleep(1700);
                liftServo.setPower(0);
                holdServo1.setPosition(0);
                holdServo2.setPosition(0.5);
                sleep(1000);
                liftServo.setPower(1);
                sleep(1000);
                liftServo.setPower(0);

            }

            if (gamepad2.right_bumper) {
                if (!BlockerservoOpen && velocity1 >= desiredVelocity && velocity2 >= desiredVelocity) {
                    BlockerservoOpen = true;
                    blockerServo.setPosition(0.5);

                }
                else if (BlockerservoOpen) {
                    BlockerservoOpen = false;
                    blockerServo.setPosition(-0.5);

                }
            }

            if (gamepad2.right_trigger > 0.5) {
                if (!BlockerservoOpen) {
                    BlockerservoOpen = true;
                    blockerServo.setPosition(0.5);
                }
                else  {
                    BlockerservoOpen = false;
                    blockerServo.setPosition(-0.5);
                }
            }

            if (gamepad2.right_stick_button){
                blockerServo.setPosition(0);
            } else {
                blockerServo.setPosition(0.3);
            }


            if (gamepad2.right_bumper) {
                shooter1.setPower(1);
                shooter2.setPower(1);


            if (gamepad2.right_stick_button){
              blockerServo.setPosition(0.5);
           } else {
                blockerServo.setPosition(0);
            }

            if (gamepad1.dpad_up){
                shooterPower = shooterPower + 0.1;
            } else if (gamepad1.dpad_down){
                shooterPower = shooterPower - 0.1;
            }
            // --- Lift servo ---
            if (gamepad2.right_bumper) {
                shooter1.setPower(shooterPower);
                shooter2.setPower(shooterPower);
            } else {
                shooter1.setPower(0);
                shooter2.setPower(0);
            }

            if (gamepad2.right_bumper) {
                int pos1 = shooter1.getCurrentPosition();
               int pos2 = shooter2.getCurrentPosition();
                double timeNow = runtime.time();

                velocity1 = (pos1 - lastPos1) / (timeNow - lastTime);
                 velocity2 = (pos2 - lastPos2) / (timeNow - lastTime);

               double error1 = desiredVelocity - velocity1;
               double error2 = desiredVelocity - velocity2;

                double syncAdjust1 = kP1 * error1;
                double syncAdjust2 = kP2 * error2;

               shooter1.setPower(shooterPower1 + syncAdjust1);
                shooter2.setPower(shooterPower2 + syncAdjust2);

               lastPos1 = pos1;
              lastPos2 = pos2;
               lastTime = timeNow;


                if (shooter1.getPower() >= desiredVelocity && shooter2.getPower() >= desiredVelocity ) {
                   feederServo.setPosition(0.5);
               }
           } else {
                shooter1.setPower(0);
               shooter2.setPower(0);
           }

            // --- Telemetry + Dashboard Data ---
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Shooter1 Pos", shooter1.getCurrentPosition());
            telemetry.addData("Shooter2 Pos", shooter2.getCurrentPosition());
            telemetry.addData("Shooter Error", shooter1.getCurrentPosition() - shooter2.getCurrentPosition());
            telemetry.addData("Shooter1 Power", shooter1.getPower());
            telemetry.addData("Shooter2 Power", shooter2.getPower());
            telemetry.addData("Desired Velocity", desiredVelocity);
            telemetry.addData("Lift Servo", liftServo.getPosition());
            telemetry.update();
        }
    }
}







