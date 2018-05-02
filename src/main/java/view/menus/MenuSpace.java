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

/**
 * StackPanen alaluokka joka toimii kaikkien menujen isäntänä.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class MenuSpace extends StackPane {

    /**
     * Menunvaihtoefektiolio.
     */
    private MenuFX menuFX = new Slider();

    /**
     * MainMenu-menu
     */
    private MainMenu mainMenu;

    /**
     * CustomizeMenu-menu
     */
    private CustomizeMenu customizeMenu;

    /**
     * PauseMenu-menu
     */
    private PauseMenu pauseMenu;

    /**
     * PlayMenu-menu
     */
    private PlayMenu playMenu;

    /**
     * SettingsMenu-menu
     */
    private SettingsMenu settingsMenu;

    /**
     * SettingsMenu-menu
     */
    private NetplayMenu netPlayMenu;

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
        customizeMenu = new CustomizeMenu(messages, this);
        pauseMenu = new PauseMenu(messages, this);
        playMenu = new PlayMenu(messages, this, gameMain);
        settingsMenu = new SettingsMenu(messages, this);
        netPlayMenu = new NetplayMenu(messages, this, gameMain);

        menus.add(mainMenu);
        menus.add(customizeMenu);
        menus.add(pauseMenu);
        menus.add(playMenu);
        menus.add(settingsMenu);
        menus.add(netPlayMenu);

        mainMenu.setPlayMenu(playMenu);
        mainMenu.setSettingsMenu(settingsMenu);
        customizeMenu.setPlayMenu(playMenu);
        playMenu.setMainMenu(mainMenu);
        playMenu.setCustomizeMenu(customizeMenu);
        settingsMenu.setMainMenu(mainMenu);
        netPlayMenu.setMainMenu(mainMenu);
        mainMenu.setNetplayMenu(netPlayMenu);


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

    public PauseMenu getPauseMenu() {
        return pauseMenu;
    }
}
