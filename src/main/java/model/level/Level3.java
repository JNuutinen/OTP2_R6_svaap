package model.level;

import controller.Controller;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import model.*;
import model.weapons.*;

import java.util.ArrayList;

import static view.GameMain.*;

/**
 * Threadin alaluokka, hoitaa vihollisten spawnauksen peliin.
 *
 * Luokka tehty ennaltamääriteltyn tason testaamista varten.
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
    public double getEnemyHealthModifier(){
        return enemyHealthModifier;
    }



    @Override
    public void run() {




        // LevelN thread pyörii niin kauan, kunnes kaikki viholliset on spawnattu.
        try {
            ArrayList<Weapon> blaster = new ArrayList<>();
            blaster.add(new Blaster(controller, 2, 20, new Point2D(0, 0), new Point2D(20, 0)));


            Thread.sleep(1000);

            // arvotaan spawnauspaikka
            //double randomYPos = ThreadLocalRandom.current().nextDouble(50, WINDOW_HEIGHT 5 100);

            Enemy testerivihu = new Enemy(controller, Color.YELLOW, null, 0, new Point2D(WINDOW_WIDTH + 50, WINDOW_HEIGHT - 200));
            testerivihu.addPrimaryWeapon(blaster.get(0));
            controller.addUpdateable(testerivihu);

            Enemy testerivihu2 = new Enemy(controller, Color.YELLOW, null, 0, new Point2D(WINDOW_WIDTH + 50, WINDOW_HEIGHT - 400));
            testerivihu2.addPrimaryWeapon(blaster.get(0));
            controller.addUpdateable(testerivihu2);

            Thread.sleep(4000);

            /*Point2D[] path = {new Point2D(WINDOW_WIDTH * 0.7,200),
                    new Point2D(WINDOW_WIDTH * 0.9, 100),
                    new Point2D(WINDOW_WIDTH * 0.9, 700)};
            Point2D[] path2 = {new Point2D(WINDOW_WIDTH * 0.7,WINDOW_HEIGHT - 200),
                    new Point2D(WINDOW_WIDTH * 0.82, WINDOW_HEIGHT - 200),
                    new Point2D(WINDOW_WIDTH * 0.82, WINDOW_HEIGHT - 700)};
            Thread.sleep(4_000);
            // 1-2
            enemyUnit = new TrackerEnemy(controller, Color.DEEPSKYBLUE, blaster, new Point2D(WINDOW_WIDTH / 2, -50), path);
            enemyUnit.setHp((int)(enemyUnit.getHp() * enemyHealthModifier));*/

            finalBoss = new Boss3(controller, new Point2D(WINDOW_WIDTH + 100, WINDOW_HEIGHT * 0.5));

            // pyöri silmukas 1 sec välein niin kauan kuin bossi on olemassa
            while(controller.getCollisionList().contains(finalBoss)){
                Thread.sleep(1_000);
            }

            System.out.println("Voitit tason " + (levelNumber) +"!");
            Thread.sleep(2_000);


            Platform.runLater(() -> controller.addScore(500));

            // Ilmoita levelin loppumisesta
            Platform.runLater(() -> controller.returnToMain());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
