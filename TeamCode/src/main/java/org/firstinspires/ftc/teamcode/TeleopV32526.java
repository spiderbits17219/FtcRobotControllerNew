package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "TeleopV32526")
public class TeleopV32526 extends LinearOpMode {

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
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Reset encoders for intake
        intakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start
        waitForStart();
        runtime.reset();

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
            if (gamepad1.a) {
                intakeMotor.setPower(1.0);
            } else if (gamepad1.b) {
                intakeMotor.setPower(-1.0);
            } else {
                intakeMotor.setPower(0);
            }

            // Transfer Control (Y = push, X = pull)
            if (gamepad1.y) {
              transferMotor.setPower(1.0);
            } else if (gamepad1.x) {
              transferMotor.setPower(-1.0);
            } else {
              transferMotor.setPower(0);
            }

            // Shooter (A Button = spin up)

              if (gamepad2.a) {
                  shooter1.setPower(1.0);
                  shooter2.setPower(1.0);
              } else {
                  shooter1.setPower(0);
                  shooter2.setPower(0);
              }

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