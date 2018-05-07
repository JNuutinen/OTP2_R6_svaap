package model.factory;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import view.GameMain;

public class UnderwaterTheme extends Theme{
    private static UnderwaterTheme INSTANCE = null;

    private UnderwaterTheme () {}

    public static UnderwaterTheme getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UnderwaterTheme();
        }
        return INSTANCE;
    }

    @Override
    public ImageView getBackground() {
        return new ImageView(new Image("images/underwater.jpg",
                GameMain.WINDOW_WIDTH, GameMain.WINDOW_HEIGHT, false, false));
        }
    }
