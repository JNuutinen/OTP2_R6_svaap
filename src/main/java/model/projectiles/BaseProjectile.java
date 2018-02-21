package model.projectiles;

import controller.Controller;
import javafx.geometry.Point2D;
import model.Player;
import model.Sprite;
import model.Unit;
import model.Updateable;

import static view.GameMain.WINDOW_HEIGHT;
import static view.GameMain.WINDOW_WIDTH;

/**
 * Apuluokka eri projectileille. Pitää sisällään projectilen perus
 * toiminnan, toiminnallisuutta voi lisätä/muuttaa perivässä luokassa.
 * Esimerkiksi Missile muuttaa update metodia.
 * Jos update metodia haluaa muuttaa, tai tarvitsee muuten kutsua kontrolleria
 * perivässä luokassa, sen voi ottaa talteen instanssimuuttujaksi perivässä luokassa.
 * Projectilen kuvan, damagen, nopeuden yms. asetus perivässä luokassa.
 */
class BaseProjectile extends Sprite implements Updateable {
    private static final String TAG_ENEMY = "projectile_enemy";
    private static final String TAG_PLAYER = "projectile_player";

    private Controller controller;
    private int damage;
    private boolean hit = false; // TODO: hit muuttujan käyttö?


    BaseProjectile(Controller controller, Unit shooter, double speed, int damage) {
        this.controller = controller;
        this.damage = damage;

        // Ammuksen tagi ampujan mukaan
        if (shooter instanceof Player) setTag(TAG_PLAYER);
        else setTag(TAG_ENEMY);

        System.out.println("Projectile tag: " + getTag());

        // Suunta ja aloituspiste otetaan ampujasta
        double direction = shooter.getDirection();
        Point2D startingLocation = shooter.getPosition();

        rotate(direction);
        setVelocity(speed);
        setIsMoving(true);

        //Projektile lähtee aluksen kärjestä. Viholliset ja pelaaja erikseen
        /*
        if (direction <= 90 || direction >= 270) { //Jos ampuja on pelaaja (kulkee vasemmalta oikealle)
            this.setPosition(startingLocation.getX() + shooter.getWidth(), startingLocation.getY());
        } else { //Jos ampuja on joku muu (kulkee oikealta vasemmalle)
            this.setPosition(startingLocation.getX() - shooter.getWidth(), startingLocation.getY());
        }
        */
        if (shooter instanceof Player) {
            setPosition(startingLocation.getX() + shooter.getWidth(), startingLocation.getY());
        } else {
            setPosition(startingLocation.getX() - shooter.getWidth(), startingLocation.getY());
        }
    }

    @Override
    public void update(double deltaTime){
        // chekkaa menikö ulos ruudulta
        if (getXPosition() < -100
                || getXPosition() > WINDOW_WIDTH+200
                || getYPosition() < -100
                || getYPosition() > WINDOW_HEIGHT+100) {
            destroyThis();
        } else {
            move(deltaTime * getVelocity());
        }
    }

    //kutsutaan aina kun osuu johonkin. tehty niin etta tietyt luokat esim. vihun ammus ja vihu ei voi osua toisiinsa.
    @Override
    public void collides(Updateable collidingUpdateable){

        destroyThis();
        for (Unit unit : controller.getCollisionList()) {
            if (unit == collidingUpdateable) {
                unit.takeDamage(damage);
            }
        }
    }

    void move(double deltaTime) {
        if (!hit) {
            moveStep(deltaTime);
        }
    }

    void destroyThis(){
        controller.removeUpdateable(this);
    }
}
