package view.menus;

import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import view.GameMain;
import view.menuScreenFX.MenuFX;
import view.menuScreenFX.Slider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MenuSpace extends StackPane {

    /**
     * Menunvaihtoefektiolio.
     */
    private MenuFX menuFX = new Slider();

    private MainMenu mainMenu;
    private NetplayMenu netplayMenu;
    private CustomizeMenu customizeMenu;
    private PauseMenu pauseMenu; // ei saa poistaa
    private PlayMenu playMenu;
    private SettingsMenu settingsMenu;

    /**
     * Lista menuista
     */
    private List<Menu> menus;

    /**
     * Pitää sisällään lokalisoidut tekstit.
     */
    private ResourceBundle messages;

    /**
     * Konstruktori. TODO tämä jdoc vituillaa
     * @param gameMain gameMain playMenua varten.
     * @param messages lokalisoidut tekstit.
     */
    public MenuSpace(GameMain gameMain, ResourceBundle messages){
        this.messages = messages;
        menus = new ArrayList<>();

        mainMenu = new MainMenu(messages, this);
        netplayMenu = new NetplayMenu(messages, this);
        customizeMenu = new CustomizeMenu(messages, this);
        //pauseMenu = new PauseMenu(messages, this);
        playMenu = new PlayMenu(messages, this, gameMain);
        settingsMenu = new SettingsMenu(messages, this);

        menus.add(mainMenu);
        menus.add(netplayMenu);
        menus.add(customizeMenu);
        //menus.add(pauseMenu);
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
     * Vaihtaa menujen lokaalit
     * @param locale Lokaali
     */
    public void changeLocales(Locale locale) {
        messages = ResourceBundle.getBundle("MessagesBundle", locale);
        for (Menu menu : menus) {
            menu.changeLocale(messages);
        }
    }
}
