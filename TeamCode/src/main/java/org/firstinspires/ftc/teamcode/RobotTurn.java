package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class RobotTurn {

    public void turn (HardwareMap hwMap, double rotationAngle) {
        // Initialize motors

           DcMotorEx rightFront;
           DcMotorEx leftFront;
           DcMotorEx rightBack;
           DcMotorEx leftBack;

           double y = -0.5;
           double x = 0.5;
           double rx = 0;

        rightFront = hwMap.get(DcMotorEx.class, "frontRight");
        leftFront = hwMap.get(DcMotorEx.class, "frontLeft");
        rightBack = hwMap.get(DcMotorEx.class, "backRight");
        leftBack = hwMap.get(DcMotorEx.class, "backLeft");

        // Rotate the movement direction counter to the bot's rotation
        double rotX = x * Math.cos(-rotationAngle) - y * Math.sin(-rotationAngle);
        double rotY = x * Math.sin(-rotationAngle) + y * Math.cos(-rotationAngle);

        rotX = rotX * 1.1;  // Counteract imperfect strafing

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio,
        // but only if at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
        double frontLeftPower = (rotY + rotX + rx) / denominator;
        double backLeftPower = (rotY - rotX + rx) / denominator;
        double frontRightPower = (rotY - rotX - rx) / denominator;
        double backRightPower = (rotY + rotX - rx) / denominator;

        leftFront.setPower(frontLeftPower);
        leftBack.setPower(backLeftPower);
      rightFront.setPower(frontRightPower);
       rightBack.setPower(backRightPower);

        }
    }