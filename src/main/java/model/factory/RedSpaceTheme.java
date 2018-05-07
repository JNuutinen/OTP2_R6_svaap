package model.factory;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import view.GameMain;

public class RedSpaceTheme extends Theme {
    private static RedSpaceTheme INSTANCE = null;

    private RedSpaceTheme() {
    }

    public static RedSpaceTheme getInstance() {
        if (INSTANCE == null) {
            synchronized (NormalSpaceTheme.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RedSpaceTheme();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public ImageView getBackground() {
        return new ImageView(new Image("images/darkSpace_red.jpg",
                GameMain.WINDOW_WIDTH, GameMain.WINDOW_HEIGHT, false, false));
    }
}
