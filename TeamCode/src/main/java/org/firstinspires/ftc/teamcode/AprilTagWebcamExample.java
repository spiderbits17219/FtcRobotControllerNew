package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

/**
 * Example OpMode that uses an AprilTag webcam.
 * It initializes the webcam, updates detections,
 * and displays info about a specific tag (ID 20).
 */
@TeleOp(name = "AprilTag Webcam Example", group = "Example")
public class AprilTagWebcamExample extends OpMode {

    private AprilTagWebcam aprilTagWebcam = new AprilTagWebcam();

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
        // Update AprilTag detections each loop
        aprilTagWebcam.update();

        // Try to find a tag with ID 20
        AprilTagDetection id20 = aprilTagWebcam.getTagBySpecificId(20);

        // Display detection info
        aprilTagWebcam.displayDetectionTelemetry(id20);
    }
}
