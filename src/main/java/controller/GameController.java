package controller;

import javafx.application.Platform;
import model.*;
import model.level.*;
import model.units.Player;
import view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Pelin kontrolleri, singleton.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class GameController implements Controller {

    /**
     * Kontrolleri-instanssi.
     */
    private static Controller INSTANCE = null;

    /**
     * Pelin view.
     */
    private View view;

    /**
     * Pelin gameloop.
     */
    private GameLoop gameLoop;

    /**
     * Pelaajaoliot.
     */
    private ArrayList<Player> players;

    /**
     * Konstruktori, luo GameLoopin. Private, koska singleton.
     * @param view Pelin view.
     */
    private GameController(View view) {
        this.view = view;
        gameLoop = new GameLoop();
    }

    /**
     * Palauttaa kontrollerin instanssin. Instanssi on null, ellei olla ennen tämän metodin kutsua
     * kutsuttu getInstance(View view) -metodia, joka alustaaa instanssin.
     * @return Controller instanssi.
     */
    public static Controller getInstance() {
        if (INSTANCE == null) {
            throw new UnsupportedOperationException("Kontrolleria ei vielä initalisoitu (getInstance(View view)).");
        }
        return INSTANCE;
    }

    /**
     * Palauttaa kontrollerin instanssin. Alustaa instanssin view parametrillä.
     * @param view Pelin view.
     * @return Controller instanssi.
     */
    public static Controller getInstance(View view) {
        if (INSTANCE == null) {
            synchronized (GameController.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GameController(view);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void addPlayers(ArrayList<Player> players) {
        this.players = players;
        gameLoop.setPlayers(players);
    }

    @Override
    public void addScore(int score) {
        for(Player player : players) {
            player.addScore(score);
        }
        view.setScore(players.get(0).getScore());
    }

    @Override
    public void setHealthbar(int hp, int selector){
       Platform.runLater(() -> view.setHealthbar(hp, selector));
    }

    @Override
    public void addHitboxObject(HitboxCircle hitboxCircle) {
        gameLoop.queueHitboxObject(hitboxCircle);
    }

    @Override
    public void addTrace(HitboxTrace hitboxTrace) {
        gameLoop.queueTrace(hitboxTrace);
    }

    @Override
    public void addUpdateableAndSetToScene(Updateable updateable) {
        Platform.runLater(() -> view.addSprite((SpriteImpl) updateable));
        gameLoop.queueUpdateable(updateable);
    }

    @Override
    public void addUpdateable(Updateable updateable) {
        gameLoop.queueUpdateable(updateable);
    }

    @Override
    public int getScore() {
        if (players != null) return players.get(0).getScore();
        return 0;
    }

    @Override
    public synchronized void removeUpdateable(Updateable updateable, HitboxCircle hitboxCircle) {
        view.removeSprite((SpriteImpl) updateable);
        gameLoop.removeUpdateable(updateable);
        gameLoop.removeHitboxObject(hitboxCircle);
    }

    @Override
    public synchronized void removeUpdateable(Updateable updateable, HitboxTrace hitboxTrace) {
        view.removeSprite((SpriteImpl) updateable);
        gameLoop.removeUpdateable(updateable);
        gameLoop.removeTrace(hitboxTrace);
    }

    @Override
    public synchronized void removeUpdateable(Updateable updateable) {
        view.removeSprite((SpriteImpl) updateable);
        gameLoop.removeUpdateable(updateable);
    }

    @Override
    public void removeHitbox(HitboxCircle hitboxCircle) {
        
    }

    @Override
    public void removeHitbox(HitboxTrace hitboxTrace) {

    }

    @Override
    public synchronized List<Updateable> getUpdateables(){ return gameLoop.getUpdateables(); }

    @Override
    public synchronized List<HitboxCircle> getPlayerHitboxObjects(){
        return gameLoop.getPlayerHitboxObjects();
    }

    @Override
    public synchronized List<HitboxCircle> getHitboxObjects(){
        return gameLoop.getHitboxObjects();
    }

    @Override
    public void startLevel(int levelNumber) {
        Level level;
        switch (levelNumber) {
            case 1:
                level = new Level1();
                break;
            case 2:
                level = new Level2();
                break;
            case 3:
                level = new Level3();
                break;
            case 4:
                level = new Level4();
                break;
            case 5:
                level = new Level5();
                break;
            default:
                level = new Level1();
                break;
        }
        level.startLevel();

    }

    @Override
    public void pauseGame() {
        gameLoop.pauseGame();
        view.pause();
    }

    @Override
    public void continueGame() {
        gameLoop.continueGame();
    }

    @Override
    public void changeBackgroundScrollSpeed(double speed, double duration) {
        view.changeBackgroundScrollSpeed(speed, duration);
    }

    @Override
    public void startLoop() {
        gameLoop.startLoop();
    }

    @Override
    public void setFps(double fps) {
        view.setFps(fps);
    }

    @Override
    public void setCurrentFps(double currentFps){
        view.setCurrentFps(currentFps);
    }

    @Override
    public void returnToMain(){
        view.returnToMain();
        gameLoop = new GameLoop();
    }
}
