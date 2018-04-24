package view.menus;

import javafx.scene.Group;

/**
 * Yläluokka käytetään MenuSpace olion säilömiseen ja olemiseen Group alaluokkana
 */
public class Menu extends Group {


    // TODO jdoc
    private MenuSpace menuSpace;

    public Menu(MenuSpace menuSpace){
        this.menuSpace = menuSpace;
    }

    public MenuSpace getMenuSpace(){
        return menuSpace;
    }
}
