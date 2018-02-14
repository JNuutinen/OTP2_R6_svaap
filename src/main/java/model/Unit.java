package model;

import controller.Controller;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;

/**
 * Lisää spriteen avaruusalukselle ominaisia piirteitä
 */
public class Unit extends Sprite implements Updateable {
    /**
     * Kontrolleri
     */
    private Controller controller;
    /**
     * Yksikön hitpointsit
     */
    private int hp;

    /**
     * Yksikön taso
     */
    private int level;
    ArrayList<Component> components = new ArrayList<>();

    public Unit(Controller controller) {
        this.controller = controller;
        hp = 30;
    }

    /**
     * Ampuu yksikön pääaseella
     */
    public void shootPrimary() {

        System.out.println("pew pew");
    }

    /**
     * Ampuu yksikön toisella aseella
     */
    public void shootSecondary() {
        System.out.println("pew");
    }

    /**
     * Yksikkö ottaa vahinkoa
     *
     * @param damage Vahinkomäärä, jonka yksikkö ottaa
     */
    public void takeDamage(int damage) {
        hp -= damage;
        if(hp <= 0){
            controller.removeUpdateable(this);
            controller.removeFromCollisionList(this);
            if(this.getTag().equals("enemy")){
                controller.addScore(100);
            }
            if(this.getTag().equals("player")){
                controller.returnToMain();
            }
        }
        if(this.getTag().equals("player")){
            controller.addScore(-50);
        }
    }

    public void move(double direction) {
        this.setIsMoving(true);
        this.setDirection(direction);

        for (Component component : components) {
            component.setIsMoving(true);
            component.setDirection(direction);
        }
    }

    public void stopMoving() {
        this.setIsMoving(false);
        for (Component component : components) {
            component.setIsMoving(false);
        }
    }

    public ArrayList sortComponents(ArrayList<Component> components) {
        for (int i = 0; i < components.size(); i++) { //Lajitellaan komponentit suurimmasta pienimpään
            for (int n = 0; n < components.size(); n++) {
                if (components.get(i).getShape().getLayoutBounds().getHeight() * components.get(i).getShape().getLayoutBounds().getWidth()
                        > components.get(n).getShape().getLayoutBounds().getHeight() * components.get(n).getShape().getLayoutBounds().getWidth()) {
                    Component x = components.get(n);
                    components.set(n, components.get(i));
                    components.set(i, x);

                }
            }
        }
        return components;
    }
    public void equipComponents(ArrayList<Component> components) {
        sortComponents(components); //Lajittelee komponentit isoimmasta pienimpään
        for (Component component : components) { //Lista käy läpi kaikki komponentit ja asettaa kuvat päällekkäin
            Shape shape = component.getShape();
            shape.setLayoutY(0); //Näitä muokkaamalla voi vaihtaa mihin komponentti tulee
            shape.setLayoutX(0);
            this.getChildren().add(component.getShape());
            setTag(getTag());
        }
    }

    public void drawShip() {
        String tag = getTag();
        Polygon triangle = new Polygon(); //Tämä tekee kolmion mikä esittää vihollisen alusta
        triangle.getPoints().addAll(new Double[]{
                0.0, 0.0,
                120.0, 30.0,
                0.0, 60.0 });

        if (tag.equals("player")) {
            triangle.setFill(Color.BLACK);
            triangle.setStroke(Color.CYAN);
            triangle.setStrokeWidth(2.0);
            this.getChildren().add(triangle);

        } else if (tag.equals("enemy")) {
            triangle.setFill(Color.BLACK);
            triangle.setStroke(Color.RED);
            triangle.setStrokeWidth(2.0);
            triangle.getTransforms().add(new Rotate(180, 50, 30));
            this.getChildren().add(triangle);
        }
    }

    public int getHp(){
        return hp;
    }

    public void setHp(int hp){
        this.hp = hp;
    }

    public void addHP(int hp){
        this.hp += hp;
    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void collides(Updateable collidingUpdateable) {

    }

    @Override
    public Updateable getUpdateable() {
        return null;
    }

    public void destroyThis(){
        controller.removeUpdateable(this);
    }

    public void setHP(int hp){
        this.hp = hp;
    }
}
