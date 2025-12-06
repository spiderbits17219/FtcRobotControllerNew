package org.firstinspires.ftc.teamcode;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
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
    public static double desiredVelocity = 2350;
    public static double kP1 = 0.028;
    public static double kP2 = 0.028;

    public static double  Ki1 = 0.004;

    public static double Ki2 = 0.004;

    public static double Kd1 = 0.0053;

    public static double Kd2 = 0.0053;


    public static double shooterPower1 = 0;
    public static double shooterPower2 = 0;
    private final ElapsedTime runtime = new ElapsedTime();
    @Override
    public void runOpMode() {
        // Initialize hardware
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        DcMotor backRight = hardwareMap.get(DcMotor.class, "backRight");
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        DcMotor intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        DcMotor transferMotor = hardwareMap.get(DcMotor.class, "transferMotor");
        DcMotor shooter1 = hardwareMap.get(DcMotor.class, "Shooter1");
        DcMotor shooter2 = hardwareMap.get(DcMotor.class, "Shooter2");
        Servo shooterServo = hardwareMap.get(Servo.class, "shooterServo");
        Servo intakeServo = hardwareMap.get(Servo.class, "intakeServo");
        CRServo liftServo = hardwareMap.get(CRServo.class, "liftServo");
        Servo holdServo1 = hardwareMap.get(Servo.class, "holdServo1");
        Servo holdServo2 = hardwareMap.get(Servo.class, "holdServo2");
        holdServo1.setPosition(0.5);
        holdServo2.setPosition(0);
        liftServo.setPower(0);
        shooterServo.setPosition(0.6);
        double velocity1 = 0;
        double velocity2 = 0;
        double previous_error1 = 0;
        double previous_error2 = 0;
        double integral_sum1 = 0;
        double integral_sum2 = 0;
        double derivative1 = 0;
        double derivative2 = 0;
        double output_power1 = 0;
        double output_power2 = 0;
        double error1 = 0;
        double error2 = 0;



        boolean shooterServoOpen = false;
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        shooter1.setDirection(DcMotor.Direction.FORWARD);
        shooter2.setDirection(DcMotor.Direction.REVERSE);
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
        // --- FTC Dashboard Setup ---
        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());
        waitForStart();
        runtime.reset();
        double lastPos1 = shooter1.getCurrentPosition();
        double lastPos2 = shooter2.getCurrentPosition();
        double lastTime = runtime.time();
        while (opModeIsActive()) {
            // --- Drive control ---
            double leftDrive = gamepad1.left_stick_y;
            double rightDrive = gamepad1.right_stick_y;
            frontLeft.setPower(leftDrive);
            frontRight.setPower(rightDrive);
            backLeft.setPower(leftDrive);
            backRight.setPower(rightDrive);
            // --- Intake ---
            if (gamepad2.a) {
                intakeMotor.setPower(0.7);
            } else if (gamepad2.y) {
                intakeMotor.setPower(-0.7);
            } else {
                intakeMotor.setPower(0);
            }
            // --- Transfer ---
            if (gamepad2.dpad_down) {
                transferMotor.setPower(0.7);
            } else if (gamepad2.dpad_up) {
                transferMotor.setPower(-0.7);
            } else {
                transferMotor.setPower(0);
            }
            if (gamepad1.left_bumper) {
                liftServo.setPower(1);
                sleep(1700);
                liftServo.setPower(0);
                holdServo1.setPosition(0);
                holdServo2.setPosition(0.5);
                sleep(1000);
                liftServo.setPower(-1);
                sleep(1000);
                liftServo.setPower(0);
            }


            if (gamepad2.right_trigger > 0.5) {
                if (!shooterServoOpen) {
                    shooterServoOpen = true;
                    shooterServo.setPosition(1);
                }
                else  {
                    shooterServoOpen = false;
                    shooterServo.setPosition(0.6);
                }
            }

            if (gamepad2.right_stick_button){
                shooterServo.setPosition(1);
            } else {
                shooterServo.setPosition(0.6);
            }

            if (gamepad2.x){
                shooterServo.setPosition(1);
                transferMotor.setPower(0.5);
                sleep(150);
                transferMotor.setPower(-0.8);
                sleep(250);
                transferMotor.setPower(0);
            } else {
                shooterServo.setPosition(0.6);

            }
//

//
            // --- Lift servo ---
//            if (gamepad2.right_bumper) {
//                shooter1.setPower(0.3);
//                shooter2.setPower(0.3);
//            } else {
//                shooter1.setPower(0);
//                shooter2.setPower(0);
//            }


            if (gamepad2.right_bumper) {
                int pos1 = shooter1.getCurrentPosition();
                int pos2 = shooter2.getCurrentPosition();
                double timeNow = runtime.time();
                velocity1 = (pos1 - lastPos1) / (timeNow - lastTime);
                velocity2 = (pos2 - lastPos2) / (timeNow - lastTime);
                error1 = desiredVelocity - velocity1;
                error2 = desiredVelocity - velocity2;
                double syncAdjust1 = kP1 * error1;
                double syncAdjust2 = kP2 * error2;
                integral_sum1 = integral_sum1 + (error1 * (timeNow - lastTime));
                integral_sum2 = integral_sum2 + (error2 * (timeNow - lastTime));

                double integral_term1 = Ki1 * integral_sum1;
                double integral_term2 = Ki2 * integral_sum2;

                if ((timeNow - lastTime) > 0) {
                    derivative1 = (error1 - previous_error1) / (timeNow - lastTime);
                    derivative2 = (error2 - previous_error2) / (timeNow - lastTime);
                }
                else {
                    derivative1 = 0;
                    derivative2 = 0;
                }
                double derivative_term1 = Kd1 * derivative1;
                double derivative_term2 = Kd2 * derivative2;

                output_power1 = syncAdjust1 + integral_term1 + derivative_term1;
                output_power2 = syncAdjust2 + integral_term2 + derivative_term2;

                if (output_power1 > 0.8 ) {
                    output_power1 = 0.8;
                }
                if (output_power2 > 0.8 ) {
                    output_power2 = 0.8;
                }

                if (output_power1 < 0) {
                    output_power1 = 0;
                }
                if (output_power2 < 0) {
                    output_power2 = 0;
                }

                shooter1.setPower(output_power1);
                shooter2.setPower(output_power2);
                previous_error1 = error1;
                previous_error2 = error2;
                lastPos1 = pos1;
                lastPos2 = pos2;
                lastTime = timeNow;

                if (velocity1 >= desiredVelocity && velocity2 >= desiredVelocity ) {
                    shooterServo.setPosition(1);
                }
            } else {
                shooter1.setPower(0);
                shooter2.setPower(0);
            }
            // --- Telemetry + Dashboard Data ---
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Shooter1 Pos", shooter1.getCurrentPosition());
            telemetry.addData("Shooter2 Pos", shooter2.getCurrentPosition());
            telemetry.addData("Shooter(1-2) Delta", shooter1.getCurrentPosition() - shooter2.getCurrentPosition());
            telemetry.addData("Shooter1 Power", shooter1.getPower());
            telemetry.addData("Shooter2 Power", shooter2.getPower());
            telemetry.addData("Desired Velocity", desiredVelocity);
            telemetry.addData(" Velocity 1", velocity1);
            telemetry.addData("Velocity 2", velocity2);
            telemetry.addData("Velocity 2", velocity2);
            telemetry.addData("error 1", error1);
            telemetry.addData("error 2", error2);
//            telemetry.addData("Lift Servo", liftServo.getPosition());
            telemetry.update();
        }
    }
}