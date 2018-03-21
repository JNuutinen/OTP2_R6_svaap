package model;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

import static view.GameMain.UNDEFINED_TAG;

/**
 * Perusluokka kaikille spriteille.
 */
public class Sprite extends Pane {

    /**
     * Spriten suunta (kulma).
     */
    private double direction;

    /**
     * Spriten nopeus.
     */
    private double velocity = 200;

    /**
     * Ympyrähitboxin halkaisija.
     */
    private double hitboxRadius = 0;

    /**
     * Spriten kuvan säiliö.
     */
    private ImageView imageView = new ImageView();

    /**
     * Kertoo onko spriten tarkoitus liikkua tällä hetkellä.
     */
    private boolean isMoving = false;

    /**
     * Spriten muoto.
     */
    private Shape shape;

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
    private int tag = UNDEFINED_TAG;

    /**
     * Toteuttaa Updateable-rajapinnan getHitboxRadius() metodin Unit-luokan kautta.
     * @return Spriten ymnpyrähitboxin säteen desimaalilukuna.
     */
    public double getHitboxRadius(){
        return hitboxRadius;
    }

    /**
     * Toteuttaa Updateable-rajapinnan getPosition() metodin Unit-luokan kautta.
     * @return Spriten nykyinen sijainti Point2D-oliona.
     */
    public Point2D getPosition(){
        return new Point2D(this.getLayoutX(), this.getLayoutY());
    }

    /**
     * Toteuttaa Updateable-rajapinnan getTag() metodin Unit-luokan kautta.
     * @return Spriten tunnistetagi.
     */
    public int getTag() {
        return tag;
    }

    /**
     * Toteuttaa Updateable-rajapinnan setTag() metodin Unit-luokan kautta.
     * @param tag Spriten tunnistetagi.
     */
    public void setTag(int tag) {
        this.tag = tag;
    }

    /**
     * Palauttaa Spriten suunnan (kulma asteina).
     * @return Spriten suunta.
     */
    public double getDirection(){ return direction;
    }

    /**
     * Asettaa Spriten kuvan niin että kuvalla ja spritellä on sama keskipiste
     * @param newImage Spritelle asetettava uusi kuva.
     * @param height kuvan korkeus
     * @param width kuvan levesys
     */
    public void setImage(Image newImage, double width, double height){
        imageView.setImage(newImage);
        imageView.setX(-width/2);
        imageView.setY(-height/2);
        this.getChildren().add(imageView);
    }

    /**
     * Asettaa isMoving parametring osoittamaan, että Sprite on/ei ole liikkeessä.
     * @param isMoving Liikkuuko Sprite.
     */
    protected void setIsMoving(boolean isMoving){
        this.isMoving = isMoving;
    }

    /**
     * Asettaa Spriten sijainnin.
     * @param newX Uusi x-koordinaatti.
     * @param newY Uusi y-koordinaatti.
     */
    public void setPosition(double newX, double newY){
        this.setLayoutX(newX);
        this.setLayoutY(newY);
    }

    /**
     * Palauttaa Spriten x-koordinaatin.
     * @return X-koordinaatti.
     */
    public double getXPosition(){
        return this.getLayoutX();
    }

