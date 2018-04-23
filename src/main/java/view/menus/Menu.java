package view.menus;

import javafx.scene.Group;
import view.MenuScreenFX.MenuSpace;

public class Menu extends Group {

    private MenuSpace menuSpace;

    public void changeToNextMenu(Group nextMenu){
        menuSpace.changeToNextMenu(this, nextMenu);
    }

    public void setMenuSpace(MenuSpace menuSpace){
        this.menuSpace = menuSpace;
    }
}
