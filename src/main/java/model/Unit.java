package model;

import view.SpriteBackup;

/**
 * Lisää spriteen avaruusalukselle ominaisia piirteitä
 */
public class Unit extends SpriteBackup {
    /**
     * Yksikön hitpointsit
     */
    private int hp;

    //private Component components[];

    /**
     * Yksikön taso
     */
    private int level;

    public Unit () {
        hp = 9000;
        level = 9000;
    }

    /**
     * Ampuu yksikön pääaseella
     */
    public void shootPrimary() {
        System.out.println("pew pew");
    }

    /**
     * Ampuu yksikön toisella aseella
     */
    public void shootSecondary() {
        System.out.println("pew");
    }

    /**
     * Yksikkö ottaa vahinkoa
     * @param damage Vahinkomäärä, jonka yksikkö ottaa
     */
    public void takeDamage(int damage) {
        hp =- damage;
    }

}
