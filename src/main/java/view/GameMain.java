package view;

import controller.Controller;
import controller.GameController;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.Player;
import model.Sprite;
import model.Unit;

import java.util.ArrayList;

public class GameMain extends Application implements View {
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;
    public static ArrayList<String> input;

    private ArrayList<Unit> units;
    private Pane pane;
    private Label debugger_fps;
    private Controller controller;
    private Label score;

    @Override
    public void init() {
        controller = new GameController(this);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        units = new ArrayList<>();
        // sulje ohjelma kun ikkunan sulkee
        primaryStage.setOnCloseRequest(event -> System.exit(0));

        // Päävalikon luonti
        MainMenu mainMenu = new MainMenu();
        Scene mainMenuScene = mainMenu.scene();
        primaryStage.setScene(mainMenuScene);

        // Start game painiketta painaessa mainMenuScene vaihdetaan pelin sceneen ja peli käynnistyy
        mainMenu.start.setOnAction((event) -> startGame(primaryStage));

        primaryStage.show();
    }

    @Override
    public void addSprite(Sprite sprite) {
        pane.getChildren().add(sprite);
    }

    @Override
    public void addUnitToCollisionList(Unit unit) {
        units.add(unit);
    }

    @Override
    public ArrayList<Unit> getCollisionList() {
        return units;
    }

    @Override
    public void removeSprite(Sprite sprite){
        pane.getChildren().remove(sprite);
    }

    @Override
    public void setFps(double fps) {
        if(fps < 60){
            debugger_fps.setTextFill(Color.web("#4c4323"));
        } else if(fps < 57){
            debugger_fps.setTextFill(Color.web("#f44242"));
        } else{
            debugger_fps.setTextFill(Color.web("#3d3d3d"));
        }
        debugger_fps.setText("avg. fps of last second: " + fps);
    }

    @Override
    public void setScore(int score) {
        this.score.setText("Score: " + score);
    }

    private void startGame(Stage primaryStage) {
        // Peligrafiikoiden luonti
        pane = new Pane();
        VBox.setVgrow(pane, Priority.NEVER);
        VBox vbox;
        vbox = new VBox(pane);
        Scene scene = new Scene(vbox, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setTitle("svaap:development");
        vbox.setStyle("-fx-background-color: black");

        score = new Label("Score: " + controller.getScore());
        score.setFont(new Font("Cambria", 32));
        pane.getChildren().add(score);

        //debugger_fps
        debugger_fps = new Label("fps");

        debugger_fps.setFont(new Font("Cambria", 32));
        debugger_fps.setLayoutX(250);
        pane.getChildren().add(debugger_fps);

        //pelaajan luonti ja lisays looppilistaan
        Player player = new Player(controller);
        player.setPosition(100, 300);
        // tieto controllerille pelaajasta
        controller.addPlayer(player);
        controller.addUpdateable(player);
        // ArrayList pitää sisällään kyseisellä hetkellä painettujen näppäinten event-koodit
        input = new ArrayList<>();

        // Näppäintä painaessa, lisää se arraylistiin, ellei se jo ole siellä
        scene.setOnKeyPressed(keyEvent -> {
            String code = keyEvent.getCode().toString();
            if (!input.contains(code)) input.add(code);
        });

        // Kun näppäintä ei enää paineta, poista se arraylististä
        scene.setOnKeyReleased(keyEvent -> {
            String code = keyEvent.getCode().toString();
            input.remove(code);
        });
        primaryStage.setScene(scene);
        
        // keskitetään ikkuna näyttöön
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((screenBounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((screenBounds.getHeight() - primaryStage.getHeight()) / 2);
        controller.startLoop();
        controller.startLevel(0);
    }
}

