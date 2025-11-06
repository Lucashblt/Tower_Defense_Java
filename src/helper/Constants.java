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

        public static float GetSpeed(int enemyType) {
			switch (enemyType) {
			case ORC:
				return 0.5f;
			case BAT:
				return 0.7f;
			case KNIGHT:
				return 0.45f;
			case WOLF:
				return 0.85f;
			}
			return 0;
		}

        public static int GetStartHealth(int enemyType) {
            switch (enemyType) {
                case ORC:
                    return 85;
                case BAT:
                    return 100;
                case KNIGHT:
                    return 400;
                case WOLF:
                    return 125;
            }
            return 0;
        }
    }

    public static class Towers {
        public static final int CANON_TOWER = 0;
        public static final int ARCHER_TOWER = 1;
        public static final int WIZARD_TOWER = 2;

        public static String getName(int towerType) {
            switch (towerType) {
                case CANON_TOWER:
                    return "Canon Tower";
                case ARCHER_TOWER:
                    return "Archer Tower";
                case WIZARD_TOWER:
                    return "Wizard Tower";
                default:
                    return "Unknown Tower";
            }
        }

        public static int getStartDamage(int towerType) {
            switch (towerType) {
                case CANON_TOWER:
                    return 15;
                case ARCHER_TOWER:
                    return 5;
                case WIZARD_TOWER:
                    return 0;
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
    }

    public static class Projectiles{
        public static final int ARROW = 0;
        public static final int BOMB = 1;
        public static final int CHAINS = 2;

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
