package model;

import controller.Controller;
import controller.GameController;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import static view.GameMain.WINDOW_HEIGHT;
import static view.GameMain.WINDOW_WIDTH;

public class Boss2 extends Unit {
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
     * ampumisen ajanlaskuri
     */
    private double fireRateCounter = 0;

    /**
     * apumuuttuja ampumistilan ajanlaskun yhteydessä
     */
    private double stageTimeCounter = 0;

    /**
     * liikkuuko alaspäin hyökkaystilassa
     */
    private boolean movingDown = true;

    /**
     * apumuuttuja ammuttavien laserien muistamiseksi
     */
    private int currentLaser = 0;

    /**
     * apumuuttuja laserien ampumisjärjestyksen hallitsemiseksi
     */
    private boolean lasersTopToDown = true;

    /**
     * apumuuttuja vahinkoefektin ajanlaskun yhteydessä
     */
    private double damagedTimeCounter = 0;

    /**
     * Joku apumuuttuja.
     */
    private boolean tookDamage2 = false;

    /**
     * apumuuttuja tilan yhteydessä
     */
    private boolean inFightingStage = false;

    public Boss2(Point2D initialPosition){
        super(Color.ORANGE, 0, 0);
        Polygon shape = new Polygon(); //Tämä tekee kolmion mikä esittää vihollisen alusta
        shape.getPoints().addAll(
                -200.0, 0.0,
                -250.0, 40.0,
                -100.0, 80.0,
                -100.0, 40.0,
                0.0, 0.0,
                -100.0, -40.0,
                -100.0, -80.0,
                -250.0, -40.0,
                -200.0, 0.0
        );
        drawShip(shape);

        controller = GameController.getInstance();
        this.setPosition(initialPosition.getX(), initialPosition.getY());
        this.setTag(Tag.SHIP_BOSS);
        this.setVelocity(30);
        setUnitSize(6);
        //armShip();

        rotate(180);
        setIsMoving(true);
        setHp(1000);
        originalHp = getHp();
        this.setHitbox(200);

        controller.addUpdateableAndSetToScene(this);
        controller.addHitboxObject(this);
    }

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

        //TODO ampuminen


        fireRateCounter += deltaTime;

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

}
