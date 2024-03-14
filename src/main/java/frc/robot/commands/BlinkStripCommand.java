package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.LED;
import frc.robot.subsystems.LED.LEDState;

public class BlinkStripCommand extends Command {
    protected final LED led;
    protected final LEDStateSupplier supplier;
    protected final long delay;
    protected long lastBlink;
    protected boolean isOn;

    public BlinkStripCommand(LEDStateSupplier supplier, LED led, long delay) {
        this.supplier = supplier;
        this.led = led;
        this.delay = delay;
    }

    public BlinkStripCommand(LEDState state, LED led, long delay) {
        this(() -> state, led, delay);
    }

    @Override
    public void initialize() {
        lastBlink = System.currentTimeMillis();
        isOn = false;
    }

    @Override
    public void execute() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBlink >= delay) {
            lastBlink = currentTime;
            isOn = !isOn;
            led.setState(isOn ? supplier.getState() : LEDState.BLACK);
        }
    }

    @FunctionalInterface
    public static interface LEDStateSupplier {
        public LEDState getState();
    }
}
