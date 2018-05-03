package view.menuScreenFX;

import javafx.scene.Group;
import javafx.scene.layout.Pane;

/**
 * Menun siirtymä ilman efektejä.
 *
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class PlainMenuTransition implements MenuFX {

    @Override
    public void changeToNextMenu(Group currentMenu, Group nextMenu, Pane pane) {
        pane.getChildren().remove(currentMenu);
        pane.getChildren().add(nextMenu);
    }

    @Override
    public void changeToPreviousMenu(Group currentMenu, Group previousMenu, Pane pane) {
        pane.getChildren().remove(currentMenu);
        pane.getChildren().add(previousMenu);
    }
}
