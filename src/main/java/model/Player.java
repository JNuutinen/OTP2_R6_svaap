package model;

import controller.Controller;
import controller.GameController;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import model.weapons.Weapon;

import static view.GameMain.*;

/**
 * Pelaajaluokka. Unitin alaluokka. Sisältää pelaajan käsittelymetodeita sekä näppäinpainalluksien kuuntelun.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class Player extends Unit {

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
     * Laskuri pääaseen ampumisesta kuluneeseen aikaan. Vertaillaan pääaseen tulinopeuteen.
     */
    private double fireRateCounter;

    /**
     * Viime päivityksestä kulunut aika.
     */
    private double deltaTime = 0;

    /**
     * Laskuri sivuaseen ampumisesta kuluneeseen aikaan. Vertaillaan sivuaseen tulinopeuteen.
     */
    private double secondaryFirerateCounter;

    /**
     * Maximi hitpointsit.
     */
    private final int MAX_HP = 200;

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
     * Konstruktori.
     * @param shipColor Aluksen väri.
     */
    public Player(Color shipColor) {
        super(shipColor, 0, 0);
        setTag(PLAYER_SHIP_TAG);
        controller = GameController.getInstance();
        setHp(MAX_HP );
        setHitbox(60);
        controller.addUnitToCollisionList(this);

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
        //controller.addUpdateableAndSetToScene(this);
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
        if(deltaTime < 1) { // TODO
            setPosition(getXPosition() + xVelocity * deltaTime, getYPosition() + yVelocity * deltaTime);
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
    public int getMaxHp() {
        return MAX_HP;
    }

    /**
     * Palauttaa pelaajan pistemäärän.
     * @return Pelaajan pisteet.
     */
    public int getScore() {
        return score;
    }


    // laske ja lisaa vauhtia alukseen riippuen sen nykyisestä nopeudesta ja sen suunnasta: x/yVelocity

    /**
     * Lisää aluksen vauhtia riippuen sen nykyisestä nopeudesta, suunnasta ja kuluneesta ajasta.
     * @param directionX Nopeus suuntaan x.
     * @param directionY Nopeus suuntaan y.
     */
    private void addVelocity(double directionX, double directionY) {
        if (directionX == 0) ;
        else if (directionX * xVelocity >= 0) { // jos kiihdyttaa nopeuden suuntaan, eli lisaa vauhtia:
            if (xVelocity < maxVelocity && xVelocity > maxVelocity * -1) { //jos alle maksiminopeuden (sama vastakkaiseen suuntaan)
                xVelocity += directionX * deltaTime * accelerationForce;
            } else { // jos ylittaa maksiminopeuden
                xVelocity = maxVelocity * directionX;
            }
        } else { // jos kiihdyttaa nopeuden vastaiseen suuntaan, eli hidastaa
            xVelocity += directionX * deltaTime * accelerationForce;
        }
        //samat Y:lle
        if (directionY == 0) ;
        else if (directionY * yVelocity >= 0) { // jos kiihdyttaa nopeuden suuntaan, eli lisaa vauhtia:
            if (yVelocity < maxVelocity && yVelocity > maxVelocity * -1) { //jos alle maksiminopeuden (sama vastakkaiseen suuntaan)
                yVelocity += directionY * deltaTime * accelerationForce;
            } else { // jos ylittaa maksiminopeuden
                yVelocity = maxVelocity * directionY;
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
}
