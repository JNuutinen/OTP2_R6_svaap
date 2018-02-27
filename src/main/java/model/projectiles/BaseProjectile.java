package model.projectiles;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import model.Component;
import model.Player;
import model.Sprite;
import model.Unit;

/**
 * Apuluokka eri projectileille. Pitää sisällään projectilen perus
 * toiminnan, toiminnallisuutta voi lisätä/muuttaa perivässä luokassa.
 * Esimerkiksi Missile muuttaa update metodia.
 * Jos update metodia haluaa muuttaa, tai tarvitsee muuten kutsua kontrolleria
 * perivässä luokassa, sen voi ottaa talteen instanssimuuttujaksi perivässä luokassa.
 * Projectilen kuvan, damagen, nopeuden yms. asetus perivässä luokassa.
 */
class BaseProjectile extends Sprite {

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
     * Viittaus ammuksen ampuneeseen Unitiin.
     */
    private Unit shooter;

    /**
     * TODO: osumamuuttuja? miten käytetään?
     */
    private boolean hit = false;

    /**
     * Konstruktori, asettaa kontrollerin ja nopeuden. Shooter
     * -parametrin avulla asettaa tagin, ammuksen suunnan, sekä ammuksen lähtösijainnin.
     * @param gc GraphicsContext, johon ammus piirretään.
     * @param shooter Unit, joka ampui ammuksen.
     * @param speed Ammuksen nopeus.
     */
    BaseProjectile(GraphicsContext gc, Unit shooter, double speed, Component component) {
        super(gc);

        // Ammuksen tagi ampujan mukaan
        if (shooter instanceof Player) setTag(TAG_PLAYER);
        else setTag(TAG_ENEMY);

        // Suunta ja aloituspiste otetaan ampujasta
        //setDirection(shooter.getDirection());
        this.shooter = shooter;
        Point2D startingLocation = shooter.getPosition();

        rotate(shooter.getDirection());
        setVelocity(speed);
        setIsMoving(true);

        //Projektile lähtee aluksen kärjestä. Viholliset ja pelaaja erikseen
        if (shooter instanceof Player) { //Jos ampuja on pelaaja
            this.setPosition(startingLocation.getX()  + component.getxOffset(), startingLocation.getY() + component.getyOffset());
        } else { //Jos ampuja on joku muu
            this.setPosition(startingLocation.getX() - shooter.getWidth() + component.getxOffset(), startingLocation.getY() + component.getyOffset());
        }
    }

    /**
     * Palauttaa tiedon ammuksen ampujasta.
     * @return Unit, joka ampui ammuksen.
     */
    Unit getShooter() {
        return shooter;
    }
}
