package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@TeleOp(name = "AprilTag Webcam Example", group = "Example")
public class AprilTagWebcamExample extends OpMode {

    private final AprilTagWebcam aprilTagWebcam = new AprilTagWebcam();

    // PERSONALIZE: Change this to the AprilTag ID you want to track
    private static final int TARGET_TAG_ID = 20;

    @Override
    public void init() {
        telemetry.addLine("Initializing AprilTag webcam...");
        telemetry.update();

        aprilTagWebcam.init(hardwareMap, telemetry);

        telemetry.addLine("Initialization complete!");
        telemetry.update();
    }

    @Override
    public void loop() {
        aprilTagWebcam.update();

        AprilTagDetection targetTag = aprilTagWebcam.getTagBySpecificId(TARGET_TAG_ID);

        telemetry.addLine("----- AprilTag Status -----");

        if (targetTag != null) {
            telemetry.addLine("Tag FOUND!");
            aprilTagWebcam.displayDetectionTelemetry(targetTag);
        } else {
            telemetry.addLine("Tag NOT detected.");
        }

        telemetry.update();
    }

    @Override
    public void stop() {
        aprilTagWebcam.stop();
    }
}
