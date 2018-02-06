package model;

import javafx.application.Platform;
import javafx.scene.image.Image;
import view.GameMain;

import static view.GameMain.input;

public class Player extends Unit implements Updateable {
    private double xVelocity;
    private double yVelocity;
    private int hp;

    private int fireRate = 30;
    private int fireRateCounter = 30;

    public Player(){
        GameMain.units.add(this);
        //Kova koodattu komponentin lisäys
        Image shipImage = new Image("/images/player_ship_9000.png");
        setImage(shipImage);


        //Kova koodattuja komponentteja
        Component b = new Component("/images/player_ship_9000.png");
        Component c = new Component("/images/Start.png");
        Component a = new Component("/images/enemy_ship_9000.png");
        components.add(b);
        components.add(a);
        components.add(c);


        for (Component component : components) { //Lista käy läpi kaikki komponentit ja asettaa kuvat päällekkäin
            //startingPosition = new Point2D(getXPosition() + 10, getYPosition() + 10);
            setImages(new Image(getClass().getResourceAsStream(component.imagePath), component.width, component.height, true, true));
            //setPosition(component.startingPosition.getX(), component.startingPosition.getY());
        }
        this.setTag("player");
    }

    @Override
    public void update(double deltaTime){
        if (fireRateCounter <= 30) fireRateCounter++;
        resetVelocities();
        if (input.contains("A")) move(getVelocity()*-1, 0);
        if (input.contains("D")) move(getVelocity(), 0);
        if (input.contains("W")) move(0, getVelocity()*-1);
        if (input.contains("S")) move(0, getVelocity());
        if (input.contains("O")) {
            if (fireRateCounter >= fireRate) {
                fireRateCounter = 0;
                Platform.runLater(() -> GameMain.addProjectile(new Projectile(this.getPosition(), 0, 10,  "projectile_player")));

                //GameLoop.queueUpdateable(new Projectile(this.getPosition(), 0, 10,  "projectile_player"));
            }
        }
        if (input.contains("V")) System.exit(0);
        setPosition(getXPosition() + xVelocity * deltaTime, getYPosition() + yVelocity * deltaTime);


    }

    public void collides(Updateable collidingUpdateable){
        // tagin saa: collidingUpdateable.getTag()
    }

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
}
