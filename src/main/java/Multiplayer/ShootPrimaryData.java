package Multiplayer;

public class ShootPrimaryData extends Data {
    int playerId;
    public ShootPrimaryData(int id) {
        this.playerId = id;
    }
    public void action() {
        Multiplayer.getPlayerById(playerId).shootPrimary();
    }
}
