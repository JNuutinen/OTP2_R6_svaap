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

    /**
     * Tagi, joka asetetaan vihollisen ampumille projectileille.
     * Käytetään osumien tarkasteluun GameLoopissa.
     */
    private static final String TAG_ENEMY = "projectile_enemy";

    /**
     * Tagi, joka asetetaan pelaajan ampumille projectileille.
     * Käytetään osumien tarkasteluun GameLoopissa.
     */
    private static final String TAG_PLAYER = "projectile_player";

    /**
     * Viittaus kontrolleriin, käytetään osumien rekisteröintiin
     * ja projectilen poistamiseen pelistä.
     */
    private Controller controller;

    /**
     * Projectilen tekemä vahinko.
     */
    private int damage;

    /**
     * TODO: osumamuuttuja? miten käytetään?
     */
    private boolean hit = false;


    /**
     * Konstruktori, asettaa kontrollerin, ammuksen vahingon ja nopeuden. Shooter
     * -parametrin avulla asettaa tagin, ammuksen suunnan, sekä ammuksen lähtösijainnin.
     * @param controller Pelin kontrolleri.
     * @param shooter Unit, joka ampui ammuksen.
     * @param speed Ammuksen nopeus.
     * @param damage Ammuksen vahinko.
     */
    BaseProjectile(Controller controller, Unit shooter, double speed, int damage) {
        this.controller = controller;
        this.damage = damage;

        // Ammuksen tagi ampujan mukaan
        if (shooter instanceof Player) setTag(TAG_PLAYER);
        else setTag(TAG_ENEMY);

        // Suunta ja aloituspiste otetaan ampujasta
        double direction = shooter.getDirection();
        Point2D startingLocation = shooter.getPosition();

        rotate(direction);
        setVelocity(speed);
        setIsMoving(true);

        //Projektile lähtee aluksen kärjestä. Viholliset ja pelaaja erikseen
        if (shooter instanceof Player) { //Jos ampuja on pelaaja
            this.setPosition(startingLocation.getX() + shooter.getWidth(), startingLocation.getY());
        } else { //Jos ampuja on joku muu
            this.setPosition(startingLocation.getX() - shooter.getWidth(), startingLocation.getY());
        }
    }

    @Override
    public void destroyThis(){
        controller.removeUpdateable(this);
    }

    @Override
    public void collides(Updateable collidingUpdateable){
        destroyThis();
        for (Unit unit : controller.getCollisionList()) {
            if (unit == collidingUpdateable) {
                unit.takeDamage(damage);
            }
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
        } else if (!hit) {
            moveStep(deltaTime * getVelocity());
        }
    }
}
