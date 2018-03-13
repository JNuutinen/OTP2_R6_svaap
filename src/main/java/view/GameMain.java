package view;

import controller.Controller;
import controller.GameController;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
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
import javafx.stage.Stage;
import javafx.util.Duration;
import model.*;
import model.weapons.Blaster;
import model.weapons.LaserGun;
import model.weapons.RocketShotgun;
import model.weapons.Weapon;
import view.menus.CustomizeMenu;
import view.menus.MainMenu;
import view.menus.PauseMenu;
import view.menus.PlayMenu;

import java.util.ArrayList;

/**
 * Pelin View. JavaFX Application.
 */
public class GameMain extends Application implements View {

    /**
     * Peli-ikkunan leveys.
     */
    public static final double WINDOW_WIDTH = 1260;

    /**
     * Peli-ikkunan korkeus.
     */
    public static final double WINDOW_HEIGHT = 820;

    /**
     * Raja x-akselilla, mitä edemmäs pelaaja ei voi liikkua.
     */
    public static final double PLAYER_X_LIMIT = WINDOW_WIDTH * .55;

    /**
     * svaap -bannerin korkeus menuissa.
     */
    public static final int BANNER_HEIGHT = 200;

    /**
     * Tagi, jota ei ole erityisesti asetettu.
     */
    public static final int UNDEFINED_TAG = 0;

    /**
     * Pelaajan aluksen tagi.
     */
    public static final int PLAYER_SHIP_TAG = 1;

    /**
     * Vihollisaluksien tagi.
     */
    public static final int ENEMY_SHIP_TAG = 2;

    /**
     * Bossien tagi.
     */
    public static final int BOSS_SHIP_TAG = 3;

    /**
     * Pelaajan ammuksien tagi.
     */
    public static final int PLAYER_PROJECTILE_TAG = 4;

    /**
     * Vihollisten ammuksien tagi.
     */
    public static final int ENEMY_PROJECTILE_TAG = 5;

    /**
     * Pelaajan trace tag.
     */
    public static final int PLAYER_TRACE_TAG = 6;

    /**
     * Vihollisten trace tag.
     */
    public static final int ENEMY_TRACE_TAG = 7;

    /**
     * Spriten määrittämätön nimitagi.
     */
    public static final String SPRITE_NAME_UNDEFINED = "undefined";


    /**
     * Levelivalikon numeroiden määrä, täytyy olla sama kuin luotujen levelien määrä GameControllerissa.
     */
    private static final int NUMBER_OF_LEVELS = 6;

    /**
     * Lista, joka sisältää tietyllä hetkellä painetut näppäimet.
     */
    public static ArrayList<String> input;


    /**
     * Pelaa -valikko.
     */
    private PlayMenu playMenu;

    /**
     * Lista Uniteista.
     */
    private ArrayList<Unit> units;

    /**
     * Ohjelman uloin Pane.
     */
    private BorderPane pane;

    /**
     * Ohjelman Scene.
     */
    private Scene scene;

    /**
     * BorderPane, jossa sijaitsee itse peli (Spritet yms).
     */
    private BorderPane gameRoot;

    /**
     * Pelin kontrolleri.
     */
    private Controller controller;

    /**
     * Label, jossa näytetään pelaajan pisteet.
     */
    private Label score;

    /**
     * Ohjelman Primary Stage.
     */
    private Stage primaryStage;

    /**
     * Kytkin, jonka perusteella näytetään debuggaus fps mittarit.
     */
    private boolean debuggerToolsEnabled = true;

    /**
     * Label debug fps 1.
     */
    private Label debugger_fps;

    /**
     * Labes debug fps 2.
     */
    private Label debugger_currentFps;

    /**
     * Kertoo, onko nykyinen fps alle tavoitteen.
     */
    private boolean debugger_droppedBelowFpsTarget = false;

    /**
     * Debuggereiden sekuntimittari.
     */
    private double debugger_secondCounter = 0;

    /**
     * Bossin healthbar ImageView.
     */
    private ImageView bossHealth = new ImageView();

    /**
     * Pelaajan healthbar ImageView.
     */
    private ImageView playerHealth = new ImageView();

    /**
     * Pane, jossa sijaitsee pelin aikana näkyvä ui.
     */
    private Pane uiPane;

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

    /**
     * Palauttaa ohjelman pelistä päävalikkoon.
     */
    public void returnToMain(){
        FadeTransition ft = new FadeTransition(Duration.millis(1000), gameRoot);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.play();
        ft.setOnFinished(event1 -> {
            pane.getChildren().remove(uiPane);
            uiPane = null;
            pane.getChildren().remove(gameRoot);
            gameRoot = null;
            setupGame(primaryStage);
        });
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
        debugger_fps.setText(String.valueOf(fps));
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
        this.score.setText(String.valueOf(score));
        this.score.toFront();
    }

