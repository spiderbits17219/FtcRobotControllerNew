package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp (name = "Mecanum Field Test")
public class MecanumFieldOrientatedOpMode extends OpMode {
    
    MecanumDrive drive = new MecanumDrive();
    double forward, strafe, rotate;

    @Override
    public void init () {
     drive.init(hardwareMap);

    }

      @Override
    public void loop(){
        forward = gamepad1.right_stick_y;
        strafe = gamepad1.right_stick_x;
        rotate = gamepad2.right_stick_x;

        drive.driveFieldRElative(forward, strafe, rotate);
      }
}