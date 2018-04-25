package Multiplayer;

public class ShootData extends Data {
    public ShootData(int id) {
        this.playerId = id;
    }
    public void action() {
        Multiplayer.getPlayerById(playerId).shootPrimary();
    }
}
