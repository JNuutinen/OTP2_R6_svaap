package Multiplayer;

public class ConnectionData extends Data {
    int playerId;
    public ConnectionData(int playerId) {
        this.playerId = playerId;
    }

    public void action() {
        Multiplayer.addPlayer(playerId);
    }
}
