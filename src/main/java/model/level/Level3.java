package model.level;

import controller.Controller;
import controller.GameController;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import model.Tag;
import model.units.Boss3;
import model.units.Enemy;
import model.units.TrackerEnemy;

import java.util.ArrayList;
import java.util.Arrays;

import static view.GameMain.WINDOW_HEIGHT;
import static view.GameMain.WINDOW_WIDTH;

/**
 * Kolmostaso. Säie, joka suorittaa kovakoodatun tason lineaarisesti, ilman looppeja.
 */
public class Level3 extends Thread implements Level {

    /**
     * Pelin kontrolleri.
     */
    private Controller controller;

    /**
     * Konstruktori, haetaan kontrolleri-instanssi.
     */
    public Level3() {
        controller = GameController.getInstance();
    }

    @Override
    public void startLevel() {
        start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);

            new Enemy(new ArrayList<>(Arrays.asList(Tag.WEAPON_BLASTER_SPRINKLER)), Enemy.MOVE_SINE, new Point2D(WINDOW_WIDTH+100, 400));
            new Enemy(new ArrayList<>(Arrays.asList(Tag.WEAPON_BLASTER)), Enemy.MOVE_STRAIGHT, new Point2D(WINDOW_WIDTH+100, 100));
            new Enemy(new ArrayList<>(Arrays.asList(Tag.WEAPON_BLASTER)), Enemy.MOVE_STRAIGHT, new Point2D(WINDOW_WIDTH+100, 650));

            Thread.sleep(3_000);

            Point2D[] path = {new Point2D(WINDOW_WIDTH-100, 150)};
            new TrackerEnemy(new ArrayList<>(Arrays.asList(Tag.WEAPON_LASER_GUN)), new Point2D(WINDOW_WIDTH+100, 150), path);
            path = new Point2D[]{new Point2D(WINDOW_WIDTH-100, 600)};
            new TrackerEnemy(new ArrayList<>(Arrays.asList(Tag.WEAPON_LASER_GUN)), new Point2D(WINDOW_WIDTH+100, 600), path);

            Thread.sleep(5_000);

            new Enemy(new ArrayList<>(Arrays.asList(Tag.WEAPON_BLASTER_SHOTGUN)), Enemy.MOVE_SINE, new Point2D(WINDOW_WIDTH+200, 400));

            Thread.sleep(1_500);

            new Enemy(new ArrayList<>(Arrays.asList(Tag.WEAPON_BLASTER, Tag.WEAPON_ROCKET_LAUNCHER)), Enemy.MOVE_STRAIGHT, new Point2D(WINDOW_WIDTH+100, 100));
            new Enemy(new ArrayList<>(Arrays.asList(Tag.WEAPON_BLASTER, Tag.WEAPON_ROCKET_LAUNCHER)), Enemy.MOVE_STRAIGHT, new Point2D(WINDOW_WIDTH+100, 650));

            Thread.sleep(4_000);

            new Enemy(new ArrayList<>(Arrays.asList(Tag.WEAPON_BLASTER)), Enemy.MOVE_STRAIGHT, new Point2D(WINDOW_WIDTH+100, 50));

            Thread.sleep(600);

            new Enemy(new ArrayList<>(Arrays.asList(Tag.WEAPON_BLASTER)), Enemy.MOVE_SINE, new Point2D(WINDOW_WIDTH+100, 550));

            Thread.sleep(400);

            new Enemy(new ArrayList<>(Arrays.asList(Tag.WEAPON_BLASTER)), Enemy.MOVE_STRAIGHT, new Point2D(WINDOW_WIDTH+100, 400));

            Thread.sleep(830);

            new Enemy(new ArrayList<>(Arrays.asList(Tag.WEAPON_BLASTER)), Enemy.MOVE_STRAIGHT, new Point2D(WINDOW_WIDTH+100, 130));

            Thread.sleep(200);

            new Enemy(new ArrayList<>(Arrays.asList(Tag.WEAPON_BLASTER)), Enemy.MOVE_SINE, new Point2D(WINDOW_WIDTH+100, 250));

            Thread.sleep(1215);

            new Enemy(new ArrayList<>(Arrays.asList(Tag.WEAPON_BLASTER)), Enemy.MOVE_STRAIGHT, new Point2D(WINDOW_WIDTH+100, 359));

            Thread.sleep(8_000);

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

            // Seuraavan tason aloitus
            controller.startLevel(4);

            // Jos ei seuraavaa tasoa, palataan päävalikkoon
            //Platform.runLater(() -> controller.returnToMain());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
