package model;

import javafx.application.Platform;
import javafx.scene.image.Image;
import view.GameMain;

import java.util.Stack;

import static view.GameMain.WINDOW_WIDTH;

public class Level1 extends Thread {
    /**
     * Stackissa joko vihollisia (Enemy) tai aikoja sekunteina (int).
     * Jos next() palauttaa vihollisen, se spawnataan. Jos taas
     * next() palauttaa ajan, Thread.sleepataan sen ajan verran.
     */
    private Stack<Object> timesAndEnemies;

    public Level1() {
        constructLevel();
    }

    @Override
    public void run() {
        // käytetään iteraattoria, ettei tule ConcurrentModificationException
        try {
            for (Object current : timesAndEnemies) {
                if (current instanceof Long) {
                    sleep((long) current * 1000);
                } else {
                    // Platform.runLater pakollinen(?), koska tää ei oo jfx thread (eli GameMain)
                    Platform.runLater(() -> GameMain.addEnemy((Enemy) current));
                }
            }
            // poista level ettei vie tilaa muistista
            timesAndEnemies = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // kovakoodi testaus leveliin
    // TODO: levelin spawnaustiedot esim JSON tiedostoon, josta luetaan?
    // TODO: tai levelit jotenkin dynaamisesti luotuna eikä scriptattuna?
    private void constructLevel() {
        Image enemyImage = new Image("/images/enemy_ship_9000.png");
        timesAndEnemies = new Stack<>();
        timesAndEnemies.push(4L);
        timesAndEnemies.push(new Enemy(enemyImage, Enemy.MOVE_STRAIGHT, WINDOW_WIDTH+100, 300, "enemy"));
        timesAndEnemies.push(5L);
        timesAndEnemies.push(new Enemy(enemyImage, Enemy.MOVE_STRAIGHT, WINDOW_WIDTH+100, 200, "enemy"));
        timesAndEnemies.push(new Enemy(enemyImage, Enemy.MOVE_STRAIGHT, WINDOW_WIDTH+100, 400, "enemy"));
        timesAndEnemies.push(5L);
        timesAndEnemies.push(new Enemy(enemyImage, Enemy.MOVE_SINE, WINDOW_WIDTH+100, 300, "enemy"));
        timesAndEnemies.push(5L);
        timesAndEnemies.push(new Enemy(enemyImage, Enemy.MOVE_SINE, WINDOW_WIDTH+100, 250, "enemy"));
        timesAndEnemies.push(new Enemy(enemyImage, Enemy.MOVE_SINE, WINDOW_WIDTH+100, 450, "enemy"));

        timesAndEnemies.push(new Enemy(enemyImage, Enemy.MOVE_SINE, WINDOW_WIDTH+100, 150, "enemy"));
        timesAndEnemies.push(new Enemy(enemyImage, Enemy.MOVE_SINE, WINDOW_WIDTH+100, 550, "enemy"));

        timesAndEnemies.push(new Enemy(enemyImage, Enemy.MOVE_SINE, WINDOW_WIDTH+100, 150, "enemy"));
        timesAndEnemies.push(new Enemy(enemyImage, Enemy.MOVE_SINE, WINDOW_WIDTH+100, 650, "enemy"));

        timesAndEnemies.push(new Enemy(enemyImage, Enemy.MOVE_SINE, WINDOW_WIDTH+100, 250, "enemy"));
        timesAndEnemies.push(new Enemy(enemyImage, Enemy.MOVE_SINE, WINDOW_WIDTH+100, 450, "enemy"));
    }
}
