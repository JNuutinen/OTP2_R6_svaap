package Multiplayer;

public class ShootPrimaryData extends Data {
    public ShootPrimaryData(int id) {
        this.playerId = id;
    }
    public void action() {
        Multiplayer.getPlayerById(playerId).shootPrimary();
    }
}
