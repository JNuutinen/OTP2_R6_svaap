package model.projectiles;

import controller.Controller;
import javafx.geometry.Point2D;
import model.*;

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
     * Viittaus kontrolleriin, käytetään osumien rekisteröintiin
     * ja projectilen poistamiseen pelistä.
     */
    private Controller controller;



    /**
     * TODO: osumamuuttuja? miten käytetään?
     */
    private boolean hit = false;

    /**
     * Konstruktori, asettaa kontrollerin ja nopeuden. Shooter
     * -parametrin avulla asettaa tagin, ammuksen suunnan, sekä ammuksen lähtösijainnin.
     * @param controller Pelin kontrolleri.
     * @param shooter Unit, joka ampui ammuksen.
     * @param speed Ammuksen nopeus.
     */
    BaseProjectile(Controller controller, Unit shooter, double speed, Component component) {
        this.controller = controller;


        // Ammuksen tagi ampujan mukaan
        if (shooter instanceof Player) setTag(TAG_PLAYER);
        else setTag(TAG_ENEMY);

        // Suunta ja aloituspiste otetaan ampujasta
        //setDirection(shooter.getDirection());
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
}
