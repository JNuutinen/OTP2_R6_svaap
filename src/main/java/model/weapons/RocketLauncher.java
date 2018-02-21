package model.weapons;

import controller.Controller;
import javafx.scene.paint.Color;
import model.Component;
import model.Unit;
import model.projectiles.Missile;

public class RocketLauncher extends Component implements Weapon {
    private static final int SPEED = 30;
    private static final int DAMAGE = 10;
    private static final double FIRE_RATE = 0.4;
    private static final Color COLOR = Color.PINK;

    private Controller controller;
    private Unit shooter;

    public RocketLauncher(Controller controller, Unit shooter, String shape, int size, int orientation, double xOffset,
                          double yOffset) {
        super(shape, size, orientation, COLOR, xOffset, yOffset);
        this.controller = controller;
        this.shooter = shooter;
    }

    @Override
    public double getFireRate() {
        return FIRE_RATE;
    }

    @Override
    public void shoot() {
        controller.addUpdateable(new Missile(controller, shooter, SPEED, DAMAGE));
    }
}
