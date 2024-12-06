package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous()
public class BBAutoMode1 extends LinearOpMode {

    private DcMotor FLW;
    private DcMotor FRW;
    private DcMotor BLW;
    private DcMotor BRW;

    private DcMotor LWD;
    private DcMotor RWD;
    private DcMotor ARM;

    private int counter;
    private ElapsedTime runtime;

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

        if(opModeIsActive()) {
            downPos();
            strafeRight(0.1, 18);
        }
    }

    public void rotate(double seconds){
        runtime = new ElapsedTime();

        while(runtime.seconds() < seconds) {
            FRW.setPower(-0.1);
            FLW.setPower(0.1);
            BRW.setPower(-0.1);
            BLW.setPower(-0.1);
        }

        FLW.setPower(0);
        FRW.setPower(0);
        BLW.setPower(0);
        BRW.setPower(0);

        runtime.reset();
    }

    public void strafeRight(double speed, double inches) {
        int FLWTarget;
        int FRWTarget;
        int BLWTarget;
        int BRWTarget;
        FLWTarget = FLW.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);
        FRWTarget = FRW.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);
        BLWTarget = BLW.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);
        BRWTarget = BRW.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);

        FLW.setTargetPosition(FLWTarget);
        FRW.setTargetPosition(FRWTarget);
        BLW.setTargetPosition(BLWTarget);
        BRW.setTargetPosition(BRWTarget);

        FLW.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        FRW.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BLW.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BRW.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        FLW.setPower(-Math.abs(speed));
        FRW.setPower(Math.abs(speed));
        BLW.setPower(-Math.abs(speed));
        BRW.setPower(-Math.abs(speed));

        while (opModeIsActive() && (FLW.isBusy() && FRW.isBusy())) {

                // Display it for the driver.
            telemetry.addData("Running to",  " %7d :%7d", inches,  speed);
            telemetry.update();
        }

        FLW.setPower(0);
        FRW.setPower(0);
        BLW.setPower(0);
        BRW.setPower(0);

        FLW.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        FRW.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BLW.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BRW.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        sleep(250);
    }

    public void downPos(){
        int wdUpPosition = 2400;
        //5degree
        int wdDownPosition = 100;
        int startPosition = 1125;

        double Rposition = RWD.getCurrentPosition();
        double CPR = 10766;
        double revolutions = Rposition/CPR;
        double angle = revolutions * 360;
        double wormdrivePower = -0.1;

        if(Rposition > wdUpPosition) {
            //test wormdrive (how much power give 90 degree --> assign that much power
            RWD.setPower(wormdrivePower);
        } else {
            RWD.setPower(0);
        }
        sleep(250);
    }

    public void pickUp(double extend) {
        double armPosition = ARM.getCurrentPosition();
        double armPower = 0.1;

        //arm
        if(armPosition < extend) {
            ARM.setPower(armPower);
        } else {
            ARM.setPower(0);
        }
    }
}
