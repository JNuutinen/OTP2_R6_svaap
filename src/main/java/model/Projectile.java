package model;

import javafx.scene.image.Image;
import view.GameMain;
import model.Sprite;

import static view.GameMain.WINDOW_HEIGHT;
import static view.GameMain.WINDOW_WIDTH;

public class Projectile extends Sprite implements Updateable{
    private int damage;
    private boolean hit = false;
    private int speed = 300;

    public Projectile(int damage, Unit shooter){
        Image projectileImage = new Image("/images/projectile_ball_small_cyan.png");
        this.setImage(projectileImage);
        this.damage = damage;
        double xPos, yPos;
        if (shooter instanceof Player) {
            xPos = shooter.getXPosition() + 50;
            setPosition(xPos, shooter.getYPosition());
            setDirection(0);
        } else if (shooter instanceof Enemy) {
            xPos = shooter.getXPosition() - 50;
            setPosition(xPos, shooter.getYPosition());
            setDirection(180);
        }
        setVelocity(speed);
        setIsMoving(true);
        GameLoop.queueUpdateable(this);
    }

    @Override
    public void update(double deltaTime){
        // chekkaa menikö ulos ruudulta
        if (getXPosition() < -100
                || getXPosition() > WINDOW_WIDTH+200
                || getYPosition() < -100
                || getYPosition() > WINDOW_HEIGHT+100) {
            GameLoop.removeUpdateable(this);
        } else {
            move(deltaTime);
        }
    }

    public void move(double deltaTime) {
        if (!hit) {
            moveStep(deltaTime);
            hitCheck();
        }
    }

    public void hitCheck() {
        for (Unit unit : GameMain.units) {
            if (this.collides(unit)) {
                unit.takeDamage(damage);
                hit = true;
                GameLoop.removeUpdateable(this);
                System.out.println("Osu!");
            }
        }
    }
}
