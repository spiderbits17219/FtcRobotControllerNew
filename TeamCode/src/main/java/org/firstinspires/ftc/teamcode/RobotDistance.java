package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class RobotDistance {
    private DistanceSensor distance;

    public void init(HardwareMap hardwareMap){
        distance = hardwareMap.get(DistanceSensor.class, "sensor_distance");

    }
    public double getDistance(){
        return distance.getDistance(DistanceUnit.INCH);

    }
}
