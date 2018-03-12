package model;

import controller.Controller;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import model.weapons.*;

import static view.GameMain.*;

/**
 * Tason loppupomo
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

    // Ampumisen kovakoodit
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

        Polygon triangle = new Polygon(); //Tämä tekee kolmion mikä esittää vihollisen alusta
        triangle.getPoints().addAll(-100.0, -230.0,
                300.0, 00.0,
                -100.0, 230.0);
        drawShip(triangle);


        this.controller = controller;
        this.setPosition(initialX, initialY);
        this.setTag(BOSS_SHIP_TAG);
        this.setVelocity(30);
        this.controller = controller;
        controller.addUnitToCollisionList(this);
        rotate(180);
        setIsMoving(true);

        setHp(1000);
        this.setHitbox(450);






        armShip();
        //equipComponents();

    }



    /**
     * Päivittää pomon liikkumisen, ampumisen ja healthbarin. Kutsu VAIN gameloopista!
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

    public void armShip(){
        Weapon laserGun = new LaserGun(controller, this, 0, -100, -240,
                -100, -240, 0.6);
        this.addToPrimaryWeapon(laserGun);
        laserGun = new LaserGun(controller, this, 0,  60, -70,
                60, -70, 0.6);
        this.addToPrimaryWeapon(laserGun);
        laserGun = new LaserGun(controller, this, 0, 60, 70,
                60, 70, 0.6);
        this.addToPrimaryWeapon(laserGun);
        laserGun = new LaserGun(controller, this, 0, -100, 240,
                -100, 240, 0.6);
        this.addToPrimaryWeapon(laserGun);


        Weapon blasterSprinkler = new BlasterSprinkler(controller, this, 2, -20, -110, Color.ORANGE,
                26, -20, -110, 5);
        this.addToPrimaryWeapon(blasterSprinkler);
        blasterSprinkler = new BlasterSprinkler(controller, this, 2, -20, 110, Color.ORANGE,
                26, -20, 110, 5);
        this.addToPrimaryWeapon(blasterSprinkler);


        Weapon rocketLauncher = new RocketLauncher(controller, this, 2, -5, 0,
                4.8, true);
        this.setSecondaryWeapon(rocketLauncher);
    }
}
