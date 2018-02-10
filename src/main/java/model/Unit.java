package model;

import controller.Controller;

import java.util.ArrayList;

/**
 * Lisää spriteen avaruusalukselle ominaisia piirteitä
 */
public class Unit extends Sprite implements Updateable {
    /**
     * Kontrolleri
     */
    private Controller controller;
    /**
     * Yksikön hitpointsit
     */
    private int hp;

    /**
     * Yksikön taso
     */
    private int level;
    ArrayList<Component> components = new ArrayList<>();

    public Unit(Controller controller) {
        this.controller = controller;
        hp = 30;
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
        hp -= damage;
        if(hp <= 0){
            System.out.println("AI SAATANA");
            controller.removeUpdateable(this);
            if(this.getTag().equals("enemy")){

                System.out.println("Anna sitä hyvää");
                controller.addScore(100);
            }
        }
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

    public int getHp(){
        return hp;
    }

    public void setHp(int hp){
        this.hp = hp;
    }

    public void addHP(int hp){
        this.hp += hp;
    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void collides(Updateable collidingUpdateable) {

    }

    @Override
    public Updateable getUpdateable() {
        return null;
    }

    public void destroyThis(){
        controller.removeUpdateable(this);
    }

    public void setHP(int hp){
        this.hp = hp;
    }
}
