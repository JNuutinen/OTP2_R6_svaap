package model;

import controller.Controller;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import model.weapons.BlasterShotgun;
import model.weapons.Weapon;

import static view.GameMain.*;

/**
 * Tason loppupomo
 */
public class Boss extends Unit implements Updateable {

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
     * Kertoo liikutaanko ylös vai alas
     */
    private boolean movingDown = true;

    /**
     * Onko boss taistelutilassa.
     */
    private boolean inFightingStage = false;

    /**
     * joku
     */
    private double damagedTimeCounter = 0;

    /**
     * joku2
     */
    private boolean tookDamage2 = false;

    /**
     * Apumuuttuja pomon alkuperäisestä hp:stä
     */
    private int originalHp;

    /**
     * Kuinka usein ammutaan
     */
    private int fireRate = 100;

    /**
     * Kasvatetaan kunnes sama kuin fireRate, jolloin ammutaan ja fireRateCounter asetetaan 0.
     */
    private int fireRateCounter = 100;


    /**
     * Konstruktori. Kutsuu yläluokan konstruktoria ja asettaa kontrollerin. orginalHp on sama kuin
     * alunperin parametrinä annettu hp. Pomo lisätään CollisionListiin (osumatarkastelu) ja sille luodaan
     * suorakulmainen hitboxi, 128x256. Saa tagin "boss".
     * @param controller Pelin kontrolleri
     * @param hp Pomon hp, asettaa samalla originalHp
     * @param initialX X-koordinaatti johon pomo ilmestyy.
     * @param initialY Y-koordinaatti johon pomo ilmestyy.
     */
    public Boss(Controller controller, int hp, double initialX, double initialY) {
        super(controller, null, null);
        this.controller = controller;
        setIsMoving(true);
        controller.addUnitToCollisionList(this);
        setHp(hp);
        originalHp = hp;
        setTag(BOSS_SHIP_TAG);
        setPosition(initialX, initialY);
        rotate(180);
        setImage(new Image("/images/bossPlaceholder.png"), 128, 256);
        setVelocity(400);
        this.setHitbox(256);

        armBoss(controller);
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
        }
        controller.setHealthbar(hpPercentage(), 0);

        moveStep(deltaTime);
        if(!inFightingStage){
            double distanceToTargetX = Math.abs(getXPosition() - (WINDOW_WIDTH * 0.8));
            setVelocity(distanceToTargetX * deltaTime * 200);
            if(distanceToTargetX < 5){
                inFightingStage = true;
                lockDirection(270);
                setVelocity(70);
            }
        }
        else if(movingDown){
            if(getYPosition() > WINDOW_HEIGHT * 0.7){
                lockDirection(90);
                movingDown = false;
            }
        }
        else{
            if (getYPosition() < WINDOW_HEIGHT * 0.3){
                lockDirection(270);
                movingDown = true;
            }
        }
    }

    /**
     * Laskee pomon jäljellä olevan hp:n kymmenyksissä. Käytetään healthbarin päivittämiseen
     * @return Palauttaa kuinka monta kymmenystä pomon hp:stä on jäljellä.
     */
    public int hpPercentage(){
        int tenthHp = originalHp / 10;
        int percentage = getHp() / tenthHp;
        if (percentage == 0 && getHp() > 0){
            return 1;
        }else {
            return percentage;
        }
    }

    /**
     * Varustaa aluksen aseilla.
     * @param controller Pelin kontrolleri.
     */
    public void armBoss(Controller controller){
        Weapon blaster1 = new BlasterShotgun(controller, 2, new Point2D(2, -92), new Point2D(80, -92));
        Weapon blaster2 = new BlasterShotgun(controller, 2, new Point2D(2, 92), new Point2D(80, 92));
        addPrimaryWeapon((Weapon) blaster1);
        setSecondaryWeapon((Weapon) blaster2);
    }
}
