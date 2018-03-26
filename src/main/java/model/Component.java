package model;

import javafx.geometry.Point2D;
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

    /** Komponentin poikkeama aluksesta. x=eteenpäin, y=vasempaan päin,
     * verrattuna aluksen suuntaan. */
    private Point2D offset;


    /** Komponentista lähtevän ammuksen aloituspaikan poikkeama aluksesta. x=eteenpäin, y=vasempaan päin,
     * verrattuna aluksen suuntaan. */
    private Point2D projectileOffset = new Point2D(0, 0);


    /** Komponentin nimi. */
    private String name = SPRITE_NAME_UNDEFINED;

    /** Konstruktori ilman projectilen offsettejä.
     * @param shape Komponentin kuvio merkkijonona: "triangle", "rectangle", ja "circle".
     * @param size Komponentin koko.
     * @param orientation Komponentin orientaatio.
     * @param color Komponentin väri.
     * @param offset Komponentin poikkeama aluksesta. x = etusuuntaan, y = vasempaan suuntaan.
     */
    public Component(String shape, int size, int orientation, Color color, Point2D offset) {
        this.offset = offset;

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
     * @param offset TODO.
     * @param projectileOffset TODO
     */
    public Component(String shape, int size, int orientation, Color color, Point2D offset,
                     Point2D projectileOffset) {
        this(shape, size, orientation, color, offset);
        this.projectileOffset = projectileOffset;
    }



    /** Palauttaa komponentin nimen.
     * @return Komponentin nimi. */
    public String getName() {
        return name;
    }

    /** Asettaa komponentin nimen.
     * @param name Komponentin nimi. */
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
    public Point2D getOffset(){
        return offset;
    }


    /**
     * Palauttaa komponentista lähtevän ammuksen aloituspaikan poikkeama aluksen etusuuntaan
     * @return ammuksen aloituspaikan poikkeama aluksen etusuuntaan
     */
    public Point2D getProjectileOffset() { return projectileOffset;}


}
