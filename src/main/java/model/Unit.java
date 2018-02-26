package model;

import controller.Controller;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import model.weapons.Weapon;

import java.util.ArrayList;

/**
 * Lisää spriteen avaruusalukselle ominaisia piirteitä.
 */
@SuppressWarnings("unused")
public class Unit extends Sprite implements Updateable {

    /**
     * Kontrolleri.
     */
    private Controller controller;

    /**
     * Yksikön hitpointsit.
     */
    private int hp = 30;

    /**
     * Yksikön taso.
     */
    private int level;

    /**
     * Komponenttilista.
     */
    ArrayList<Component> components = new ArrayList<>();

    /**
     * Pääase.
     */
    private Weapon primaryWeapon;

    /**
     * Sivuase.
     */
    private Weapon secondaryWeapon;

    public Unit(){

    }

    /**
     * Konstruktori asettaa kontrollerin.
     * @param controller Pelin kontrolleri.
     */
    public Unit(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void collides(Updateable collidingUpdateable) {
        // Alusten törmäily ei tee mitään
    }

    @Override
    public void destroyThis() {
        controller.removeUpdateable(this);
    }

    @Override
    public void update(double deltaTime) {
        // Overridetaan perivissä luokissa.
    }

    /**
     * Palauttaa yksikön hitpointsit.
     * @return Hitpointsien määrä.
     */
    public int getHp(){
        return hp;
    }

    /**
     * Asettaa yksikön hitpointsit.
     * @param hp Hitpointsien määrä.
     */
    public void setHp(int hp){
        this.hp = hp;
    }

    /**
     * Palauttaa Unitin pääaseen.
     * @return Unitin pääase.
     */
    Weapon getPrimaryWeapon() {
        return primaryWeapon;
    }

    /**
     * Asettaa Unitin pääaseen.
     * @param primaryWeapon Weapon-rajapinnan toteuttava ase.
     */
    public void setPrimaryWeapon(Weapon primaryWeapon) {
        this.primaryWeapon = primaryWeapon;
    }

    /**
     * Palauttaa Unitin sivuaseen.
     * @return Unitin sivuase.
     */
    Weapon getSecondaryWeapon() {
        return secondaryWeapon;
    }

    /**
     * Asettaa Unitin sivuaseen.
     * @param secondaryWeapon Weapon-rajapinnan toteuttava ase.
     */
    public void setSecondaryWeapon(Weapon secondaryWeapon) {
        this.secondaryWeapon = secondaryWeapon;
    }

    /**
     * Lisää Unitin hitpointseja.
     * @param hp Määrä, jolla hitpointseja lisätään.
     */
    public void addHP(int hp){
        this.hp += hp;
    }

    /**
     * Asettaa Unitin aluksen visuaalisia piirteitä sen mukaan, onko alus pelaaja vai vihollinen.
     * @param shape Aluksen Shape-olio.
     */
    void drawShip(Shape shape) {
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

    /**
     * Kiinnittää komponentit Unittiin. Jos samaa komponenttia koitetaan lisätä usealla kutsulla, tulee virhe.
     * @param components Unitin komponenttilista.
     */
    public void equipComponents(ArrayList<Component> components) {
        //int offset = -5;
        sortComponents(components); //Lajittelee komponentit isoimmasta pienimpään
        for (Component component : components) { //Lista käy läpi kaikki komponentit ja asettaa kuvat päällekkäin
            Shape shape = component.getShape();
            shape.setLayoutY(component.getyOffset()); //Näitä muokkaamalla voi vaihtaa mihin komponentti tulee
            shape.setLayoutX(component.getxOffset());
            //setPosition(this.getXPosition(), this.getYPosition() + 100);
            this.getChildren().add(component.getShape());
            setTag(getTag());
           // offset += 20;
        }
    }

    /**
     * Ampuu yksikön pääaseella.
     */
    void shootPrimary() {
        if (primaryWeapon != null) {
            primaryWeapon.shoot();
        } else {
            System.out.println(getTag() + ": No primary weapon set.");
        }
    }

    /**
     * Ampuu yksikön sivuaseella.
     */
    void shootSecondary() {
        if (secondaryWeapon != null) {
            secondaryWeapon.shoot();
        } else {
            System.out.println(getTag() + ": No secondary weapon set.");
        }
    }

    /**
     * Lajittelee komponenttilistan komoponenttien koon mukaan suurimmasta pienimpään. Apumetodi equipComponents():lle.
     * @param components Unitin komponenttilista.
     */
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

    /**
     * Yksikkö ottaa vahinkoa. Kutsutaan osuman tapahtuessa.
     *
     * @param damage Vahinkomäärä, jonka yksikkö ottaa.
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
            if(this.getTag().equals("boss")){
                controller.addScore(1000);
                controller.setHealthbar(0);
            }
        }
        if(this.getTag().equals("player")){
            controller.addScore(-50);
        }
    }

    /*
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
    */
}
