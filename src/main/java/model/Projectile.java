package model;

import view.GameMain;
//import view.Sprite;
import view.SpriteBackup;

public class Projectile extends SpriteBackup {
    private int damage;
    private int speed = 2;
    private int size;
    private int direction;
    private boolean hit = false;
    private int xPosition = 200;
    private int yPosition = 100;

    public Projectile(int damage, int direction){
        this.damage = damage;
        size = 2;
    }

    public void move() {
        if (!hit) {
            xPosition += speed * direction;
            hitCheck();
        }
    }

    public void hitCheck() {
        for (int i = 0; i < GameMain.units.size(); i++) {

            if (this.collides(GameMain.units.get(i))) {

                GameMain.units.get(i).takeDamage(damage);
                hit = true;
                System.out.println("Osu!");
            }
        }
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }
}
