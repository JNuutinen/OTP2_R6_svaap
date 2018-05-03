package view.menus;

import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import view.GameMain;
import view.menuScreenFX.MenuFX;
import view.menuScreenFX.PlainMenuTransition;

import java.util.*;

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
    private MenuFX menuFX = PlainMenuTransition.getInstance();

    /**
     * PauseMenu-menu
     */
    private PauseMenu pauseMenu;

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
    public MenuSpace(GameMain gameMain, ResourceBundle messages, Map<String, Locale> locales) {
        this.messages = messages;
        menus = new ArrayList<>();

        MainMenu mainMenu = new MainMenu(messages, this);

        CustomizeMenu customizeMenu = new CustomizeMenu(messages, this);
        pauseMenu = new PauseMenu(messages, this);

        PlayMenu playMenu = new PlayMenu(messages, this, gameMain);

        SettingsMenu settingsMenu = new SettingsMenu(messages, locales, this);
        settingsMenu = new SettingsMenu(messages, locales, this);

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
        netPlayMenu.setCustomizeMenu(customizeMenu);
        mainMenu.setNetplayMenu(netPlayMenu);


        this.getChildren().add(mainMenu);
    }

    /**
     * Vaihtaa lokaalin menuFX:n eteenpäin mentävällä efektillä parametrin menuun.
     * @param currentMenu tämänhetkinen Group alaluokan menu josta siirrytään.
     * @param nextMenu Group alaluokan menu johon siirrytään.
     */
    void changeToNextMenu(Group currentMenu, Group nextMenu) {
        menuFX.changeToNextMenu(currentMenu, nextMenu, this);
    }

    /**
     * Vaihtaa lokaalin menuFX:n taaksepäin mentävällä efektillä parametrin menuun.
     * @param currentMenu tämänhetkinen Group alaluokan menu josta siirrytään.
     * @param previousMenu Group alaluokan menu johon siirrytään.
     */
    void changeToPreviousMenu(Group currentMenu, Group previousMenu) {
        menuFX.changeToPreviousMenu(currentMenu, previousMenu, this);
    }

    /**
     * Vaihtaa menujen lokaalit
     * @param locale Lokaali
     */
    void changeLocales(Locale locale) {
        messages = ResourceBundle.getBundle("MessagesBundle", locale);
        for (Menu menu : menus) {
            menu.changeLocale(messages);
        }
    }

    /**
     * Getteri menuFX:lle.
     *
     * @return Nykyinen menujen siirtymäefekti.
     */
    MenuFX getMenuFX() {
        return menuFX;
    }

    /**
     * Setteri menuFX:lle.
     *
     * @param menuFX Menujen siirtymäefektiksi asetettava MenuFX.
     */
    void setMenuFX(MenuFX menuFX) {
        this.menuFX = menuFX;
    }

    /**
     * Getteri pausemenulle.
     *
     * @return PauseMenu.
     */
    public PauseMenu getPauseMenu() {
        return pauseMenu;
    }
}
