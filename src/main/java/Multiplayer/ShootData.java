package Multiplayer;

import model.Unit;

public class ShootData extends Data {
    public ShootData(Unit unit) {
        this.unit = unit;
    }
    public void action() {
        unit.shootPrimary();
    }
}
