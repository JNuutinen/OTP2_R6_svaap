package view.menus;

import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import view.GameMain;
import view.MenuScreenFX.MenuFX;
import view.MenuScreenFX.Slider;

import java.util.ResourceBundle;

public class MenuSpace extends StackPane {

    private MenuFX menuFX = new Slider();
    private GameMain gameMain;

    private MainMenu mainMenu;
    private NetplayMenu netplayMenu;
    private CustomizeMenu customizeMenu;
    private PauseMenu pauseMenu;
    private PlayMenu playMenu;


    public MenuSpace(GameMain gameMain, ResourceBundle messages){
        this.gameMain = gameMain;
        mainMenu = new MainMenu(messages, this);
        netplayMenu = new NetplayMenu(messages, this);
        customizeMenu = new CustomizeMenu(messages, this);
        pauseMenu = new PauseMenu(messages, this);
        playMenu = new PlayMenu(messages, this, gameMain);

        mainMenu.setNetplayMenu(netplayMenu);
        mainMenu.setPlayMenu(playMenu);
        netplayMenu.setMainMenu(mainMenu);
        customizeMenu.setPlayMenu(playMenu);
        playMenu.setMainMenu(mainMenu);
        playMenu.setCustomizeMenu(customizeMenu);

        this.getChildren().add(mainMenu);
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
