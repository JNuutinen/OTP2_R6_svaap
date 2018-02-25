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

    public void setHealthbar(int hp){
       Platform.runLater(() -> view.setHealthbar(hp));
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
        //((Sprite) updateable).setPosition(-50, -50);
        view.removeSprite((Sprite)updateable);
        gameLoop.removeUpdateable(updateable);
    }

    public synchronized ArrayList<Updateable> getUpdateables(){ return gameLoop.getUpdateables(); }

    @Override
    public void startLevel(int levelNumber) {
        Level level;
        ArrayList<Enemy> enemies;
        switch (levelNumber) {
            default:
                // Luodaan enemy tyypit listaan, mikä annetaan levelille parametrina
                enemies = createEnemyTypes();
                int numberOfEnemies = 0;
                double spawnFrequencyModifier = 1;
                double enemyHealthModifier = 1;
                double enemyDamageModifier = 1;

                level = new Level(this, enemies, numberOfEnemies, spawnFrequencyModifier, enemyHealthModifier,
                        enemyDamageModifier, levelNumber);
                break;
            case 1:
                enemies = createEnemyTypes();
                numberOfEnemies = 1;
                spawnFrequencyModifier = 0.9;
                enemyHealthModifier = 1.5;
                enemyDamageModifier = 1.5;

                level = new Level(this, enemies, numberOfEnemies, spawnFrequencyModifier, enemyHealthModifier,
                        enemyDamageModifier, levelNumber);
                break;
            case 2:
                enemies = createEnemyTypes();
                numberOfEnemies = 20;
                spawnFrequencyModifier = 0.8;
                enemyHealthModifier = 2.5;
                enemyDamageModifier = 2;

                level = new Level(this, enemies, numberOfEnemies, spawnFrequencyModifier, enemyHealthModifier,
                        enemyDamageModifier, levelNumber);
                break;
            case 3:
                enemies = createEnemyTypes();
                numberOfEnemies = 25;
                spawnFrequencyModifier = 0.7;
                enemyHealthModifier = 3;
                enemyDamageModifier = 2;

                level = new Level(this, enemies, numberOfEnemies, spawnFrequencyModifier, enemyHealthModifier,
                        enemyDamageModifier, levelNumber);
                break;
            case 4:
                enemies = createEnemyTypes();
                numberOfEnemies = 30;
                spawnFrequencyModifier = 0.2;
                enemyHealthModifier = 0.1;
                enemyDamageModifier = 2;

                level = new Level(this, enemies, numberOfEnemies, spawnFrequencyModifier, enemyHealthModifier,
                        enemyDamageModifier, levelNumber);
                break;
            case 5:
                enemies = createEnemyTypes();
                numberOfEnemies = 30;
                spawnFrequencyModifier = 0.05;
                enemyHealthModifier = 0.1;
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

    public void setCurrentFps(double currentFps){
        view.setCurrentFps(currentFps);
    }

    private ArrayList<Enemy> createEnemyTypes() {
        Image enemyImage = new Image("/images/enemy_ship_9000.png");
        Enemy enemy1 = new Enemy(this, 0, 0, 0, "enemy");
        enemy1.setMovementPattern(MOVE_STRAIGHT);
        enemy1.setImage(enemyImage);
        ArrayList<Enemy> enemies = new ArrayList<>();
        enemies.add(enemy1);
        return enemies;
    }

    @Override
    public void returnToMain(){
        Platform.runLater(()->view.returnToMain());
        gameLoop.stopLoops();
    }
}
