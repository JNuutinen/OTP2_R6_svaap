package Multiplayer;

public class ShootData extends Data {
    public ShootData(int id) {
        this.PlayerId = id;
    }
    public void action() {
        System.out.println(Multiplayer.getPlayerById(PlayerId));
        //Multiplayer.getPlayerById(PlayerId).shootPrimary();
        //unit.shootPrimary();
    }
}
