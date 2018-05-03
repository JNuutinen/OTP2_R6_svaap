package view.menuScreenFX;

import javafx.scene.Group;
import javafx.scene.layout.Pane;

/**
 * Erilaisten menujen siirtymäefektien state.
 */
public interface MenuFX {

    /**
     * Tekee siirtymän kahden menun välillä sisäänpäin tai seuraavaan menuun hierarkiassa.
     *
     * @param currentMenu Menun Group, josta siirrytään "eteenpäin" seuraavaan menuun. Poistetaan Panesta siirtymän
     *                    lopuksi.
     * @param nextMenu    Menun Group, johon siirrytään. Ei saa olla panen lapsi ennen kutsua, sillä se lisätään paneen
     *                    siirtymän aikana.
     * @param pane        Pane, josta currentMenu poistetaan ja johon nextMenu lisätään.
     */
    void changeToNextMenu(Group currentMenu, Group nextMenu, Pane pane);

    /**
     * Tekee siirtymän kahden menun välillä ulospäin tai takaisinpäin edelliseen menuun hierarkiassa.
     *
     * @param currentMenu  Menun Group, josta siirrytään "taaksepäin" edelliseen menuun. Poistetaan Panesta siirtymän
     *                     lopuksi.
     * @param previousMenu Menun Group, johon siirrytään. Ei saa olla panen lapsi ennen kutsua, sillä se lisätään paneen
     *                     siirtymän aikana.
     * @param pane         Pane, josta currentMenu poistetaan ja johon previousMenu lisätään.
     */
    void changeToPreviousMenu(Group currentMenu, Group previousMenu, Pane pane);
}
