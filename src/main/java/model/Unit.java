package model;

import controller.Controller;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import model.weapons.Weapon;

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

    /**
     * Komponenttilista
     */
    ArrayList<Component> components = new ArrayList<>();

    /**
     * Pääase
     */
    private Weapon primaryWeapon;

    /**
     * Toissijainen ase
     */
    private Weapon secondaryWeapon;

    public Unit(Controller controller) {
        this.controller = controller;
        // TODO: kovakoodattu hp
        hp = 30;
    }

    public void setPrimaryWeapon(Weapon primaryWeapon) {
        this.primaryWeapon = primaryWeapon;
    }

    public Weapon getPrimaryWeapon() {
        return primaryWeapon;
    }

    public void setSecondaryWeapon(Weapon secondaryWeapon) {
        this.secondaryWeapon = secondaryWeapon;
    }

    public Weapon getSecondaryWeapon() {
        return secondaryWeapon;
    }

    /**
     * Ampuu yksikön pääaseella
     */
    public void shootPrimary() {
        if (primaryWeapon != null) {
            primaryWeapon.shoot();
        } else {
            System.out.println(getTag() + ": No primary weapon set.");
        }
    }

    /**
     * Ampuu yksikön toisella aseella
     */
    public void shootSecondary() {
        if (secondaryWeapon != null) {
            secondaryWeapon.shoot();
        } else {
            System.out.println(getTag() + ": No secondary weapon set.");
        }
    }

    /**
     * Yksikkö ottaa vahinkoa
     *
     * @param damage Vahinkomäärä, jonka yksikkö ottaa
     */
    public void takeDamage(int damage) {
        hp -= damage;
        if(hp <= 0){
            controller.removeUpdateable(this);
            controller.removeFromCollisionList(this);
            if(this.getTag().equals("enemy")){
                controller.addScore(100);
            }
            if(this.getTag().equals("player")){
                controller.returnToMain();
            }
        }
        if(this.getTag().equals("player")){
            controller.addScore(-50);
        }
    }

    public void setDirection(double direction) {
        this.rotate(direction);

        for (Component component : components) {
            component.rotate(direction);
        }
    }

    public void stopMoving() {
        this.setIsMoving(false);
        for (Component component : components) {
            component.setIsMoving(false);
        }
    }

    private void sortComponents(ArrayList<Component> components) {
        for (int i = 0; i < components.size(); i++) { //Lajitellaan komponentit suurimmasta pienimpään
            for (int n = 0; n < components.size(); n++) {
                if (components.get(i).getShape().getLayoutBounds().getHeight() * components.get(i).getShape().getLayoutBounds().getWidth()
                        > components.get(n).getShape().getLayoutBounds().getHeight() * components.get(n).getShape().getLayoutBounds().getWidth()) {
                    Component x = components.get(n);
                    components.set(n, components.get(i));
                    components.set(i, x);

                }
            }
        }
    }

    public void equipComponents(ArrayList<Component> components) {
        int offset = -5;
        sortComponents(components); //Lajittelee komponentit isoimmasta pienimpään
        for (Component component : components) { //Lista käy läpi kaikki komponentit ja asettaa kuvat päällekkäin
            Shape shape = component.getShape();
            shape.setLayoutY(offset); //Näitä muokkaamalla voi vaihtaa mihin komponentti tulee
            shape.setLayoutX(0);
            //setPosition(this.getXPosition(), this.getYPosition() + 100);
            this.getChildren().add(component.getShape());
            setTag(getTag());
            offset += 20;
        }
    }

    public void drawShip(Shape shape) {
        String tag = getTag();
        shape.setEffect(new GaussianBlur(2.0));
        shape.setFill(Color.TRANSPARENT);
        shape.setStrokeWidth(3.0);
        this.getChildren().add(shape);
        if (tag.equals("player")) {
            shape.setStroke(Color.CYAN);
        } else if (tag.equals("enemy")) {
            shape.setStroke(Color.RED);
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
        // Alusten törmäily ei tee mitään
    }

    public void destroyThis(){
        controller.removeUpdateable(this);
    }

    public void setHP(int hp){
        this.hp = hp;
    }
}
