package model;

import controller.Controller;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;


import java.awt.geom.GeneralPath;
import java.util.ArrayList;

import static view.GameMain.input;

public class Player extends Unit implements Updateable {
    private Controller controller;
    private double xVelocity;
    private double yVelocity;
    private int score = 0;
    private double fireRate = 0.1;//sekunneissa
    private double fireRateCounter = 0;
    private double deltaTime = 0;

    private final double accelerationForce = 5000; // voima joka kiihdyttaa alusta
    private final double maxVelocity = 300.0; // maksiminopeus
    private final double decelerateForce = 1000; // kitkavoima joka  hidastaa alusta jos nappia ei paineta

    public Player(Controller controller) {
        super(controller);
        this.controller = controller;
        this.setHp(60);
        controller.addUnitToCollisionList(this);
        drawShip("player");



        Component b = new Component("circle", 10, 0, Color.RED);
        components.add(b);

        Component c = new Component("rectangle", 10 , 0, Color.WHITE);
        components.add(c);

        Component d = new Component("triangle", 10, 0, Color.BLUE);
        components.add(d);

        equipComponents(components, "player");

    }


    @Override
    public void update(double deltaTime){
        this.deltaTime = deltaTime;
        resetVelocity();
        if (input.contains("A")) addVelocity(-1, 0);
        else if (input.contains("D")) addVelocity(1, 0);
        else if(xVelocity != 0){ decelerateX();
        }
        if (input.contains("W")) addVelocity(0, -1);
        else if (input.contains("S")) addVelocity(0, 1);
        else if(yVelocity != 0){ decelerateY();
        }

        if (input.contains("O")) {
            if(fireRateCounter > fireRate){
                fireRateCounter = 0;
                spawnProjectile();
            }
        }
        fireRateCounter += deltaTime;
        //System.out.println(xVelocity);
        if (input.contains("V")) System.exit(0);
        setPosition(getXPosition() + xVelocity * deltaTime, getYPosition() + yVelocity * deltaTime);

    }

    public void addScore(int points){
        score += points;
    }

    /*
    public void setScore(int points) {
        score = points;
    }
    */

    public int getScore() {
        return score;
    }

    public void collides(Updateable collidingUpdateable){
        // tagin saa: collidingUpdateable.getTag()
    }

    public Updateable getUpdateable(){
        return this;
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
        /* TAA OTTI HUOMIOON KIIHTYVYYSKERTOIMEN MUTTA SE OLI PERSEESTA TODO HEGE POISTAA
        if(directionY==0);
        else if(directionY * yVelocity >= 0){ // jos kiihdyttaa nopeuden suuntaan, eli lisaa vauhtia:
            if(yVelocity < maxVelocity && yVelocity > maxVelocity * -1) { //jos alle maksiminopeuden (sama vastakkaiseen suuntaan)
                yVelocity *=accelerationMultiplier;
                yVelocity += directionY * deltaTime * accelerationForce;
            }
            else{ // jos ylittaa maksiminopeuden
                yVelocity = maxVelocity * directionY;
            }
        }
        else{
            yVelocity *= (1/accelerationMultiplier);
            yVelocity += directionY * deltaTime * accelerationForce;
        }*/
    }

    // hidasta x suunnassa
    private void decelerateX() {
        if (xVelocity > 0) {
            if (xVelocity < decelerateForce * deltaTime) {
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
            if(yVelocity < decelerateForce * deltaTime){
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

    private void resetVelocity() {
        //xVelocity = 0;
        //yVelocity = 0;
    }

    // TODO: tää sit joskus aseeseen
    private void spawnProjectile(){
        Projectile projectile = new Projectile(controller, this.getPosition(), 0, 10,
                "projectile_player", this);
        controller.addUpdateable(projectile);
    }

    public void destroyThis(){
        controller.removeUpdateable(this);
    }
}
