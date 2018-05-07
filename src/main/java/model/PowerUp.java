package model;

import controller.Controller;
import controller.GameController;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import model.units.Player;
import model.units.Unit;

/**
 * Power uppien luokka.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class PowerUp extends SpriteImpl implements Updateable, HitboxCircle {

    /**
     * Powerupin arvo.
     */
    int value = 0;

    /**
     * Powerupin tyyppi.
     */
    int type;

    /**
     * Powerupin tyyppien vakiot.
     */
    public static final int HP = 0, DAMAGE = 1, SPEED = 2, SCORE = 3;

    /**
     * Powerupin koko.
     */
    int size = 5;

    /**
     * Powerupin Shape.
     */
    Shape shape;

    /**
     * Pelin kontrolleri.
     */
    Controller controller;


    /**
     * Konstruktori, powerupin sijainti otetaan kuolleesta Unitista.
     *
     * @param deadUnit    Unit, joka pudottaa power up:in
     * @param powerUpType Power up:in tyyppi: HP, DAMAGE, SPEED, SCORE;
     *                    HP lisää pelaajan elämäpisteitä
     *                    SCORE lisää pelaajan pisteitä
     * @param value       Arvo, joka lisätään power up:in vaikuttamaan muutujaan
     */
    public PowerUp(Unit deadUnit, int powerUpType, int value){
        switch (powerUpType) {
            case HP:
                type = HP;
                shape = circle(size, Color.GREEN);
                break;
            case DAMAGE:
                type = DAMAGE;
                shape = circle(size, Color.PURPLE);
                break;
            case SPEED:
                type = SPEED;
                shape = circle(size, Color.BLUE);
                break;
            case SCORE:
                type = SCORE;
                shape = circle(size, Color.YELLOW);
                break;
            default:
                return; //Jos vihollinen ei droppaa mitään
        }
        controller = GameController.getInstance();
        setTag(Tag.POWERUP); //ENEMY_PROJECTILE_TAG collisionia varten. Toimii toistaseksi ihan hyvin!
        this.value = value;
        type = powerUpType;

        double xOffset = degreesToVector(deadUnit.getDirection()).getX();
        double yOffset = degreesToVector(deadUnit.getDirection()).getY();
        Point2D startingLocation = new Point2D(deadUnit.getPosition().getX() + xOffset, deadUnit.getPosition().getY() + yOffset);
        setPosition(startingLocation.getX(), startingLocation.getY());

        setIsMoving(true);
        setVelocity(-50);
        System.out.println(controller);
        controller.addUpdateableAndSetToScene(this);
        controller.addHitboxObject(this);
        getChildren().add(shape);
    }

    /**
     * Konstruktori, powerupin sijainti otetaan kuolleesta Unitista. Käytetään vakio value arvoja.
     *
     * @param deadUnit    Unit, joka pudottaa power up:in
     * @param powerUpType Power up:in tyyppi: HP, DAMAGE, SPEED, SCORE;
     *                    HP lisää pelaajan elämäpisteitä
     *                    SCORE lisää pelaajan pisteitä
     */
    public PowerUp(Unit deadUnit, int powerUpType) {
        switch (powerUpType) {
            case HP:
                type = HP;
                shape = circle(size, Color.GREEN);
                value = Player.getMaxHp() / 10;
                break;
            case DAMAGE:
                type = DAMAGE;
                shape = circle(size, Color.PURPLE);
                break;
            case SPEED:
                type = SPEED;
                shape = circle(size, Color.BLUE);
                break;
            case SCORE:
                type = SCORE;
                shape = circle(size, Color.YELLOW);
                break;
            default:
                return; //Jos vihollinen ei droppaa mitään
        }
        controller = GameController.getInstance();
        setTag(Tag.POWERUP); //ENEMY_PROJECTILE_TAG collisionia varten. Toimii toistaseksi ihan hyvin!
        type = powerUpType;

        double xOffset = degreesToVector(deadUnit.getDirection()).getX();
        double yOffset = degreesToVector(deadUnit.getDirection()).getY();
        Point2D startingLocation = new Point2D(deadUnit.getPosition().getX() + xOffset, deadUnit.getPosition().getY() + yOffset);
        setPosition(startingLocation.getX(), startingLocation.getY());

        setIsMoving(true);
        setVelocity(-50);
        setHitbox(30);
        System.out.println(controller);
        controller.addUpdateableAndSetToScene(this);
        controller.addHitboxObject(this);
        getChildren().add(shape);
    }

    /**
     * Konstruktori, sijainti annetaan koordinaatteina.
     *
     * @param powerUpType Power up:in tyyppi: HP, DAMAGE, SPEED, SCORE;
     *                    HP lisää pelaajan elämäpisteitä
     *                    SCORE lisää pelaajan pisteitä
     * @param value       Arvo, joka lisätään power up:in vaikuttamaan muutujaan
     * @param position    PowerUpin x- ja y-koordinaatit
     */
    public PowerUp(int powerUpType, int value, Point2D position) {
        switch (powerUpType) {
            case HP:
                type = HP;
                shape = circle(size, Color.GREEN);
                break;
            case DAMAGE:
                type = DAMAGE;
                shape = circle(size, Color.PURPLE);
                break;
            case SPEED:
                type = SPEED;
                shape = circle(size, Color.BLUE);
                break;
            case SCORE:
                type = SCORE;
                shape = circle(size, Color.YELLOW);
                break;
            default:
                return; //Jos vihollinen ei droppaa mitään
        }
        controller = GameController.getInstance();
        setTag(Tag.POWERUP); //ENEMY_PROJECTILE_TAG collisionia varten. Toimii toistaseksi ihan hyvin!
        this.value = value;
        type = powerUpType;
        setPosition(position.getX(), position.getY());
        setIsMoving(true);
        setVelocity(-50);
        controller.addUpdateableAndSetToScene(this);
        controller.addHitboxObject(this);
        getChildren().add(shape);
    }

    /**
     * Konstruktori, sijainti annetaan koordinaatteina. Käytetään vakio value arvoja.
     *
     * @param powerUpType Power up:in tyyppi: HP, DAMAGE, SPEED, SCORE;
     *                    HP lisää pelaajan elämäpisteitä
     *                    SCORE lisää pelaajan pisteitä
     * @param position    PowerUpin x- ja y-koordinaatit
     */
    public PowerUp(int powerUpType, Point2D position) {
        switch (powerUpType) {
            case HP:
                type = HP;
                shape = circle(size, Color.GREEN);
                value = Player.getMaxHp() / 10;
                break;
            case DAMAGE:
                type = DAMAGE;
                shape = circle(size, Color.PURPLE);
                break;
            case SPEED:
                type = SPEED;
                shape = circle(size, Color.BLUE);
                break;
            case SCORE:
                type = SCORE;
                shape = circle(size, Color.YELLOW);
                break;
            default:
                return; //Jos vihollinen ei droppaa mitään
        }
        controller = GameController.getInstance();
        setTag(Tag.POWERUP); //ENEMY_PROJECTILE_TAG collisionia varten. Toimii toistaseksi ihan hyvin!
        type = powerUpType;
        setPosition(position.getX(), position.getY());
        setIsMoving(true);
        setVelocity(-50);
        controller.addUpdateableAndSetToScene(this);
        controller.addHitboxObject(this);
        getChildren().add(shape);
    }

    /*
    public Shape triangle(int size, int orientation, Color color) {
        double tip = 6 * size * 1.3;
        double point = 3 * size * 1.3;
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(
                tip/2 * -1, point / 2,
                tip/2, 0.0,
                tip/2 * -1, point / 2 * -1);
        triangle.setFill(Color.BLACK);
        triangle.setStroke(color);
        triangle.setStrokeWidth(2.0);
        triangle.getTransforms().add(new Rotate(90 * orientation, 50, 30));
        return triangle;
    }

    public Shape rectangle(int size, int orientation, Color color) {
        double x = 5 * size;
        double y = 5 * size;
        Rectangle rectangle = new Rectangle(x, y);
        rectangle.setFill(Color.BLACK);
        rectangle.setStroke(color);
        rectangle.setStrokeWidth(2.0);
        rectangle.getTransforms().add(new Rotate(90 * orientation, 50, 30));
        return (Shape)rectangle;
    }
    */
    /**
     * @param size säteen pituus
     * @param color muodon väri
     * Palauttaa tiedon ammuksen ampujasta.
     * @return Shape, ympyrä muoto.
     */

    /**
     * Luo ympyränmuotoisen Shapen.
     * @param size Shapen koko.
     * @param color Shapen väri.
     * @return Luotu Shape.
     */
    public Shape circle(int size, Color color) {
        Circle circle = new Circle(3 * size);
        circle.setFill(Color.BLACK);
        circle.setStroke(color);
        circle.setStrokeWidth(3.0);
        return circle;
    }
    
    /**
     * Asetetaan power up:in vaikutukset pelaajan.
     * @param player Pelaaja, jolle powerup annetaan.
     */
    public void givePowerUp(Player player) {
        switch (type) {
            case HP:
                System.out.println(Player.getMaxHp());
                if (player.getHp() < Player.getMaxHp()) {
                    player.addHP(value);
                    if (player.getHp() > Player.getMaxHp()) {
                        player.setHp(Player.getMaxHp());
                    }
                }
                break;
            case DAMAGE:
                player.setDamageMultiplier(2);
                System.out.println("DOUBLE DAMAGE ACTIVATED");
                break;
            case SPEED:
                player.setVelocityMulitplier(1.2);
                System.out.println("GAS GAS GAS");
                break;
            case SCORE:
                controller.addScore(value);
                System.out.println("Score++");
                break;
        }
    }

    /**
     * Päivittää PowerUpin paikkaa suhteessa maailmaan.
     * @param deltaTime Kulunut aika viime päivityksestä.
     */
    @Override
    public void update(double deltaTime) {
        moveStep(deltaTime);
    }

    @Override
    public void collides(Object collidingTarget) {
        System.out.println("powerup collides");
        if (collidingTarget instanceof Player) {
            System.out.println("hp++");
            givePowerUp((Player)collidingTarget);
        }
        destroyThis();
    }


    /**
     * Poistaa objektin pelimaailmasta.
     */
    @Override
    public void destroyThis() {
        controller.removeUpdateable(this, this);

    }
}
