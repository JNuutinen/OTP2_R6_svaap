package model;

import controller.Controller;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;


import java.awt.geom.GeneralPath;
import java.util.ArrayList;

import static view.GameMain.input;

public class Player extends Unit implements Updateable {
    private Controller controller;
    private double xVelocity;
    private double yVelocity;
    private int score = 0;
    private int fireRate = 5;
    private int fireRateCounter = 5;

    public Player(Controller controller) {
        super(controller);
        this.controller = controller;
        this.setHp(9999);
        controller.addUnitToCollisionList(this);

        Polygon triangle = new Polygon(); //Tämä tekee kolmion mikä esittää pelaajan alusta
        triangle.getPoints().addAll(new Double[]{
                0.0, 0.0,
                130.0, 40.0,
                00.0, 80.0 });
        triangle.setFill(Color.BLACK);
        triangle.setStroke(Color.CYAN);
        triangle.setStrokeWidth(2.0);
        this.getChildren().add(triangle);

        Rectangle rec = new Rectangle(70, 50); //Tämä tekee nelikulmion
        rec.setX(getXPosition() - 10);
        rec.setFill(Color.BLACK);
        rec.setStroke(Color.RED);
        rec.setStrokeWidth(2.0);

        Rectangle rec1 = new Rectangle(90, 60);
        rec1.setX(getXPosition() - 10);
        rec1.setFill(Color.BLACK);
        rec1.setStroke(Color.BLUE);
        rec1.setStrokeWidth(2.0);

        Rectangle rec2 = new Rectangle(95, 70);
        rec2.setX(getXPosition() - 10);
        rec2.setFill(Color.BLACK);
        rec2.setStroke(Color.WHITE);
        rec2.setStrokeWidth(2.0);

        Polygon triangle1 = new Polygon();
        triangle1.getPoints().addAll(new Double[]{
                0.0, 0.0,
                130.0, 150.0,
                00.0, 300.0 });
        triangle1.setFill(Color.BLACK);
        triangle1.setStroke(Color.GREEN);
        triangle1.setStrokeWidth(2.0);


        Component b = new Component(rec);
        components.add(b);
        Component b1 = new Component(rec1);
        components.add(b1);
        Component b2 = new Component(rec2);
        components.add(b2);

        equipComponents(components, "player");
    }


    @Override
    public void update(double deltaTime){
        if (fireRateCounter <= fireRate) fireRateCounter++;
        resetVelocity();
        if (input.contains("A")) addVelocity(getVelocity()*-1, 0);
        if (input.contains("D")) addVelocity(getVelocity(), 0);
        if (input.contains("W")) addVelocity(0, getVelocity()*-1);
        if (input.contains("S")) addVelocity(0, getVelocity());
        if (input.contains("O")) {
            if (fireRateCounter >= fireRate) {
                fireRateCounter = 0;
                spawnProjectile();
            }
        }
        if (input.contains("V")) System.exit(0);
        setPosition(getXPosition() + xVelocity * deltaTime, getYPosition() + yVelocity * deltaTime);

    }

    public void addScore(int points){
        score += points;
    }

    /*
    public void setScore(int points) {
        score = points;
    }
    */

    public int getScore() {
        return score;
    }

    public void collides(Updateable collidingUpdateable){
        // tagin saa: collidingUpdateable.getTag()
    }

    /* //Todo näitä ei varmaan enää tarvita, koska komponentit on osa alusta
    void move (double x, double y) {
        addVelocity(x, y);

        for (Component component : components) {
            component.addVelocity(x, y);
        }
    }

    private void resetVelocities() {
        resetVelocity();

        for (Component component : components) {
            component.resetVelocity();
        }
    }
    */

    public Updateable getUpdateable(){
        return this;
    }
    private void addVelocity(double x, double y) {
        xVelocity += x;
        yVelocity += y;
    }

    private void resetVelocity() {
        xVelocity = 0;
        yVelocity = 0;
    }

    // TODO: tää sit joskus aseeseen
    private void spawnProjectile(){
        Projectile projectile = new Projectile(controller, this.getPosition(), 0, 10,
                "projectile_player", this);
        controller.addUpdateable(projectile);
    }

    public void destroyThis(){
        controller.removeUpdateable(this);
    }
}
