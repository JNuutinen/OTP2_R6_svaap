package model.factory;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import view.GameMain;

public class PurpleSpaceTheme extends Theme {
    private static PurpleSpaceTheme INSTANCE = null;

    private PurpleSpaceTheme() {
    }

    public static PurpleSpaceTheme getInstance() {
        if (INSTANCE == null) {
            synchronized (PurpleSpaceTheme.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PurpleSpaceTheme();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public ImageView getBackground() {
        return new ImageView(new Image("images/darkSpace_purple.jpg",
                GameMain.WINDOW_WIDTH, GameMain.WINDOW_HEIGHT, false, false));
    }
}
