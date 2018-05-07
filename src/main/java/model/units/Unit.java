package model.units;

import controller.Controller;
import controller.GameController;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import model.*;
import model.fx.Explosion;
import model.weapons.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Lisää spriteen avaruusalukselle ominaisia piirteitä.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class Unit extends SpriteImpl implements Updateable, HitboxCircle {

    /**
     * apumuuttuja jotta komponentit tietävät onko alus oikeasti tuhottu, koska aluksesta jää viittaus komponenttiin.
     */
    private boolean isDestroyed = false;

    /**
     * Aluksen koko. Käytetään mm. räjähdyksen suuruuden määrittelemisessä.
     */
    private double unitSize = 1;

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
    ArrayList<Weapon> components = new ArrayList<>(); */

    /**
     * Kaikki aseet mm. järjestämistä varten.
     */
    private List<Weapon> weapons = new ArrayList<>();

    /**
     * Pääaseet.
     */
    private List<Weapon> primaryWeapons = new ArrayList<>();

    /**
     * Sivuase.
     */
    private Weapon secondaryWeapon;

    /**
     * Konstruktori.
     * @param color Unitin shapen väri.
     * @param componentSideGaps Visuaalinen väli komponenteilla toisiinsa kun ne kasataan alukseen.
     * @param projectilesFrontOffset Alukseen kasattavien aseiden ammusten poikkeama aluksen keskeltä sen etusuuntaan. Sivusuunnassa ammuksen syntyvät komponenttien kohdasta.
     */
    public Unit(Color color, double componentSideGaps, double projectilesFrontOffset){
        controller = GameController.getInstance();
        this.color = color;
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
        // Tyhjä oletustoteutus, overridetaan perivissä luokissa.
    }

    /**
     * Luodaan aseet int-listasta.
     * Aseet on luotava sen jälkeen kun aluksen muoto on laitettu (tai ollaan laittamassa) spriten lapseksi.
     * @param primaries primary-aseet tägeinä eli int muodossa.
     */
    public void makePrimaryWeapons(List<Tag> primaries) {
        List<Weapon> initialPrimaryWeapons = new ArrayList<>();

        if(primaries != null && controller != null) {
            for (Tag primaryWeaponTag : primaries) {
                switch (primaryWeaponTag){
                    default:
                        break;
                    case WEAPON_BLASTER:
                        initialPrimaryWeapons.add(new Blaster(2, 20, 1.5));
                        break;
                    case WEAPON_BLASTER_SHOTGUN:
                        initialPrimaryWeapons.add(new BlasterShotgun(2, 20, 1.5));
                        break;
                    case WEAPON_BLASTER_SPRINKLER:
                        initialPrimaryWeapons.add(new BlasterSprinkler(2, 20, 2, 3));
                        break;
                    case WEAPON_ROCKET_LAUNCHER:
                        initialPrimaryWeapons.add(new RocketLauncher(2, 4.8, 3, true));
                        break;
                    case WEAPON_ROCKET_SHOTGUN:
                        initialPrimaryWeapons.add(new RocketShotgun(2, 0, 5, 4.8, true));
                        break;
                    case WEAPON_LASER_GUN:
                        initialPrimaryWeapons.add(new LaserGun(2, 1, 1.5));
                        break;
                    case WEAPON_MACHINE_GUN:
                        initialPrimaryWeapons.add(new MachineGun(2, 55, 0.05));
                        break;
                }
            }
        }

        for(Weapon weapon : initialPrimaryWeapons){
            addPrimaryWeapon(weapon);
        }
    }

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
        (primaryWeapon).setParentUnit(this); // asettaa aseen ampujaksi tämän.
        primaryWeapons.add(primaryWeapon); // lisää pääaseisiin parametrin
        Platform.runLater(()-> this.getChildren().add(primaryWeapon.getShape()));

        sortComponents();
    }

    /**
     * Lisätään pääase unittiin. Asetta ei laiteta koottavien komponenttien joukkoon, sen sijaan ase sijoitetaan sen komponentin poikkeaman perusteella.
     * @param primaryWeapon Weapon-rajapinnan toteuttava olio.
     */
    public void addPrimaryWeaponWithCustomOffsets(Weapon primaryWeapon) {
        (primaryWeapon).setParentUnit(this);

        // asetetaan komponentin poikkeama
        Shape componentShape = primaryWeapon.getShape();
        componentShape.setLayoutX(primaryWeapon.getOffset().getX());
        componentShape.setLayoutY(primaryWeapon.getOffset().getY());

        this.primaryWeapons.add(primaryWeapon);
        Platform.runLater(()-> this.getChildren().add((primaryWeapon).getShape()));
        // tästä metodista ei mene koottavien komponenttien joukkoon
    }

    /**
     * Ampuu yksikön pääaseella.
     */
    public void shootPrimary() {
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
        (secondaryWeapon).setParentUnit(this); // asettaa aseen ampujaksi tämän.
        Platform.runLater(()->this.getChildren().add((secondaryWeapon).getShape()));
        this.secondaryWeapon = secondaryWeapon; // asettaa sekundaariaseeksi parametrin

        sortComponents();
    }

    /**
     * Asetetaan sekundaarinen ase unittiin. Asetta ei laiteta koottavien komponenttien joukkoon, sen sijaan ase sijoitetaan sen komponentin poikkeaman perusteella.
     * @param secondaryWeapon Weapon-rajapinnan toteuttava olio.
     */
    public void setSecondaryWeaponWithCustomOffsets(Weapon secondaryWeapon) {
        secondaryWeapon.setParentUnit(this);

        // asetetaan komponentin poikkeama
        Shape componentShape = secondaryWeapon.getShape();
        componentShape.setLayoutX(secondaryWeapon.getOffset().getX());
        componentShape.setLayoutY(secondaryWeapon.getOffset().getY());

        this.secondaryWeapon = secondaryWeapon;
        Platform.runLater(()-> this.getChildren().add((secondaryWeapon).getShape()));
        // tästä metodista ei mene koottavien komponenttien joukkoon
    }

    /**
     * Ampuu yksikön sivuaseella.
     */
    public void shootSecondary() {
        if (secondaryWeapon != null) {
            secondaryWeapon.shoot();
        } else {
            System.out.println(getTag() + ": No secondary weapon set.");
        }
    }

    /**
     * Lajittelee primaryWeapons ja secondaryWeapon -aseet, niin että luo jonoja aina parittoman määrän vähintään yhtä monta kun kyseisissä listoissa
     * on eniten saman tyyppisiä aseita. Pariton määrä jonoja siksi jotta aluksella on aina olemassa 1 jono keskellä alusta. Saman tyyppiset aseet lajitellaan
     * eri jonoihin eli aluksen sivusuuntaisesti niin että parillisesta määrästä samoista aseista ei mene asetta aluksen keskijonoon, ja toisinpäin, jotta lajittelu
     * pysyisi symmetrisenä. Yksi jono voi taas sisältää rajaton määrä eri tyyppisiä aseita jotka lajitellaan aluksen etusuuntaisesti.
     * Aseet lajitellaan siis siten että aluksen keskeltä alkaen lisätään aseita sivu- tai/ja etusuuntaan riippuen
     * kuinka monta saman tyyppistä asetta ja kuinka monta eri asetta on aluksella.
     */
    private void sortComponents() {
        // lista johon lisätään lajiteltavat aseet.
        List<List<Weapon>> weaponLists = new ArrayList<>();
        weaponLists.add(new ArrayList<>());
        // aseiden indeksit jota on jo lisätty lajiteltaviin aseisiin
        List<Integer> alreadyAddedIndex = new ArrayList<>();
        // aseiden indeksit jotka lisätään lajiteltaviin aseisiiin. apumuuttuja.
        List<Integer> toBeAddedIndexes = new ArrayList<>();

        // tuplaiterointi
        for(int i = 0; i < primaryWeapons.size(); i++) {
            // tyhjää lisättävät aseiden indeksit ja lisää tarkasteltava alkio
            toBeAddedIndexes.clear();
            if(!alreadyAddedIndex.contains(i)){
                toBeAddedIndexes.add(i);
            }

            for (int j = 0; j < primaryWeapons.size(); j++) {
                // jos tarkasteltavat alkiot eivät ole sama olio, ja jos alkiot ovat samaa alaluokkaa (sama ase), ja jos tarkasteltavaa alkiota
                // ei ole jo lisätty lajiteltaviin...
                if (primaryWeapons.get(i) != primaryWeapons.get(j) && primaryWeapons.get(i).getClass() == primaryWeapons.get(j).getClass() &&
                        !alreadyAddedIndex.contains(i)) {
                    // ...niin lisää alkio lajiteltuihin ja lisättäviin aseisiin joka pitää sisällään vain saman tyyppisiä aseita.
                    alreadyAddedIndex.add(j);
                    toBeAddedIndexes.add(j);
                }
            }
            // niin kauan kun asejonoja (aluksen sivusuuntaan) ei ole yhtä monta kuin lisättäviä saman tyyppisiä aseita, luodaan 2 uutta jonoa.
            // jotta jonolukumäärä pysyy parittomana.
            while (weaponLists.size() < toBeAddedIndexes.size()) {
                weaponLists.add(0, new ArrayList<>());
                weaponLists.add(new ArrayList<>());
            }

            // eka indeksi johon lisättävistä lisätään ase jotta weaponList lista pysyy symmetrisenä.
            int firstIndex = (weaponLists.size() - toBeAddedIndexes.size()) / 2;
            // keskimmäinen indeksi aselistoista. apumuuttuja
            int iMidIndex = (weaponLists.size()) / 2;
            // apumuuttuja tilanteeseen jossa ei lisätä keskimmäiseen listaan asetta, eli silloin kun saman tyyppisiä aseita on parillinen määrä.
            int addedIndex = 0;

            // lisätään secondaryWeapon keskimmäiseen listaan.
            if(getSecondaryWeapon() != null){
                weaponLists.get(iMidIndex).add(getSecondaryWeapon());
            }

            // listään lisättävät, eli samantyyppiset, aseet lajiteltavien listaan:
            for (int k = 0; k < toBeAddedIndexes.size(); k++) {
                // jos lisättävät on parillinen määrä
                if(toBeAddedIndexes.size() % 2 == 0){
                    // ja jos lisättävä kohta on keskimmäinen lista, niin lisää sitä seuraavaan listaan, ja apumuuttujan avulla pidä huoli että
                    // seuraavat aseet lisätään aina yhtä pidemmälle olevaan listaan (koska keskilista skipattiin)
                    if(k + firstIndex == iMidIndex){
                        addedIndex = 1;
                        weaponLists.get(k + firstIndex + addedIndex).add(primaryWeapons.get(toBeAddedIndexes.get(k)));
                    }
                    else{
                        weaponLists.get(k + firstIndex + addedIndex).add(primaryWeapons.get(toBeAddedIndexes.get(k)));
                    }
                }
                // jos lisättävät on pariton määrä
                else{
                    weaponLists.get(k + firstIndex).add(primaryWeapons.get(toBeAddedIndexes.get(k)));
                }
            }
        }
        // Vaihda lajiteltavien aseiden sijantia ja ammuksen aloitussijaintia riippuen lajiteltavat listan listan indeksistä ja
        // sen listan aseen indeksistä, verrattuna kummankin llistan keski-indekseihin.
        int iMidIndex = (weaponLists.size()) / 2;
        for(int i = 0; i < weaponLists.size(); i++){
            int jMidIndex = (weaponLists.get(i).size()) / 2;
            for(int j = 0; j < weaponLists.get(i).size(); j++){
                Shape componentShape = weaponLists.get(i).get(j).getShape();
                componentShape.setLayoutX(26 * (j - jMidIndex));
                componentShape.setLayoutY(26 * (i - iMidIndex));
                weaponLists.get(i).get(j).setProjectileOffset(new Point2D(weaponLists.get(i).get(j).getProjectileOffset().getX(), 26 * (i - iMidIndex)));
            }
        }
    }

    @Override
    public void collides(Object obj) {
        // Tyhjä oletustoteutus, overridetaan perivissä luokissa.
    }

    /**
     * getteri onko alus null, koska aluksesta saattaa jäädä viittaus mm. komponenttiin.
     * @return Tieto onko alus null.
     */
    public boolean isDestroyed(){
        return isDestroyed;
    }

    @Override
    public void destroyThis() {
        isDestroyed = true;
        if (this instanceof Boss) {
            // Bossi droppaa kuollessaan läjän HP poweruppeja
            for (int i = 0; i < 10; i++) {
                int x = ThreadLocalRandom.current().nextInt((int) (getXPosition()), (int) (getXPosition() + getHitboxRadius()) + 1);
                int y = ThreadLocalRandom.current().nextInt((int) (getYPosition()), (int) (getYPosition() + getHitboxRadius()) + 1);
                new PowerUp(PowerUp.HP, new Point2D(x, y));
            }
        } else {
            // Muut vihut random powerup
            new PowerUp(this, (int) (Math.random() * 5)); //Tiputtaa jonkun komponentin jos random < powerup tyyppien määrä
        }
        new Explosion(color, getPosition(), unitSize);
        controller.removeUpdateable(this, this);
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
     * @param damage Vahinkomäärä, jonka yksikkö ottaa.
     */
    public void takeDamage(int damage) {
        tookDamage = true; // efektiä varten
        if(shape != null){
            shape.setStroke(Color.WHITE);
            shape.setStrokeWidth(9.0);
        }

        hp -= damage;
        if(hp <= 0){
            hp = 9999; // ettei voi ottaa vahinkoa poiston yhteydessä
            if (getTag() == Tag.SHIP_ENEMY) {
                controller.addScore(100);
            }
            if (getTag() == Tag.SHIP_PLAYER) {
                controller.setHealthbar(0, 1);
                controller.returnToMain();
            }
            if (getTag() == Tag.SHIP_BOSS) {
                controller.addScore(1000);
                controller.setHealthbar(0, 0);
            }

            destroyThis();
        }
        if (getTag() == Tag.SHIP_PLAYER) {
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
     * Asettaa Unitin shapen värin vakioksi.
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

    /**
     * Palauttaa Unitin värin.
     * @return Unitin väri.
     */
    public Color getUnitColor(){
        return color;
    }

    /**
     * Asettaa Unitin koon.
     * @param unitSize Unitin koko.
     */
    public void setUnitSize(double unitSize) {
        this.unitSize = unitSize;
    }

    /**
     * Getteri Unitin koolle.
     *
     * @return Unitin koko.
     */
    public double getUnitSize() {
        return unitSize;
    }

}
