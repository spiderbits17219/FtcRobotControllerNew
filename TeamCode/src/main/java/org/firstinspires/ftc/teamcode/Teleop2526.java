package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Teleop2526")
public class Teleop2526 extends LinearOpMode {

    private final ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        // Initialize motors
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        DcMotor backRight = hardwareMap.get(DcMotor.class, "backRight");
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        DcMotor intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        DcMotor transferMotor = hardwareMap.get(DcMotor.class, "transferMotor");
        DcMotor shooter1 = hardwareMap.get(DcMotor.class, "Shooter1");
        DcMotor shooter2 = hardwareMap.get(DcMotor.class, "Shooter2");

        // --- New Servo for feeder ---
        Servo feederServo = hardwareMap.get(Servo.class, "feederServo");

        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // --- Shooter Encoders ---
        shooter1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //Reset encoders for intake
        intakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Status", "Initialized");

        telemetry.update();

        // Wait for the game to start
        waitForStart();
        runtime.reset();
// PID sync variables
        double kP = 0.002; // proportional constant to sync shooter2
        double shooterPower = 1.0;
        // Run until the end of the match
        while (opModeIsActive()) {

            double leftDrive = -gamepad1.left_stick_y;
            double rightDrive  =  -gamepad1.right_stick_y;

            frontLeft.setPower(leftDrive);
            frontRight.setPower(rightDrive);
            backLeft.setPower(leftDrive);
            backRight.setPower(rightDrive);

//            frontLeft.setPower(0.2);
//            frontRight.setPower(0.2);
//            backLeft.setPower(0.2);
//            backRight.setPower(0.2);

            //  --- Mechanisms (example placeholders) ---
            // Intake control (A = forward, B = reverse)
            if (gamepad2.a) {
                intakeMotor.setPower(1.0);
            } else if (gamepad1.b) {
                intakeMotor.setPower(-1.0);
            } else {
                intakeMotor.setPower(0);
            }

            // Transfer Control (Y = push, X = pull)
            if (gamepad2.y) {
                transferMotor.setPower(1.0);
            } else if (gamepad1.x) {
                transferMotor.setPower(-1.0);
            } else {
                transferMotor.setPower(0);
            }

            // Shooter (A Button = spin up)

            if (gamepad2.right_bumper) {
                shooter1.setPower(1.0);
                if (gamepad2.left_bumper)
                    shooter2.setPower(1.0);
            } else {
                shooter1.setPower(0);
                shooter2.setPower(0);
            }
            // --- Shooter Control with Encoder Sync ---
            if (gamepad2.right_bumper) {
                shooter1.setPower(shooterPower);
                // keep shooter2 in sync using encoder feedback
                int pos1 = shooter1.getCurrentPosition();
                int pos2 = shooter2.getCurrentPosition();
                double error = pos1 - pos2;
                double syncAdjust = kP * error;
                shooter2.setPower(shooterPower + syncAdjust);
                // --- Servo Control based on shooter encoder ---
                int SHOOTER_READY_POSITION = 500; // <-- replace with your desired encoder target
                if (Math.abs(pos1) >= SHOOTER_READY_POSITION) {
                    feederServo.setPosition(1.0); // open servo
                } else {
                    feederServo.setPosition(0.0); // keep closed until ready
                }
            } else {
                shooter1.setPower(0);
                shooter2.setPower(0);
                feederServo.setPosition(0.0);
            }

            // --- Telemetry ---
            // --- Telemetry ---
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Shooter1 Pos", shooter1.getCurrentPosition());
            telemetry.addData("Shooter2 Pos", shooter2.getCurrentPosition());
            telemetry.addData("Shooter Error", shooter1.getCurrentPosition() - shooter2.getCurrentPosition());
            telemetry.addData("Shooter2 Adjusted Power", shooter2.getPower());
            telemetry.addData("Feeder Servo", feederServo.getPosition());
            telemetry.update();
        }
    }
}
