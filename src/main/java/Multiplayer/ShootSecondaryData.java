package Multiplayer;

public class ShootSecondaryData extends Data {
    int playerId;
    public ShootSecondaryData(int playerId) {
        this.playerId = playerId;
    }
    public void action() {
        Multiplayer.getPlayerById(playerId).shootSecondary();
    }
}
