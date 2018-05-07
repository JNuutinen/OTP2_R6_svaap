package model.factory;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import view.GameMain;

public class GreenSpaceTheme extends Theme {
    private static GreenSpaceTheme INSTANCE = null;

    private GreenSpaceTheme() {
    }

    public static GreenSpaceTheme getInstance() {
        if (INSTANCE == null) {
            synchronized (GreenSpaceTheme.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GreenSpaceTheme();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public ImageView getBackground() {
        return new ImageView(new Image("images/darkSpace_green.jpg",
                GameMain.WINDOW_WIDTH, GameMain.WINDOW_HEIGHT, false, false));
    }
}
