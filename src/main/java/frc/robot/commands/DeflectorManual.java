package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.Constants.DeflectorConstants.DeflectorState;
import frc.robot.subsystems.Kinesthetics;

public class DeflectorManual extends Command{
    private Kinesthetics s_Kinesthetics;
    private DeflectorState deflectorState;

    public DeflectorManual(Kinesthetics k, DeflectorState d){
        s_Kinesthetics = k;
        deflectorState = d;
    }

    @Override
    public void execute(){
        s_Kinesthetics.setDeflectorState(deflectorState);
    }

    @Override
    public boolean isFinished(){
        return Math.abs(s_Kinesthetics.getDeflectorPos() - deflectorState.setPoint) < Constants.DeflectorConstants.deflectorTolerance;
    }

    @Override
    public void end(boolean isInterupted){
        if(isInterupted){
            s_Kinesthetics.setDeflectorState(DeflectorState.OFF);
        }
        super.end(isInterupted);
    }
}
