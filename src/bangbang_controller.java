package wallFollower;
import lejos.hardware.motor.*;
import lejos.robotics.RegulatedMotor;

public class BangBangController implements UltrasonicController{
	private final int bandCenter, bandwidth;
	private final int motorLow, motorHigh;
	private int distance, filterControl, FILTER_OUT = 30; 
	private EV3LargeRegulatedMotor leftMotor, rightMotor;
	private EV3LargeRegulatedMotor usMotor;
	
	public BangBangController(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor,
							  int bandCenter, int bandwidth, int motorLow, int motorHigh, EV3LargeRegulatedMotor usMotor) {
		//Default Constructor
		this.bandCenter = bandCenter;
		this.bandwidth = bandwidth;
		this.motorLow = motorLow;
		this.motorHigh = motorHigh;
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		this.usMotor = usMotor;
		leftMotor.setSpeed(motorHigh);				// Start robot moving forward
		rightMotor.setSpeed(motorHigh);
		leftMotor.forward();
		rightMotor.forward();
		// This motor is used to set the sensor at a precise 45 degree angle and that
		// happens at a speed of 700
		usMotor.setSpeed(700);
		usMotor.rotateTo(45);
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
		
		int distError = (bandCenter - distance);
		// Within limits so no change to speed happens and robot continues to move forward 
		if (Math.abs(distError) <= bandwidth) {	
			// Start moving forward
			leftMotor.setSpeed(motorHigh);		
			rightMotor.setSpeed(motorHigh);
			leftMotor.forward();
			rightMotor.forward();				
		}
		// Too close to the wall
		else if (distError > 0) {			
			// Being too close to the wall, and since our sensor is on the right side, the right motor speed increases
			// greatly to avoid collision with the wall/block while the left motor speed decreases by a smaller amount
			// to avoid excessive rotation
			leftMotor.setSpeed(motorHigh - 130);
			rightMotor.setSpeed(motorHigh + 400);
			leftMotor.forward();
			rightMotor.forward();				
		}
		// Too far from the wall
		else if (distError < 0) {  
			// Being too far from the wall, the left motor speed increases with a small amount of 100 to avoid overshooting
			// and hitting the wall, while the right motor speed decreases with an even smaller amount of 50 to allow
			// for a smooth turn
			leftMotor.setSpeed(motorHigh + 100);
			rightMotor.setSpeed(motorHigh - 50 );
			leftMotor.forward();
			rightMotor.forward();
		}
	}

	@Override
	public int readUSDistance() {
		return this.distance;
	}
}
