package model.level;

import controller.Controller;
import controller.GameController;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import model.Tag;
import model.units.Enemy;
import model.units.TeleporterEnemy;

import java.util.ArrayList;
import java.util.Arrays;

import static view.GameMain.WINDOW_WIDTH;

/**
 * Nelostaso. Säie, joka suorittaa kovakoodatun tason lineaarisesti, ilman looppeja.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class Level5 extends Thread implements Level {

    /**
     * Pelin kontrolleri.
     */
    private Controller controller;

    /**
     * Konstruktori, haetaan kontrolleri-instanssi.
     */
    public Level5() {
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
            new TeleporterEnemy(new ArrayList<>(Arrays.asList(Tag.WEAPON_MACHINE_GUN)), new Point2D(WINDOW_WIDTH - 700, 200));

            new Enemy(null, Enemy.MOVE_NONE, new Point2D(WINDOW_WIDTH-300, 50));
            new Enemy(null, Enemy.MOVE_NONE, new Point2D(WINDOW_WIDTH-300, 150));
            new Enemy(null, Enemy.MOVE_NONE, new Point2D(WINDOW_WIDTH-300, 200));
            new Enemy(null, Enemy.MOVE_NONE, new Point2D(WINDOW_WIDTH-300, 250));
            new Enemy(null, Enemy.MOVE_NONE, new Point2D(WINDOW_WIDTH-300, 300));
            new Enemy(null, Enemy.MOVE_NONE, new Point2D(WINDOW_WIDTH-300, 350));
            new Enemy(null, Enemy.MOVE_NONE, new Point2D(WINDOW_WIDTH-300, 400));
            new Enemy(null, Enemy.MOVE_NONE, new Point2D(WINDOW_WIDTH-300, 450));
            new Enemy(null, Enemy.MOVE_NONE, new Point2D(WINDOW_WIDTH-300, 500));
            new Enemy(null, Enemy.MOVE_NONE, new Point2D(WINDOW_WIDTH-300, 550));
            new Enemy(null, Enemy.MOVE_NONE, new Point2D(WINDOW_WIDTH-300, 600));
            new Enemy(null, Enemy.MOVE_NONE, new Point2D(WINDOW_WIDTH-300, 650));
            new Enemy(null, Enemy.MOVE_NONE, new Point2D(WINDOW_WIDTH-300, 700));

            Thread.sleep(500_000);

            // Hyperdrive
            //controller.changeBackgroundScrollSpeed(2000, 5);
            //Thread.sleep(6_000);

            Platform.runLater(() -> controller.addScore(500));

            // Seuraavan tason aloitus
            //controller.startLevel(2);

            // Jos ei seuraavaa tasoa, palataan päävalikkoon
            Platform.runLater(() -> controller.returnToMain());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
