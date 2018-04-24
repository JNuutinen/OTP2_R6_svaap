package view.menus;

import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import view.GameMain;
import view.MenuScreenFX.MenuFX;
import view.MenuScreenFX.Slider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MenuSpace extends StackPane {

    private MenuFX menuFX = new Slider();
    private GameMain gameMain;

    private MainMenu mainMenu;
    private NetplayMenu netplayMenu;
    private CustomizeMenu customizeMenu;
    private PauseMenu pauseMenu;
    private PlayMenu playMenu;
    private SettingsMenu settingsMenu;

    private List<Menu> menus;
    /**
     * Pitää sisällään lokalisoidut tekstit.
     */
    private ResourceBundle messages;


    public MenuSpace(GameMain gameMain, ResourceBundle messages){
        this.gameMain = gameMain;
        this.messages = messages;
        menus = new ArrayList<>();

        mainMenu = new MainMenu(messages, this);
        netplayMenu = new NetplayMenu(messages, this);
        customizeMenu = new CustomizeMenu(messages, this);
        pauseMenu = new PauseMenu(messages, this);
        playMenu = new PlayMenu(messages, this, gameMain);
        settingsMenu = new SettingsMenu(messages, this);

        menus.add(mainMenu);
        menus.add(netplayMenu);
        menus.add(customizeMenu);
        menus.add(pauseMenu);
        menus.add(playMenu);
        menus.add(settingsMenu);

        mainMenu.setNetplayMenu(netplayMenu);
        mainMenu.setPlayMenu(playMenu);
        mainMenu.setSettingsMenu(settingsMenu);
        netplayMenu.setMainMenu(mainMenu);
        customizeMenu.setPlayMenu(playMenu);
        playMenu.setMainMenu(mainMenu);
        playMenu.setCustomizeMenu(customizeMenu);
        settingsMenu.setMainMenu(mainMenu);

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

    /**
     * Muuttaa pelin lokalisaatiota.
     *
     * @param locale Asetettava lokaali.
     */
    public void changeLocale(Locale locale) {
        messages = ResourceBundle.getBundle("MessagesBundle", locale);
        for (Menu menu : menus) {
            menu.changeLocale(messages);
        }
    }

    public GameMain getGameMain() {
        return gameMain;
    }
}
