package view.menuScreenFX;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * Liukumasiirtymä, näkymää vieritetään vasemmalle tai oikealle.
 */
public class Slider implements MenuFX {


    @Override
    public void changeToNextMenu(Group currentMenu, Group nextMenu, Pane pane) {
        pane.getChildren().add(nextMenu);
        double width = pane.getWidth();
        KeyFrame start = new KeyFrame(Duration.ZERO,
                new KeyValue(nextMenu.translateXProperty(), width),
                new KeyValue(currentMenu.translateXProperty(), 0));
        KeyFrame end = new KeyFrame(Duration.seconds(0.5),
                new KeyValue(nextMenu.translateXProperty(), 0),
                new KeyValue(currentMenu.translateXProperty(), -width));
        Timeline slide = new Timeline(start, end);
        slide.setOnFinished(e -> pane.getChildren().remove(currentMenu));
        slide.play();
    }

    @Override
    public void changeToPreviousMenu(Group currentMenu, Group previousMenu, Pane pane) {
        pane.getChildren().add(previousMenu);
        double width = pane.getWidth();
        KeyFrame start = new KeyFrame(Duration.ZERO,
                new KeyValue(previousMenu.translateXProperty(), -width),
                new KeyValue(currentMenu.translateXProperty(), 0));
        KeyFrame end = new KeyFrame(Duration.seconds(0.5),
                new KeyValue(previousMenu.translateXProperty(), 0),
                new KeyValue(currentMenu.translateXProperty(), width));
        Timeline slide = new Timeline(start, end);
        slide.setOnFinished(e -> pane.getChildren().remove(currentMenu));
        slide.play();
    }
}
