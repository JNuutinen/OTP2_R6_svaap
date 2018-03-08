package controller;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import model.*;
import view.GameMain;
import view.View;

import java.util.ArrayList;

import static model.Enemy.MOVE_SINE;
import static model.Enemy.MOVE_STRAIGHT;
import static view.GameMain.ENEMY_SHIP_TAG;

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

    public void setHealthbar(int hp, int selector){
       Platform.runLater(() -> view.setHealthbar(hp, selector));
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
    public synchronized ArrayList<Unit> getCollisionList() {
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
                level.start();
                break;
            case 1:
                enemies = createEnemyTypes();
                numberOfEnemies = 3;
                spawnFrequencyModifier = 0.9;
                enemyHealthModifier = 1.5;
                enemyDamageModifier = 1.5;

                level = new Level(this, enemies, numberOfEnemies, spawnFrequencyModifier, enemyHealthModifier,
                        enemyDamageModifier, levelNumber);
                level.start();
                break;
            case 2:
                enemies = createEnemyTypes();
                numberOfEnemies = 20;
                spawnFrequencyModifier = 0.8;
                enemyHealthModifier = 2.5;
                enemyDamageModifier = 2;

                level = new Level(this, enemies, numberOfEnemies, spawnFrequencyModifier, enemyHealthModifier,
                        enemyDamageModifier, levelNumber);
                level.start();
                break;
            case 3:
                enemies = createEnemyTypes();
                numberOfEnemies = 1;
                spawnFrequencyModifier = 0.7;
                enemyHealthModifier = 3;
                enemyDamageModifier = 2;

                Level3 level3 = new Level3(this, enemies, numberOfEnemies, spawnFrequencyModifier, enemyHealthModifier,
                        enemyDamageModifier, levelNumber);
                level3.start();
                break;
            case 4:
                enemies = createEnemyTypes();
                numberOfEnemies = 30;
                spawnFrequencyModifier = 0.2;
                enemyHealthModifier = 0.1;
                enemyDamageModifier = 2;

                level = new Level(this, enemies, numberOfEnemies, spawnFrequencyModifier, enemyHealthModifier,
                        enemyDamageModifier, levelNumber);
                level.start();
                break;
            case 5:
                enemies = createEnemyTypes();
                numberOfEnemies = 30;
                spawnFrequencyModifier = 0.05;
                enemyHealthModifier = 0.1;
                enemyDamageModifier = 2;

                level = new Level(this, enemies, numberOfEnemies, spawnFrequencyModifier, enemyHealthModifier,
                        enemyDamageModifier, levelNumber);
                level.start();
                break;
            case 6:
                enemies = createEnemyTypes();
                numberOfEnemies = 1000;
                spawnFrequencyModifier = 0.1;
                enemyHealthModifier = 1;
                enemyDamageModifier = 0;

                level = new Level(this, enemies, numberOfEnemies, spawnFrequencyModifier, enemyHealthModifier,
                        enemyDamageModifier, levelNumber);
                level.start();
                break;
        }

    }

    @Override
    public void pauseGame() {
        gameLoop.pauseGame();
        Level.pauseLevel();
        view.pause();
    }

    @Override
    public void continueGame() {
        gameLoop.continueGame();
        Level.continueLevel();
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
        Enemy enemy1 = new Enemy(this, Color.GRAY, MOVE_STRAIGHT, 0, 0, ENEMY_SHIP_TAG);
        enemy1.setImage(enemyImage, 1, 1);// width aj height arvoilla ei ole merkitysta koska vihu piirretty vektoreilla
        Enemy enemy2 = new Enemy(this, Color.PALEGOLDENROD, MOVE_SINE, 0, 0, ENEMY_SHIP_TAG);
        enemy2.setImage(enemyImage, 1, 1);
        ArrayList<Enemy> enemies = new ArrayList<>();
        enemies.add(enemy1);
        enemies.add(enemy2);
        return enemies;
    }

    @Override
    public void returnToMain(){
        Platform.runLater(()->view.returnToMain());
        gameLoop.stopLoops();
    }
}
