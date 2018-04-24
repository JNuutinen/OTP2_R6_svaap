package view.menus;

import javafx.scene.Group;

import java.util.ResourceBundle;

/**
 * Yläluokka käytetään MenuSpace olion säilömiseen ja olemiseen Group alaluokkana
 */
public abstract class Menu extends Group {


    // TODO jdoc
    private MenuSpace menuSpace;

    public Menu(MenuSpace menuSpace){
        this.menuSpace = menuSpace;
    }

    public MenuSpace getMenuSpace(){
        return menuSpace;
    }

    public abstract void changeLocale(ResourceBundle messages);
}
