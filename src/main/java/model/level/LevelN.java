package model.level;

import controller.Controller;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import model.*;
import model.weapons.Blaster;
import model.weapons.Weapon;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static view.GameMain.*;

/**
 * Threadin alaluokka, hoitaa vihollisten spawnauksen peliin.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class LevelN extends Thread implements Level {

    /** Millisekunteina spawnausajan lyhyin aika, pohjalukema johon vaikuttaa konstruktorin
     * parametrina annettu spawnFrequencyModifier. */
    private final int BASE_SPAWN_FREQ_LOW = 3000;

    /** Millisekunteina spawnausajan pisin aika, pohjalukema johon vaikuttaa konstruktorin
     * parametrina annettu spawnFrequencyModifier. */
    private final int BASE_SPAWN_FREQ_HIGH = 6000;

    /** Jos true, thread on käynnissä, muuten false */
    private volatile boolean isRunning = true;

    /** Jos true, thread on käynnissä tai tauolla. Jos false,
     * thread lakkaa olemasta. */
    private volatile boolean isAlive = true;

    /** Viittaus pelin kontrolleriin, mahdollistaa vihollisolioiden lisäämisen peliin. */
    private Controller controller;

    /**
     * Lista, joka sisältää kyseisessä tasossa esiintyvät eri vihollistyypit.
     */
    private ArrayList<Enemy> enemyTypes;

    /**
     * Vihollisten nykyinen jäljellä oleva lukumäärä.
     */
    private int numberOfEnemies;

    /** TODO */
    public double getEnemyHealthModifier(){
        return enemyHealthModifier;
    }

    /**
     * Kerroin, joka vaikuttaa vihollisten spawnauksen aikahaarukkaan.
     * BASE_SPAWN_FREQ_LOW ja -HIGH kerrotaan tällä lukemalla, joten luvut > 1 lisäävät spawnausaikaa
     * ja luvut < 1 lyhentävät spawnausaikaa.
     */
    private double spawnFrequencyModifier;

    /**
     * Kerroin, joka vaikuttaa vihollisten hitpointsien määrään.
     * Vihollisen hitpointsit kerrotaan tällä luvulla, joten luvut > 1 lisäävät hitpointseja
     * ja luvut < 1 vähentävät hitpointseja.
     */
    private double enemyHealthModifier;

    /**
     * Kerroin, joka vaikuttaa vihollisten aseiden tekemään vahinkoon.
     * Vihollisten aseiden tekemä vahinko kerrotaan tällä luvulla, joten luvut > 1 lisäävät vahinkoa
     * ja luvut < 1 vähentävät vahinkoa.
     * TODO: ei käytössä
     */
    private double enemyDamageModifier;

    /** Nykyisen tason numero.
     * TODO: voidaan käyttää tasokohtaisen bossin määrittämiseen. */
    private int levelNumber;

    /**
     * Viittaa tason viimeiseen viholliseen (Bossiin). Käytetään tason loppumisen tarkkailuun.
     */
    private Updateable lastEnemy;


    /**
     * Konstruktori.
     * @param controller Pelin kontrolleri.
     * @param enemyTypes Lista joka sisältää tasossa ilmentyvät vihollistyypit.
     * @param numberOfEnemies Vihollisten kokonaislukumäärä tason aikana.
     * @param spawnFrequencyModifier Kerroin, joka lyhentää/kasvattaa spawnausaikahaarukkaa.
     * @param enemyHealthModifier Kerroin, joka kasvattaa/pienentää vihollisten hitpointseja.
     * @param enemyDamageModifier Kerroin, joka kasvattaa/pienentää vihollisten aseiden tekemää vahinkoa. TODO: ei käytös
     * @param levelNumber Tason numero.
     */
    public LevelN(Controller controller, ArrayList<Enemy> enemyTypes, int numberOfEnemies, double spawnFrequencyModifier,
                  double enemyHealthModifier, double enemyDamageModifier, int levelNumber) {
        this.controller = controller;
        this.enemyTypes = enemyTypes;
        this.numberOfEnemies = numberOfEnemies;
        this.spawnFrequencyModifier = spawnFrequencyModifier;
        this.enemyHealthModifier = enemyHealthModifier;
        this.enemyDamageModifier = enemyDamageModifier;
        this.levelNumber = levelNumber;
    }

    @Override
    public void continueLevel() {
        isRunning = true;
    }

    @Override
    public void destroyLevel() {
        isAlive = false;
    }

    @Override
    public void pauseLevel() {
        isRunning = false;
    }

    @Override
    public void startLevel() {
        start();
    }

    @Override
    public void run() {

        // LevelN thread pyörii niin kauan, kunnes kaikki viholliset on spawnattu.
        try {
            while (numberOfEnemies > 0 || controller.getCollisionList().contains(lastEnemy)) {
                if (!isAlive) {
                    break;
                } else if (isRunning) {
                    // luodaan laster jonka voi asettaa myöhemmin aseeksi vihollisille
                    ArrayList blaster = new ArrayList<Weapon>();
                    blaster.add(new Blaster(controller, 2, 100, new Point2D(0, 0), new Point2D(20, 0)));


                    // paussi vihollisten välillä
                    // TODO: Monen vihollisen yhtäaikainen spawnaus
                    if (numberOfEnemies > 0) {
                        long sleepTime = ThreadLocalRandom.current().nextLong((long) (BASE_SPAWN_FREQ_LOW * spawnFrequencyModifier),
                                (long) (BASE_SPAWN_FREQ_HIGH * spawnFrequencyModifier + 1));
                        Thread.sleep(sleepTime);


                        // arvotaan spawnauspaikka
                        double randomYPos = ThreadLocalRandom.current().nextDouble(50, WINDOW_HEIGHT - 50);

                        // arvotaan vihollinen tyyppilistasta
                        Enemy enemyType = enemyTypes.get(ThreadLocalRandom.current().nextInt(enemyTypes.size()));
                        Enemy enemy = new Enemy(controller, Color.YELLOW, blaster, enemyType.getMovementPattern(), new Point2D(WINDOW_WIDTH + 50,
                                randomYPos));
                        enemy.setHp((int) (enemy.getHp() * enemyHealthModifier));



                        // Kun vihuja on yksi jäljellä, tallennetaan se lastEnemyyn. While loopista poistutaan kun
                        // lastEnemy on poistuu collisionListiltä, eli on tuhottu tai poistuu ruudulta.
                        if (numberOfEnemies == 1) {

                            Thread.sleep(2000);
                            lastEnemy = new Boss(controller, 1000, WINDOW_WIDTH + 100, WINDOW_HEIGHT * 0.5);
                            //controller.addUpdateableAndSetToScene(lastEnemy, lastEnemy);
                        }
                        numberOfEnemies--;
                    }
                }
            }


            // Ilmoita levelin loppumisesta
            Platform.runLater(() -> controller.returnToMain());
            //controller.startLevel(levelNumber+1);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
