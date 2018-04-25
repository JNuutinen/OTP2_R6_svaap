package Multiplayer;

public class ConnectionData extends Data {

    public ConnectionData(int playerId) {
        this.playerId = playerId;
    }

    public void action() {
        Multiplayer.addPlayer(playerId);
    }
}
