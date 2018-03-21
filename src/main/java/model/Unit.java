package model;

import controller.Controller;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import model.weapons.Weapon;

import java.util.ArrayList;
import java.util.List;

import static view.GameMain.*;

/**
 * Lisää spriteen avaruusalukselle ominaisia piirteitä.
 */
public class Unit extends Sprite implements Updateable {

    /**
     * Kontrolleri.
     */
    private Controller controller;

    /**
     * Unitin alkuperäiset hitpointsit.
     */
    private int originalHp;

    /**
     * Unitin shapen väri.
     */
    private Color color;

    /**
     * Tieto siitä, onko Unit ottanut vahinkoa. Käytetään osumaefektin tekemiseen.
     */
    private boolean tookDamage = false;

    /**
     * Unitin shape.
     */
    private Shape shape;


    /**
     * Yksikön hitpointsit.
     */
    int hp = 30;

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
    private List<Weapon> primaryWeapons = new ArrayList<>();

    /**
     * Sivuase.
     */
    private Weapon secondaryWeapon;

    /**
     * Konstruktori.
     * @param controller Pelin kontrolleri.
     *
     */
    public Unit(Controller controller) {
        this.controller = controller;
    }

    /**
     * Konstruktori Unitin shapen värin valinnalla.
     * @param controller Pelin kontrolleri.
     * @param color Unitin shapen väri.
     */
    public Unit(Controller controller, Color color){
        this(controller);
        this.color = color;
    }

    @Override
    public void collides(Updateable collidingUpdateable) {
        // Alusten törmäily ei tee mitään
    }

    @Override
    public void destroyThis() {

        new PowerUp(controller, this, (int)(Math.random() * 5), 10); //Tiputtaa jonkun komponentin jos random < powerup tyyppien määrä
        new Explosion(controller, color, getPosition(), 1);
        controller.removeUpdateable(this);
        controller.removeFromCollisionList(this);
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
        originalHp = hp;
    }

    /**
     * Palauttaa yksikön alkuperäiset hitpointit
     * @return Alkuperäiset hitpointit.
     */
    public int getOriginalHp(){
        return originalHp;
    }

    /**
     * Palauttaa Unitin pääaseen.
     * @return Unitin pääase.
     */
    List<Weapon> getPrimaryWeapons() {
        return primaryWeapons;
    }

    /**
     * Asettaa Unitin pääaseen.
     * @param primaryWeapon Weapon-rajapinnan toteuttava olio.
     */
    public void addToPrimaryWeapon(Weapon primaryWeapon) {
        addComponent((Component)primaryWeapon);
        this.primaryWeapons.add(primaryWeapon);
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
        addComponent((Component)secondaryWeapon);
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
        this.shape = shape;
        int tag = getTag();
        this.shape.setEffect(new GaussianBlur(2.0));
        this.shape.setFill(Color.BLACK);
        this.shape.setStrokeWidth(5.0);
        getChildren().add(this.shape);
        this.shape.setStroke(color);
    }

    /**
     * Kiinnittää komponentit Unittiin. Jos samaa komponenttia koitetaan lisätä usealla kutsulla, tulee virhe.
     * Ei käytössä
     */
    public void equipComponentss() {
        //int offset = -5;
        sortComponents(); //Lajittelee komponentit isoimmasta pienimpään
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
     * Kiinnittää komponentin Unittiin.
     * @param component Komponentti, joka lisätään.
     */
    public void equipComponent(Component component) {
        Shape shape = component.getShape();
        shape.setLayoutY(component.getyOffset());
        shape.setLayoutX(component.getxOffset());
        this.getChildren().add(component.getShape());
        setTag(getTag());
    }

    /**
     * Lisää komponentin komponenttilistaan. Komponenttilistassa olevat komponentit kiinnitetään Unittiin
     * equipComponents() metodissa.
     * @param component Komponentti, joka lisätään komponenttilistaan.
     */
    public void addComponent(Component component){
        equipComponent(component);
        components.add(component);
    }

    /**
     * Ampuu yksikön pääaseella.
     */
    void shootPrimary() {
        if (primaryWeapons.get(0) != null) {
            for(Weapon primaryWeapon : primaryWeapons) {
                primaryWeapon.shoot();
            }
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
     */
    private void sortComponents() {
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
        tookDamage = true; // efektiä varten
        if(shape != null){
            shape.setStroke(Color.WHITE);
        }

        hp -= damage;
        if(hp <= 0){
            hp = 9999; // ettei voi ottaa vahinkoa poiston yhteydessä
            if(getTag() == ENEMY_SHIP_TAG){
                controller.addScore(100);
            }
            if(getTag() == PLAYER_SHIP_TAG){
                controller.setHealthbar(0, 1);
                controller.returnToMain();
            }
            if(getTag() == BOSS_SHIP_TAG){
                controller.addScore(1000);
                controller.setHealthbar(0, 0);
            }

            destroyThis();
        }
        if(getTag() == PLAYER_SHIP_TAG){
            controller.addScore(-50);
        }
    }

    /**
     * Palauttaa tiedon, onko Unit ottanut vahinkoa.
     * @return Tieto onko Unit ottanut vahinkoa.
     */
    public boolean getTookDamage(){
        return tookDamage;
    }

    /**
     * Asettaa Unitin shapen värin.
     */
    public void setOriginalColor(){
        shape.setStroke(color);
    }

    /**
     * Asettaa Unittiin tiedon, onko se ottanut vahinkoa.
     * @param tookDamage True jos Unit on ottanut vahinkoa, muuten false.
     */
    public void setTookDamage(boolean tookDamage){
        this.tookDamage = tookDamage;
    }

}
