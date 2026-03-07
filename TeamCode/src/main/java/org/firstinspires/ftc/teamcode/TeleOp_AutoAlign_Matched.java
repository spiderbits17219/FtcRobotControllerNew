package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
@TeleOp(name="TeleOp_AutoAlign_Matched")
public class TeleOp_AutoAlign_Matched extends LinearOpMode {
    DcMotor leftFront, leftRear, rightFront, rightRear;
    AprilTagWebcam aprilTagWebcam = new AprilTagWebcam();
    int targetTagId = 20; // YOUR TAG ID
    double kP = 0.02; // Controller constant
    @Override
    public void runOpMode() {
// Motors
        leftFront = hardwareMap.get(DcMotor.class, "frontLeft");
        leftRear = hardwareMap.get(DcMotor.class, "backLeft");
        rightFront = hardwareMap.get(DcMotor.class, "frontRight");
        rightRear = hardwareMap.get(DcMotor.class, "backRight");
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightRear.setDirection(DcMotor.Direction.REVERSE);
// Initialize the AprilTag webcam pipeline
        aprilTagWebcam.init(hardwareMap, telemetry);
        telemetry.addLine("Ready!");
        telemetry.update();
        waitForStart();
        while (opModeIsActive()) {
            aprilTagWebcam.update();
            double drive = -gamepad1.left_stick_y;
            double turn = gamepad1.left_stick_x;
            boolean autoAlign = gamepad1.a;
            if (autoAlign) {
                AprilTagDetection tag = aprilTagWebcam.getTagBySpecificId(targetTagId);
                if (tag != null) {
                    double error = tag.ftcPose.bearing; // correct for left/right angle
                    double rotatePower = kP * error;
                    rotatePower = Math.max(-0.4, Math.min(0.4, rotatePower));
// Rotate only
                    setPower(-rotatePower, -rotatePower, rotatePower, rotatePower);
                    telemetry.addLine("Auto Align: ON");
                    telemetry.addData("Bearing Error", error);
                } else {
                    telemetry.addLine("Tag not found");
                    stopDrive();
                }
            } else {
// Manual drive
                setPower(drive + turn, drive + turn, drive - turn, drive - turn);
            }
            telemetry.update();
        }
        aprilTagWebcam.stop();
    }
    private void setPower(double lf, double lr, double rf, double rr) {
        leftFront.setPower(lf);
        leftRear.setPower(lr);
        rightFront.setPower(rf);
        rightRear.setPower(rr);
    }
    private void stopDrive() {
        setPower(0, 0, 0, 0);
    }
}