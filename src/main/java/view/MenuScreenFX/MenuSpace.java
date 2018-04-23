package view.MenuScreenFX;

import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import view.GameMain;

public class MenuSpace extends StackPane {

    private Group currentMenu;
    private MenuFX menuFX = new Slider();
    private GameMain gameMain;


    public MenuSpace(GameMain gameMain, Group initialMenu){
        this.gameMain = gameMain;
        this.currentMenu = initialMenu;
        this.getChildren().add(initialMenu);
    }

    /**
     * Vaihtaa lokaalin menuFX:n eteenpäin mentävällä efektillä parametrin menuun.
     * @param currentMenu tämänhetkinen Group alaluokan menu josta siirrytään.
     * @param nextMenu Group alaluokan menu johon siirrytään.
     */
    public void changeToNextMenu(Group currentMenu, Group nextMenu){
        menuFX.changeToNextMenu(currentMenu, nextMenu, this);
    }

    /**
     * Vaihtaa lokaalin menuFX:n taaksepäin mentävällä efektillä parametrin menuun.
     * @param currentMenu tämänhetkinen Group alaluokan menu josta siirrytään.
     * @param previousMenu Group alaluokan menu johon siirrytään.
     */
    public void changeToPreviousMenu(Group currentMenu, Group previousMenu){
        menuFX.changeToPreviousMenu(currentMenu, previousMenu, this);
    }

    public GameMain getGameMain() {
        return gameMain;
    }
}
