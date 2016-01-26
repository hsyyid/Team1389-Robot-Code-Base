
package com.team1389.base;

import com.team1389.base.webserver.WebServer;

import edu.wpi.first.wpilibj.IterativeRobot;

/*
 * This class is the base of all of the code. Everything else is called from here.
 */

public abstract class Team1389RobotBase<IOLayoutType extends IO> extends IterativeRobot {
	
	private WebServer server;
	
	RobotCode<IOLayoutType> robotCode;
	
	private Mode mode;
	
   	public abstract RobotCode<IOLayoutType> getCode();
   	
    public void robotInit() {
    	robotCode = getCode();
    	robotCode.setup();

    	mode = Mode.DISABLED;
    	
    	//start webserver
    	server = new WebServer(robotCode);
    	server.start();
    	
    }
    
    @Override
    public void autonomousInit() {
    	mode = Mode.AUTON;
    	robotCode.getAutonomousBase().autonStart(robotCode.getIO());
    }
    private void disabledAuton() {
    	robotCode.getAutonomousBase().autonEnd();
	}


    @Override
    public void teleopInit() {
    	mode = Mode.TELEOP;
    	robotCode.getTeleopBase().start(robotCode.getIO());
    }
    @Override
    public void testInit(){
    	mode = Mode.TEST;
    }
    public void disabledInit(){
    	switch(mode){
		case AUTON:
			disabledAuton();
			break;
		case DISABLED:
			break;
		case TELEOP:
			disabledTeleop();
			break;
		case TEST:
			disabledTest();
			break;
    	}
    	mode = Mode.DISABLED;
    }

	private void disabledTeleop() {
		robotCode.getTeleopBase().disable();
	}

	private void disabledTest() {
    	//TODO cancel whatever command was running
	}

	/*Each of these functions is called periodically in their respective modes.
     * The scheduler needs to be run so that Commands which are scheduled will run.*/
    public void teleopPeriodic() {
    }
    public void autonomousPeriodic() {
    }
    public void testPeriodic() {
//        LiveWindow.run();//TODO figure out what this is, should it happen in other modes?
    }
	public void disabledPeriodic() {
	}
}
