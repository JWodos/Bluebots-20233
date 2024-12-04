package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Autonomous Mode", group = "Concept")
public class BBAutoMode1 extends LinearOpMode {

    private DcMotor FLW;
    private DcMotor FRW;
    private DcMotor BLW;
    private DcMotor BRW;

    private DcMotor LWD;
    private DcMotor RWD;
    private DcMotor ARM;

    private int counter;
    private ElapsedTime runtime = new ElapsedTime();

    static final double COUNTS_PER_MOTOR_REV = 5267.65;
    static final double WHEEL_DIAMETER_INCHES = 4.094; // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV) / (WHEEL_DIAMETER_INCHES * 3.1415);

    @Override
    public void runOpMode() {
        FLW = hardwareMap.get(DcMotor.class, "FLW");
        FRW = hardwareMap.get(DcMotor.class, "FRW");
        BLW = hardwareMap.get(DcMotor.class, "BLW");
        BRW = hardwareMap.get(DcMotor.class, "BRW");

        LWD = hardwareMap.get(DcMotor.class,"LWD");
        RWD = hardwareMap.get(DcMotor.class,"RWD");

        LWD.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LWD.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        RWD.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RWD.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        encoderDrive(0.1,  5,  5, 5.0);
    }

    public void encoderDrive(double speed, double leftInches, double rightInches, double timeoutS) {
        int newLeftTarget;
        int newRightTarget;
        if(opModeIsActive()){
            newLeftTarget = FLW.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = FRW.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            FLW.setTargetPosition(newLeftTarget);
            FRW.setTargetPosition(newRightTarget);

            FLW.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            FRW.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();
            FLW.setPower(Math.abs(speed));
            FRW.setPower(Math.abs(speed));

            while (opModeIsActive() && (runtime.seconds() < timeoutS) && (FLW.isBusy() && FRW.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Running to",  " %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Currently at",  " at %7d :%7d",
                        FLW.getCurrentPosition(), FRW.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            FLW.setPower(0);
            FRW.setPower(0);

            // Turn off RUN_TO_POSITION
            FLW.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            FRW.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);   // optional pause after each move.
        }
    }
}
