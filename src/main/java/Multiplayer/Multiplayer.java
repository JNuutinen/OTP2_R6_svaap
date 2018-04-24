package Multiplayer;

import model.Unit;

public class Multiplayer {
    public Multiplayer () {}

    public void move(Unit unit, int x, int y) {
        unit.setPosition(x, y);
    }

    public void shoot(Unit unit) {
        Data d = new ShootData(unit);
        Host.streamOut(d);
    }

}
