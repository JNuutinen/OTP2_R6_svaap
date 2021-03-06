package model.units;

import Multiplayer.Multiplayer;
import controller.Controller;
import controller.GameController;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import model.Tag;
import model.weapons.Weapon;

import static view.GameMain.*;

/**
 * Pelaajaluokka. Unitin alaluokka. Sisältää pelaajan käsittelymetodeita sekä näppäinpainalluksien kuuntelun.
 *
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class Player extends Unit {

    private int playerId;

    /**
     * Pelin kontrolleri.
     */
    private Controller controller;

    /**
     * Nopeus suuntaan x.
     */
    private double xVelocity;

    /**
     * Nopeus suuntaan y.
     */
    private double yVelocity;

    /**
     * Pelaajan pisteet.
     */
    private int score = 0;

    /**
     * Viime päivityksestä kulunut aika.
     */
    private double deltaTime = 0;

    /**
     * Maximi hitpointsit.
     */
    private static final int MAX_HP = 1000;

    /**
     * Alusta kiihdyttävän voiman suuruus.
     */
    private final double accelerationForce = 5000;

    /**
     * Aluksen maximinopeus.
     */
    private final double maxVelocity = 300.0;

    /**
     * Alusta hidastava kitkavoima, kun kiihdytystä ei ole.
     */
    private final double decelerateForce = 1000;

    /**
     * Laskuri, jota käytetään aluksen ottaessa osumaa, osumaefektiä varten.
     */
    private double damagedTimeCounter = 0;

    /**
     * Ilmoittaa, kun alus ottaa osumaa.
     */
    private boolean tookDamage2 = false;

    /**
     * Apumuuttuja asevahingon kertoimen ajanlaskua varten.
     */
    private double damageMultipliedCounter = 0;

    /**
     * Apumuuttuja
     */
    private boolean damageMultiplied = false;

    /**
     * Apumuuttuja nopeuden kertoimen ajanlaskua varten.
     */
    private double velocityMulitpliedCounter = 0;

    /**
     * Apumuuttuaj
     */
    private boolean velocityMultiplied = false;

    /**
     * Aluksen nopeuden kerroin.
     */
    private double velocityMultiplier = 1;

    /**
     * Konstruktori.
     * @param shipColor Aluksen väri.
     */
    public Player(Color shipColor) {
        super(shipColor, 0, 0);
        setTag(Tag.SHIP_PLAYER);
        controller = GameController.getInstance();
        setHp(MAX_HP);
        setHitbox(60);
        setIsMoving(false);

        Polygon shape = new Polygon();
        shape.getPoints().addAll(60.0, 1.0,
                6.0, 14.0,
                -8.0, 40.0,
                -20.0, 40.0,
                -26.0, 14.0,
                -40.0, 26.0,
                -46.0, 26.0,
                -40.0, 6.0,
                -32.0, 6.0,
                -32.0, -6.0,
                -40.0, -6.0,
                -46.0, -26.0,
                -40.0, -26.0,
                -26.0, -14.0,
                -20.0, -40.0,
                -8.0, -40.0,
                6.0, -14.0,
                60.0, -1.0);

        drawShip(shape);
        //controller.addTrace(this);

        playerId = (int)Math.random() * 100;

    }

    public int getPlayerId() {
        return playerId;
    }

    @Override
    public void update(double deltaTime){

        if(getTookDamage()){
            tookDamage2 = true;
            damagedTimeCounter = 0;
            setTookDamage(false);
        }
        if(tookDamage2 && damagedTimeCounter > 0.2){
            tookDamage2 = false;
            setOriginalColor();
            damagedTimeCounter = 0;
        }
        else if(tookDamage2){
            damagedTimeCounter += deltaTime;
        }

        this.deltaTime = deltaTime;
        if (input.contains("A")) {
            // TODO: 70px kovakoodattu
            if (getXPosition() > 70) {
                addVelocity(-1 * velocityMultiplier, 0);
            } else {
                decelerateX();
            }
        } else if (input.contains("D")) {
            if (getXPosition() < PLAYER_X_LIMIT) {
                addVelocity(1 * velocityMultiplier, 0);
            } else {
                decelerateX();
            }
        } else if(xVelocity != 0) {
            decelerateX();
        }

        if (input.contains("W")) {
            if (getYPosition() > 50) {
                addVelocity(0, -1 * velocityMultiplier);
            } else {
                decelerateY();
            }
        } else if (input.contains("S")) {
            if (getYPosition() < WINDOW_HEIGHT - 150) {
                addVelocity(0, 1 * velocityMultiplier);
            } else {
                decelerateY();
            }
        } else if(yVelocity != 0) {
            decelerateY();
        }

        // Primary fire
        if (input.contains("J")) {
            if (getPrimaryWeapons().get(0) != null) {
                for(Weapon primaryWeapon : getPrimaryWeapons()){
                    primaryWeapon.shoot();
                    Multiplayer.shootPrimary();
                }
            }
        }

        // Secondary fire
        if (input.contains("K")) {
            if (getSecondaryWeapon() != null) {
                if (getSecondaryWeapon() != null) {
                    shootSecondary();
                    Multiplayer.shootSecondary();
                }
            }
        }

        if (input.contains("V")) System.exit(0);

        /* TODO: pause ei toimi joten ei anneta laittaa pauselle
        if (input.contains(KeyCode.ESCAPE.toString())) {
            controller.pauseGame();
        }
        */

        // Päivitä sijainti
        if(deltaTime < 1) { // TODO
            setPosition(getXPosition() + (xVelocity * deltaTime), getYPosition() + (yVelocity * deltaTime));
            Multiplayer.move(getXPosition() + (xVelocity * deltaTime), getYPosition() + (yVelocity * deltaTime));
        }

        if(damageMultiplied && damageMultipliedCounter > 8){
            damageMultiplied = false;
            for(Weapon weapon : getPrimaryWeapons()){
                weapon.setDamageMultiplier(1);
            }
            getSecondaryWeapon().setDamageMultiplier(1);
        }
        else{
            damageMultipliedCounter += deltaTime;
        }

        if(velocityMultiplied && velocityMulitpliedCounter > 8){
            velocityMultiplied = false;
            velocityMultiplier = 1;
        }
        else{
            velocityMulitpliedCounter += deltaTime;
        }
        //System.out.println("player " + (getAngleFromTarget(new Point2D(0, 0))));
        controller.setHealthbar(hpPercentage(), 1);
    }

    /**
     * Kasvattaa pelaajan pistemäärää.
     * @param points Pisteet, jotka lisätään pelaajalle.
     */
    public void addScore(int points){
        score += points;
        if(score < 0){
            score = 0;
        }
    }

    @Override
    public void addHP(int hp){
        this.hp += hp;
        controller.setHealthbar(hpPercentage(), 1);
    }

    /**
     * Palauttaa pelaajan maximihitpointsit.
     * @return Pelaajan maximihitpointsit.
     */
    public static int getMaxHp() {
        return MAX_HP;
    }

    /**
     * Palauttaa pelaajan pistemäärän.
     * @return Pelaajan pisteet.
     */
    public int getScore() {
        return score;
    }

    /**
     * Lisää aluksen vauhtia riippuen sen nykyisestä nopeudesta, suunnasta ja kuluneesta ajasta.
     * @param directionX Nopeus suuntaan x.
     * @param directionY Nopeus suuntaan y.
     */
    private void addVelocity(double directionX, double directionY) {
        if (directionX == 0) ;
        else if (directionX * xVelocity >= 0) { // jos kiihdyttaa nopeuden suuntaan, eli lisaa vauhtia:
            //jos alle maksiminopeuden (sama vastakkaiseen suuntaan)
            if (xVelocity < maxVelocity * velocityMultiplier && xVelocity > maxVelocity * -1 * velocityMultiplier) {
                xVelocity += directionX * deltaTime * accelerationForce;
            } else { // jos ylittaa maksiminopeuden
                xVelocity = maxVelocity * directionX * velocityMultiplier;
            }
        } else { // jos kiihdyttaa nopeuden vastaiseen suuntaan, eli hidastaa
            xVelocity += directionX * deltaTime * accelerationForce;
        }
        //samat Y:lle
        if (directionY == 0) ;
        else if (directionY * yVelocity >= 0) { // jos kiihdyttaa nopeuden suuntaan, eli lisaa vauhtia:
            //jos alle maksiminopeuden (sama vastakkaiseen suuntaan)
            if (yVelocity < maxVelocity * velocityMultiplier && yVelocity > maxVelocity * -1 * velocityMultiplier) {
                yVelocity += directionY * deltaTime * accelerationForce;
            } else { // jos ylittaa maksiminopeuden
                yVelocity = maxVelocity * directionY * velocityMultiplier;
            }
        } else { // jos kiihdyttaa nopeuden vastaiseen suuntaan, eli hidastaa
            yVelocity += directionY * deltaTime * accelerationForce;
        }

    }

    /**
     * Hidastaa aluksen vauhtia suunnassa x.
     */
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

    /**
     * Hidastaa aluksen vauhtia suunnassa y.
     */
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

    /**
     * Palauttaa pelaajan nykyisten hitpointsien määrän prosentteina maximin suhteen.
     * @return Pelaajan hitpointsit prosentteina maximiin nähden.
     */
    public int hpPercentage(){
        int tenthHp = this.getOriginalHp() / 10;
        int percentage = getHp() / tenthHp;
        if (percentage == 0 && getHp() > 0){
            return 1;
        }else {
            return percentage;
        }
    }

    public void setDamageMultiplier(double damageMultiplier){
        damageMultiplied = true;
        damageMultipliedCounter = 0;
        for(Weapon weapon : getPrimaryWeapons()){
            weapon.setDamageMultiplier(damageMultiplier);
        }
        getSecondaryWeapon().setDamageMultiplier(damageMultiplier);
    }

    public void setVelocityMulitplier(double velocityMultiplier){
        velocityMultiplied = true;
        velocityMulitpliedCounter = 0;
        this.velocityMultiplier = velocityMultiplier;
    }
}
