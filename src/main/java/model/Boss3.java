package model;

import controller.Controller;
import controller.GameController;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import model.weapons.BlasterSprinkler;
import model.weapons.LaserGun;
import model.weapons.RocketLauncher;
import model.weapons.Weapon;

import static view.GameMain.*;

/**
 * Tason loppupomo 3
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class Boss3 extends Unit {

    /** Pelin kontrolleri */
    private Controller controller;

    /** Apumuuttuja pomon alkuperäisestä hp:stä */
    private int originalHp;

    /** Apumuuttuja pomon alkuperäisestä hp:stä */
    private int originalhp;

    /** tulinopeus */
    private double fireRate = 0.7;

    /**  ampumisen ajanlaskuri */
    private double fireRateCounter = 0;

    /** apumuuttuja ampumistilan ajanlaskun yhteydessä */
    private double stageTimeCounter = 0;

    /** liikkuuko alaspäin hyökkaystilassa */
    private boolean movingDown = true;

    /** apumuuttuja ammuttavien laserien muistamiseksi */
    private int currentLaser = 0;

    /** apumuuttuja laserien ampumisjärjestyksen hallitsemiseksi */
    private boolean lasersTopToDown = true;

    /** apumuuttuja vahinkoefektin ajanlaskun yhteydessä */
    private double damagedTimeCounter = 0;

    private boolean tookDamage2 = false;

    /** apumuuttuja tilan yhteydessä*/
    private boolean inFightingStage = false;



    public Boss3(Point2D initialPosition){

        // alus varustetaan erikseen armShip(), niin sitä ei tehdä tässä.
        super(Color.ORANGE, 0, 0);

        Polygon shape = new Polygon(); //Tämä tekee kolmion mikä esittää vihollisen alusta
        shape.getPoints().addAll(300.0, 25.0,
                275.0, 50.0,
                250.0, 50.0,
                200.0, 100.0,
                150.0, 100.0,
                125.0, 125.0,
                125.0, 175.0,
                75.0, 250.0,
                -50.0, 250.0,
                -50.0, 225.0,
                -100.0, 175.0,
                -150.0, 175.0,
                -150.0, 100.0,
                -200.0, 75.0,
                -200.0, 35.0,
                -175.0, 35.0,
                -250.0, 25.0,

                -250.0, -25.0,
                -175.0, -35.0,
                -200.0, -35.0,
                -200.0, -75.0,
                -150.0, -100.0,
                -150.0, -175.0,
                -100.0, -175.0,
                -50.0, -225.0,
                -50.0, -250.0,
                75.0, -250.0,
                125.0, -175.0,
                125.0, -125.0,
                150.0, -100.0,
                200.0, -100.0,
                250.0, -50.0,
                275.0, -50.0,
                300.0, -25.0);
        drawShip(shape);



        controller = GameController.getInstance();
        this.setPosition(initialPosition.getX(), initialPosition.getY());
        this.setTag(BOSS_SHIP_TAG);
        this.setVelocity(30);
        setUnitSize(6);
        controller.addUnitToCollisionList(this);
        armShip();

        rotate(180);
        setIsMoving(true);
        setHp(1000);
        originalHp = getHp();
        this.setHitbox(450);


        controller.addUpdateableAndSetToScene(this);
        controller.addHitboxObject(this);

    }



    /**
     * Päivittää pomon liikkumisen, ampumisen, värin kun ottaa vahinkoa ja healthbarin. Kutsu VAIN gameloopista!
     * @param deltaTime Kulunut aika viime päivityksestä.
     */
    @Override
    public void update(double deltaTime){

        if(getTookDamage()){
            tookDamage2 = true;
            damagedTimeCounter = 0;
            setTookDamage(false);
        }
        if(tookDamage2 && damagedTimeCounter > 0.1){
            tookDamage2 = false;
            setOriginalColor();
            damagedTimeCounter = 0;
        }
        else if(tookDamage2){
            damagedTimeCounter += deltaTime;
        }

        if(stageTimeCounter > 14){
            getPrimaryWeapons().get(4).shoot();
            getPrimaryWeapons().get(5).shoot();
            stageTimeCounter = 0;
        }
        else if (fireRateCounter >= fireRate && stageTimeCounter > 7) {
            fireRateCounter = 0;
            switch (currentLaser) {
                case 0:
                    getSecondaryWeapon().shoot();
                    getPrimaryWeapons().get(0).shoot();
                    break;
                case 1:
                    getPrimaryWeapons().get(1).shoot();
                    break;
                case 2:
                    getSecondaryWeapon().shoot();
                    getPrimaryWeapons().get(2).shoot();
                    break;
                case 3:
                    getPrimaryWeapons().get(3).shoot();
                    break;
            }
            if(lasersTopToDown){
                currentLaser++;
                if(currentLaser >= 3){
                    lasersTopToDown = false;
                }
            }
            else{
                currentLaser--;
                if(currentLaser < 1){
                    lasersTopToDown = true;
                }
            }
        }
        fireRateCounter += deltaTime;
        stageTimeCounter += deltaTime;

        moveStep(deltaTime);
        if(!inFightingStage){
            double distanceToTargetX = Math.abs(getXPosition() - (WINDOW_WIDTH * 0.8));
            setVelocity(distanceToTargetX * deltaTime * 200);
            if(distanceToTargetX < 5){
                inFightingStage = true;
                lockDirection(270);
                setVelocity(70);
                stageTimeCounter = 6;
            }
        }
        else if(movingDown){
            if(getYPosition() > WINDOW_HEIGHT * 0.55){
                lockDirection(90);
                movingDown = false;
            }
        }
        else{
            if (getYPosition() < WINDOW_HEIGHT * 0.45){
                lockDirection(270);
                movingDown = true;
            }
        }
        // chekkaa menikö ulos ruudulta
        if (getXPosition() < -100
                || getXPosition() > WINDOW_WIDTH+200
                || getYPosition() < -100
                || getYPosition() > WINDOW_HEIGHT+100) {
            destroyThis();
        }
        controller.setHealthbar(hpPercentage(), 0);
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

    /** varustaa aluksen aseilla */
    public void armShip(){

        Weapon laserGun = new LaserGun(0, 0.8, new Point2D(0, -240), new Point2D(0, -240));
        this.addPrimaryWeaponWithCustomOffsets(laserGun);
        laserGun = new LaserGun(0, 0.8, new Point2D(200, -70), new Point2D(200, -70));
        this.addPrimaryWeaponWithCustomOffsets(laserGun);
        laserGun = new LaserGun(0, 0.8, new Point2D(200, 70), new Point2D(200, 70));
        this.addPrimaryWeaponWithCustomOffsets(laserGun);
        laserGun = new LaserGun(0, 0.8, new Point2D(0, 240), new Point2D(0, 240));
        this.addPrimaryWeaponWithCustomOffsets(laserGun);


        Weapon blasterSprinkler = new BlasterSprinkler(2, 26, 5, new Point2D(-20, -110),
                new Point2D(-20, -110));
        this.addPrimaryWeaponWithCustomOffsets(blasterSprinkler);
        blasterSprinkler = new BlasterSprinkler(2, 26, 5, new Point2D(-20, 110),
                new Point2D(-20, 110));
        this.addPrimaryWeaponWithCustomOffsets(blasterSprinkler);


        Weapon rocketLauncher = new RocketLauncher(2, 4.8, true, new Point2D(-15, 0), new Point2D(-15, 0));
        this.setSecondaryWeaponWithCustomOffsets(rocketLauncher);
    }
}
