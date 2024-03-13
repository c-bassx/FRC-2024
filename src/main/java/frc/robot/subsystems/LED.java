package frc.robot.subsystems;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class LED extends SubsystemBase {
    // Enumeration for LED states
    public enum LEDState {
        WHITE, BLACK, YELLOW, LIGHT_BLUE, ORANGE, RED, GREEN, DARK_BLUE // Add more states here
    }

    // Map to store the commands corresponding to each LED state
    private Map<LEDState, InstantCommand> commands = new HashMap<>();

    private AddressableLED strip;
    private AddressableLEDBuffer buffer; // Buffer to store LED data
    private LEDState state = LEDState.BLACK; // Default state

    /**
     * Constructor for the LED subsystem
     * Initializes the LED strip and its buffer, and sets up commands for each state
     */
    public LED() {
        strip = new AddressableLED(Constants.LED.port);
        buffer = new AddressableLEDBuffer(Constants.LED.length);
        strip.setLength(buffer.getLength());

        // Initialize commands for changing LED colors       
        commands.put(LEDState.WHITE, new InstantCommand(() -> setLED(75, 75, 75), this));
        commands.put(LEDState.BLACK, new InstantCommand(() -> setLED(0, 0, 0), this));
        // Add more commands here
        commands.put(LEDState.YELLOW, new InstantCommand(() -> setLED(248, 211, 7)));
        commands.put(LEDState.LIGHT_BLUE, new InstantCommand(() -> setLED(69, 146, 213)));
        commands.put(LEDState.ORANGE, new InstantCommand(() -> setLED(254, 153, 0)));
        commands.put(LEDState.RED, new InstantCommand(() -> setLED(255, 0, 0)));
        commands.put(LEDState.GREEN, new InstantCommand(() -> setLED(0, 255, 0)));
        commands.put(LEDState.DARK_BLUE, new Instant Command(() -> setLED(2, 11, 150)));

    }

    /**
     * Sets the current state of the LED
     * If the state is different from the current state, the corresponding command is scheduled
     * 
     * @param state The new state of the LED.
     */
    public void setState(LEDState state) {
        if (state.equals(this.state)) return;
        getStateCommand().cancel();
        this.state = state;
        startLED();
    }

    /**
     * Retrieves the command associated with the current LED state
     * 
     * @return The command associated with the current LED state
     */
    private Command getStateCommand() {
        return commands.get(state);
    }

    /**
     * Starts the LED strip and schedules the command for the current state
     */
    public void startLED() {
        strip.start();
        getStateCommand().schedule();
    }

    /**
     * Stops the LED strip and cancels the command for the current state
     * Resets the LED to black (off)
     */
    public void stopLED() {
        getStateCommand().cancel();
        setLED(0, 0, 0); // Reset to black (off)
        strip.stop();
    }

    /**
     * Sets the color of the LED strip
     * 
     * @param r The red component of the color
     * @param g The green component of the color
     * @param b The blue component of the color
     */
    private void setLED(int r, int g, int b) {
        for (int i = 0; i < buffer.getLength(); i++) 
            buffer.setRGB(i, r, g, b);
        strip.setData(buffer);
    }
}
