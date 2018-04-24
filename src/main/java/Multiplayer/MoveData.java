package Multiplayer;

import model.Unit;

public class MoveData extends Data {
    public MoveData(Unit unit) {
        this.unit = unit;
    }

    public void action () {
        unit.shootPrimary();
    }
}
