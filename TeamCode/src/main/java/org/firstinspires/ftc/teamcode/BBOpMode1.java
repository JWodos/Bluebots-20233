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


    private DcMotor VLS;
    private DcMotor HLS;
    private Servo Claw;
    private Servo Wrist;

    @Override
    public void runOpMode() {
        FLW = hardwareMap.get(DcMotor.class, "FLW");
        FRW = hardwareMap.get(DcMotor.class, "FRW");
        BLW = hardwareMap.get(DcMotor.class, "BLW");
        BRW = hardwareMap.get(DcMotor.class, "BRW");

        VLS = hardwareMap.get(DcMotor.class,"VLS");
        HLS = hardwareMap.get(DcMotor.class,"HLS");
        Claw = hardwareMap.get(Servo.class,"Claw");
        Wrist = hardwareMap.get(Servo.class,"Wrist");

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

            telemetry.addData("Outtake Arm Motor Power", VLS.getPower());
            telemetry.addData("Intake Arm Motor Power", HLS.getPower());
            telemetry.addData("Outtake Claw Servo Position", Claw.getPosition());
            telemetry.addData("Intake Claw Servo Position", Wrist.getPosition());

            telemetry.addData("Status", "Running");
            telemetry.update();
        }
    }

    public void moveWheels(Gamepad movepad){
        double drivePower = 0;
        double strafePower = 0;
        double turnPower = 0;

        // moves the robot's (wheel) motors forward and back using the game pad 1 left joystick
        drivePower = -movepad.left_stick_y;

        // moves the robot's (wheel) motors left and right using the game pad 1 left joystick
        strafePower = movepad.left_stick_x;

        // turns the robot's (wheel) motors left and right using the game pad 1 right joystick
        turnPower = movepad.right_stick_x;

        //if drive
        if(drivePower != 0){
            /*
            //also turn
            if(turnPower != 0){
                //Drive and Turn
                FRW.setPower(-drivePower);
                FLW.setPower(turnPower);
                BRW.setPower(-drivePower);
                BLW.setPower(turnPower);
                //or strafe
            } else if(strafePower  != 0){
                // ** recheck this
                //Drive and Strafe
                FLW.setPower(drivePower);
                FRW.setPower(-strafePower);
                BLW.setPower(-strafePower);
                BRW.setPower(drivePower);
                //only driving
            }
             else {
             */
                // just driving
                FRW.setPower(drivePower);
                FLW.setPower(-drivePower);
                BRW.setPower(drivePower);
                BLW.setPower(-drivePower);
            //}


            //not driving
            //only turns
        } else if(turnPower != 0){
            // just turning
            FLW.setPower(turnPower);
            FRW.setPower(turnPower);
            BLW.setPower(turnPower);
            BRW.setPower(turnPower);
            //only strafe
        } else if(strafePower != 0){
            // just strafing
            FLW.setPower(strafePower);
            FRW.setPower(strafePower);
            BLW.setPower(strafePower);
            BRW.setPower(strafePower);
            //not doing anything
        } else {
            FRW.setPower(0);
            FLW.setPower(0);
            BRW.setPower(0);
            BLW.setPower(0);
        }

        /* vv things i tried but couldn't figure out vv

        boolean motorsPowered = false;

        // basically, if none of the game pad 1 joysticks are being used, this means the robot's
        // wheels shouldn't be moving, meaning those motors should return to a power of 0
        if(drivePower == 0 && strafePower == 0 && turnPower == 0){
            motorsPowered = false;
        } else {
            motorsPowered = true;
        }

        // gives different power values to the motors (depending on the action being done) if the
        // joysticks are being used
        if(motorsPowered){
            // driving and strafing
            if(drivePower != 0 && strafePower != 0){
                // im not exactly sure if this will work, i just copied it from last year's code
                FRW.setPower(-strafePower);
                FLW.setPower(drivePower);
                BRW.setPower(drivePower);
                BLW.setPower(-strafePower);
                // just driving
            } if(drivePower != 0){
                FLW.setPower(drivePower);
                FRW.setPower(drivePower);
                BLW.setPower(drivePower);
                BRW.setPower(drivePower);
            }
        } else {
            FLW.setPower(0);
            FRW.setPower(0);
            BLW.setPower(0);
            BRW.setPower(0);
        }
        */
    }

    public void moveArm(Gamepad armpad){
        // ** the functions below are for the polymorphism bot **
        // presets servo positions
        Claw.setPosition(0);
        Wrist.setPosition(0);

        // powers the robot's (arm) motors using the game pad 2 left joystick
        // (this will make the arm's motor power vary depending on how much you're using the joystick.
        // this can be changed later if we want the motor to have a constant power no matter what
        // value the joystick is giving)
        // linear slide(s) for outtake arm
        // (provides a threshold for deactivating motor power since joystick may not be at exactly 0)
        if(armpad.left_stick_y > 0.25 || armpad.left_stick_y < -0.25) {
            VLS.setPower(-armpad.left_stick_y);
        } else {
            VLS.setPower(0);
        }

        // same logic for intake and outtake linear slides
        if(armpad.right_stick_y > 0.25 || armpad.right_stick_y < -0.25) {
            HLS.setPower(-armpad.right_stick_y);
        } else {
            HLS.setPower(0);
        }

        // uses the gamepad's X button as a switch to tilt and un-tilt the outtake's "claw's" bucket
        // (1 = tilt, 0 = not tilt)
        if(armpad.right_trigger>0.5) {
            if(Claw.getPosition() < 0.5) {
                Claw.setPosition(1);
            } else {
                Claw.setPosition(0);
            }
        }

        // uses the gamepad's circle button as a switch to open and unopen the intake's claw
        // (1 = open, 0 = closed)
        if(armpad.left_trigger>0.5) {
            if(Wrist.getPosition() < 0.5) {
                Wrist.setPosition(1);
            } else {
                Wrist.setPosition(0);
            }
        }

        // pressing the triangle to trigger whatever actions will happen to hang/climb
        if(armpad.triangle) {
            // trigger hanging maneuver
        }
    }
}