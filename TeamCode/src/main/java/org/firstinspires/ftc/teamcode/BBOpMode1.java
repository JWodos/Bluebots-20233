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
    private Servo Bucket;

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
        Bucket = hardwareMap.get(Servo.class,"Bucket");

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        Claw.setPosition(0);
        Wrist.setPosition(0);
        Bucket.setPosition(0.33);

        while (opModeIsActive()) {
            moveWheels(gamepad1);
            moveArm(gamepad2);

            telemetry.addData("FLW Motor Power", FLW.getPower());
            telemetry.addData("FRW Motor Power", FRW.getPower());
            telemetry.addData("BLW Motor Power", BLW.getPower());
            telemetry.addData("BRW Motor Power", BRW.getPower());

            telemetry.addData("Outtake Arm Motor Power", VLS.getPower());
            telemetry.addData("Intake Arm Motor Power", HLS.getPower());
            telemetry.addData("HLS Position", HLS.getCurrentPosition());
            telemetry.addData("VLS Position", VLS.getCurrentPosition());
            telemetry.addData("Outtake Claw Servo Position", Claw.getPosition());
            telemetry.addData("Intake Claw Servo Position", Wrist.getPosition());
            telemetry.addData("Bucket", Bucket.getPosition());

            telemetry.addData("Status", "Running");
            telemetry.update();
        }
    }

    public void moveWheels(Gamepad movepad){
        double drivePower = 0;
        double strafePower = 0;
        double turnPower = 0;

        // moves the robot's (wheel) motors forward and back using the game pad 1 left joystick
        drivePower = movepad.left_stick_y * 0.1;
        telemetry.addData("drivePower", drivePower);
        // moves the robot's (wheel) motors left and right using the game pad 1 left joystick
        strafePower = movepad.left_stick_x * 0.1;
        telemetry.addData("strafePower", strafePower);
        // turns the robot's (wheel) motors left and right using the game pad 1 right joystick
        turnPower = movepad.right_stick_x * 0.1;
        telemetry.addData("turnPower", turnPower);

        if(drivePower != 0||strafePower != 0||turnPower != 0){
            FLW.setPower(drivePower+turnPower+strafePower);
            FRW.setPower(drivePower-turnPower-strafePower);
            BLW.setPower(drivePower+turnPower-strafePower);
            BRW.setPower(drivePower-turnPower+strafePower);

            } else {

            FRW.setPower(0);
            FLW.setPower(0);
            BRW.setPower(0);
            BLW.setPower(0);
        }
    }

    public void moveArm(Gamepad armpad){

        if(armpad.left_stick_y > 0.25 || armpad.left_stick_y < -0.25) {
            VLS.setPower(-armpad.left_stick_y);
        } else {
            VLS.setPower(0);
        }

        if(armpad.right_stick_y > 0.25 || armpad.right_stick_y < -0.25) {
            HLS.setPower(-armpad.right_stick_y);
        } else {
            HLS.setPower(0);
        }

        if(armpad.right_trigger>0.5) {
            if(Claw.getPosition()>0) {
                Claw.setPosition(Claw.getPosition() - 0.01);
            }
        }

        if(armpad.right_bumper) {
            if(Claw.getPosition()<0.5) {
                Claw.setPosition(Claw.getPosition() + 0.01);
            }
        }

        //writs
        if(armpad.left_trigger>0.5) {
            if(Wrist.getPosition()>0) {
                Wrist.setPosition(Wrist.getPosition() - 0.01);
            }
        }

        if(armpad.left_bumper) {
            if(Wrist.getPosition()<1) {
                Wrist.setPosition(Wrist.getPosition() + 0.01);
            }
        }

        if(armpad.x) {
            Bucket.setPosition(0.1);
        }

        if(Bucket.getPosition()<0.33) {
            Bucket.setPosition(Bucket.getPosition() + 0.001);
        }

    }
}