    /**
     * Palauttaa Spriten y-koordinaatin.
     * @return Y-koordinaatti.
     */
    public double getYPosition(){
        return this.getLayoutY();
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
    public void setVelocity(double velocity){
        this.velocity = velocity;
    }


    double maxVelocity = 300.0;
    double xVelocity = 0;
    public double yVelocity = 0;
    double accelerationForce = 5000;
    double deltaTime;

    public void addVelocity(double directionX, double directionY, double deltaTime) {
        if (directionX == 0) ;
        else if (directionX * xVelocity >= 0) { // jos kiihdyttaa nopeuden suuntaan, eli lisaa vauhtia:
            if (xVelocity < maxVelocity && xVelocity > maxVelocity * -1) { //jos alle maksiminopeuden (sama vastakkaiseen suuntaan)
                xVelocity += directionX * deltaTime * accelerationForce;
            } else { // jos ylittaa maksiminopeuden
                xVelocity = maxVelocity * directionX;
            }
        } else { // jos kiihdyttaa nopeuden vastaiseen suuntaan, eli hidastaa
            xVelocity += directionX * deltaTime * accelerationForce;
        }
        //samat Y:lle
        if (directionY == 0) ;
        else if (directionY * yVelocity >= 0) { // jos kiihdyttaa nopeuden suuntaan, eli lisaa vauhtia:
            if (yVelocity < maxVelocity && yVelocity > maxVelocity * -1) { //jos alle maksiminopeuden (sama vastakkaiseen suuntaan)
                yVelocity += directionY * deltaTime * accelerationForce;
            } else { // jos ylittaa maksiminopeuden
                yVelocity = maxVelocity * directionY;
            }
        } else { // jos kiihdyttaa nopeuden vastaiseen suuntaan, eli hidastaa
            yVelocity += directionY * deltaTime * accelerationForce;
        }
    }

    double decelerateForce = 1000;

    public void decelerateY(double deltaTime){
        if(yVelocity > 0){
            if(yVelocity < decelerateForce * deltaTime){ // pysayta jos nopeus < seuraavan framen nopeus
                yVelocity = 0;
            }
            else {
                yVelocity -= decelerateForce * deltaTime;
            }
        }
        else {//xVelociy < 0
            if (yVelocity * -1 < decelerateForce * deltaTime) {
                yVelocity = 0;
            } else {
                yVelocity += decelerateForce * deltaTime;
            }
        }
    }

    /**
     * Asettaa Spriten ympyränmuotoisen hitboxin.
     * @param circleHitboxDiameter Hitboxin halkaisija.
     */


    protected void setHitbox(double circleHitboxDiameter){
        hitboxRadius = circleHitboxDiameter/2;
        shape = new Circle(0, 0, circleHitboxDiameter/2);
        shape.setFill(Color.TRANSPARENT);
        if(debuggerToolsEnabled) {
            //shape.setStroke(Color.LIGHTGREY);
        }
        shape.setStrokeWidth(0.4);
        this.getChildren().add(shape);
    }

    /**
     * Asettaa Spriten koon.
     * @param newSize Spriten uusi koko Point2D-oliona.
     */
    public void setSize(Point2D newSize){//TODO ei kaytos atm
        this.resize(newSize.getX(), newSize.getY());
    }

    /**
     * Muuttaa astekulman vektoriksi.
     * @param degrees Kulma, joka pitää muuntaa.
     * @return Kulma vektorina esitettynä (Point2D).
     */
    public Point2D degreesToVector(double degrees){
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
        return Math.sqrt(Math.pow(target.getX() - this.getXPosition(), 2) + Math.pow(target.getY() - this.getYPosition(), 2));
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
            this.setPosition(currentPosition.getX(),
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
                this.setPosition(currentPosition.getX() + (directionInVector.getX() * velocity * deltaTime),
                        currentPosition.getY() + (directionInVector.getY() * velocity * deltaTime));
            }
            else{
                Point2D directionInVector = degreesToVector(direction);
                Point2D currentPosition = getPosition();
                this.setPosition(currentPosition.getX() + (directionInVector.getX() * velocity * deltaTime),
                        currentPosition.getY() + (directionInVector.getY() * velocity * deltaTime));
            }
        }
    }

    /**
     * Kääntää Spriteä.
     * @param degrees Kulma asteina, jonka verran spriteä käännetään.
     */
    protected void rotate(double degrees){
        this.getTransforms().add(new Rotate(degrees * -1, Rotate.Z_AXIS));
        this.direction += degrees;
        while(direction >= 180.0){
            direction -= 360.0;
        }
        while(direction < -180){
            direction += 360.0;
        }
    }



    public void move(double deltaTime) {
        //System.out.println("asd");
        setPosition(getXPosition() + xVelocity * deltaTime, getYPosition() + yVelocity * deltaTime);
    }
}
