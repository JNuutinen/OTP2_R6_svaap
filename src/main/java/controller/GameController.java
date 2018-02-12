package controller;

import javafx.application.Platform;
import javafx.scene.image.Image;
import model.*;
import view.GameMain;
import view.View;

import java.util.ArrayList;

import static model.Enemy.MOVE_STRAIGHT;

public class GameController implements Controller {
    private View view;
    private GameLoop gameLoop;
    private Player player;

    public GameController(GameMain view) {
        gameLoop = new GameLoop(this);
        this.view = view;
    }

    @Override
    public void addPlayer(Player player) {
        this.player = player;
    }


    @Override
    public void addScore(int score) {
        player.addScore(score);
        view.setScore(player.getScore());
    }

    @Override
    public void addUnitToCollisionList(Unit unit) {
        view.addUnitToCollisionList(unit);
    }

    @Override
    public void removeFromCollisionList(Unit unit){
        Platform.runLater(() ->view.removeFromCollisionList(unit));
    }

    @Override
    public void addUpdateable(Updateable updateable) {
        Platform.runLater(() -> view.addSprite((Sprite)updateable));
        gameLoop.queueUpdateable(updateable);
    }

    @Override
    public int getScore() {
        if (player != null) return player.getScore();
        return 0;
    }

    @Override
    public ArrayList<Unit> getCollisionList() {
        return view.getCollisionList();
    }

    @Override
    public synchronized void removeUpdateable(Updateable updateable) {
        // TODO: hitboxi jää viel?
        view.removeSprite((Sprite)updateable);
        gameLoop.removeUpdateable(updateable);
    }

    public synchronized ArrayList<Updateable> getUpdateables(){ return gameLoop.getUpdateables(); }

    @Override
    public void startLevel(int levelNumber) {
        Level level;
        switch (levelNumber) {
            default:
                // Luodaan enemy tyypit listaan, mikä annetaan levelille parametrina
                ArrayList<Enemy> enemies = createEnemyTypes();
                int numberOfEnemies = 4;
                int spawnFrequencyModifier = 1;
                int enemyHealthModifier = 1;
                int enemyDamageModifier = 1;

                level = new Level(this, enemies, numberOfEnemies, spawnFrequencyModifier, enemyHealthModifier,
                        enemyDamageModifier, levelNumber);
                break;
            case 1:
                enemies = createEnemyTypes();
                numberOfEnemies = 10;
                spawnFrequencyModifier = 3;
                enemyHealthModifier = 2;
                enemyDamageModifier = 2;

                level = new Level(this, enemies, numberOfEnemies, spawnFrequencyModifier, enemyHealthModifier,
                        enemyDamageModifier, levelNumber);
                break;
        }
        level.start();
    }

    @Override
    public void startLoop() {
        gameLoop.startLoop();
    }

    @Override
    public void setFps(double fps) {
        view.setFps(fps);
    }

    private ArrayList<Enemy> createEnemyTypes() {
        Image enemyImage = new Image("/images/enemy_ship_9000.png");
        Enemy enemy1 = new Enemy(this);
        enemy1.setMovementPattern(MOVE_STRAIGHT);
        enemy1.setImage(enemyImage);
        ArrayList<Enemy> enemies = new ArrayList<>();
        enemies.add(enemy1);
        return enemies;
    }

}
