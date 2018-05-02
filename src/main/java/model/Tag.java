package model;

/**
 * Vakiot tunnistetagit pelin eri osille. Tagin avulla erotetaan esimerkiksi vihollisten ammukset
 * pelaajan ammuksista. Tagit kokonaislukuja.
 */
public enum Tag {

    /**
     * Tagi, jota ei olla määritelty.
     */
    UNDEFINED,

    /**
     * Pelaajan aluksen tagi.
     */
    SHIP_PLAYER,

    /**
     * Vihollisten aluksien tagi.
     */
    SHIP_ENEMY,

    /**
     * Pomon aluksen tagi.
     */
    SHIP_BOSS,

    /**
     * Pelaajan ampumien ammuksien tagi.
     */
    PROJECTILE_PLAYER,

    /**
     * Vihollisten ampumien ammuksien tagi.
     */
    PROJECTILE_ENEMY,

    /**
     * Poweruppien tagi.
     */
    POWERUP,

    /**
     * Aseen Blaster tagi.
     */
    WEAPON_BLASTER,

    /**
     * Aseen Blaster Shotgun tagi.
     */
    WEAPON_BLASTER_SHOTGUN,

    /**
     * Aseen Blaster Sprinkler tagi.
     */
    WEAPON_BLASTER_SPRINKLER,

    /**
     * Aseen Rocket Launcher tagi.
     */
    WEAPON_ROCKET_LAUNCHER,

    /**
     * Aseen Rocket Shotgun tagi.
     */
    WEAPON_ROCKET_SHOTGUN,

    /**
     * Aseen Laser Gun tagi.
     */
    WEAPON_LASER_GUN,

    /**
     * Aseen Machine Gun tagi.
     */
    WEAPON_MACHINE_GUN
}
