package helper;

public class Utilz {
   
    public static int getHypoDistance(int a, int b, int x, int y) {
        int xDiff = Math.abs(a - x);
        int yDiff = Math.abs(b - y);

        return (int) Math.hypot(xDiff, yDiff);
    }
}
