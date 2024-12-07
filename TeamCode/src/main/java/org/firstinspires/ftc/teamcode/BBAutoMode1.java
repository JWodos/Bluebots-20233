package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous()
public class BBAutoMode1 extends LinearOpMode {

    private DcMotor FLW;
    private DcMotor FRW;
    private DcMotor BLW;
    private DcMotor BRW;

    private DcMotor RWD;
    private DcMotor ARM;
    private Servo Wrist;
    private Servo Claw;

    private int counter;
    private int armCounter;
    private boolean start;

    static final double COUNTS_PER_MOTOR_REV = 5267.65;
    static final double WHEEL_DIAMETER_INCHES = 4.094; // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV) / (WHEEL_DIAMETER_INCHES * 3.1415);

    @Override
    public void runOpMode() {
        FLW = hardwareMap.get(DcMotor.class, "FLW");
        FRW = hardwareMap.get(DcMotor.class, "FRW");
        BLW = hardwareMap.get(DcMotor.class, "BLW");
        BRW = hardwareMap.get(DcMotor.class, "BRW");

        RWD = hardwareMap.get(DcMotor.class,"RWD");
        Wrist = hardwareMap.get(Servo.class,"Wrist");
        Claw = hardwareMap.get(Servo.class,"Claw");
        ARM = hardwareMap.get(DcMotor.class,"ARM");
        start = true;

        ARM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        ARM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        if(opModeIsActive()) {

            Claw.setPosition(1);
            Wrist.setPosition(0);
            move(1);
            arm(0.5);

            //arm(-1);
            //move(-1);
            //strafeRight(30);
        }
    }

    public void move(double inches) {

        double count = inches;
        count*=4500;

        while(count > 0) {
            FLW.setPower(0.5);
            FRW.setPower(0.5);
            BLW.setPower(-0.5);
            BRW.setPower(0.5);
            count--;
        }

        FLW.setPower(0);
        FRW.setPower(0);
        BLW.setPower(0);
        BRW.setPower(0);
    }

    public void arm(double armPower){

        double Rposition = RWD.getCurrentPosition();

        double CPR = 10766;
        double revolutions = Rposition/CPR;
        double rad = revolutions * 2;

        double armPosition = -ARM.getCurrentPosition();
        double cosRad = Math.cos(Math.abs(rad));
        double limitLength = (34.5/cosRad)*55;

        if(limitLength>=3500 || rad >.4){
            limitLength=3500;
        }

        while(armPosition<limitLength){
            armPosition = -ARM.getCurrentPosition();
            ARM.setPower(-armPower);
        }

        ARM.setPower(0);
    }
}
