package model.level;

import controller.Controller;
import controller.GameController;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import model.units.BomberEnemy;
import model.units.Boss3;
import model.units.Enemy;
import model.units.TrackerEnemy;

import java.util.ArrayList;

import static java.util.Arrays.asList;
import static model.Tag.*;
import static model.units.Enemy.MOVE_SINE;
import static model.units.Enemy.MOVE_STRAIGHT;
import static view.GameMain.WINDOW_HEIGHT;
import static view.GameMain.WINDOW_WIDTH;

/**
 * Threadin alaluokka, hoitaa vihollisten spawnauksen peliin.
 * Luokka tehty ennaltamääriteltyn tason testaamista varten.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class Level1 extends Thread implements Level {

    /**
     * Pelin kontrolleri.
     */
    private Controller controller;

    /**
     * Konstruktori.
     */
    public Level1() {
        this.controller = GameController.getInstance();
    }

    @Override
    public void startLevel() {
        start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1_000);

            Point2D[] tracker1Path = {
                    new Point2D(WINDOW_WIDTH * 0.85, 200),
                    new Point2D(WINDOW_WIDTH * 0.9, 500),
                    new Point2D(WINDOW_WIDTH * 0.8, 700)
            };

            Point2D[] tracker2Path = {
                    new Point2D(WINDOW_WIDTH * 0.7, 200),
                    new Point2D(WINDOW_WIDTH * 0.8, 500),
                    new Point2D(WINDOW_WIDTH * 0.9, 700)
            };

            Point2D[] tracker3Path = {
                    new Point2D(WINDOW_WIDTH * 0.8, 700),
                    new Point2D(WINDOW_WIDTH * 0.85, 450),
                    new Point2D(WINDOW_WIDTH * 0.7, 300)
            };

            Point2D[] bomber1Path = {
                    new Point2D(1100, 300),
                    new Point2D(1000, 100),

                    new Point2D(200, 100),
                    new Point2D(100, 300),

                    new Point2D(100, 400),
                    new Point2D(200, 600),

                    new Point2D(1000, 600),
                    new Point2D(1100, 400),
            };

            Point2D[] bomber2Path = {
                    new Point2D(WINDOW_WIDTH * 0.7, -220),
                    new Point2D(WINDOW_WIDTH * 0.6, WINDOW_HEIGHT * 0.33),
                    new Point2D(WINDOW_WIDTH * 0.75, WINDOW_HEIGHT * 0.66),
                    new Point2D(WINDOW_WIDTH * 0.9, WINDOW_HEIGHT + 300)
            };

            new Enemy(new ArrayList<>(asList(WEAPON_BLASTER)), MOVE_STRAIGHT,
                    new Point2D(WINDOW_WIDTH + 100, 650));

            Thread.sleep(3_000);

            new Enemy(new ArrayList<>(asList(WEAPON_BLASTER)), MOVE_STRAIGHT,
                    new Point2D(WINDOW_WIDTH + 100, 250));

            Thread.sleep(3_000);

            new Enemy(new ArrayList<>(asList(WEAPON_BLASTER)), MOVE_STRAIGHT,
                    new Point2D(WINDOW_WIDTH + 100, 400));

            Thread.sleep(1_000);

            new Enemy(new ArrayList<>(asList(WEAPON_BLASTER)), MOVE_STRAIGHT,
                    new Point2D(WINDOW_WIDTH + 100, 550));

            Thread.sleep(3_000);

            new Enemy(new ArrayList<>(asList(WEAPON_BLASTER, WEAPON_BLASTER)), MOVE_SINE,
                    new Point2D(WINDOW_WIDTH + 100, 300));

            Thread.sleep(2_000);

            new Enemy(new ArrayList<>(asList(WEAPON_BLASTER, WEAPON_BLASTER)), MOVE_SINE,
                    new Point2D(WINDOW_WIDTH + 100, 550));

            Thread.sleep(2_000);

            new Enemy(new ArrayList<>(asList(WEAPON_BLASTER, WEAPON_BLASTER_SHOTGUN)), MOVE_SINE,
                    new Point2D(WINDOW_WIDTH + 100, 220));

            Thread.sleep(2_000);

            for(int i = 0; i < 4; i++){
                new BomberEnemy(new ArrayList<>(asList(WEAPON_BLASTER)),
                        new Point2D(WINDOW_WIDTH * 0.7, -220),
                        bomber2Path, -1);
                Thread.sleep(500);
            }

            Thread.sleep(6_000);


            new TrackerEnemy(new ArrayList<>(asList(WEAPON_BLASTER)), new Point2D(WINDOW_WIDTH + 50, 200), tracker1Path);

            Thread.sleep(3_000);

            new BomberEnemy(new ArrayList<>(asList(WEAPON_BLASTER, WEAPON_BLASTER, WEAPON_BLASTER)),
                    new Point2D(1300, 600),
                    bomber1Path, 0);

            new TrackerEnemy(new ArrayList<>(asList(WEAPON_LASER_GUN)), new Point2D(WINDOW_WIDTH + 50, 200), tracker2Path);

            Thread.sleep(3_000);

            new TrackerEnemy(new ArrayList<>(asList(WEAPON_BLASTER_SHOTGUN, WEAPON_BLASTER_SHOTGUN)), new Point2D(WINDOW_WIDTH + 50, 700), tracker3Path);

            Thread.sleep(2_000);



            Thread.sleep(15_000);

            //Viittaa tason viimeiseen pääviholliseen. Käytetään tason loppumisen tarkkailuun.
            Boss3 finalBoss = new Boss3(new Point2D(WINDOW_WIDTH + 100, WINDOW_HEIGHT * 0.5));

            // pyöri silmukas 1 sec välein niin kauan kuin bossi on olemassa
            while(!finalBoss.isDestroyed()){
                Thread.sleep(1_000);
            }

            // Hyperdrive
            controller.changeBackgroundScrollSpeed(2000, 5);
            Thread.sleep(6_000);

            Platform.runLater(() -> controller.addScore(500));

            controller.startLevel(3);

            // Ilmoita levelin loppumisesta
            //Platform.runLater(() -> controller.returnToMain());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