    /**
     * Asettaa näkyvän healthbarin selectorin mukaan, 0 tarkoittaa bossia ja 1 tarkoittaa pelaajaa.
     * @param hp Unitin hp kymmenyksissä.
     * @param selector Unitin tunniste. 0 = boss ja 1 = pelaaja
     */
    public void setHealthbar(int hp, int selector){
        switch(selector){
            case 0:
                if(hp > 0 && hp <= 10) {
                    Image healthbar = new Image("/images/bossHPBar/"+hp+".png");
                    bossHealth.setX(700);
                    bossHealth.setY(7);
                    bossHealth.setImage(healthbar);
                    bossHealth.toFront();
                }else{
                    bossHealth.setImage(null);
                }
                break;
            case 1:
                if(hp > 0 && hp <= 10) {
                    Image healthbar = new Image("/images/playerHPBar/"+hp+".png");
                    playerHealth.setX(200);
                    playerHealth.setY(7);
                    playerHealth.setImage(healthbar);
                    playerHealth.toFront();
                }else{
                    playerHealth.setImage(null);
                }
        }
    }

    /**
     * Alustaa ohjelman.
     * @param primaryStage Ohjelman Primary Stage.
     */
    private void setupGame(Stage primaryStage) {
        controller = new GameController(this);
        units = new ArrayList<>();
        primaryStage.setOnCloseRequest(event -> System.exit(0));

        // Banneri
        ImageView svaapBanner = new ImageView(new Image("images/SVAAP_logo_white.png"));
        svaapBanner.resize(200, 50);
        svaapBanner.setStyle("-fx-background-color: transparent");
        StackPane bannerSpace = new StackPane(svaapBanner);
        bannerSpace.setPadding(new Insets(100,0,100,0));

        // Luodaan gameRoot jo tässä, koska pelaaja luodaan ja sen Sprite lisätään siihen
        gameRoot = new BorderPane();

        //pelaajan luonti jo tässä, jotta saadaan luotua aseet customizemenulle (aseet vaatii playerin parametrina)
        Player player = new Player(controller, Color.LIME);


        // Valittavat aselistat
        ArrayList<Weapon> primaries = createPlayerPrimaries1(player);
        ArrayList<Weapon> secondaries = createPlayerSecondaries(player);

        // Main menu
        MainMenu mainMenu = new MainMenu();
        Group mainMenuGroup = mainMenu.getGroup();

        // Play menu
        playMenu = new PlayMenu(NUMBER_OF_LEVELS);
        Group playMenuGroup = playMenu.getGroup();

        // Pane kaikille menuille
        StackPane menuSpace = new StackPane(mainMenuGroup);

        // Customize menu
        CustomizeMenu customizeMenu = new CustomizeMenu(primaries, secondaries);
        Group customizeMenuGroup = customizeMenu.getGroup();
        customizeMenu.backButton.setOnAction(event -> slideOut(customizeMenuGroup, playMenuGroup, menuSpace));

        // Main menun play click event
        mainMenu.play.setOnAction(event ->
            slideIn(mainMenuGroup, playMenuGroup, menuSpace));

        // Play menun back button click event
        playMenu.backButton.setOnAction(event -> slideOut(playMenuGroup, mainMenuGroup, menuSpace));

        // Play menun customize button click event
        playMenu.customizeButton.setOnAction(event -> slideIn(playMenuGroup, customizeMenuGroup, menuSpace));

        // Pane, se kaikkien isä (uiRoot, gameRoot)
        pane = new BorderPane();
        pane.setStyle("-fx-background-color: black");
        BorderPane uiRoot = new BorderPane();
        uiRoot.setStyle("-fx-background-color: black");

        // Banner uiRootin yläosaan
        uiRoot.setTop(bannerSpace);

        // kaikki menut uiRootin keskelle
        uiRoot.setCenter(menuSpace);

        // uiRoot pääpaneen
        pane.getChildren().add(uiRoot);
        FadeTransition ft = new FadeTransition(Duration.millis(1000), uiRoot);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();

        // Ohjelman scene
        scene = new Scene(pane, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);

        // Pelin aloituspainike click event
        playMenu.startButton.setOnAction(event -> {
            playMenu.startButton.setDisable(true);
            FadeTransition ft2 = new FadeTransition(Duration.millis(1000), uiRoot);
            ft2.setFromValue(1.0);
            ft2.setToValue(0.0);
            ft2.play();
            ft2.setOnFinished(event1 -> {
                pane.getChildren().remove(uiRoot);
                startGame(primaryStage, player, customizeMenu.getSelectedPrimaryWeapon(), customizeMenu.getSelectedSecondaryWeapon());
            });
        });

        // Show käyntiin
        primaryStage.show();
    }

