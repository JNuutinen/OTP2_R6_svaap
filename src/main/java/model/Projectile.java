package model;

import javafx.scene.image.Image;
import view.GameMain;
import model.Sprite;


public class Projectile extends Sprite{
    private int damage;
    private int speed = 2;
    private int size;
    private int direction;
    private boolean hit = false;
    private int xPosition = 100;
    private int yPosition = 100;

    public Projectile(int damage, int direction){
        Image projectileImage = new Image("/images/projectile_ball_small_cyan.png");
        this.setImage(projectileImage);
        this.damage = damage;
        setVelocity(speed);
        setPosition(xPosition, yPosition);
        setDirection(0);
        size = 2;
        setIsMoving(true);
        GameMain.projectiles.add(this);

    }

    public void move() {
        if (!hit) {
            update();
            hitCheck();
        }
    }

    public void hitCheck() {
        for (Unit unit : GameMain.units) {
            /*
            if (this.collides(unit)) {
                unit.takeDamage(damage);
                hit = true;
                GameMain.projectiles.remove(this);
                System.out.println("Osu!");
            }
            */
        }
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }
}
