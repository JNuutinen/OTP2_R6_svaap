package model;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * Perusluokka kaikille spriteille.
 */
public class Sprite {

    /**
     * GraphicsContext, johon Sprite piirtää itsensä.
     */
    private GraphicsContext gc;

    /**
     * Spriten sijainti x-akselilla.
     */
    private double xPosition;

    /**
     * Spriten sijainti y-akselilla.
     */
    private double yPosition;

    /**
     * Spriten suunta (kulma asteina).
     */
    private double direction;

    /**
     * Spriten nopeus.
     */
    private double velocity = 200;

    /**
     * Spriten kuva. Spritellä on joko Image, tai Polygon (ks. spritePolygon instanssimuuttuja).
     */
    private Image image;

    /**
     * Spriten Shape.
     */
    private Shape shape;

    /**
     * Kertoo onko spriten tarkoitus liikkua tällä hetkellä.
     */
    private boolean isMoving = false;

    /**
     * Debuggaustyökalujen toggle.
     */
    private boolean debuggerToolsEnabled = true;

    /**
     * Toggle lockedDirectionille.
     */
    private boolean movingDirectionLocked = false;

    /**
     * Voi laittaa spriten menee tiettyyn suuntaan vaikka sita kaantelisi samalla ks. lockDirection().
     */
    private double lockedDirection = 0;

    /**
     * Spriten tunnistetagi.
     */
    private String tag = "undefined";

    public Sprite(GraphicsContext gc) {
        this.gc = gc;
        shape = new Polygon();
    }

    /**
     * Toteuttaa Updateable-rajapinnan getPosition() metodin Unit-luokan kautta.
     * @return Spriten nykyinen sijainti Point2D-oliona.
     */
    public Point2D getPosition(){
        return new Point2D(xPosition, yPosition);
    }

    /**
     * Toteuttaa Updateable-rajapinnan getTag() metodin Unit-luokan kautta.
     * @return Spriten tunnistetagi.
     */
    public String getTag() {
        return tag;
    }

    /**
     * Toteuttaa Updateable-rajapinnan setTag() metodin Unit-luokan kautta.
     * @param tag Spriten tunnistetagi.
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * Palauttaa Spriten suunnan (kulma asteina).
     * @return Spriten suunta.
     */
    public double getDirection(){
        return direction;
    }

    /**
     * Asettaa Spriten kuvan.
     * @param newImage Spritelle asetettava uusi kuva.
     */
    public void setImage(Image newImage){
        image = newImage;
    }

    /**
     * Asettaa isMoving parametring osoittamaan, että Sprite on/ei ole liikkeessä.
     * @param isMoving Liikkuuko Sprite.
     */
    protected void setIsMoving(boolean isMoving){
        this.isMoving = isMoving;
    }

    public Shape getShape() {
        return shape;
    }

    /**
     * Asettaa spriten Shapen.
     * @param shape Shape, joka asetetaan Spriten Shapeksi.
     */
    public void setShape(Shape shape) {
        this.shape = shape;
    }

    /**
     * Asettaa Spriten sijainnin.
     * @param newX Uusi x-koordinaatti.
     * @param newY Uusi y-koordinaatti.
     */
    public void setPosition(double newX, double newY){
        xPosition = newX;
        yPosition = newY;
    }

    /**
     * Palauttaa Spriten x-koordinaatin.
     * @return X-koordinaatti.
     */
    protected double getXPosition(){
        return xPosition;
    }

    /**
     * Palauttaa Spriten y-koordinaatin.
     * @return Y-koordinaatti.
     */
    protected double getYPosition(){
        return yPosition;
    }

    /**
     * Palauttaa Spriten nopeuden.
     * @return Spriten nopeus.
     */
    protected double getVelocity(){
        return velocity;
    }

    /**
     * Asettaa Spriten nopeuden.
     * @param velocity Spriten uusi nopeus.
     */
    protected void setVelocity(double velocity){
        this.velocity = velocity;
    }

    /**
     * Palauttaa spriten Polygonin leveyden.
     * @return Spriten leveys.
     */
    public double getWidth() {
        return shape.getLayoutBounds().getWidth();
    }

    /**
     * Palauttaa spriten Polygonin korkeuden.
     * @return Spriten korkeus.
     */
    public double getHeight() {
        return shape.getLayoutBounds().getHeight();
    }

    /**
     * Asettaa Spriten ympyränmuotoisen hitboxin.
     * @param circleHitboxDiameter Hitboxin halkaisija.
     */
    protected void setHitbox(double circleHitboxDiameter){
        Shape hb = new Circle(0, 0, circleHitboxDiameter/2);
        hb.setFill(Color.TRANSPARENT);
        if(debuggerToolsEnabled) {
            hb.setStroke(Color.LIGHTGREY);
        }
        hb.setStrokeWidth(0.4);
        // TODO: circle polygoniksi
        //spritePolygon = (Polygon) Shape.union(spritePolygon, hb);
    }

    /**
     * Asettaa Spriten neliönmuotoisen hitboxin.
     * @param rectangleHitboxWidth Hitboxin leveys.
     * @param rectangleHitboxHeight Hitboxin korkeus.
     */
    public void setHitbox(double rectangleHitboxWidth, double rectangleHitboxHeight) {
        Shape hb = new Rectangle(rectangleHitboxWidth, rectangleHitboxHeight);
        hb.setFill(Color.TRANSPARENT);
        if(debuggerToolsEnabled) {
            hb.setStroke(Color.LIGHTGREY);
        }
        hb.setStrokeWidth(0.4);
        shape = Shape.union(shape, hb);
    }

