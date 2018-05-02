package model.units;

import controller.Controller;
import controller.GameController;
import javafx.scene.image.Image;
import model.Tag;

import static view.GameMain.WINDOW_HEIGHT;
import static view.GameMain.WINDOW_WIDTH;

/**
 * Tason loppupomo
 *
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class Boss1 extends Unit implements Boss {

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
     * Apumuuttuja pomon alkuperäisestä hp:stä
     */
    private int originalHp;

    /**
     * Konstruktori. Kutsuu yläluokan konstruktoria ja asettaa kontrollerin. orginalHp on sama kuin
     * alunperin parametrinä annettu hp. Pomo lisätään CollisionListiin (osumatarkastelu) ja sille luodaan
     * suorakulmainen hitboxi, 128x256. Saa tagin "boss".
     *
     * @param hp       Pomon hp, asettaa samalla originalHp
     * @param initialX X-koordinaatti johon pomo ilmestyy.
     * @param initialY Y-koordinaatti johon pomo ilmestyy.
     */
    public Boss1(int hp, double initialX, double initialY) {
        super(null, 0, 0);
        controller = GameController.getInstance();
        setIsMoving(true);
        setHp(hp);
        originalHp = hp;
        setTag(Tag.SHIP_BOSS);
        setPosition(initialX, initialY);
        rotate(180);
        setImage(new Image("/images/bossPlaceholder.png"), 128, 256);
        setVelocity(400);
        this.setHitbox(256);
        setUnitSize(6);

        armBoss();

        controller.addUpdateableAndSetToScene(this);
        controller.addHitboxObject(this);
    }

    /**
     * Asettaa alkuposition pomolle
     *
     * @param initialX Alkuposition x-koordinaatti.
     * @param initialY Alkuposition y-koordinaatti.
     */
    public void setInitPosition(double initialX, double initialY) {
        this.initialX = initialX;
        this.initialY = initialY;
    }

    @Override
    public void update(double deltaTime) {
        // chekkaa menikö ulos ruudulta
        if (getXPosition() < -100
                || getXPosition() > WINDOW_WIDTH + 200
                || getYPosition() < -100
                || getYPosition() > WINDOW_HEIGHT + 100) {
            destroyThis();
        }
        controller.setHealthbar(hpPercentage(), 0);
        shootPrimary();
        shootSecondary();

        moveStep(deltaTime);
        if (!inFightingStage) {
            double distanceToTargetX = Math.abs(getXPosition() - (WINDOW_WIDTH * 0.8));
            setVelocity(distanceToTargetX * deltaTime * 200);
            if (distanceToTargetX < 5) {
                inFightingStage = true;
                lockDirection(270);
                setVelocity(70);
            }
        } else if (movingDown) {
            if (getYPosition() > WINDOW_HEIGHT * 0.7) {
                lockDirection(90);
                movingDown = false;
            }
        } else {
            if (getYPosition() < WINDOW_HEIGHT * 0.3) {
                lockDirection(270);
                movingDown = true;
            }
        }
    }

    /**
     * Laskee pomon jäljellä olevan hp:n kymmenyksissä. Käytetään healthbarin päivittämiseen
     *
     * @return Palauttaa kuinka monta kymmenystä pomon hp:stä on jäljellä.
     */
    public int hpPercentage() {
        int tenthHp = originalHp / 10;
        int percentage = getHp() / tenthHp;
        if (percentage == 0 && getHp() > 0) {
            return 1;
        } else {
            return percentage;
        }
    }

    /**
     * Varustaa aluksen aseilla.
     */
    public void armBoss() {
        /*
        Weapon blaster1 = new BlasterShotgun(controller, 2, new Point2D(2, -92), new Point2D(80, -92));
        Weapon blaster2 = new BlasterShotgun(controller, 2, new Point2D(2, 92), new Point2D(80, 92));
        addPrimaryWeapon((Weapon) blaster1);
        setSecondaryWeapon((Weapon) blaster2);*/
    }
}
