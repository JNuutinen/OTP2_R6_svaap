package view.menuScreenFX;

import javafx.scene.Group;
import javafx.scene.layout.Pane;

public interface MenuFX {

    /**
     * Siirtyy parametrin menuun eteenpäin mentävällä efektillä.
     * @param currentMenu tämänhetkinen Group alaluokan menu
     * @param nextMenu Group alaluokan menu johon siirrytään.
     * @param pane rootPane
     */
    void changeToNextMenu(Group currentMenu, Group nextMenu, Pane pane);

    /**
     * Siirtyy parametrin menuun taaksepäin mentävällä efektillä.
     * @param currentMenu tämänhetkinen Group alaluokan menu
     * @param previousMenu Group alaluokan menu johon siirrytään.
     * @param pane rootPane
     */
    void changeToPreviousMenu(Group currentMenu, Group previousMenu, Pane pane);
}