    /**
     * Asettaa Spriten koon.
     * @param newSize Spriten uusi koko Point2D-oliona.
     */
    public void setSize(Point2D newSize){//TODO ei kaytos atm
        shape.resize(newSize.getX(), newSize.getY());
    }

    /**
     * Lisää Shapen spriten Shapeen.
     * @param shape Shape joka lisätään.
     */
    public void addShape(Shape shape) {
        shape = Shape.union(shape, shape);
    }


    public void drawSprite() {
        // Placeholderin piirto
        //gc.drawImage(new Image("images/projectile_ball_small_cyan.png"), xPosition, yPosition);
        gc.drawImage(new Image("images/player_ship_9000.png"), xPosition, yPosition);
    }

    private void drawPolygon(Polygon polygon) {
        gc.setFill(polygon.getFill());
        double[] xPoints = new double[polygon.getPoints().size() / 2];
        double[] yPoints = new double[polygon.getPoints().size() / 2];
        Double[] polygonPoints = polygon.getPoints().toArray(new Double[polygon.getPoints().size()]);
        // Hakee x koordinaatit spriten shapesta
        for (int i = 0; i < polygonPoints.length; i++) {
            if (i % 2 == 0) {
                yPoints[i] = polygonPoints[i];
            } else {
                xPoints[i] = polygonPoints[i];
            }
        }
        gc.fillPolygon(xPoints, yPoints, polygonPoints.length);
    }

    /**
     * Muuttaa astekulman vektoriksi.
     * @param degrees Kulma, joka pitää muuntaa.
     * @return Kulma vektorina esitettynä (Point2D).
     */
    private Point2D degreesToVector(double degrees){
        double sin = Math.sin(Math.toRadians(degrees) * -1);
        double cos = Math.cos(Math.toRadians(degrees));
        return new Point2D(cos, sin);
    }

    /**
     * Laskee kulman itsensa ja kohteen valilla yksikkoympyran mukaisesti (esim. jos kohde suoraan ylapuolella, kulma on 90)
     * @param target Kohteen sijainti Point2D oliona.
     * @return Kulma Spriten ja kohteen välillä.
     */
    protected double getAngleFromTarget(Point2D target){
        return Math.toDegrees(Math.atan2(getYPosition() - target.getY(), getXPosition() * -1 - target.getX() * -1));
    }

    /**
     * Laskee itsensa ja kohteen valisen etaisyyden.
     * @param target Kohteen sijainti Point2D oliona.
     * @return Etäisyys Spriten ja kohteen välillä.
     */
    public double getDistanceFromTarget(Point2D target){
        return Math.sqrt(Math.pow(target.getX() - getXPosition(), 2) + Math.pow(target.getY() - getYPosition(), 2));
    }

    /**
     * lukitsee suunnan niin etta jatkaa tiettyyn suuntaan menemista vaikka spritea kaantelisi samalla.
     * @param angle lukittu suunta.
     */
    void lockDirection(double angle){
        movingDirectionLocked = true;
        lockedDirection = angle;
    }

    /**
     * Liikuttaa bossia.
     * @param deltaTime Kulunut aika viime päivityksestä.
     */
    void moveBoss(double deltaTime){
        if (isMoving) {
            Point2D directionInVector = degreesToVector(direction);
            Point2D currentPosition = getPosition();
            setPosition(currentPosition.getX(),
                    currentPosition.getY() + (directionInVector.getY()));
        }
    }

    /**
     * Liikuttaa Spritea askeleen direction-suuntaan kerrottuuna velocity-muuttujalla. Käytetään GameLoopissa.
     * @param deltaTime Kulunut aika viime päivityksestä.
     */
    protected void moveStep(double deltaTime) {
        if (isMoving) {
            if(movingDirectionLocked) {
                Point2D directionInVector = degreesToVector(lockedDirection);
                Point2D currentPosition = getPosition();
                setPosition(currentPosition.getX() + (directionInVector.getX() * velocity * deltaTime),
                        currentPosition.getY() + (directionInVector.getY() * velocity * deltaTime));
            }
            else{
                Point2D directionInVector = degreesToVector(direction);
                Point2D currentPosition = getPosition();
                setPosition(currentPosition.getX() + (directionInVector.getX() * velocity * deltaTime),
                        currentPosition.getY() + (directionInVector.getY() * velocity * deltaTime));
            }

            // Piirretään Sprite, jos se on liikkunut
            drawSprite();
        }
    }

    /**
     * Kääntää Spriteä.
     * @param degrees Kulma asteina, jonka verran spriteä käännetään.
     */
    protected void rotate(double degrees){
        // Täs vanha tapa kääntää pane spritea
        //this.getTransforms().add(new Rotate(degrees * -1, Rotate.Z_AXIS));

        // Shapen kääntely
        shape.setRotate(degrees * -1);

        direction += degrees;
        while(direction >= 180.0){
            direction -= 360.0;
        }
        while(direction < -180){
            direction += 360.0;
        }
    }
}
