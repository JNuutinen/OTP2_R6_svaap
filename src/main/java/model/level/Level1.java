package model.level;

import controller.Controller;
import controller.GameController;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import model.*;
import model.units.BomberEnemy;
import model.units.Boss3;
import model.units.Enemy;

import java.util.ArrayList;
import java.util.Arrays;

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
            Thread.sleep(1000);

            new Enemy(new ArrayList<>(Arrays.asList(Tag.WEAPON_LASER_GUN, Tag.WEAPON_BLASTER, Tag.WEAPON_BLASTER)),
                    0, new Point2D(WINDOW_WIDTH + 50, 200));

            //new Enemy(controller, Color.YELLOW, new ArrayList<>(Arrays.asList(WEAPON_LASER_GUN, WEAPON_ROCKET_LAUNCHER)),
            //        0, new Point2D(WINDOW_WIDTH + 50, WINDOW_HEIGHT - 400));
            Thread.sleep(1500);
            //new Enemy(new ArrayList<>(Arrays.asList(Tag.WEAPON_LASER_GUN)), 0, new Point2D(WINDOW_WIDTH + 50, 500));

            Thread.sleep(4_000);

            Point2D[] path = {new Point2D(WINDOW_WIDTH * 0.7,200),
                    new Point2D(WINDOW_WIDTH * 0.9, 100),
                    new Point2D(WINDOW_WIDTH * 0.9, 700)};
            Point2D[] path2 = {new Point2D(WINDOW_WIDTH * 0.7,WINDOW_HEIGHT - 150),
                    new Point2D(WINDOW_WIDTH * 0.85, WINDOW_HEIGHT - 250),
                    new Point2D(WINDOW_WIDTH * 0.1, WINDOW_HEIGHT - 400),
                    new Point2D(WINDOW_WIDTH * 0.82, WINDOW_HEIGHT - 700)};

            //new TrackerEnemy(new ArrayList<>(Arrays.asList(Tag.WEAPON_LASER_GUN)), new Point2D(WINDOW_WIDTH + 50, 300), path);

            new BomberEnemy(new ArrayList<>(Arrays.asList(Tag.WEAPON_LASER_GUN)), new Point2D(WINDOW_WIDTH + 50, 300),
                    path2, -1);

            Thread.sleep(40_000);

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

            controller.startLevel(2);

            // Ilmoita levelin loppumisesta
            //Platform.runLater(() -> controller.returnToMain());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
