package model;

import controller.Controller;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import model.weapons.Blaster;
import model.weapons.Weapon;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static view.GameMain.*;

/**
 * Threadin alaluokka, hoitaa vihollisten spawnauksen peliin.
 */
public class Level extends Thread {

    /**
     * Millisekunteina spawnausajan lyhyin aika, pohjalukema johon vaikuttaa konstruktorin
     * parametrina annettu spawnFrequencyModifier.
     */
    private final int BASE_SPAWN_FREQ_LOW = 3000;

    /**
     * Millisekunteina spawnausajan pisin aika, pohjalukema johon vaikuttaa konstruktorin
     * parametrina annettu spawnFrequencyModifier.
     */
    private final int BASE_SPAWN_FREQ_HIGH = 6000;

    /**
     * Viittaus pelin kontrolleriin, mahdollistaa vihollisolioiden lisäämisen peliin.
     */
    private Controller controller;

    /**
     * Lista, joka sisältää kyseisessä tasossa esiintyvät eri vihollistyypit.
     */
    private ArrayList<Enemy> enemyTypes;

    /**
     * Vihollisten nykyinen jäljellä oleva lukumäärä.
     */
    private int numberOfEnemies;

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

    /**
     * Nykyisen tason numero.
     * TODO: voidaan käyttää tasokohtaisen bossin määrittämiseen.
     */
    private int levelNumber;

    /**
     * Viittaa tason viimeiseen viholliseen (Bossiin). Käytetään tason loppumisen tarkkailuun.
     */
    private Updateable lastEnemy;
    private Boss boss = new Boss();


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
    public Level(Controller controller, ArrayList<Enemy> enemyTypes, int numberOfEnemies, double spawnFrequencyModifier,
                 double enemyHealthModifier, double enemyDamageModifier, int levelNumber) {
        this.controller = controller;
        this.enemyTypes = enemyTypes;
        this.numberOfEnemies = numberOfEnemies;
        this.spawnFrequencyModifier = spawnFrequencyModifier;
        this.enemyHealthModifier = enemyHealthModifier;
        this.enemyDamageModifier = enemyDamageModifier;
        this.levelNumber = levelNumber;
        boss.constructBosses(controller);
    }

    @Override
    public void run() {
        try {
            Thread.sleep(200);
            hegenTestausMetodi();

            Thread.sleep(900);
            hegenTestausMetodi();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // Level thread pyörii niin kauan, kunnes kaikki viholliset on spawnattu.
        try {
            while (numberOfEnemies > 0 || controller.getCollisionList().contains(lastEnemy)) {



                // paussi vihollisten välillä
                // TODO: Monen vihollisen yhtäaikainen spawnaus
                if(numberOfEnemies > 0) {
                    long sleepTime = ThreadLocalRandom.current().nextLong((long)(BASE_SPAWN_FREQ_LOW * spawnFrequencyModifier),
                            (long)(BASE_SPAWN_FREQ_HIGH * spawnFrequencyModifier + 1));
                    Thread.sleep(sleepTime);



                    // arvotaan spawnauspaikka
                    double randomYPos = ThreadLocalRandom.current().nextDouble(50, WINDOW_HEIGHT - 50);

                    // arvotaan vihollinen tyyppilistasta
                    Enemy enemyType = enemyTypes.get(ThreadLocalRandom.current().nextInt(enemyTypes.size()));
                    Enemy enemy = new Enemy(controller, Color.YELLOW, enemyType.getMovementPattern(), WINDOW_WIDTH + 50, randomYPos, ENEMY_SHIP_TAG);
                    enemy.setHp((int)(enemy.getHp() * enemyHealthModifier));
                    Component blaster = new Blaster(controller, enemy, "triangle", 5, 2, 50, 0, Color.CORAL,
                            20, 100, 0);
                    enemy.addToPrimaryWeapons((Weapon) blaster);

                    controller.addUpdateable(enemy);
                    // Kun vihuja on yksi jäljellä, tallennetaan se lastEnemyyn. While loopista poistutaan kun
                    // lastEnemy on poistuu collisionListiltä, eli on tuhottu tai poistuu ruudulta.
                    if (numberOfEnemies == 1) {
                        Thread.sleep(5000);
                        boss = boss.bossList.get(0);
                        lastEnemy = boss;
                        controller.addUpdateable(boss);
                    }
                    numberOfEnemies--;
                }
            }
            // Levelin viholliset spawnattu, venataan vähän aikaa ennen levelin loppumista

            System.out.println("Voitit tason " + (levelNumber) +"!");
            Platform.runLater(() -> controller.addScore(500));

            // Ilmoita levelin loppumisesta
            Platform.runLater(() -> controller.returnToMain());
           //controller.startLevel(levelNumber+1);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void hegenTestausMetodi(){
        //enem_tracker testausta varten
        Point2D[] path = {new Point2D(WINDOW_WIDTH * 0.7,100),
                new Point2D(WINDOW_WIDTH * 0.9, 100),
                new Point2D(WINDOW_WIDTH * 0.9, 650)};
        Point2D[] path2 = {new Point2D(WINDOW_WIDTH * 0.7,WINDOW_HEIGHT - 100),
                new Point2D(WINDOW_WIDTH * 0.82, WINDOW_HEIGHT - 100),
                new Point2D(WINDOW_WIDTH * 0.82, WINDOW_HEIGHT - 650)};

        TrackerEnemy trackerEnemy = new TrackerEnemy(controller, Color.DEEPSKYBLUE, 0,
                WINDOW_WIDTH / 2, -50, path, ENEMY_SHIP_TAG);
        trackerEnemy.setHp((int)(trackerEnemy.getHp() * enemyHealthModifier));
        Weapon blaster = new Blaster(controller, trackerEnemy, "triangle", 5, 2, 50, 0, Color.DEEPSKYBLUE,
                20, 100, 0);
        trackerEnemy.addToPrimaryWeapons(blaster);
        controller.addUpdateable(trackerEnemy);

        trackerEnemy = new TrackerEnemy(controller, Color.DEEPSKYBLUE, 0,
                WINDOW_WIDTH  * 0.5, WINDOW_HEIGHT + 50, path2,  ENEMY_SHIP_TAG);
        trackerEnemy.setHp((int)(trackerEnemy.getHp() * enemyHealthModifier));
        blaster = new Blaster(controller, trackerEnemy, "triangle", 5, 2, 0, 0, Color.DEEPSKYBLUE,
                20, 50, 0);
        trackerEnemy.addToPrimaryWeapons(blaster);
        controller.addUpdateable(trackerEnemy);
    }
}
