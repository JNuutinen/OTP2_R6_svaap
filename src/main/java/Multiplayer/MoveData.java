package Multiplayer;

public class MoveData extends Data {
    int playerId;
    double x;
    double y;

    public MoveData(int playerId, double x, double y) {
        this.playerId = playerId;
        this.x = x;
        this.y = y;
    }

    public void action() {
        Multiplayer.getPlayerById(playerId).setPosition(x, y);
        System.out.println(Multiplayer.getPlayerById(playerId).getPosition());
    }
}
