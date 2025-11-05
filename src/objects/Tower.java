package objects;

public class Tower {

    private int x, y, id, towerType;
    
    public Tower(int x, int y, int id, int towerType) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.towerType = towerType;
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
}
