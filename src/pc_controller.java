package wallFollower;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class PController implements UltrasonicController {
	
	private final int bandCenter, bandwidth;
	private final int motorStraight = 200, FILTER_OUT = 30;
	private EV3LargeRegulatedMotor leftMotor, rightMotor;
	private int distance;
	private int filterControl;
	
	public PController(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor,
					   int bandCenter, int bandwidth) {
		//Default Constructor
		this.bandCenter = bandCenter;
		this.bandwidth = bandwidth;
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		leftMotor.setSpeed(motorStraight);					// Initialize motor rolling forward
		rightMotor.setSpeed(motorStraight);
		leftMotor.forward();
		rightMotor.forward();
		filterControl = 0;
	}
	
	@Override
	public void processUSData(int distance) {
		// This filter was provided by the TA on the discussion board
		if (distance >= 255 && filterControl < FILTER_OUT) {
			// bad value, do not set the distance variable, however do increment the
			// filter value
			filterControl++;
		} else if (distance >= 255) {
			// We have repeated large values, so there must actually be nothing
			// there: leave the distance alone
			this.distance = distance;
		} else {
			// distance went below 255: reset filter and leave
			// distance alone.
			filterControl = 0;
			this.distance = distance;
		}
		
		// There error is the distance wanted - the distance the robot is actually at
		int distError = bandCenter - distance;
		// The absolute value of our error is then multiplied by a constant, in this particular case 9
		// giving us the gain, the gain is used to modify the speed of the motors
		int gain = Math.abs(distError) * 9;
		// Adding the gain to our motorStraight speed ( 200 ) gives us the highSpeed		
		int highSpeed = motorStraight + gain;
		// Subtracting the gain from our motorStraight speed gives us lowSpeed
		int lowSpeed = motorStraight - gain;
		// A maximum value of highSpeed is set to 450, not allowing the robot to go faster
		// than this value
		if (highSpeed > 450) {
			highSpeed = 450;
		}
		// A minimum value of 100 is set to 100, not allowing the robot to go slower than
		// this value
		if ( lowSpeed < 100) {
			lowSpeed = 100;
		} 
		// If the error is within limits the robot will continue moving forwards
		if (Math.abs(distError) <= bandwidth) {	
			leftMotor.setSpeed(motorStraight);		
			rightMotor.setSpeed(motorStraight);
			leftMotor.forward();
			rightMotor.forward();				
		}
		// Too close to the wall 
		else if (distError > 2) {	
			// The left motor will go at a significantly lower speed compared to the right motor, this is
			// to avoid collisions with the wall/brick. The constants used were chosen after a numerous
			// number of trial and error.
			leftMotor.setSpeed( (int)( lowSpeed/1.5) );
			rightMotor.setSpeed( (int) ( highSpeed*2 ) );
			leftMotor.forward();
			rightMotor.forward();				
		}
		// Too far from wall
		else if (distError < -2) {  
			// The right motor will go at a significantly lower speed compared to the left motor, this is
			// to allow for a smooth turn. The constants used were chosen after a numerous
			// number of trial and error.
			leftMotor.setSpeed( (int) (highSpeed/1.5 ));
			rightMotor.setSpeed(  (int) ( lowSpeed*2 ) );
			leftMotor.forward();
			rightMotor.forward();
		}
	}

	
	@Override
	public int readUSDistance() {
		return this.distance;
	}

}
