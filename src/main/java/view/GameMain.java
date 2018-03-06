package view;

import controller.Controller;
import controller.GameController;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.*;
import model.weapons.Blaster;
import model.weapons.RocketShotgun;
import model.weapons.Weapon;

import java.util.ArrayList;

public class GameMain extends Application implements View {
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;
    public static final int BANNER_HEIGHT = 200;
    public static final int UNDEFINED_TAG = 0;
    public static final int PLAYER_SHIP_TAG = 1;
    public static final int ENEMY_SHIP_TAG = 2;
    public static final int BOSS_SHIP_TAG = 3;
    public static final int PLAYER_PROJECTILE_TAG = 4;
    public static final int ENEMY_PROJECTILE_TAG = 5;
    public static final int PLAYER_TRACE_TAG = 6;
    public static final int ENEMY_TRACE_TAG = 7;


    /**
     * Levelivalikon numeroiden määrä, täytyy olla sama kuin luotujen levelien määrä GameControllerissa.
     */
    private static final int NUMBER_OF_LEVELS = 6;
    public static ArrayList<String> input;

    private PlayMenu playMenu;
    private ArrayList<Unit> units;
    private Pane pane;
    private Scene scene;
    private Pane gameRoot;
    private Controller controller;
    private Label score;
    private Stage primaryStage;
    private boolean debuggerToolsEnabled = true;
    private Label debugger_fps;
    private Label debugger_currentFps;
    private boolean debugger_droppedBelowFpsTarget = false;
    private double debugger_secondCounter = 0;
    private ImageView healthIv = new ImageView();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        primaryStage.setTitle("svaap: SivuvieritysAvaruusAmmuntaPeli");
        primaryStage.setResizable(false);
        setupGame(this.primaryStage);
    }

    public void returnToMain(){
        pane.getChildren().remove(gameRoot);
        setupGame(primaryStage);
    }

    @Override
    public void addSprite(Sprite sprite) {
        gameRoot.getChildren().add(sprite);
    }

    @Override
    public void addUnitToCollisionList(Unit unit) {
        units.add(unit);
    }

    @Override
    public ArrayList<Unit> getCollisionList() {
        return units;
    }

    public void removeFromCollisionList(Unit unit){
        units.remove(unit);
    }

    @Override
    public void removeSprite(Sprite sprite){
        gameRoot.getChildren().remove(sprite);
    }

    @Override
    public void setFps(double fps) {
        if(fps < 60){
            debugger_fps.setTextFill(Color.web("#ffe500"));//keltane
        } else if(fps < 57){
            debugger_fps.setTextFill(Color.web("#ff2b2b"));//punane
        } else{
            debugger_fps.setTextFill(Color.web("#ffffff"));//valkone
        }
        debugger_fps.setText("mainLoop frames in last sec: " + fps);
    }

    // ------ Debugger

    @Override
    public void setCurrentFps(double currentFps){

        if(debugger_droppedBelowFpsTarget) {
            debugger_secondCounter += (1 / currentFps);
            if (debugger_secondCounter > 0.8) {
                debugger_droppedBelowFpsTarget = false;
                debugger_currentFps.setTextFill(Color.web("#ffffff"));//valkone
            }
        }
        else if (currentFps < 56) {
            debugger_currentFps.setText("| mainLoop fps: " + currentFps);
            debugger_currentFps.setTextFill(Color.web("#ff8884"));//punane
            debugger_droppedBelowFpsTarget = true;
            debugger_secondCounter = 0;
        }
        else if (currentFps < 60) {
            debugger_currentFps.setText("| mainLoop fps: " + currentFps);
            debugger_currentFps.setTextFill(Color.web("#ffef7c"));//keltane
            debugger_droppedBelowFpsTarget = true;
            debugger_secondCounter = 0;
        }
        else{
            debugger_currentFps.setText("| mainLoop fps: " + currentFps);
        }
    }

    @Override
    public void setScore(int score) {
        this.score.setText("Score: " + score);
    }

    public void setHealthbar(int hp){
        if(hp > 0 && hp <= 10) {
            Image healthbar = new Image("/images/healthbar/" + hp + ".png");
            healthIv.setX(WINDOW_WIDTH / 2);
            healthIv.setImage(healthbar);
        }else{
            healthIv.setImage(null);
        }
    }

    private void setupGame(Stage primaryStage) {
        controller = new GameController(this);
        units = new ArrayList<>();
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        MainMenu mainMenu = new MainMenu();
        Group mainMenuGroup = mainMenu.getGroup();
        playMenu = new PlayMenu(NUMBER_OF_LEVELS);
        Group playMenuGroup = playMenu.getGroup();
        StackPane menuSpace = new StackPane(mainMenuGroup);

        ImageView svaapBanner = new ImageView(new Image("images/svaap.png"));
        svaapBanner.resize(200, 50);
        svaapBanner.setStyle("-fx-background-color: transparent");
        StackPane bannerSpace = new StackPane(svaapBanner);
        bannerSpace.setPadding(new Insets(100,0,100,0));

        mainMenu.play.setOnAction(event -> slideIn(mainMenuGroup, playMenuGroup, menuSpace));

        playMenu.backButton.setOnAction(event -> slideOut(playMenuGroup, mainMenuGroup, menuSpace));

        pane = new Pane();
        pane.setStyle("-fx-background-color: black");
        BorderPane uiRoot = new BorderPane();
        uiRoot.setStyle("-fx-background-color: black");
        uiRoot.setTop(bannerSpace);
        uiRoot.setCenter(menuSpace);

        pane.getChildren().add(uiRoot);
        scene = new Scene(pane, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        playMenu.startButton.setOnAction(event -> {
            FadeTransition ft = new FadeTransition(Duration.millis(1000), uiRoot);
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            ft.play();
            ft.setOnFinished(event1 -> {
                pane.getChildren().remove(uiRoot);
                startGame(primaryStage);
            });
        });
        primaryStage.show();
    }

    private void startGame(Stage primaryStage) {
        gameRoot = new Pane();
        pane.getChildren().add(gameRoot);
        FadeTransition ft = new FadeTransition(Duration.millis(2000), gameRoot);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();

        score = new Label("Score: " + controller.getScore());
        score.setFont(new Font("Cambria", 32));
        gameRoot.getChildren().add(score);
        gameRoot.getChildren().add(healthIv);

        GameBackground gmg = new GameBackground(controller);

        //---------------- debugger
        if(debuggerToolsEnabled) {
            debugger_fps = new Label("fps");
            debugger_fps.setFont(new Font("Cambria", 16));
            debugger_fps.setLayoutX(250);
            gameRoot.getChildren().add(debugger_fps);

            debugger_currentFps = new Label("currentFps");
            debugger_currentFps.setFont(new Font("Cambria", 16));
            debugger_currentFps.setLayoutX(540);
            gameRoot.getChildren().add(debugger_currentFps);
        }


        //pelaajan luonti
        Player player = new Player(controller, Color.CYAN);
        player.setHp(1000000);

        // Pelaajan aseet
        // PÄÄASE
        Component primary = new Blaster(controller, player, "circle", 5, 0, 0, 5, Color.LIGHTBLUE,
                45, 100, 12);
        player.addToPrimaryWeapons((Weapon) primary);
        primary = new Blaster(controller, player, "circle", 5, 0, 0, 5, Color.LIGHTBLUE,
                45, 100, -12);
        player.addToPrimaryWeapons((Weapon) primary);


        //SIVUASE
        //Component secondary = new RocketLauncher(controller, player, "circle", 7, 0, -5, 0);
        Component secondary = new RocketShotgun(controller, player, "circle", 7, 0, -5, 0, 3, 20);
        /*Component secondary = new LaserGun(controller, player, "circle", 5, 0, 0, 5, Color.WHITE,
                80, 0, 0.5);*/
        /*Component secondary = new Blaster(controller, player, "circle", 5, 0, 0, 5, Color.FUCHSIA,
                45, 100, -20);*/
        player.setSecondaryWeapon((Weapon) secondary);

        // Aseiden lisäys komponentteihin, jotta aseet näkyvissä
        ArrayList<Component> components = new ArrayList<>();
        components.add(primary);
        components.add(secondary);

        Component b = new Component("circle", 10, 0, Color.RED, 0,0);
        components.add(b);

        Component c = new Component("rectangle", 10 , 0, Color.WHITE, 0,0);
        components.add(c);

        Component d = new Component("triangle", 10, 0, Color.BLUE, 0,0);
        components.add(d);

        player.equipComponents(components);


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
        System.out.println(playMenu.getSelectedLevel());
        controller.startLevel(playMenu.getSelectedLevel());
    }

    /**
     * Liukumasiirtymä, näkymää vieritetään vasemmalle.
     * @param from Group, joka vieritetään pois näytöstä. Täytyy olla jo lisättynä Paneen.
     * @param to Group, joka vieritetään näyttöön. Lisätään paneen metodissa.
     * @param pane Pane, jota käsitellään.
     */
    private void slideIn(Group from, Group to, Pane pane) {
        pane.getChildren().add(to);
        double width = pane.getWidth();
        KeyFrame start = new KeyFrame(Duration.ZERO,
                new KeyValue(to.translateXProperty(), width),
                new KeyValue(from.translateXProperty(), 0));
        KeyFrame end = new KeyFrame(Duration.seconds(0.5),
                new KeyValue(to.translateXProperty(), 0),
                new KeyValue(from.translateXProperty(), -width));
        Timeline slide = new Timeline(start, end);
        slide.setOnFinished(e -> pane.getChildren().remove(from));
        slide.play();
    }

    /**
     * Liukumasiirtymä, näkymää vieritetään oikealle.
     * @param from Group, joka vieritetään pois näytöstä. Täytyy olla jo lisättynä Paneen.
     * @param to Group, joka vieritetään näyttöön. Lisätään paneen metodissa.
     * @param pane Pane, jota käsitellään.
     */
    private void slideOut(Group from, Group to, Pane pane) {
        pane.getChildren().add(to);
        double width = pane.getWidth();
        KeyFrame start = new KeyFrame(Duration.ZERO,
                new KeyValue(to.translateXProperty(), -width),
                new KeyValue(from.translateXProperty(), 0));
        KeyFrame end = new KeyFrame(Duration.seconds(0.5),
                new KeyValue(to.translateXProperty(), 0),
                new KeyValue(from.translateXProperty(), width));
        Timeline slide = new Timeline(start, end);
        slide.setOnFinished(e -> pane.getChildren().remove(from));
        slide.play();
    }



}

