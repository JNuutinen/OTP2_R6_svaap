package model.factory;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import view.GameMain;

public class NormalSpaceTheme extends Theme {
    private static NormalSpaceTheme INSTANCE = null;

    private NormalSpaceTheme() {
    }

    public static NormalSpaceTheme getInstance() {
        if (INSTANCE == null) {
            synchronized (NormalSpaceTheme.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NormalSpaceTheme();
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
