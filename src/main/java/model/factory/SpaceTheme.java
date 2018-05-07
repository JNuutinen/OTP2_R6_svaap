package model.factory;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import view.GameMain;

public class SpaceTheme extends Theme {
    private static SpaceTheme INSTANCE = null;

    private SpaceTheme () {}

    public static SpaceTheme getInstance() {
        if (INSTANCE == null) {
            synchronized (SpaceTheme.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SpaceTheme();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public ImageView getBackground() {
        return new ImageView(new Image("images/darkSpace.jpg",
                GameMain.WINDOW_WIDTH, GameMain.WINDOW_HEIGHT, false, false));
    }
}
