package model;

import controller.Controller;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import model.weapons.BlasterSprinkler;
import model.weapons.LaserGun;
import model.weapons.RocketLauncher;
import model.weapons.Weapon;

import static view.GameMain.*;
//TODO: javadoc
/**
 * Tason loppupomo 3
 */
public class Boss3 extends Unit implements Updateable {

    /**
     * Pelin kontrolleri
     */
    private Controller controller;

    /**
     * Apumuuttuja pomon alkuperäisestä hp:stä
     */
    private int originalHp;

    /**
     * Apumuuttuja pomon alkuperäisestä hp:stä
     */
    private int originalhp;

    /**
     * tulinopeus
     */
    private double fireRate = 0.7;

    /**
     *  ampumisen ajanlaskuri
     */
    private double fireRateCounter = 0;


    private double stageTimeCounter = 0;

    private boolean movingDown = true;

    private int currentLaser = 0;

    private boolean lasersTopToDown = true;

    private double damagedTimeCounter = 0;
    private boolean tookDamage2 = false;
    private boolean inFightingStage = false;

    public Boss3(Controller controller, double initialX, double initialY){

        super(controller, Color.ORANGE);

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


        this.controller = controller;
        this.setPosition(initialX, initialY);
        this.setTag(BOSS_SHIP_TAG);
        this.setVelocity(30);
        this.controller = controller;
        controller.addUnitToCollisionList(this);
        rotate(180);
        setIsMoving(true);

        setHp(1000);
        originalHp = getHp();
        this.setHitbox(450);

        Component c3 = new Component("triangle", 3, 0, Color.PURPLE, 20, 10);
        addComponent(c3);

        armShip();

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
        setPosition(getXPosition(), getYPosition() + yVelocity * deltaTime);
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

    /**
     * varustaa aluksen aseilla
     */
    public void armShip(){
        //Platform.runLater(() ->
        Weapon laserGun = new LaserGun(controller, this, 0, 0, -240,
                0, -240, 0.6);
        this.addToPrimaryWeapon(laserGun);
        laserGun = new LaserGun(controller, this, 0,  200, -70,
                200, -70, 0.6);
        this.addToPrimaryWeapon(laserGun);
        laserGun = new LaserGun(controller, this, 0, 200, 70,
                200, 70, 0.6);
        this.addToPrimaryWeapon(laserGun);
        laserGun = new LaserGun(controller, this, 0, 0, 240,
                0, 240, 0.6);
        this.addToPrimaryWeapon(laserGun);


        Weapon blasterSprinkler = new BlasterSprinkler(controller, this, 2, -20, -110, Color.ORANGE,
                26, -20, -110, 5);
        this.addToPrimaryWeapon(blasterSprinkler);
        blasterSprinkler = new BlasterSprinkler(controller, this, 2, -20, 110, Color.ORANGE,
                26, -20, 110, 5);
        this.addToPrimaryWeapon(blasterSprinkler);


        Weapon rocketLauncher = new RocketLauncher(controller, this, 2, -15, 0,
                4.8);
        this.setSecondaryWeapon(rocketLauncher);
    }
}
