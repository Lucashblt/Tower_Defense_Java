package helper;

/**
 * Interface pour les objets réutilisables dans un pool.
 * Principe POO : Polymorphisme et contrat d'interface.
 */
public interface Poolable {
    /**
     * Réinitialise l'objet à son état initial pour réutilisation
     */
    void reset();
    
    /**
     * Indique si l'objet est actuellement actif
     */
    boolean isActive();
}
