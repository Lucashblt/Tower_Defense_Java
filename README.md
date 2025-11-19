# Tower Defense - Java (POO)

Un jeu Tower Defense développé en Java utilisant le paradigme de Programmation Orientée Objet (POO).

## 🎮 Description du Projet

Ce projet est un jeu de tower defense classique où le joueur doit défendre sa base contre des vagues d'ennemis en plaçant stratégiquement des tours défensives. Le jeu est construit avec Java Swing et utilise une architecture orientée objet.

### Fonctionnalités principales

- **3 types de tours** : Canon, Archer, Wizard
- **4 types d'ennemis** : Orc, Bat, Knight, Wolf
- **Système d'upgrade** : 3 niveaux pour chaque tour
- **Gestion des vagues** : Difficulté progressive avec buffs d'ennemis
- **Système économique** : Achat, vente et amélioration de tours
- **Interface graphique** : Menu, gameplay, écrans de victoire/défaite

## 📊 Simulation de Performance

### Objectif

Ce projet inclut un **mode simulation** permettant de tester les performances du modèle POO Java dans des conditions de stress extrême. L'objectif est de comparer ces performances avec une implémentation équivalente en **Rust utilisant le modèle ECS** (Entity Component System).

### Caractéristiques de la simulation

- **Durée** : 5 minutes de test automatique
- **Placement automatique** : 112 tours au tier 3 maximal
- **Génération massive** : 600 ennemis/seconde (10 par update)
- **Objectif** : 25 000+ entités vivantes simultanées au bout de 4 minutes
- **Métriques enregistrées** : FPS, UPS, mémoire, nombre d'entités

### Lancer la simulation

1. Compiler et exécuter le projet
2. Dans le menu principal, cliquer sur **"SIMULATION PERF"**
3. La simulation démarre automatiquement
4. Consulter le fichier `performance_simulation_YYYYMMDD_HHmmss.txt` généré

Pour plus de détails, voir [SIMULATION_PERFORMANCE.md](SIMULATION_PERFORMANCE.md)

## 🔬 Comparaison POO vs ECS

### Modèle POO (Java) - Ce projet

**Architecture** :
- Héritage : `Enemy` (classe abstraite) → `Orc`, `Bat`, `Knight`, `Wolf`
- Managers : `EnemyManager`, `TowerManager`, `ProjectileManager`
- Pattern State : `GameStates` pour les scènes de jeu
- Couplage fort entre objets

**Avantages** :
- Code intuitif et facile à comprendre
- Encapsulation claire des comportements
- Bon pour des jeux de taille moyenne

**Limites observées** :
- Performance dégradée avec 20 000+ entités
- Overhead de la virtualisation et des références
- Garbage Collection impactant le framerate

### Modèle ECS (Rust) - Projet comparatif

**Architecture** :
- **Entities** : Simples identifiants (IDs)
- **Components** : Données pures (Position, Health, Speed, Damage, etc.)
- **Systems** : Logique pure traitant les components en batch
- Découplage total entre données et comportements

**Avantages attendus** :
- Cache-friendly : données contigües en mémoire
- Parallélisation native avec Bevy ECS
- Pas de GC : gestion mémoire déterministe
- Performance linéaire même avec 50 000+ entités

## 🏗️ Structure du Projet

```
src/
├── main/           # Boucle de jeu, rendu, états
├── scenes/         # Menu, Playing, GameOver, SimulationPerformance
├── managers/       # EnemyManager, TowerManager, ProjectileManager, WaveManager
├── enemies/        # Classes Enemy (Orc, Bat, Knight, Wolf)
├── objects/        # Tower, Projectile, Tile, PathPoint
├── ui/             # Boutons, barres d'action, interface
├── inputs/         # Gestion clavier/souris
├── helper/         # Constants, LoadSave, LevelBuild, Utilz
└── events/         # Wave

bin/                # Classes compilées
res/                # Sprites, textures
```

## 🛠️ Technologies

- **Langage** : Java 21
- **GUI** : Swing
- **Build** : Eclipse IDE / javac
- **Architecture** : POO avec patterns (Manager, State, Observer)

## 🚀 Exécution

### Prérequis
- JDK 21 ou supérieur

### Compilation et lancement
```bash
# Compiler
javac -d bin src/**/*.java

# Lancer le jeu
java -cp bin main.Game

# Ou via Eclipse/IntelliJ : Run main.Game
```

## 📈 Résultats de Performance

Les résultats de comparaison entre Java POO et Rust ECS seront documentés après les tests sur machines identiques.

**Métriques comparées** :
- FPS moyen et minimum sur 5 minutes
- Temps de réponse aux 25 000 entités
- Utilisation mémoire (heap vs allocation)
- Stabilité du framerate

## 📝 Licence

Projet éducatif - UQAC - 8INF957-11 Programmation objet avancée

## 👥 Auteurs

Lucas HUBLART - Comparaison des paradigmes de programmation pour le développement de jeux
