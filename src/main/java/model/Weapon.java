package model;

public class Weapon {
    int damage;
    int fireRate;
    int direction = 1;
    Unit shooter;
 /*
    public Weapon(double unitX, double unitY, double width, double height, String imagePath, int damage, int fireRate) {
        super(unitX, unitY, width, height, imagePath);
        spawn(unitX, unitY, width, height, imagePath);
        this.damage = damage;
        this.fireRate = fireRate;
    }
    public void shoot() {
        Projectile projectile = new Projectile(damage, direction, shooter, "projectile_player");
    }
    */
}
