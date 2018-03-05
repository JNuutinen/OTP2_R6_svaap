package model.projectiles;

import controller.Controller;
import javafx.geometry.Point2D;
import model.Component;
import model.Player;
import model.Sprite;
import model.Unit;

import static view.GameMain.ENEMY_PROJECTILE_TAG;
import static view.GameMain.PLAYER_PROJECTILE_TAG;

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
     * Viittaus kontrolleriin, käytetään osumien rekisteröintiin
     * ja projectilen poistamiseen pelistä.
     */
    private Controller controller;

    /**
     * Viittaus ammuksen ampuneeseen Unitiin.
     */
    private Unit shooter;

    /**
     * Konstruktori, asettaa kontrollerin ja nopeuden. Shooter
     * -parametrin avulla asettaa tagin, ammuksen suunnan, sekä ammuksen lähtösijainnin.
     * @param controller Pelin kontrolleri.
     * @param shooter Unit, joka ampui ammuksen.
     * @param speed Ammuksen nopeus.
     */
    BaseProjectile(Controller controller, Unit shooter, double speed, int tag) {
        this.controller = controller;
        setTag(tag);
        // Suunta ja aloituspiste otetaan ampujasta johon laskettu mukaan aloituspisteen poikkeama.
        this.shooter = shooter;
        Point2D startingLocation = new Point2D(shooter.getPosition().getX(), shooter.getPosition().getY());
        this.setPosition(startingLocation.getX(), startingLocation.getY());

        rotate(shooter.getDirection());
        setVelocity(speed);
        setIsMoving(true);
    }

    /**
     *
     * @param controller
     * @param shooter
     * @param speed
     * @param frontOffset ammuksen aloituspaikan poikkeus aluksen etusuuntaan
     * @param leftOffset ammuksne aloituspaikan poikkeus aluksen vasempaan suuntaan
     */
    BaseProjectile(Controller controller, Unit shooter, double speed, double frontOffset, double leftOffset, int tag) {
        this(controller, shooter, speed, tag);
        double xOffset = degreesToVector(shooter.getDirection()).getX() * frontOffset;
        double yOffset = degreesToVector(shooter.getDirection()).getY() * frontOffset;
        xOffset = xOffset + degreesToVector(shooter.getDirection() + 90).getX() * leftOffset;
        yOffset = yOffset + degreesToVector(shooter.getDirection() + 90).getY() * leftOffset;
        Point2D startingLocation = new Point2D(shooter.getPosition().getX() + xOffset, shooter.getPosition().getY() + yOffset);
        this.setPosition(startingLocation.getX(), startingLocation.getY());
    }

    /**
     * Palauttaa tiedon ammuksen ampujasta.
     * @return Unit, joka ampui ammuksen.
     */
    Unit getShooter() {
        return shooter;
    }
}
