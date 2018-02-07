package model;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
        Image shipImage = new Image("/images/player_ship_9000.png");
        setImage(shipImage);

        //Kova koodattuja komponentteja
        Component b = new Component("/images/player_ship_9000.png");
        Component c = new Component("/images/Start.png");
        Component a = new Component("/images/enemy_ship_9000.png");
        components.add(b);
        components.add(a);
        components.add(c);

        int componentOffset = 10; //Tätä vaihtamalla voi muokata minne komponentti tulee Y-akselilla

        for (Component component : components) { //Lista käy läpi kaikki komponentit ja asettaa kuvat päällekkäin
            /*
            setImages(new Image(getClass().getResourceAsStream(component.imagePath),
                    component.width, component.height, true, true));
            */
            /*
            component.getHeight();
            component.getWidth();
            */
            Image componentImage = new Image(component.imagePath);
            ImageView componentView = new ImageView();
            componentView.setImage(componentImage);
            componentView.setX(getXPosition());
            componentView.setY(getYPosition() + componentOffset);
            componentOffset += 30;

            this.getChildren().add(componentView);
        }
        this.setTag("player");
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
                Platform.runLater(() -> GameMain.addProjectile(new Projectile(10, this, "projectile_player")));
            }
        }
        if (input.contains("V")) System.exit(0);
        setPosition(getXPosition() + xVelocity * deltaTime, getYPosition() + yVelocity * deltaTime);


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
}
