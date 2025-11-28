package helper;

import java.util.ArrayList;
import java.util.function.Supplier;

/**
 * Pool générique d'objets réutilisables pour éviter les allocations constantes.
 * Principe POO : Encapsulation de la gestion du cycle de vie des objets.
 */
public class ObjectPool<T extends Poolable> {
    
    private ArrayList<T> available;
    private ArrayList<T> inUse;
    private Supplier<T> factory;
    private int maxSize;
    
    public ObjectPool(Supplier<T> factory, int initialSize, int maxSize) {
        this.factory = factory;
        this.maxSize = maxSize;
        this.available = new ArrayList<>(initialSize);
        this.inUse = new ArrayList<>(initialSize);
        
        // Pré-allocation pour éviter les créations pendant le jeu
        for (int i = 0; i < initialSize; i++) {
            available.add(factory.get());
        }
    }
    
    /**
     * Obtient un objet du pool (le crée si nécessaire et si sous la limite)
     */
    public T obtain() {
        T obj;
        if (available.isEmpty()) {
            if (inUse.size() < maxSize) {
                obj = factory.get();
            } else {
                // Pool saturé, réutiliser le plus ancien
                return null;
            }
        } else {
            obj = available.remove(available.size() - 1);
        }
        inUse.add(obj);
        return obj;
    }
    
    /**
     * Libère un objet pour le remettre dans le pool
     */
    public void free(T obj) {
        if (inUse.remove(obj)) {
            obj.reset();
            available.add(obj);
        }
    }
    
    /**
     * Libère tous les objets inactifs
     */
    public void freeInactive() {
        for (int i = inUse.size() - 1; i >= 0; i--) {
            T obj = inUse.get(i);
            if (!obj.isActive()) {
                inUse.remove(i);
                obj.reset();
                available.add(obj);
            }
        }
    }
    
    public ArrayList<T> getInUse() {
        return inUse;
    }
    
    public int getAvailableCount() {
        return available.size();
    }
    
    public int getInUseCount() {
        return inUse.size();
    }
    
    public void clear() {
        available.addAll(inUse);
        inUse.clear();
        for (T obj : available) {
            obj.reset();
        }
    }
}
