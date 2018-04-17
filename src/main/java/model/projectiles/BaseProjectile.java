package model.projectiles;

import javafx.geometry.Point2D;
import model.Sprite;
import model.Unit;

/**
 * Apuluokka eri projectileille. Pitää sisällään projectilen perus
 * toiminnan, toiminnallisuutta voi lisätä/muuttaa perivässä luokassa.
 * Esimerkiksi Missile muuttaa update metodia.
 * Jos update metodia haluaa muuttaa, tai tarvitsee muuten kutsua kontrolleria
 * perivässä luokassa, sen voi ottaa talteen instanssimuuttujaksi perivässä luokassa.
 * Projectilen kuvan, damagen, nopeuden yms. asetus perivässä luokassa.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
class BaseProjectile extends Sprite {

    /**
     * Viittaus ammuksen ampuneeseen Unitiin.
     */
    private Unit shooter;

    /**
     * Konstruktori.
     * @param shooter Ammuksen ampuja.
     * @param speed Ammuksen nopeus.
     * @param tag Ammuksen tagi.
     */
    BaseProjectile(Unit shooter, double speed, int tag) {
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
     * Konstruktori aloitussijainnin poikkeamalla.
     * @param shooter Ammuksen ampuja.
     * @param speed Ammuksen nopeus.
     * @param offset Ammuken alkusijainnin poikkeama aluksesta.
     * @param tag Ammuksen tagi.
     */
    BaseProjectile(Unit shooter, double speed, Point2D offset, int tag) {
        this(shooter, speed, tag);
        double xOffset = degreesToVector(shooter.getDirection()).getX() * offset.getX();
        double yOffset = degreesToVector(shooter.getDirection()).getY() * offset.getX();
        xOffset = xOffset + degreesToVector(shooter.getDirection() - 90).getX() * offset.getY();
        yOffset = yOffset + degreesToVector(shooter.getDirection() - 90).getY() * offset.getY();
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
