package model;

import controller.Controller;
import controller.GameController;
import javafx.application.Platform;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import model.weapons.*;

import java.util.ArrayList;
import java.util.List;

import static view.GameMain.*;

/**
 * Lisää spriteen avaruusalukselle ominaisia piirteitä.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class Unit extends Sprite implements Updateable, HitboxObject {

    /**
     * apumuuttuja jotta komponentit tietävät onko alus oikeasti null, koska aluksesta jää viittaus komponenttiin.
     */
    private boolean isNull = false;

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
     *
    ArrayList<Component> components = new ArrayList<>(); */

    /**
     * Kaikki aseet mm. järjestämistä varten.
     */
    private List<Weapon> weapons = new ArrayList<>();

    /**
     * Pääase.
     */
    private List<Weapon> primaryWeapons = new ArrayList<>();

    /**
     * Sivuase.
     */
    private Weapon secondaryWeapon;

    /**
     * Konstruktori Unitin shapen värin valinnalla.
     * @param color Unitin shapen väri.
     * @param componentSideGaps Visuaalinen väli komponenteilla toisiinsa kun ne kasataan alukseen.
     * @param projectilesFrontOffset Alukseen kasattavien aseiden ammusten poikkeama aluksen keskeltä sen etusuuntaan. Sivusuunnassa ammuksen syntyvät komponenttien kohdasta.
     */
    public Unit(Color color, double componentSideGaps, double projectilesFrontOffset){
        controller = GameController.getInstance();
        this.color = color;
        //makePrimaryWeapons(primariesTags);

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
     * Lisää Unitin hitpointseja.
     * @param hp Määrä, jolla hitpointseja lisätään.
     */
    public void addHP(int hp){
        this.hp += hp;
    }

    /**
     * Palauttaa yksikön hitpointsit.
     * @return Hitpointsien määrä.
     */
    public int getHp(){
        return hp;
    }

    /**
     * Palauttaa yksikön alkuperäiset hitpointit
     * @return Alkuperäiset hitpointit.
     */
    public int getOriginalHp(){
        return originalHp;
    }


    @Override
    public void update(double deltaTime) {
        // Overridetaan perivissä luokissa.
    }

    /**
     * Luodaan aseet int-listasta.
     * Aseet on luotava sen jälkeen kun aluksen muoto on laitettu (tai ollaan laittamassa) spriten lapseksi.
     * @param primaries primary-aseet tägeinä eli int muodossa.
     */
    public void makePrimaryWeapons(ArrayList<Integer> primaries){
        List<Weapon> initialPrimaryWeapons = new ArrayList<>();

        if(primaries != null && controller != null) {
            for (int primaryWeaponTag : primaries) {
                switch (primaryWeaponTag){
                    default:
                        break;
                    case WEAPON_BLASTER:
                        initialPrimaryWeapons.add(new Blaster(2, 20));
                        break;
                    case WEAPON_BLASTER_SHOTGUN:
                        initialPrimaryWeapons.add(new BlasterShotgun(2, 20));
                        break;
                    case WEAPON_BLASTER_SPRINKLER:
                        initialPrimaryWeapons.add(new BlasterSprinkler(2, 20, 2));
                        break;
                    case WEAPON_ROCKET_LAUNCHER:
                        initialPrimaryWeapons.add(new RocketLauncher(2, 4.8, true));
                        break;
                    case WEAPON_ROCKET_SHOTGUN:
                        initialPrimaryWeapons.add(new RocketShotgun(2, 0, 4.8, true));
                        break;
                    case WEAPON_LASER_GUN:
                        initialPrimaryWeapons.add(new LaserGun(2, 1));
                        break;
                }
            }
        }

        for(Weapon weapon : initialPrimaryWeapons){
            addPrimaryWeapon(weapon);
        }
    }

    //      Primary

    /**
     * Palauttaa Unitin pääaseen.
     * @return Unitin pääase.
     */
    List<Weapon> getPrimaryWeapons() {
        return primaryWeapons;
    }

    /**
     * Lisää pääaseen unittiin. Ei oteta huomioon aseen kompontin poikkeamia. Jos halutaan ottaa huomioon niin ks. addPrimaryWeaponWithCustomOffsets().
     * @param primaryWeapon Weapon-rajapinnan toteuttava olio.
     */
    public void addPrimaryWeapon(Weapon primaryWeapon) {
        ((Component)primaryWeapon).setParentUnit(this);
        this.primaryWeapons.add(primaryWeapon);
        Platform.runLater(()-> this.getChildren().add(((Component) primaryWeapon).getShape()));

        //sortComponents(); TODO
    }

    /**
     * Lisätään pääase unittiin. Asetta ei laiteta koottavien komponenttien joukkoon, sen sijaan ase sijoitetaan sen komponentin poikkeaman perusteella.
     * @param primaryWeapon Weapon-rajapinnan toteuttava olio.
     */
    public void addPrimaryWeaponWithCustomOffsets(Weapon primaryWeapon) {
        Component component = (Component)primaryWeapon;
        ((Component)primaryWeapon).setParentUnit(this);

        // asetetaan komponentin poikkeama
        Shape componentShape = component.getShape();
        componentShape.setLayoutX(component.getOffset().getX());
        componentShape.setLayoutY(component.getOffset().getY());

        this.primaryWeapons.add(primaryWeapon);
        Platform.runLater(()-> this.getChildren().add(((Component) primaryWeapon).getShape()));
        // tästä metodista ei mene koottavien komponenttien joukkoon
    }

    /**
     * Ampuu yksikön pääaseella.
     */
    void shootPrimary() {
        if (primaryWeapons != null) {
            for(Weapon primaryWeapon : primaryWeapons) {
                primaryWeapon.shoot();
            }
        } else {
            System.out.println(getTag() + ": No primary weapon set.");
        }
    }

    /**
     * Palauttaa Unitin sivuaseen.
     * @return Unitin sivuase.
     */
    Weapon getSecondaryWeapon() {
        return secondaryWeapon;
    }

    /**
     * Asettaa sekundaarisen aseen unittiin. Ei oteta huomioon aseen kompontin poikkeamia. Jos halutaan ottaa huomioon niin ks. setSecondaryWeaponWithCustomOffsets().
     * @param secondaryWeapon Weapon-rajapinnan toteuttava ase.
     */
    public void setSecondaryWeapon(Weapon secondaryWeapon) {
        ((Component)secondaryWeapon).setParentUnit(this);
        Platform.runLater(()->this.getChildren().add(((Component)secondaryWeapon).getShape()));
        this.secondaryWeapon = secondaryWeapon;

        //sortComponents(); TODO
    }

    /**
     * Asetetaan sekundaarinen ase unittiin. Asetta ei laiteta koottavien komponenttien joukkoon, sen sijaan ase sijoitetaan sen komponentin poikkeaman perusteella.
     * @param secondaryWeapon Weapon-rajapinnan toteuttava olio.
     */
    public void setSecondaryWeaponWithCustomOffsets(Weapon secondaryWeapon) {
        Component component = (Component)secondaryWeapon;
        ((Component)secondaryWeapon).setParentUnit(this);

        // asetetaan komponentin poikkeama
        Shape componentShape = component.getShape();
        componentShape.setLayoutX(component.getOffset().getX());
        componentShape.setLayoutY(component.getOffset().getY());

        this.secondaryWeapon = secondaryWeapon;
        Platform.runLater(()-> this.getChildren().add(((Component) secondaryWeapon).getShape()));
        // tästä metodista ei mene koottavien komponenttien joukkoon
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

    //      /Secondary

    /**
     * Lajittelee komponenttilistan komoponenttien koon mukaan suurimmasta pienimpään. Apumetodi equipComponents():lle.
     */
    private void sortComponents() {

        if(primaryWeapons.size() > 0){
            for(Weapon primaryWeapon : primaryWeapons){
                System.out.print(" shape " + ((Component)primaryWeapon).getShape());
                System.out.println(",  luokka " + primaryWeapon);
                Component primaryWeaponComponent = (Component)primaryWeapon;
                primaryWeaponComponent.getShape().setLayoutX(primaryWeaponComponent.getOffset().getX());
                primaryWeaponComponent.getShape().setLayoutY(primaryWeaponComponent.getOffset().getY());
            }
        }


        /*
        for (int i = 0; i < components.size(); i++) { //Lajitellaan komponentit suurimmasta pienimpään
            for (int n = 0; n < components.size(); n++) {
                if (components.get(i).getShape().getLayoutBounds().getHeight() * components.get(i).getShape().getLayoutBounds().getWidth()
                        > components.get(n).getShape().getLayoutBounds().getHeight() * components.get(n).getShape().getLayoutBounds().getWidth()) {
                    Component x = components.get(n);
                    components.set(n, components.get(i));
                    components.set(i, x);

                }
            }
        }*/
    }


    @Override
    public void collides(Object obj) {

    }

    /**
     * getteri onko alus null, koska aluksesta saattaa jäädä viittaus mm. komponenttiin.
     * @return
     */
    public boolean isNull(){
        return isNull;
    }

    @Override
    public void destroyThis() {
        isNull = true;
        new PowerUp(this, (int)(Math.random() * 5), 10); //Tiputtaa jonkun komponentin jos random < powerup tyyppien määrä
        new Explosion(color, getPosition(), 1);
        controller.removeUpdateable(this, this);
        controller.removeFromCollisionList(this);
    }


    /**
     * Asettaa Unitin aluksen visuaalisia piirteitä.
     * Käyttää Plarform.runLater kuvion asettamiseksi.
     * @param shape Aluksen Shape-olio.
     */
    void drawShip(Shape shape) {
        this.shape = shape;
        this.shape.setEffect(new GaussianBlur(2.0));
        this.shape.setFill(Color.BLACK);
        this.shape.setStrokeWidth(5.0);
        Platform.runLater(()->getChildren().add(this.shape));
        this.shape.setStroke(color);
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
            shape.setStrokeWidth(7.0);
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
        shape.setStrokeWidth(5);
    }

    /**
     * Asettaa Unittiin tiedon, onko se ottanut vahinkoa.
     * @param tookDamage True jos Unit on ottanut vahinkoa, muuten false.
     */
    public void setTookDamage(boolean tookDamage){
        this.tookDamage = tookDamage;
    }

    public Color getUnitColor(){
        return color;
    }

}
