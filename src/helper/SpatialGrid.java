package helper;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import enemies.Enemy;

/**
 * QuadTree pour spatial partitioning des ennemis.
 * Principe POO : Structure de données spécialisée pour optimiser les requêtes spatiales.
 * Réduit la complexité de O(n²) à O(n log n) pour la détection de proximité.
 */
public class SpatialGrid {
    
    private static final int MAX_OBJECTS = 10;
    private static final int MAX_LEVELS = 5;
    
    private int level;
    private List<Enemy> objects;
    private Rectangle bounds;
    private SpatialGrid[] nodes;
    
    public SpatialGrid(int level, Rectangle bounds) {
        this.level = level;
        this.bounds = bounds;
        this.objects = new ArrayList<>();
        this.nodes = new SpatialGrid[4];
    }
    
    public void clear() {
        objects.clear();
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                nodes[i].clear();
                nodes[i] = null;
            }
        }
    }
    
    private void split() {
        int subWidth = bounds.width / 2;
        int subHeight = bounds.height / 2;
        int x = bounds.x;
        int y = bounds.y;
        
        nodes[0] = new SpatialGrid(level + 1, new Rectangle(x + subWidth, y, subWidth, subHeight));
        nodes[1] = new SpatialGrid(level + 1, new Rectangle(x, y, subWidth, subHeight));
        nodes[2] = new SpatialGrid(level + 1, new Rectangle(x, y + subHeight, subWidth, subHeight));
        nodes[3] = new SpatialGrid(level + 1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight));
    }
    
    private int getIndex(Enemy enemy) {
        int index = -1;
        double verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2);
        double horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2);
        
        boolean topQuadrant = (enemy.getY() < horizontalMidpoint && enemy.getY() + 32 < horizontalMidpoint);
        boolean bottomQuadrant = (enemy.getY() > horizontalMidpoint);
        
        if (enemy.getX() < verticalMidpoint && enemy.getX() + 32 < verticalMidpoint) {
            if (topQuadrant) {
                index = 1;
            } else if (bottomQuadrant) {
                index = 2;
            }
        } else if (enemy.getX() > verticalMidpoint) {
            if (topQuadrant) {
                index = 0;
            } else if (bottomQuadrant) {
                index = 3;
            }
        }
        
        return index;
    }
    
    public void insert(Enemy enemy) {
        if (nodes[0] != null) {
            int index = getIndex(enemy);
            if (index != -1) {
                nodes[index].insert(enemy);
                return;
            }
        }
        
        objects.add(enemy);
        
        if (objects.size() > MAX_OBJECTS && level < MAX_LEVELS) {
            if (nodes[0] == null) {
                split();
            }
            
            int i = 0;
            while (i < objects.size()) {
                int index = getIndex(objects.get(i));
                if (index != -1) {
                    nodes[index].insert(objects.remove(i));
                } else {
                    i++;
                }
            }
        }
    }
    
    /**
     * Récupère tous les ennemis dans un rayon donné autour d'un point
     */
    public List<Enemy> retrieve(List<Enemy> returnObjects, float x, float y, int range) {
        Rectangle searchArea = new Rectangle((int)(x - range), (int)(y - range), range * 2, range * 2);
        return retrieve(returnObjects, searchArea);
    }
    
    private List<Enemy> retrieve(List<Enemy> returnObjects, Rectangle rect) {
        if (nodes[0] != null) {
            for (int i = 0; i < nodes.length; i++) {
                if (nodes[i].bounds.intersects(rect)) {
                    nodes[i].retrieve(returnObjects, rect);
                }
            }
        }
        
        returnObjects.addAll(objects);
        return returnObjects;
    }
}
