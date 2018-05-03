package view;

import controller.Controller;
import controller.GameController;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
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
import model.GameBackground;
import model.PlayerFactory;
import model.Sprite;
import model.units.Player;
import model.units.Unit;
import model.weapons.Weapon;
import view.menus.MenuSpace;
import view.menus.PauseMenu;
import view.menus.PlayMenu;

import java.util.*;

/**
 * Pelin View. JavaFX Application.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
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
     * Lista, joka sisältää tietyllä hetkellä painetut näppäimet.
     */
    public static List<String> input;

    /**
     * Pitää sisällään lokalisoidut tekstit.
     */
    private ResourceBundle messages;

    /**
     * Map jossa pelin eri lokaalit.
     */
    private Map<String, Locale> locales;

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
     * TODO
     */
    private PauseMenu pauseMenu;

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

    /**
     * Pelin tausta.
     */
    private GameBackground gameBg;

    // TODO jdoc
    private BorderPane uiRoot;

    public static Player player;

    /**
     * Käynnistää ohjelman. Kutsuu launch(args) metodia, joka käynnistää JavaFX:n.
     * @param args Komentoriviargumentit.
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        // alustetaan lokaalit ja asetetaan vakiolokaali
        initLocales();

        this.primaryStage = primaryStage;
        primaryStage.setTitle("svaap: SivuvieritysAvaruusAmmuntaPeli");
        primaryStage.setResizable(false);

        // Kontrolleri-singletonin (parametrillinen) alustaminen.
        controller = GameController.getInstance(this);



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
        gameRoot.getChildren().add((Pane) sprite);
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
    public void removeFromCollisionList(Unit unit){
        units.remove(unit);
    }

    @Override
    public void removeSprite(Sprite sprite){
        Platform.runLater(()->gameRoot.getChildren().remove(sprite));
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
        debugger_fps.setText(String.valueOf((int)fps));
    }

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
     * Alustaa ohjelman. Luo menut.
     * @param primaryStage Ohjelman Primary Stage.
     */
    private void setupGame(Stage primaryStage) {
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


        // Pane kaikille menuille
        MenuSpace menuSpace = new MenuSpace(this, messages, locales);

        // Asetetaan pausemenu joka haetaan MenuSpacesta
        pauseMenu = menuSpace.getPauseMenu();

        // Pane, se kaikkien isä (uiRoot, gameRoot)
        pane = new BorderPane();
        pane.setStyle("-fx-background-color: black");
        uiRoot = new BorderPane();
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


        // Show käyntiin
        primaryStage.show();
    }

    @Override
    public void pause() {

        pauseMenu.continueButton.setOnAction(event -> {
            gameRoot.setCenter(null);
            controller.continueGame();
        });

        pauseMenu.quitButton.setOnAction(event -> {
            pauseMenu.quitButton.setDisable(true);
            controller.returnToMain();
        });
        gameRoot.setCenter(pauseMenu);
    }

    @Override
    public void changeBackgroundScrollSpeed(double speed, double duration) {
        gameBg.changeBackgroundScrollSpeed(speed, duration);
    }

    /**
     * Käynnistää pelin. Käskee kontrolleria aloittamaan GameLoopin ja Levelin.
     * @param primary Pelaajan pääase.
     * @param secondary Pelaajan sivuase.
     */
    public void init(Weapon primary, Weapon secondary) {
        pane.getChildren().remove(uiRoot);


        uiPane = new Pane();
        ImageView uiIV = new ImageView();
        Image uiIMG = new Image("/images/PlayerUi_i18n.png");
        uiIV.setImage(uiIMG);
        uiPane.getChildren().add(uiIV);
        pane.setCenter(gameRoot);
        pane.setTop(uiPane);
        FadeTransition ft = new FadeTransition(Duration.millis(2000), gameRoot);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();

        Font uiFont = new Font("Cambria", 32);
        Label playerHpText = new Label(messages.getString("player_hp"));
        playerHpText.setTextFill(Color.WHITE);
        playerHpText.setLayoutX(20);
        playerHpText.setLayoutY(8);
        playerHpText.setFont(uiFont);

        Label scoreText = new Label(messages.getString("score"));
        scoreText.setTextFill(Color.WHITE);
        scoreText.setLayoutX(20);
        scoreText.setLayoutY(51);
        scoreText.setFont(uiFont);

        Label bossHpText = new Label(messages.getString("boss_hp"));
        bossHpText.setTextFill(Color.WHITE);
        bossHpText.setLayoutX(1085);
        bossHpText.setLayoutY(8);
        bossHpText.setFont(uiFont);

        Label fpsText = new Label(messages.getString("fps"));
        fpsText.setTextFill(Color.WHITE);
        fpsText.setLayoutX(1085);
        fpsText.setLayoutY(51);
        fpsText.setFont(uiFont);

        score = new Label(String.valueOf(controller.getScore()));
        score.setTextFill(Color.WHITE);
        score.setLayoutX(210);
        score.setLayoutY(51);
        score.setFont(uiFont);

        gameBg = new GameBackground();

        uiPane.getChildren().addAll(playerHpText, scoreText, score, playerHealth, bossHpText, bossHealth, fpsText);

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

        player = PlayerFactory.getPlayer(Color.BLUE, primary, secondary);

        //      ArrayList pitää sisällään kyseisellä hetkellä painettujen näppäinten event-koodit
        input = new ArrayList<>();

        //      Näppäintä painaessa, lisää se arraylistiin, ellei se jo ole siellä
        scene.setOnKeyPressed(keyEvent -> {
            String code = keyEvent.getCode().toString();
            if (!input.contains(code)) input.add(code);
        });

        //      Kun näppäintä ei enää paineta, poista se arraylististä
        scene.setOnKeyReleased(keyEvent -> {
            String code = keyEvent.getCode().toString();
            input.remove(code);
        });
    }

    public void startGame() {
        controller.getGameLoop().setPlayers(controller.getPlayers());
        for(Player p : controller.getPlayers()) {
            controller.addHitboxObject(p);
        }

        primaryStage.setScene(scene);

        controller.startLoop();
        controller.startLevel(PlayMenu.getSelectedLevel());
    }

    public BorderPane getUiRoot(){
        return uiRoot;
    }


    /**
     * Luo pelin valittavat lokaalit ja tallentaa ne locales Mappiin. Asettaa vakiolokaalin.
     */
    private void initLocales() {
        locales = new HashMap<>();
        locales.put("en_NZ", new Locale("en", "NZ"));
        locales.put("fi_FI", new Locale("fi", "FI"));
        locales.put("se_SE", new Locale("se", "SE"));
        messages = ResourceBundle.getBundle("MessagesBundle", locales.get("en_NZ"));
    }

}

