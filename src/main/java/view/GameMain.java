package view;

import controller.Controller;
import controller.GameController;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.*;
import model.weapons.Blaster;
import model.weapons.RocketShotgun;
import model.weapons.Weapon;

import java.util.ArrayList;

public class GameMain extends Application implements View {
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;
    public static final int MAIN_MENU_WIDTH = 400;
    public static final int MAIN_MENU_HEIGHT = 400;

    /**
     * Levelivalikon numeroiden määrä, täytyy olla sama kuin luotujen levelien määrä GameControllerissa.
     */
    private static final int NUMBER_OF_LEVELS = 6;
    public static ArrayList<String> input;

    private MainMenu mainMenu;
    private ArrayList<Unit> units;
    private Pane pane;
    private Controller controller;
    private Label score;
    private Stage primaryStage;
    private boolean debuggerToolsEnabled = true;
    private Label debugger_fps;
    private Label debugger_currentFps;
    private boolean debugger_droppedBelowFpsTarget = false;
    private double debugger_secondCounter = 0;
    private ImageView healthIv = new ImageView();


    @Override
    public void init() {
        //controller = new GameController(this);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        primaryStage.setTitle("svaap: SivuvieritysAvaruusAmmuntaPeli");
        primaryStage.setResizable(false);
        mainMenu(this.primaryStage);
    }

    public void returnToMain(){
        mainMenu(primaryStage);
    }


    public void mainMenu(Stage primaryStage) {
        controller = new GameController(this);
        units = new ArrayList<>();
        // sulje ohjelma kun ikkunan sulkee
        primaryStage.setOnCloseRequest(event -> System.exit(0));

        // Päävalikon luonti
        mainMenu = new MainMenu(NUMBER_OF_LEVELS);
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

    public void removeFromCollisionList(Unit unit){
        units.remove(unit);
    }

    @Override
    public void removeSprite(Sprite sprite){
        pane.getChildren().remove(sprite);
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

    /* TODO TAA MITTARI ON RIKKI JOKA KORJATAAN MYOHEMMIN
     */
    @Override
    public void setCurrentFps(double currentFps){
        if(debugger_droppedBelowFpsTarget) {
            debugger_secondCounter += (1 / currentFps);
            if (debugger_secondCounter > 0) {
                debugger_droppedBelowFpsTarget = false;
                debugger_currentFps.setTextFill(Color.web("#ffffff"));//valkone
            }
        }
        else if (currentFps < 56) {
            debugger_currentFps.setText("rikki-> " + currentFps);
            debugger_currentFps.setTextFill(Color.web("#ff8884"));//punane
            debugger_droppedBelowFpsTarget = true;
            debugger_secondCounter = 0;
        }
        else if (currentFps < 60) {
            debugger_currentFps.setText("rikki-> " + currentFps);
            debugger_currentFps.setTextFill(Color.web("#ffef7c"));//keltane
            debugger_droppedBelowFpsTarget = true;
            debugger_secondCounter = 0;
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

    private void startGame(Stage primaryStage) {





        // Peligrafiikoiden luonti
        pane = new Pane();
        VBox.setVgrow(pane, Priority.NEVER);
        VBox vbox;
        vbox = new VBox(pane);
        Scene scene = new Scene(vbox, WINDOW_WIDTH, WINDOW_HEIGHT);
        vbox.setStyle("-fx-background-color: black");

        score = new Label("Score: " + controller.getScore());
        score.setFont(new Font("Cambria", 32));
        pane.getChildren().add(score);
        pane.getChildren().add(healthIv);


        GameBackground gmg = new GameBackground(controller);


        //---------------- debugger
        if(debuggerToolsEnabled) {
            debugger_fps = new Label("fps");
            debugger_fps.setFont(new Font("Cambria", 16));
            debugger_fps.setLayoutX(250);
            pane.getChildren().add(debugger_fps);

            debugger_currentFps = new Label("currentFps");
            debugger_currentFps.setFont(new Font("Cambria", 16));
            debugger_currentFps.setLayoutX(540);
            pane.getChildren().add(debugger_currentFps);
        }


        //pelaajan luonti
        Player player = new Player(controller);
        player.setHp(1000000);

        // Pelaajan aseet
        // PÄÄASE
        Component primary = new Blaster(controller, player, "circle", 5, 0, 0, 5);
        player.setPrimaryWeapon((Weapon) primary);
        //SIVUASE
        //Component secondary = new RocketLauncher(controller, player, "circle", 7, 0, -5, 0);
        Component secondary = new RocketShotgun(controller, player, "circle", 7, 0, -5, 0,
                3, 11);
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
        controller.startLevel(mainMenu.getSelectedLevel());

    }



}

