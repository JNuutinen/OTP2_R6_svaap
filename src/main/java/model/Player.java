package model;

import controller.Controller;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import model.weapons.Weapon;

import static view.GameMain.*;

public class Player extends Unit {
    private Controller controller;
    private double xVelocity;
    private double yVelocity;
    private int score = 0;
    private double fireRate;//sekunneissa
    private double fireRateCounter;
    private double deltaTime = 0;
    private double secondaryFirerate;
    private double secondaryFirerateCounter;
    private final int MAX_HP = 50;
    private final double accelerationForce = 5000; // voima joka kiihdyttaa alusta
    private final double maxVelocity = 300.0; // maksiminopeus
    private final double decelerateForce = 1000; // kitkavoima joka  hidastaa alusta jos nappia ei paineta

    private double damagedTimeCounter = 0;
    private boolean tookDamage2 = false;

    public Player(Controller controller, Color shipColor) {
        super(controller, shipColor);
        setTag(PLAYER_SHIP_TAG);
        this.controller = controller;
        setHp(MAX_HP );
        controller.addUnitToCollisionList(this);

        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(-60.0, -30.0,
                60.0, 00.0,
                -60.0, 30.0);

        drawShip(triangle);

        setHitbox(45);
    }


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

        this.deltaTime = deltaTime;
        resetVelocity(); // TODO: tää kutsu?
        if (input.contains("A")) {
            // TODO: 70px kovakoodattu
            if (getXPosition() > 70) {
                addVelocity(-1, 0);
            } else {
                decelerateX();
            }
        } else if (input.contains("D")) {
            if (getXPosition() < PLAYER_X_LIMIT) {
                addVelocity(1, 0);
            } else {
                decelerateX();
            }
        } else if(xVelocity != 0) {
            decelerateX();
        }

        if (input.contains("W")) {
            // TODO: 50px kovakoodattu
            if (getYPosition() > 50) {
                addVelocity(0, -1);
            } else {
                decelerateY();
            }
        } else if (input.contains("S")) {
            if (getYPosition() < WINDOW_HEIGHT - getHitboxRadius()) {
                addVelocity(0, 1);
            } else {
                decelerateY();
            }
        } else if(yVelocity != 0) {
            decelerateY();
        }

        // Primary fire
        if (input.contains("O")) {
            if (getPrimaryWeapons().get(0) != null) {
                if (fireRateCounter > getPrimaryWeapons().get(0).getFireRate()) {
                    fireRateCounter = 0;
                    for(Weapon primaryWeapon : getPrimaryWeapons()){
                        primaryWeapon.shoot();
                    }
                }
            }
        }

        // Secondary fire
        if (input.contains("P")) {
            if (getSecondaryWeapon() != null) {
                if (secondaryFirerateCounter > getSecondaryWeapon().getFireRate()) {
                    secondaryFirerateCounter = 0;
                    shootSecondary();
                }
            }
        }

        fireRateCounter += deltaTime;
        secondaryFirerateCounter += deltaTime;
        if (input.contains("V")) System.exit(0);

        if (input.contains(KeyCode.ESCAPE.toString())) {
            controller.pauseGame();
        }

        // Päivitä sijainti
        setPosition(getXPosition() + xVelocity * deltaTime, getYPosition() + yVelocity * deltaTime);
        //System.out.println("player " + (getAngleFromTarget(new Point2D(0, 0))));
        controller.setHealthbar(hpPercentage(), 1);
    }

    public void addScore(int points){

        score += points;
        if(score < 0){
            score = 0;
        }
    }

    public void addHP(int hp){
        this.hp += hp;
        controller.setHealthbar(hpPercentage(), 1);
    }

    public int getMaxHp() {
        return MAX_HP;
    }

    public int getScore() {
        return score;
    }


    // laske ja lisaa vauhtia alukseen riippuen sen nykyisestä nopeudesta ja sen suunnasta: x/yVelocity
    private void addVelocity(double directionX, double directionY) {
        if(directionX == 0);
        else if(directionX * xVelocity >= 0) { // jos kiihdyttaa nopeuden suuntaan, eli lisaa vauhtia:
            if (xVelocity < maxVelocity && xVelocity > maxVelocity * -1) { //jos alle maksiminopeuden (sama vastakkaiseen suuntaan)
                xVelocity += directionX * deltaTime * accelerationForce;
            } else { // jos ylittaa maksiminopeuden
                xVelocity = maxVelocity * directionX;
            }
        }
        else{ // jos kiihdyttaa nopeuden vastaiseen suuntaan, eli hidastaa
            xVelocity += directionX * deltaTime * accelerationForce;
        }
        //samat Y:lle
        if(directionY==0);
        else if(directionY * yVelocity >= 0) { // jos kiihdyttaa nopeuden suuntaan, eli lisaa vauhtia:
            if (yVelocity < maxVelocity && yVelocity > maxVelocity * -1) { //jos alle maksiminopeuden (sama vastakkaiseen suuntaan)
                yVelocity += directionY * deltaTime * accelerationForce;
            } else { // jos ylittaa maksiminopeuden
                yVelocity = maxVelocity * directionY;
            }
        }
        else{ // jos kiihdyttaa nopeuden vastaiseen suuntaan, eli hidastaa
            yVelocity += directionY * deltaTime * accelerationForce;
        }
    }

    // hidasta x suunnassa
    private void decelerateX() {
        if (xVelocity > 0) {
            if (xVelocity < decelerateForce * deltaTime) { // pysayta jos nopeus < seuraavan framen nopeus
                xVelocity = 0;
            } else {
                xVelocity -= decelerateForce * deltaTime;
            }
        } else {//xVelociy < 0
            if (xVelocity * -1 < decelerateForce * deltaTime) {
                xVelocity = 0;
            } else {
                xVelocity += decelerateForce * deltaTime;
            }
        }
    }

    // hidasta y suunnassa
    private void decelerateY(){
        if(yVelocity > 0){
            if(yVelocity < decelerateForce * deltaTime){ // pysayta jos nopeus < seuraavan framen nopeus
                yVelocity = 0;
            }
            else {
                yVelocity -= decelerateForce * deltaTime;
            }
        }
        else {//xVelociy < 0
            if (yVelocity * -1 < decelerateForce * deltaTime) {
                yVelocity = 0;
            } else {
                yVelocity += decelerateForce * deltaTime;
            }
        }
    }

    // TODO: ei käytössä?
    private void resetVelocity() {
        //xVelocity = 0;
        //yVelocity = 0;
    }

    public int hpPercentage(){
        int tenthHp = this.getOriginalHp() / 10;
        int percentage = getHp() / tenthHp;
        if (percentage == 0 && getHp() > 0){
            return 1;
        }else {
            return percentage;
        }
    }
}
