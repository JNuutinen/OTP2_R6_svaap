package model.level;

import controller.Controller;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import model.*;
import model.weapons.Blaster;
import model.weapons.LaserGun;
import model.weapons.RocketLauncher;
import model.weapons.Weapon;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static view.GameMain.*;

/**
 * Threadin alaluokka, hoitaa vihollisten spawnauksen peliin.
 */
public class Level3 extends Thread implements Level {

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
     * Jos true, thread on käynnissä, muuten false
     */
    private volatile boolean isRunning = true;

    /**
     * Jos true, thread on käynnissä tai tauolla. Jos false,
     * thread lakkaa olemasta.
     */
    private volatile boolean isAlive = true;

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
     * Viittaa tason viimeiseen pääviholliseen. Käytetään tason loppumisen tarkkailuun.
     */
    private Updateable finalBoss;


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
    public Level3(Controller controller, ArrayList<Enemy> enemyTypes, int numberOfEnemies, double spawnFrequencyModifier,
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
            Enemy enemy;
            Component weapon;
            TrackerEnemy trackerEnemy;

            Thread.sleep(1000);

            // arvotaan spawnauspaikka
            //double randomYPos = ThreadLocalRandom.current().nextDouble(50, WINDOW_HEIGHT 5 100);

            for(int i = 0; i < 3; i++){


                enemy = new Enemy(controller, Color.YELLOW, 0, WINDOW_WIDTH + 50, WINDOW_HEIGHT - 200, ENEMY_SHIP_TAG);
                enemy.setHp((int) (enemy.getHp() * enemyHealthModifier));
                weapon = new Blaster(controller, enemy, 2, 0, 0, Color.CORAL,
                        20, 100, 0);
                enemy.addToPrimaryWeapon((Weapon) weapon);
                controller.addUpdateable(enemy);

                enemy = new Enemy(controller, Color.YELLOW, 0, WINDOW_WIDTH + 50, 100, ENEMY_SHIP_TAG);
                enemy.setHp((int) (enemy.getHp() * enemyHealthModifier));
                weapon = new Blaster(controller, enemy, 2, 0, 0, Color.CORAL,
                        20, 100, 0);
                enemy.addToPrimaryWeapon((Weapon) weapon);
                controller.addUpdateable(enemy);


                Thread.sleep(2000);
            }

            Thread.sleep(1000);

            Point2D[] path = {new Point2D(WINDOW_WIDTH * 0.7,200),
                    new Point2D(WINDOW_WIDTH * 0.9, 100),
                    new Point2D(WINDOW_WIDTH * 0.9, 700)};
            Point2D[] path2 = {new Point2D(WINDOW_WIDTH * 0.7,WINDOW_HEIGHT - 200),
                    new Point2D(WINDOW_WIDTH * 0.82, WINDOW_HEIGHT - 200),
                    new Point2D(WINDOW_WIDTH * 0.82, WINDOW_HEIGHT - 700)};

            // 1-1
            trackerEnemy = new TrackerEnemy(controller, Color.DEEPSKYBLUE, 0,
                    WINDOW_WIDTH / 2, -50, path, ENEMY_SHIP_TAG);
            trackerEnemy.setHp((int)(trackerEnemy.getHp() * enemyHealthModifier));
            weapon = new Blaster(controller, trackerEnemy, 2, 0, 0, Color.DEEPSKYBLUE,
                    20, 100, 0);
            trackerEnemy.addToPrimaryWeapon((Weapon) weapon);
            controller.addUpdateable(trackerEnemy);

            // 2-1
            trackerEnemy = new TrackerEnemy(controller, Color.DEEPSKYBLUE, 0,
                    WINDOW_WIDTH  * 0.5, WINDOW_HEIGHT + 50, path2,  ENEMY_SHIP_TAG);
            trackerEnemy.setHp((int)(trackerEnemy.getHp() * enemyHealthModifier));
            weapon = new Blaster(controller, trackerEnemy, 2, 0, 0, Color.DEEPSKYBLUE,
                    20, 100, 0);
            trackerEnemy.addToPrimaryWeapon((Weapon) weapon);
            controller.addUpdateable(trackerEnemy);

            Thread.sleep(4_000);

            // 1-2
            trackerEnemy = new TrackerEnemy(controller, Color.DEEPSKYBLUE, 0,
                    WINDOW_WIDTH / 2, -50, path, ENEMY_SHIP_TAG);
            trackerEnemy.setHp((int)(trackerEnemy.getHp() * enemyHealthModifier));
            weapon = new Blaster(controller, trackerEnemy, 2, 0, 0, Color.DEEPSKYBLUE,
                    20, 100, 0);
            trackerEnemy.addToPrimaryWeapon((Weapon) weapon);
            controller.addUpdateable(trackerEnemy);

            // 2-2
            trackerEnemy = new TrackerEnemy(controller, Color.DEEPSKYBLUE, 0,
                    WINDOW_WIDTH  * 0.5, WINDOW_HEIGHT + 50, path2,  ENEMY_SHIP_TAG);
            trackerEnemy.setHp((int)(trackerEnemy.getHp() * enemyHealthModifier));
            weapon = new LaserGun(controller, trackerEnemy, 0, 0, 0,
                    20, 0, 1.2d);
            trackerEnemy.addToPrimaryWeapon((Weapon) weapon);
            controller.addUpdateable(trackerEnemy);

            Thread.sleep(12_000);

            for(int i = 0; i < 2; i++){


                if(i == 0 || i == 1){
                    enemy = new Enemy(controller, Color.YELLOW, 0, WINDOW_WIDTH + 50, WINDOW_HEIGHT - 200, ENEMY_SHIP_TAG);
                    enemy.setHp((int) (enemy.getHp() * enemyHealthModifier));
                    weapon = new RocketLauncher(controller, enemy, 2, -5, 0,
                            4);
                    enemy.addToPrimaryWeapon((Weapon) weapon);
                    controller.addUpdateable(enemy);
                }

                //-

                if(i == 1) {
                    enemy = new Enemy(controller, Color.YELLOW, 0, WINDOW_WIDTH + 50, 150, ENEMY_SHIP_TAG);
                    enemy.setHp((int) (enemy.getHp() * enemyHealthModifier));
                    weapon = new LaserGun(controller, enemy, 0, 0, 0,
                            20, 0, 1.2d);
                    enemy.addToPrimaryWeapon((Weapon) weapon);
                    controller.addUpdateable(enemy);
                }


                Thread.sleep(2800);
            }

            for(int i = 0; i < 3; i++){
                enemy = new Enemy(controller, Color.YELLOW, 0, WINDOW_WIDTH + 50, 200 + (i * 50), ENEMY_SHIP_TAG);
                enemy.setHp((int) (enemy.getHp() * enemyHealthModifier));
                weapon = new LaserGun(controller, enemy, 0, 0, 0,
                        20, 0, 1.2d);
                enemy.addToPrimaryWeapon((Weapon) weapon);
                controller.addUpdateable(enemy);


                Thread.sleep(00);
            }





            Point2D[] path3 = {new Point2D(100,200),
                    new Point2D(WINDOW_WIDTH * 0.1, WINDOW_HEIGHT - 200),
                    new Point2D(WINDOW_WIDTH * 0.8, WINDOW_HEIGHT - 170),
                    new Point2D(WINDOW_WIDTH * 0.8, WINDOW_HEIGHT - 600),
                    new Point2D(WINDOW_WIDTH * 0.97, WINDOW_HEIGHT - 300),
                    new Point2D(WINDOW_WIDTH * 0.75, WINDOW_HEIGHT - 200)};

            Point2D[] path5 = {
                    new Point2D(WINDOW_WIDTH * 0.25,WINDOW_HEIGHT - 200),
                    new Point2D(WINDOW_WIDTH * 0.8, WINDOW_HEIGHT - 170),
                    new Point2D(WINDOW_WIDTH * 0.8, WINDOW_HEIGHT - 450)};

            Point2D[] path4 = {
                    new Point2D(WINDOW_WIDTH * 0.5,WINDOW_HEIGHT - 200),
                    new Point2D(WINDOW_WIDTH * 0.8, WINDOW_HEIGHT - 170),
                    new Point2D(WINDOW_WIDTH * 0.8, WINDOW_HEIGHT - 700)};



            trackerEnemy = new TrackerEnemy(controller, Color.DEEPSKYBLUE, 0,
                    WINDOW_WIDTH / 2, -50, path3, ENEMY_SHIP_TAG);
            trackerEnemy.setHp((int)(trackerEnemy.getHp() * enemyHealthModifier));
            weapon = new Blaster(controller, trackerEnemy, 2, 0, 0, Color.DEEPSKYBLUE,
                    20, 100, 0);
            trackerEnemy.addToPrimaryWeapon((Weapon) weapon);
            controller.addUpdateable(trackerEnemy);

            Thread.sleep(1_000);

            trackerEnemy = new TrackerEnemy(controller, Color.DEEPSKYBLUE, 0,
                    WINDOW_WIDTH * 0.1, WINDOW_HEIGHT + 50, path4, ENEMY_SHIP_TAG);
            trackerEnemy.setHp((int)(trackerEnemy.getHp() * enemyHealthModifier));
            weapon = new Blaster(controller, trackerEnemy, 2, 0, 0, Color.DEEPSKYBLUE,
                    20, 100, 0);
            trackerEnemy.addToPrimaryWeapon((Weapon) weapon);
            controller.addUpdateable(trackerEnemy);

            Thread.sleep(1_000);

            trackerEnemy = new TrackerEnemy(controller, Color.DEEPSKYBLUE, 0,
                    WINDOW_WIDTH * 0.1, WINDOW_HEIGHT + 50, path5, ENEMY_SHIP_TAG);
            trackerEnemy.setHp((int)(trackerEnemy.getHp() * enemyHealthModifier));
            weapon = new Blaster(controller, trackerEnemy, 2, 0, 0, Color.DEEPSKYBLUE,
                    20, 100, 0);
            trackerEnemy.addToPrimaryWeapon((Weapon) weapon);
            controller.addUpdateable(trackerEnemy);






            Thread.sleep(16_000);


            finalBoss = new Boss3(controller, WINDOW_WIDTH + 100, WINDOW_HEIGHT * 0.5);
            controller.addUpdateable(finalBoss);

            while(controller.getCollisionList().contains(finalBoss)){
                //bossi on olemas
                Thread.sleep(1_000);
            }

            Thread.sleep(4_000);

            System.out.println("Voitit tason " + (levelNumber) +"!");
            Platform.runLater(() -> controller.addScore(500));

            // Ilmoita levelin loppumisesta
            Platform.runLater(() -> controller.returnToMain());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
