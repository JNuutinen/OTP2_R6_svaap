package view.menuScreenFX;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * Liukumasiirtymä, näkymää vieritetään vasemmalle tai oikealle.
 *
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class SliderMenuTransition implements MenuFX {

    /**
     * Siirtymän kesto sekunteina.
     */
    private static final Duration DURATION = Duration.seconds(0.5);

    @Override
    public void changeToNextMenu(Group currentMenu, Group nextMenu, Pane pane) {
        pane.getChildren().add(nextMenu);
        double width = pane.getWidth();
        KeyFrame start = new KeyFrame(Duration.ZERO,
                new KeyValue(nextMenu.translateXProperty(), width),
                new KeyValue(currentMenu.translateXProperty(), 0));
        KeyFrame end = new KeyFrame(DURATION,
                new KeyValue(nextMenu.translateXProperty(), 0),
                new KeyValue(currentMenu.translateXProperty(), -width));
        playEffect(start, end, currentMenu, pane);
    }

    @Override
    public void changeToPreviousMenu(Group currentMenu, Group previousMenu, Pane pane) {
        pane.getChildren().add(previousMenu);
        double width = pane.getWidth();
        KeyFrame start = new KeyFrame(Duration.ZERO,
                new KeyValue(previousMenu.translateXProperty(), -width),
                new KeyValue(currentMenu.translateXProperty(), 0));
        KeyFrame end = new KeyFrame(DURATION,
                new KeyValue(previousMenu.translateXProperty(), 0),
                new KeyValue(currentMenu.translateXProperty(), width));
        playEffect(start, end, currentMenu, pane);
    }

    /**
     * Toistaa siirtymäefektin ja poistaa panesta menun, josta ollaan siirrytty pois.
     *
     * @param start Efektin aloitus KeyFrame.
     * @param end   Efektin lopetus KeyFrame.
     * @param from  Menun Group, josta siirtymä alkoi ja joka poistetaan panestaan.
     * @param pane  Pane, jossa menu sijaitsee.
     */
    private void playEffect(KeyFrame start, KeyFrame end, Group from, Pane pane) {
        Timeline fade = new Timeline(start, end);
        fade.setOnFinished(e -> pane.getChildren().remove(from));
        fade.play();
    }
}
