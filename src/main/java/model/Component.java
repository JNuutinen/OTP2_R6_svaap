package model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

import static view.GameMain.SPRITE_NAME_UNDEFINED;

/**
 * Komponenttien luokka. Komponentit voivat olla aseita tai passiivisia komponentteja,
 * jotka näkyvät aluksessa, johon ne ovat lisätty.
 */
public class Component extends Sprite {

    /**
     * Komponentin x-offset Spritesta, johon se kiinnitetään.
     */
    private double xOffset;

    /**
     * Komponentin y-offset Spritesta, johon se kiinnitetään.
     */
    private double yOffset;

    /**
     * Komponentista lähtevän ammuksen aloituspaikan poikkeama aluksen etusuuntaan
     */
    private double projectileFrontOffset = 0;

    /**
     * Komponentista lähtevän ammuksen aloituspaikan poikkeama aluksen vasempaan suuntaan
     */
    private double projectileLeftOffset = 0;

    /**
     * Komponentin nimi.
     */
    private String name = SPRITE_NAME_UNDEFINED;

    /**
     *
     * @param shape parametreina "triangle", "rectangle" ja "circle"
     * @param size
     * @param orientation
     * @param color
     * @param xOffset
     * @param yOffset
     */
    /**
     * Konstruktori ilman projectilen offsettejä.
     * @param shape Komponentin kuvio merkkijonona: "triangle", "rectangle", ja "circle".
     * @param size Komponentin koko.
     * @param orientation Komponentin orientaatio.
     * @param color Komponentin väri.
     * @param xOffset Komponentin x-offset.
     * @param yOffset Komponentin y-offset.
     */
    public Component(String shape, int size, int orientation, Color color, double xOffset, double yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;

        if (shape.equals("triangle")) {
            setShape(triangle(size, orientation, color));
        } else if (shape.equals("rectangle")) {
            setShape(rectangle(size, orientation, color));
        } else if (shape.equals("circle")) {
            setShape(circle(size, color));
        }
    }

    /**
     * Konstruktori projectilen offseteillä.
     * @param shape Komponentin kuvio merkkijonona: "triangle", "rectangle", ja "circle".
     * @param size Komponentin koko.
     * @param orientation Komponentin orientaatio.
     * @param color Komponentin väri.
     * @param xOffset Komponentin x-offset.
     * @param yOffset Komponentin y-offset.
     * @param projectileFrontOffset ammuksen aloituspaikan poikkeama aluksen etusuuntaan
     * @param projectileLeftOffset ammuksen aloituspaikan poikkeama aluksen vasempaan suuntaan
     */
    public Component(String shape, int size, int orientation, Color color, double xOffset, double yOffset,
                     double projectileFrontOffset, double projectileLeftOffset) {
        this(shape, size, orientation, color, xOffset, yOffset);
        this.projectileFrontOffset = projectileFrontOffset;
        this.projectileLeftOffset = projectileLeftOffset;
    }

    /**
     * Palauttaa komponentin nimen.
     * @return Komponentin nimi.
     */
    public String getName() {
        return name;
    }

    /**
     * Asettaa komponentin nimen.
     * @param name Komponentin nimi.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Luo komponentista kolmion muotoisen.
     * @param size Komponentin koko.
     * @param orientation Komponentin orientaatio.
     * @param color Komponentin väri.
     * @return Tehty Shape kolmio.
     */
    private Shape triangle(int size, int orientation, Color color) {
        double tip = 6 * size * 1.3;
        double point = 3 * size * 1.3;
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(
                tip/2 * -1, point / 2,
                tip/2, 0.0,
                tip/2 * -1, point / 2 * -1);
        triangle.setFill(Color.BLACK);
        triangle.setStroke(color);
        triangle.setStrokeWidth(4.0);
        triangle.getTransforms().add(new Rotate(90 * orientation, 50, 30));
        return triangle;
    }

    /**
     * Luo komponentista neliön muotoisen.
     * @param size Komponentin koko.
     * @param orientation Komponentin orientaatio.
     * @param color Komponentin väri.
     * @return Tehty Shape neliö.
     */
    private Shape rectangle(int size, int orientation, Color color) {
        double x = 10 * size;
        double y = 6 * size;
        Rectangle rectangle = new Rectangle(x * 0.5, y * 0.5);
        rectangle.setFill(Color.BLACK);
        rectangle.setStroke(color);
        rectangle.setStrokeWidth(4.0);
        rectangle.setX(x * -0.25);
        rectangle.setY(y * -0.25);
        //rectangle.getTransforms().add(new Rotate(90 * orientation, x, y));
        return rectangle;
    }

    /**
     * Luo komponentista ympyrän muotoisen.
     * @param size Komponentin koko.
     * @param color Komponentin väri.
     * @return Tehty Shape ympyrä.
     */
    private Shape circle(int size, Color color) {
        Circle circle = new Circle(3 * size);
        circle.setFill(Color.BLACK);
        circle.setStroke(color);
        circle.setStrokeWidth(4.0);
        return circle;
    }

    /**
     * Palauttaa komponentin x-offsetin.
     * @return Komponentin x-offset.
     */
    public double getxOffset(){
        return xOffset;
    }

    /**
     * Palauttaa komponentin y-offsetin.
     * @return Komponentin y-offset.
     */
    public double getyOffset(){
        return yOffset;
    }

    /**
     * Palauttaa komponentista lähtevän ammuksen aloituspaikan poikkeama aluksen etusuuntaan
     * @return ammuksen aloituspaikan poikkeama aluksen etusuuntaan
     */
    public double getProjectileFrontOffset() { return projectileFrontOffset;}

    /**
     * Palauttaa komponentista lähtevän ammuksen aloituspaikan poikkeama aluksen vasempaan suuntaan
     * @return ammuksen aloituspaikan poikkeama aluksen vasempaan suuntaan
     */
    public double getProjectileLeftOffset() { return projectileLeftOffset;}

}
