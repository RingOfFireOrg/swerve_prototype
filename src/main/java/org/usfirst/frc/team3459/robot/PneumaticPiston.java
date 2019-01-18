package org.usfirst.frc.team3459.robot;
import edu.wpi.first.wpilibj.Solenoid;

public class PneumaticPiston {
    
    private Solenoid openSolenoid;
    private Solenoid closeSolenoid;

    private static boolean out;
    public PneumaticPiston() {
        openSolenoid = new Solenoid(0);
        closeSolenoid = new Solenoid(1);
    }

    public void out(){
        closeSolenoid.set(false);
        openSolenoid.set(true); 
        out = true;
    }
    public void in(){
        closeSolenoid.set(true);
        openSolenoid.set(false);
        out = false;
    }

    public boolean pusherOut(){
        return out;
    }
}