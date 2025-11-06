package objects;

import static helper.Constants.Towers.*;

public class Tower {

    private int x, y, id, towerType;
    private int damage, range, cooldown;

    public Tower(int x, int y, int id, int towerType) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.towerType = towerType;
        setDefaultDamage();
        setDefaultRange();
        setCooldownTime();
    }

    private void setDefaultDamage() {
        this.damage = getStartDamage(towerType);
    }

    private void setDefaultRange() {
        this.range = getDefaultRange(towerType);
    }

    private void setCooldownTime() {
        this.cooldown = getCooldownTime(towerType);
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public int getTowerType() {
        return towerType;
    }

    public int getId() {
        return id;
    }

    public int getDamage() {
        return damage;
    }

    public int getRange() {
        return range;
    }

    public int getCooldown() {
        return cooldown;
    }
}
