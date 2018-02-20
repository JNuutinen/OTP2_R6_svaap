package model;

import controller.Controller;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static view.GameMain.WINDOW_HEIGHT;
import static view.GameMain.WINDOW_WIDTH;

public class Level extends Thread {
    // Millisekunteina base spawnausajan lyhyin aika
    private final int BASE_SPAWN_FREQ_LOW = 3000;
    // Millisekunteina base spawnausajan pisin aika
    private final int BASE_SPAWN_FREQ_HIGH = 6000;

    private Controller controller;
    private ArrayList<Enemy> enemyTypes;
    private int numberOfEnemies;
    private double spawnFrequencyModifier;
    private double enemyHealthModifier;
    private double enemyDamageModifier;
    private int levelNumber;
    private Updateable lastEnemy;


    public Level(Controller controller, ArrayList<Enemy> enemyTypes, int numberOfEnemies, double spawnFrequencyModifier,
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
                    Enemy enemy = new Enemy(controller, enemyType.getMovementPattern(),
                            WINDOW_WIDTH + 50, randomYPos, "enemy");
                    enemy.setHp((int)(enemy.getHp() * enemyHealthModifier));
                    // Kun vihuja on yksi jäljellä, tallennetaan se lastEnemyyn. While loopista poistutaan kun
                    //
                    if (numberOfEnemies == 1) {
                        lastEnemy = enemy;
                    }
                    controller.addUpdateable(enemy);
                    numberOfEnemies--;
                }
            }
            // Levelin viholliset spawnattu, venataan vähän aikaa ennen levelin loppumista

            System.out.println("Voitit tason " + (levelNumber+1) +"!");
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

        Enemy_tracker enemy_tracker = new Enemy_tracker(controller, 0,
                WINDOW_WIDTH / 2, -50, path,  "enemy");
        enemy_tracker.setHp((int)(enemy_tracker.getHp() * enemyHealthModifier));
        controller.addUpdateable(enemy_tracker);

        enemy_tracker = new Enemy_tracker(controller, 0,
                WINDOW_WIDTH  * 0.5, WINDOW_HEIGHT + 50, path2,  "enemy");
        enemy_tracker.setHp((int)(enemy_tracker.getHp() * enemyHealthModifier));
        controller.addUpdateable(enemy_tracker);
    }
}
