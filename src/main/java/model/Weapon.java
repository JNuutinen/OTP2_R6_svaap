package model;

public class Weapon extends Component{
    int damage;
    int fireRate;

    public Weapon(int unitX, int unitY, double width, double height, String imagePath, int damage, int fireRate) {
        super(unitX, unitY, width, height, imagePath);
        spawn(unitX, unitY, width, height, imagePath);
    }
    public void shoot() {



    }
}
