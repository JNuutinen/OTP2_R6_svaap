package model.level;

import controller.Controller;
import controller.GameController;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import model.units.Boss3;

import static view.GameMain.WINDOW_HEIGHT;
import static view.GameMain.WINDOW_WIDTH;

/**
 * Vitostaso. Säie, joka suorittaa kovakoodatun tason lineaarisesti, ilman looppeja.
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
            /*
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
            */

            Thread.sleep(8_000);

            //Viittaa tason viimeiseen pääviholliseen. Käytetään tason loppumisen tarkkailuun.
            Boss3 finalBoss = new Boss3(new Point2D(WINDOW_WIDTH + 100, WINDOW_HEIGHT * 0.5));

            // pyöri silmukas 1 sec välein niin kauan kuin bossi on olemassa
            while (!finalBoss.isDestroyed()) {
                Thread.sleep(1_000);
            }

            // Hyperdrive
            controller.changeBackgroundScrollSpeed(2000, 5);
            Thread.sleep(6_000);

            Platform.runLater(() -> controller.addScore(500));

            // Seuraavan tason aloitus
            controller.startLevel(3);

            // Jos ei seuraavaa tasoa, palataan päävalikkoon
            //Platform.runLater(() -> controller.returnToMain());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
