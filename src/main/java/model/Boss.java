package model;

import controller.Controller;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import model.weapons.BlasterShotgun;
import model.weapons.Weapon;

import java.util.ArrayList;

import static view.GameMain.*;

/**
 * Tason loppupomo
 */
public class Boss extends Unit implements Updateable {

    public ArrayList<Boss> bossList = new ArrayList<>();
    /**
     * Pelin kontrolleri
     */
    private Controller controller;

    /**
     * Alku X-koordinaatti
     */
    private double initialX;
    /**
     * Alku y-koordinaatti
     */
    private double initialY;

    /**
     * Laskuri sille, kuinka monta pistettä koordinaatistossa on liikuttu
     */
    private int movementCounter = 0;

    /**
     * Kertoo liikutaanko ylös vai alas
     */
    private boolean up = false;

    /**
     * Apumuuttuja pomon alkuperäisestä hp:stä
     */
    private int originalhp;

    // Ampumisen kovakoodit'
    /**
     * Kuinka usein ammutaan
     */
    private int fireRate = 100;
    /**
     * Kasvatetaan kunnes sama kuin fireRate, jolloin ammutaan ja fireRateCounter asetetaan 0.
     */
    private int fireRateCounter = 100;

    public Boss(){

    }

    /**
     * Konstruktori. Kutsuu yläluokan konstruktoria ja asettaa kontrollerin. orginalHp on sama kuin
     * alunperin parametrinä annettu hp. Pomo lisätään CollisionListiin (osumatarkastelu) ja sille luodaan
     * suorakulmainen hitboxi, 128x256. Saa tagin "boss".
     * @param controller Pelin kontrolleri
     * @param hp Pomon hp, asettaa samalla originalHp
     * @param image Pomon sprite
     * @param initialX X-koordinaatti johon pomo ilmestyy.
     * @param initialY Y-koordinaatti johon pomo ilmestyy.
     */
    public Boss(Controller controller, Color shipColor, int hp, Image image, double initialX, double initialY) {
        super(controller, shipColor);
        this.controller = controller;
        setHp(hp);
        originalhp = hp;
        setTag(BOSS_SHIP_TAG);
        controller.addUnitToCollisionList(this);
        setPosition(initialX, initialY);
        rotate(180);
        setImage(image, 128, 256);
        setIsMoving(true);
        this.initialX = initialX;
        this.initialY = initialY;
        this.setHitbox(256);
    }

    /**
     * Asettaa alkuposition pomolle
     * @param initialX Alkuposition x-koordinaatti.
     * @param initialY Alkuposition y-koordinaatti.
     */
    public void setInitPosition(double initialX, double initialY) {
        this.initialX = initialX;
        this.initialY = initialY;
    }

    /**
     * Päivittää pomon liikkumisen, ampumisen ja healthbarin. Kutsu VAIN gameloopista!
     * @param deltaTime Kulunut aika viime päivityksestä.
     */
    @Override
    public void update(double deltaTime){
        if (fireRateCounter <= fireRate) fireRateCounter++;
        if (fireRateCounter >= fireRate) {
            fireRateCounter = 0;
            shootPrimary();
            shootSecondary();
        }
        // chekkaa menikö ulos ruudulta
        if (getXPosition() < -100
                || getXPosition() > WINDOW_WIDTH+200
                || getYPosition() < -100
                || getYPosition() > WINDOW_HEIGHT+100) {
            destroyThis();
        } else {
            setPosition(getXPosition(), upOrDown());
            moveBoss(deltaTime);
        }
        controller.setHealthbar(hpPercentage());
    }

    /**
     * Liikuttaa pomoa ylös tai alas, riippuen siitä kuinka paljon pomo on liikkunut yhteen suuntaan.
     * @return Palauttaa y-koordinaatin, johon pomon liikkuu.
     */
    public double upOrDown(){
        if (!up){
            if(movementCounter >= 400){ up = true;}
            return initialY + movementCounter++;
        }else{
            if(movementCounter <= 100){ up = false;}
            return initialY + movementCounter--;
        }
    }

    /**
     * Laskee pomon jäljellä olevan hp:n kymmenyksissä. Käytetään healthbarin päivittämiseen
     * @return Palauttaa kuinka monta kymmenystä pomon hp:stä on jäljellä.
     */
    public int hpPercentage(){
        int tenthHp = originalhp / 10;
        int percentage = getHp() / tenthHp;
        if (percentage == 0 && getHp() > 0){
            return 1;
        }else {
            return percentage;
        }
    }

    public void constructBosses(Controller controller){
        Boss boss1 = new Boss(controller, Color.WHITE, 100, new Image("/images/bossPlaceholder.png"), WINDOW_WIDTH - 100, 100);
        Component blaster1 = new BlasterShotgun(controller, boss1, "circle", 5, 2,
                Color.CORAL, 400, -98, 80, -98);
        Component blaster2 = new BlasterShotgun(controller, boss1, "circle", 5, 2,
                Color.CORNSILK, 400, 98, 80, 98);
        boss1.addToPrimaryWeapons((Weapon) blaster1);
        boss1.setSecondaryWeapon((Weapon) blaster2);
        bossList.add(boss1);
    }
}
