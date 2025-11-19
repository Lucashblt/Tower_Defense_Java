# Simulation de Performance - Tower Defense

## Description

Cette fonctionnalité permet de tester les performances du jeu Tower Defense en mode automatique pendant 5 minutes.

## Caractéristiques

### Placement automatique des tours

Au démarrage de la simulation, des tours sont automatiquement placées sur toutes les cases d'herbe (GRASS) selon les règles suivantes :

1. **Tour Canon** : Placée sur une case d'herbe adjacente à la route
2. **Tour Wizard** : Placée sur une case d'herbe adjacente à un canon
3. **Tour Archer** : Placée sur toutes les autres cases d'herbe

Toutes les tours sont automatiquement upgradées au **tier 3 (niveau maximal)**.

**Total : 112 tours** sont placées automatiquement.

### Génération d'ennemis

- **10 ennemis sont générés à chaque update** (60 updates/seconde)
- Taux de génération : **600 ennemis par seconde**
- Les types d'ennemis sont choisis aléatoirement parmi : ORC, BAT, KNIGHT, WOLF
- La simulation se poursuit pendant **5 minutes**
- **Objectif** : Atteindre 25 000+ ennemis vivants simultanément au bout de 4 minutes
- **Pas de limitation de vies** : les ennemis qui atteignent la fin n'affectent pas la simulation
- **Pas de buffs** : Tous les ennemis ont leurs statistiques de base (pas de bonus de vitesse ou de vie)

### Fichier de sortie des performances

Un fichier texte est créé automatiquement au démarrage de la simulation :
- Nom : `performance_simulation_YYYYMMDD_HHmmss.txt`
- Format : `Temps(min:sec:ms), Total Ennemis, FPS, UPS, Ennemis actifs, Tours actives, Projectiles actifs, Mémoire utilisée (MB)`
- Contenu enregistré **chaque seconde** avec :
  - Temps écoulé (format MM:SS:mmm)
  - Total d'ennemis générés
  - FPS (Frames Per Second)
  - UPS (Updates Per Second)
  - Nombre d'ennemis vivants
  - Nombre de tours actives (112)
  - Nombre de projectiles actifs
  - Mémoire utilisée (en MB)

### Interface utilisateur

La barre d'interface en bas de l'écran affiche :
- **Bouton MENU** : Retour au menu principal (arrête la simulation)
- **Bouton PAUSE** : Met en pause / reprend la simulation
- **Timer** : Temps écoulé / Temps total (format MM:SS / 05:00)
- **Total spawnés** : Nombre total d'ennemis générés
- **Compteur d'ennemis** : Nombre d'ennemis vivants / Total généré

Note : Les fonctionnalités de clic sur les tours pour les vendre ou les améliorer sont désactivées (les tours sont déjà au niveau max).

## Optimisations techniques

Pour éviter les `ConcurrentModificationException` lors du parcours des listes pendant leur modification :
- Les listes d'explosions et d'ennemis sont copiées avant itération dans les méthodes de dessin et de mise à jour
- Permet une exécution stable même avec 25 000+ entités simultanées

## Utilisation

1. Lancer le jeu
2. Dans le menu principal, cliquer sur **"SIMULATION PERF"**
3. La simulation démarre automatiquement
4. Observer les performances pendant 5 minutes
5. À la fin, consulter le fichier de performance généré dans le répertoire du projet

## Objectif

Cette simulation permet de comparer les performances du modèle de programmation orientée objet (POO) actuel avec d'autres modèles de programmation alternatifs en conditions de stress (25 000+ entités).

