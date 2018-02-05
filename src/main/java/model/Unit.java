package model;

import java.util.ArrayList;

/**
 * Lisää spriteen avaruusalukselle ominaisia piirteitä
 */
public class Unit extends Sprite {
    /**
     * Yksikön hitpointsit
     */
    private int hp;

    //private Component components[];

    /**
     * Yksikön taso
     */
    private int level;


    ArrayList<Component> components = new ArrayList<>();

    public Unit() {
        hp = 9000;
        level = 9000;
    }

    /**
     * Ampuu yksikön pääaseella
     */
    public void shootPrimary() {

        System.out.println("pew pew");
    }

    /**
     * Ampuu yksikön toisella aseella
     */
    public void shootSecondary() {
        System.out.println("pew");
    }

    /**
     * Yksikkö ottaa vahinkoa
     *
     * @param damage Vahinkomäärä, jonka yksikkö ottaa
     */
    public void takeDamage(int damage) {
        hp =- damage;
    }

    public void move(double direction) {
        this.setIsMoving(true);
        this.setDirection(direction);

        for (Component component : components) {
            component.setIsMoving(true);
            component.setDirection(direction);
        }
    }

    public void stopMoving() {
        this.setIsMoving(false);
        for (Component component : components) {
            component.setIsMoving(false);
        }
    }
}
