package model.projectiles;

import javafx.geometry.Point2D;
import model.SpriteImpl;
import model.Tag;
import model.units.Unit;

/**
 * Apuluokka eri projectileille. Pitää sisällään projectilen perus
 * toiminnan, toiminnallisuutta voi lisätä/muuttaa perivässä luokassa.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
class BaseProjectile extends SpriteImpl {

    /**
     * Viittaus ammuksen ampuneeseen Unitiin.
     */
    private Unit shooter;

    /**
     * ammuksen tekemä vahinko
     */
    private double damage = 0;

    /**
     * Konstruktori.
     * @param shooter Ammuksen ampuja.
     * @param speed Ammuksen nopeus.
     * @param tag Ammuksen tagi.
     */
    BaseProjectile(Unit shooter, double speed, double damage, Tag tag) {
        this.damage = damage;
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
    BaseProjectile(Unit shooter, double speed, Point2D offset, double damage, Tag tag) {
        this(shooter, speed, damage, tag);
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

    /**
     * ammuksen tekemä vahinko -getteri
     * @return ammuksen tekemä vahinko
     */
    public double getDamage() {
        return damage;
    }
}