    @Override
    public void pause() {
        PauseMenu pauseMenu = new PauseMenu();
        Group pauseMenuGroup = pauseMenu.getGroup();

        pauseMenu.continueButton.setOnAction(event -> {
            gameRoot.setCenter(null);
            controller.continueGame();
        });

        pauseMenu.quitButton.setOnAction(event -> {
            pauseMenu.quitButton.setDisable(true);
            controller.returnToMain();
        });
        gameRoot.setCenter(pauseMenuGroup);
    }

    /**
     * Käynnistää pelin. Käskee kontrolleria aloittamaan GameLoopin ja Levelin.
     * @param primaryStage Ohjelman Primary Stage.
     * @param player Pelaaja.
     * @param primary Pelaajan pääase.
     * @param secondary Pelaajan sivuase.
     */
    private void startGame(Stage primaryStage, Player player, Weapon primary, Weapon secondary) {
        player.addToPrimaryWeapon(primary);
        player.setSecondaryWeapon(secondary);
        uiPane = new Pane();
        ImageView uiIV = new ImageView();
        Image uiIMG = new Image("/images/PlayerUi.png");
        uiIV.setImage(uiIMG);
        uiPane.getChildren().add(uiIV);
        pane.setCenter(gameRoot);
        pane.setTop(uiPane);
        FadeTransition ft = new FadeTransition(Duration.millis(2000), gameRoot);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();

        score = new Label(String.valueOf(controller.getScore()));
        score.setTextFill(Color.WHITE);
        score.setLayoutX(210);
        score.setLayoutY(51);
        score.setFont(new Font("Cambria", 32));

        GameBackground gmg = new GameBackground(controller);

        uiPane.getChildren().add(score);
        uiPane.getChildren().add(playerHealth);
        uiPane.getChildren().add(bossHealth);

        //---------------- debugger
        if(debuggerToolsEnabled) {
            debugger_fps = new Label("fps");
            debugger_fps.setFont(new Font("Cambria", 32));
            debugger_fps.setLayoutX(1010);
            debugger_fps.setLayoutY(51);
            uiPane.getChildren().add(debugger_fps);


          /*  debugger_currentFps = new Label("currentFps");
            debugger_currentFps.setFont(new Font("Cambria", 16));
            debugger_currentFps.setLayoutX(540);
            uiPane.getChildren().add(debugger_currentFps);*/
        }

        // Aseiden lisäys komponentteihin, jotta aseet näkyvissä
        ArrayList<Component> components = new ArrayList<>();
        components.add((Component)primary);
        components.add((Component)secondary);

        Component b = new Component("circle", 10, 0, Color.RED, 0,0);
        components.add(b);

        /*
        Component c = new Component("rectangle", 10 , 0, Color.WHITE, 0,0);
        components.add(c);

        Component d = new Component("triangle", 6, 0, Color.BLUE, 0,0);
        components.add(d);

        */
        //player.equipComponents();


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

        controller.startLoop();
        System.out.println(playMenu.getSelectedLevel());
        controller.startLevel(playMenu.getSelectedLevel());
    }

    /**
     * Luo listan valittavissa olevista pääaseista
     * @param player Pelaaja
     * @return Lista, joka sisältää aseita
     */
    private ArrayList<Weapon> createPlayerPrimaries1(Player player) {
        ArrayList<Weapon> weapons = new ArrayList<>();

        Weapon blaster = new Blaster(controller, player, 0, -30, 0, Color.LIME,
                45, 100, 0);
        ((Component) blaster).setName("Blaster");

        Weapon rocketShotgun = new RocketShotgun(controller, player, 1, -30, 0, 3,
                20, false);
        ((Component) rocketShotgun).setName("Rocket Shotgun");

        Weapon laserGun = new LaserGun(controller, player, 0, -30, 5,
                80, 0, 0.5);
        ((Component) laserGun).setName("Laser Gun");

        weapons.add(blaster);
        weapons.add(rocketShotgun);
        weapons.add(laserGun);
        return weapons;
    }


    /**
     * Luo listan valittavissa olevista sivuaseista
     * @param player Pelaaja
     * @return Lista, joka sisältää aseita
     */
    private ArrayList<Weapon> createPlayerSecondaries(Player player) {
        ArrayList<Weapon> weapons = new ArrayList<>();

        Weapon rocketShotgun = new RocketShotgun(controller, player, 1, 10, 0, 3,
                20, false);
        ((Component) rocketShotgun).setName("Rocket Shotgun");

        Weapon laserGun = new LaserGun(controller, player, 0, 10, 50,
                80, 0, 0.5);
        ((Component) laserGun).setName("Laser Gun");

        weapons.add(rocketShotgun);
        weapons.add(laserGun);
        return weapons;
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

