package helper;

public class Constants {
    

    public static class Directions {
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }

    public static class Tiles {
        public static final int WATER_TILE = 0;
        public static final int GRASS_TILE = 1;
        public static final int ROAD_TILE = 2;
        public static final int START_TILE = 3;
        public static final int END_TILE = 4;
    }

    public static class Enemies {
        public static final int ORC = 0;
        public static final int BAT = 1;    
        public static final int KNIGHT = 2;
        public static final int WOLF = 3;

        public static int GetEnemyReward(int enemyType) {
            switch (enemyType) {
                case ORC:
                    return 5;
                case BAT:
                    return 5;
                case KNIGHT:
                    return 17;
                case WOLF:
                    return 8;
            }
            return 0;
        }

        public static float GetSpeed(int enemyType) {
			return GetSpeed(enemyType, 0);
		}

		public static float GetSpeed(int enemyType, int waveNumber) {
			float baseSpeed = 0;
			switch (enemyType) {
			case ORC:
				baseSpeed = 0.5f;
				break;
			case BAT:
				baseSpeed = 0.7f;
				break;
			case KNIGHT:
				baseSpeed = 0.45f;
				break;
			case WOLF:
				baseSpeed = 0.85f;
				break;
			}
			// Buff speed by 10% every 5 waves
			int buffLevel = waveNumber / 5;
			float buffMultiplier = 1.0f + (buffLevel * 0.10f);
			return baseSpeed * buffMultiplier;
		}  
        public static int GetStartHealth(int enemyType) {
            return GetStartHealth(enemyType, 0);
        }

        public static int GetStartHealth(int enemyType, int waveNumber) {
            int baseHealth = 0;
            switch (enemyType) {
                case ORC:
                    baseHealth = 85;
                    break;
                case BAT:
                    baseHealth = 100;
                    break;
                case KNIGHT:
                    baseHealth = 400;
                    break;
                case WOLF:
                    baseHealth = 125;
                    break;
            }
            // Buff health by 15% every 5 waves
            int buffLevel = waveNumber / 5;
            float buffMultiplier = 1.0f + (buffLevel * 0.15f);
            return (int)(baseHealth * buffMultiplier);
        }
        
        public static int GetStartHealthSimulation(int enemyType, int buffLevel) {
            int baseHealth = 0;
            switch (enemyType) {
                case ORC:
                    baseHealth = 85;
                    break;
                case BAT:
                    baseHealth = 100;
                    break;
                case KNIGHT:
                    baseHealth = 400;
                    break;
                case WOLF:
                    baseHealth = 125;
                    break;
            }
            // Simulation mode: Buff health by 0.3% every 100 enemies
            float buffMultiplier = 1.0f + (buffLevel * 0.003f);
            return (int)(baseHealth * buffMultiplier);
        }
    }

    public static class Towers {
        public static final int CANON_TOWER = 0;
        public static final int ARCHER_TOWER = 1;
        public static final int WIZARD_TOWER = 2;

        public static String getName(int towerType) {
            switch (towerType) {
                case CANON_TOWER:
                    return "Canon";
                case ARCHER_TOWER:
                    return "Archer";
                case WIZARD_TOWER:
                    return "Wizard";
                default:
                    return "Unknown";
            }
        }

        public static int getStartDamage(int towerType) {
            switch (towerType) {
                case CANON_TOWER:
                    return 15;
                case ARCHER_TOWER:
                    return 5;
                case WIZARD_TOWER:
                    return 1;
                default:
                    return 0;
            }
        }

        public static int getDefaultRange(int towerType) {
            switch (towerType) {
                case CANON_TOWER:
                    return 75;
                case ARCHER_TOWER:
                    return 120;
                case WIZARD_TOWER:
                    return 100;
                default:
                    return 0;
            }
        }

        public static int getCooldownTime(int towerType) {
            switch (towerType) {
                case CANON_TOWER:
                    return 120;
                case ARCHER_TOWER:
                    return 35;
                case WIZARD_TOWER:
                    return 50;
                default:
                    return 0;
            }
        }    
        
        public static int getTowerCost(int towerType) {
            switch (towerType) {
                case CANON_TOWER:
                    return 65;
                case ARCHER_TOWER:
                    return 35;
                case WIZARD_TOWER:
                    return 50;
                default:
                    return 0;
            }
        }
    }

    public static class Projectiles{
        public static final int ARROW = 0;
        public static final int CHAINS = 1;
        public static final int BOMB = 2;

        public static float GetSpeed(int type) {
			switch (type) {
			case ARROW:
				return 8f;
			case BOMB:
				return 4f;
			case CHAINS:
				return 6f;
			}
			return 0f;
		}
    }
}
