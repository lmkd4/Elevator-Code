package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.I2C;

import com.revrobotics.Rev2mDistanceSensor;
import com.revrobotics.Rev2mDistanceSensor.Port;
import com.revrobotics.Rev2mDistanceSensor.Unit;

public class Elevator extends SubsystemBase {
    
    // Motors
    private final CANSparkMax motor1;
    private final CANSparkMax motor2;

    // Encoder and PID controller
    private final RelativeEncoder encoder;
    private final SparkMaxPIDController pidController;
    
    public Rev2mDistanceSensor distanceSensor;

    // Constants (modify these based on your elevator design)
    private static final double kElevatorSpeed = 0.5;
    private static final double kP = 0.1;
    private static final double kI = 0.0;
    private static final double kD = 0.0;
    private static final double kMaxHeight = 100.0; // Example max position
    private static final double kMinHeight = 0.0;
    private static final double distTestHeight = -20;

    public Elevator(int motor1Port, int motor2Port) {
        motor1 = new CANSparkMax(motor1Port, MotorType.kBrushless);
        motor2 = new CANSparkMax(motor2Port, MotorType.kBrushless);

        distanceSensor = new Rev2mDistanceSensor(Port.kOnboard);

        // Encoder and PID controller from motor1
        encoder = motor1.getEncoder();
        pidController = motor1.getPIDController();
        
        
        // Configure PID coefficients
        pidController.setP(kP);
        pidController.setI(kI);
        pidController.setD(kD);
    }

    // Method to move the elevator to a target height
    public void setPosition(double position) {
        if (position > kMaxHeight) {
            position = kMaxHeight;
        } else if (position < kMinHeight) {
            position = kMinHeight;
        }
        pidController.setReference(position, CANSparkMax.ControlType.kPosition);
    }

    // Stops the elevator motor
    public void stop() {
        motor1.set(0);
    }

    public void moveUp() {
        motor1.set(-kElevatorSpeed);
        motor2.set(kElevatorSpeed);
    }

    public void moveDown() {
        motor1.set(kElevatorSpeed);
        motor2.set(-kElevatorSpeed);
    }



    @Override
    public void periodic() {
        // Display encoder and limit switch states on SmartDashboard
        SmartDashboard.putNumber("Elevator Position", encoder.getPosition());
        SmartDashboard.putNumber("Distance Sensor: ", distanceSensor.GetRange());
        // Stop the elevator if it reaches a limit switch

    }

    public boolean exampleCondition() {
        // Query some boolean state, such as a digital sensor.
        return false;
      }

    public boolean buttonReleased() {
        return false;
    }

    public Command motorUp() {
        return runOnce(() -> {
            motor1.set(0.2);
            motor2.set(-0.2);
        });
    }

    public Command motorDown() {
        return runOnce(() -> {
            motor1.set(-0.2);
            motor2.set(0.2);
        });
    }

    public Command motorStopUp() {
        return runOnce(() -> {
            motor1.set(0);
            motor2.set(0);
        });
    }

    public Command motorStopDown() {
        return runOnce(() -> {
            motor1.set(0);
            motor2.set(0);
        });
    }


}