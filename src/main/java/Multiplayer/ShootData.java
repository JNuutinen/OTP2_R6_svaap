package Multiplayer;

public class ShootData extends Data {
    public ShootData(int id) {
        this.playerId = id;
    }
    public void action() {
        System.out.println(Multiplayer.getPlayerById(playerId));
        //Multiplayer.getPlayerById(PlayerId).shootPrimary();
        //unit.shootPrimary();
    }
}
