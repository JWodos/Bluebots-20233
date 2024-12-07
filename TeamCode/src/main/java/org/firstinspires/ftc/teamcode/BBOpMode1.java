package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class BBOpMode1 extends LinearOpMode {
    private DcMotor FLW;
    private DcMotor FRW;
    private DcMotor BLW;
    private DcMotor BRW;

    private DcMotor RWD;
    private DcMotor ARM;
    private Servo Claw;
    private Servo Wrist;
    private int upPos;
    private int downPos;

    private float startingPos;

    private int counter;
    private boolean start;

    @Override
    public void runOpMode() {
        FLW = hardwareMap.get(DcMotor.class, "FLW");
        FRW = hardwareMap.get(DcMotor.class, "FRW");
        BLW = hardwareMap.get(DcMotor.class, "BLW");
        BRW = hardwareMap.get(DcMotor.class, "BRW");

        RWD = hardwareMap.get(DcMotor.class,"RWD");
        Claw = hardwareMap.get(Servo.class,"Claw");
        Wrist = hardwareMap.get(Servo.class,"Wrist");
        ARM = hardwareMap.get(DcMotor.class,"ARM");
        start = true;

        RWD.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RWD.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        ARM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        ARM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            moveWheels(gamepad1);
            moveArm(gamepad2);

            telemetry.addData("FLW Motor Power", FLW.getPower());
            telemetry.addData("FRW Motor Power", FRW.getPower());
            telemetry.addData("BLW Motor Power", BLW.getPower());
            telemetry.addData("BRW Motor Power", BRW.getPower());

            telemetry.addData("Status", "Running");
            telemetry.update();
        }
    }

    public void moveWheels(Gamepad movepad) {

        //right wheels are backward
        // moves the robot's (wheel) motors forward and back using the game pad 1 left joystick
        boolean driveFor = movepad.dpad_up;
        boolean driveBack = movepad.dpad_down;
        int driveF10 = driveFor ? 1 : 0;
        int driveB10 = driveBack ? 1 : 0;
        double drivePower = driveF10 - driveB10;
        telemetry.addData("driveFor", driveFor);
        telemetry.addData("driveBack", driveBack);

        // moves the robot's (wheel) motors left and right using the game pad 1 left joystick
        boolean strafeRight = movepad.dpad_left;
        boolean strafeLeft = movepad.dpad_right;
        int strafeL10 = strafeLeft ? 1 : 0;
        int strafeR10 = strafeRight ? 1 : 0;
        double strafePower = strafeL10 - strafeR10;
        telemetry.addData("strafeRight", strafeRight);
        telemetry.addData("strafeLeft", strafeLeft);

        // turns the robot's (wheel) motors left and right using the game pad 1 right joystick
        double turnPower = movepad.right_stick_x;
        telemetry.addData("turnPower", turnPower);

        telemetry.addData("drivePower", drivePower);
        telemetry.addData("strafePower", strafePower);
        telemetry.addData("turnPower", turnPower);

        boolean one = true;
        if (drivePower != 0) {

            if (counter > 10) {
                one = false;
            }

            if (one) {
                counter++;
            }

            FRW.setPower(drivePower * 0.1 * counter);
            FLW.setPower(drivePower * 0.1 * counter);
            BRW.setPower(drivePower * 0.1 * counter);
            BLW.setPower(-drivePower * 0.1 * counter);

        } else if (strafePower != 0) {
            if (counter > 10) {
                one = false;
            }

            if (one) {
                counter++;
            }

            //-1 to strafe right
            FRW.setPower(-strafePower * 0.1 * counter);
            FLW.setPower(strafePower * 0.1 * counter);
            BRW.setPower(strafePower * 0.1 * counter);
            BLW.setPower(strafePower * 0.1 * counter);

        } else if (turnPower != 0) {
            if (counter > 10) {
                one = false;
            }
            if (one) {
                counter++;
            }

            FRW.setPower(-turnPower * 0.1 * counter);
            FLW.setPower(turnPower * 0.1 * counter);
            BRW.setPower(-turnPower * 0.1 * counter);
            BLW.setPower(-turnPower * 0.1 * counter);

        } else {
            counter = 0;
            FRW.setPower(0);
            FLW.setPower(0);
            BRW.setPower(0);
            BLW.setPower(0);
        }
    }

    public void moveArm(Gamepad armpad){
        if(start) {
            startingPos = RWD.getCurrentPosition();
            upPos = Math.round(startingPos)+1025;
            downPos = Math.round(startingPos)-1575;
            start = false;
        }

        double Rposition = RWD.getCurrentPosition();
        double CPR = 10766;
        double revolutions = (Rposition+1675)/CPR;
        double rad = revolutions * 2;

        double armPosition = -ARM.getCurrentPosition();
        double cosRad = Math.cos(Math.abs(rad*Math.PI));
        double limitLength = (34.5/cosRad)*55;

        if(limitLength>=3500|| rad>.4){
            limitLength=3500;
        }

        telemetry.addData("ARM Position", armPosition);
        telemetry.addData("cosAng", cosRad);
        telemetry.addData("ARM limitLength", limitLength);
        telemetry.addData("RWD Position", Rposition);
        telemetry.addData("WD Rad", rad);

        double wormdrivePower = -armpad.left_stick_y;
        double armPower = -armpad.right_stick_y;

        if(wormdrivePower>0.25 && Rposition<upPos) {
            //test wormdrive (how much power give 90 degree --> assign that much power
            RWD.setPower(wormdrivePower);
        }else if(wormdrivePower<-0.25 && Rposition>downPos){
            RWD.setPower(wormdrivePower);
        }else{
            RWD.setPower(0);
        }

        //arm
        if(armPower> 0.25 && armPosition<limitLength){
            ARM.setPower(-armPower);
        } else if(armPower<-0.25 && armPosition>0){
            ARM.setPower(-armPower);
        }else{
            ARM.setPower(0);
        }

        if(armpad.right_trigger > 0) {
            Claw.setPosition(1);
        } else {
            Claw.setPosition(0.5);
        }

        if(armpad.left_trigger > 0) {
            Wrist.setPosition(1);
        } else {
            Wrist.setPosition(0);
        }

        telemetry.addData("wormdrive power", -wormdrivePower);
        telemetry.addData("armPower", armPower);
        telemetry.addData("first time", start);
        telemetry.addData("wristPos", Wrist.getPosition());
        telemetry.addData("clawPos", Claw.getPosition());
        //telemetry.addData("wormdrive power", -wormdrivePower);

    }
}