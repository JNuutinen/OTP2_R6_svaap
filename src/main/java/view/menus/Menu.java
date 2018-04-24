package view.menus;

import javafx.scene.Group;

import java.util.ResourceBundle;

/**
 * Yläluokka käytetään MenuSpace olion säilömiseen ja olemiseen Group alaluokkana
 */
public abstract class Menu extends Group {


    /**
     * Menuspace jossa tiedot kaikista menuista.
     */
    private MenuSpace menuSpace;

    /**
     * Konstruktori. Asettaa MenuSpacen
     * @param menuSpace MenusSpace
     */
    Menu(MenuSpace menuSpace){
        this.menuSpace = menuSpace;
    }

    /**
     * Getteri MenuSpacelle
     * @return MenuSpace
     */
    MenuSpace getMenuSpace(){
        return menuSpace;
    }

    /**
     * Muuttaa menun lokalisaatiota.
     *
     * @param messages Lokalisoidut tekstit.
     */
    public abstract void changeLocale(ResourceBundle messages);
}
