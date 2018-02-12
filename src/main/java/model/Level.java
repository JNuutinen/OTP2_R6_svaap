package model;

import controller.Controller;
import javafx.application.Platform;
import view.GameMain;

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
    private int spawnFrequencyModifier;
    private int enemyHealthModifier;
    private int enemyDamageModifier;
    private int levelNumber;
    private Updateable lastEnemy;


    public Level(Controller controller, ArrayList<Enemy> enemyTypes, int numberOfEnemies, int spawnFrequencyModifier,
                 int enemyHealthModifier, int enemyDamageModifier, int levelNumber) {
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
        // Level thread pyörii niin kauan, kunnes kaikki viholliset on spawnattu.
        try {
            while (numberOfEnemies > 0 || controller.getCollisionList().contains(lastEnemy)) {
                // paussi vihollisten välillä
                // TODO: Monen vihollisen yhtäaikainen spawnaus
                if(numberOfEnemies > 0) {
                    long sleepTime = ThreadLocalRandom.current().nextLong(BASE_SPAWN_FREQ_LOW / spawnFrequencyModifier,
                            BASE_SPAWN_FREQ_HIGH / spawnFrequencyModifier + 1);
                    Thread.sleep(sleepTime);

                    // arvotaan spawnauspaikka
                    double randomYPos = ThreadLocalRandom.current().nextDouble(50, WINDOW_HEIGHT - 50);

                    // arvotaan vihollinen tyyppilistasta
                    Enemy enemyType = enemyTypes.get(ThreadLocalRandom.current().nextInt(enemyTypes.size()));
                    Enemy enemy = new Enemy(controller, enemyType.getImage(), enemyType.getMovementPattern(),
                            WINDOW_WIDTH + 50, randomYPos, "enemy");

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

           controller.startLevel(levelNumber+1);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
