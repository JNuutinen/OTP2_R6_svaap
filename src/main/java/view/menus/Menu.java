package view.menus;

import javafx.scene.Group;

import java.util.ResourceBundle;

/**
 * Valikoiden interface.
 */
public interface Menu {

    /**
     * Vaihtaa valikon elementtien tekstit parametrina annetun ResourceBundlen mukaan.
     *
     * @param messages Lokalisoidut resurssit.
     */
    void changeLocale(ResourceBundle messages);

    /**
     * Palauttaa menun groupin.
     *
     * @return Group, johon menu on rakennettu.
     */
    Group getGroup();
}
