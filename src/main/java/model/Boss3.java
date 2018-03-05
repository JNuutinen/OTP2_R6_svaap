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
    private int originalhp;

    // Ampumisen kovakoodit
    /**
     * tulinopeus
     */
    private double fireRate = 0.5;
    /**
     *  ampumisen ajanlaskuri
     */
    private double fireRateCounter = 0;

    private double stageTimeCounter = 0;

    private boolean movingDown = true;

    private int currentLaser = 0;

    private boolean lasersTopToDown = true;

    public Boss3(Controller controller, double initialX, double initialY){

        super(controller, Color.ORANGERED);
        this.controller = controller;
        this.setPosition(initialX, initialY);
        this.setTag(BOSS_SHIP_TAG);
        this.setVelocity(60);
        this.controller = controller;
        controller.addUnitToCollisionList(this);
        rotate(180);
        setIsMoving(true);
        lockDirection(270);
        armShip();
        setHp(1000);


        Component c = new Component("triangle", 3, 0, Color.PURPLE, 30, 40);
        components.add(c);
        Component c2 = new Component("triangle", 3, 0, Color.PURPLE, 0, -20);
        components.add(c2);
        Component c3 = new Component("triangle", 3, 0, Color.PURPLE, 20, 10);
        components.add(c3);
        Component c4 = new Component("triangle", 3, 0, Color.PURPLE, 20, -10);
        components.add(c4);
        equipComponents(components);
        this.setHitbox(160);

        Polygon triangle = new Polygon(); //Tämä tekee kolmion mikä esittää vihollisen alusta
        triangle.getPoints().addAll(-200.0, -230.0,
                200.0, 00.0,
                -200.0, 230.0);
        drawShip(triangle);

    }



    /**
     * Päivittää pomon liikkumisen, ampumisen ja healthbarin. Kutsu VAIN gameloopista!
     * @param deltaTime Kulunut aika viime päivityksestä.
     */
    @Override
    public void update(double deltaTime){


        if(stageTimeCounter > 12){
            getPrimaryWeapons().get(4).shoot();
            getPrimaryWeapons().get(5).shoot();
            stageTimeCounter = 0;
        }
        else if (fireRateCounter >= fireRate && stageTimeCounter > 7) {
            fireRateCounter = 0;
            switch (currentLaser) {
                case 0:
                    getPrimaryWeapons().get(0).shoot();
                    break;
                case 1:
                    getPrimaryWeapons().get(1).shoot();
                    break;
                case 2:
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
        if(movingDown){
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
        int tenthHp = originalhp / 10;
        int percentage = getHp() / tenthHp;
        if (percentage == 0 && getHp() > 0){
            return 1;
        }else {
            return percentage;
        }
    }

    public void armShip(){
        Weapon laserGun = new LaserGun(controller, this, "circle", 5, 0, 0, 5, Color.WHITE,
                -140, -240, 0.5);
        this.addToPrimaryWeapons(laserGun);
        laserGun = new LaserGun(controller, this, "circle", 5, 0, 0, 5, Color.WHITE,
                60, -70, 0.5);
        this.addToPrimaryWeapons(laserGun);
        laserGun = new LaserGun(controller, this, "circle", 5, 0, 0, 5, Color.WHITE,
                60, 70, 0.5);
        this.addToPrimaryWeapons(laserGun);
        laserGun = new LaserGun(controller, this, "circle", 5, 0, 0, 5, Color.WHITE,
                -140, 240, 0.5);
        this.addToPrimaryWeapons(laserGun);

        Weapon blasterSprinkler = new BlasterSprinkler(controller, this, "circle", 5, 2, 100, -110, Color.CORAL,
                26, -20, -110, 5);
        this.addToPrimaryWeapons(blasterSprinkler);
        blasterSprinkler = new BlasterSprinkler(controller, this, "circle", 5, 2, 100, -110, Color.CORAL,
                26, -20, 110, 5);
        this.addToPrimaryWeapons(blasterSprinkler);
    }
}
