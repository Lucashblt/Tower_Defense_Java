package helper;

import objects.PathPoint;

public class LevelBuild {
 
    
    public static int[][] getLevelData() {

        /*
         * Legend:
         * 0  - GRASS
         * 1  - WATER
         * 2  - ROAD_LR
         * 3  - ROAD_TB
         * 4  - ROAD_B_TO_R
         * 5  - ROAD_L_TO_B
         * 6  - ROAD_L_TO_T
         * 7  - ROAD_T_TO_R
         * 8  - BL_WATER_CORNER
         * 9  - TL_WATER_CORNER
         * 10 - TR_WATER_CORNER
         * 11 - BR_WATER_CORNER
         * 12 - T_WATER (beach top)
         * 13 - R_WATER (beach right)
         * 14 - B_WATER (beach bottom)
         * 15 - L_WATER (beach left)
         * 16 - TL_ISLE
         * 17 - TR_ISLE
         * 18 - BR_ISLE
         * 19 - BL_ISLE
         * 20 - START_PATH
         * 21 - END_PATH
         */

        int[][] lvl = {
            
            { 0, 0, 0, 0, 15, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
            { 0, 0, 0, 0, 15, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
            { 0, 0, 0, 0, 15, 1, 1, 1, 18, 14, 14, 14, 19, 1, 1, 1, 1, 18, 14, 14 },
            { 0, 0, 0, 0, 15, 1, 1, 1, 13, 0, 0, 0, 15, 1, 1, 1, 1, 13, 4, 21},
            { 0, 0, 0, 0, 15, 1, 1, 1, 13, 0, 0, 0, 15, 1, 1, 1, 1, 13, 3, 0 },
            { 0, 0, 0, 0, 15, 1, 1, 1, 13, 0, 0, 0, 15, 1, 1, 1, 1, 13, 3, 0 },
            { 0, 0, 0, 0, 15, 1, 1, 1, 17, 12, 12, 12, 16, 1, 1, 1, 1, 13, 3, 0 },
            { 0, 0, 0, 0, 8, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 11, 3, 0 },
            { 0, 0, 4, 2, 2, 2, 2, 5, 0, 0, 0, 4, 2, 2, 2, 2, 2, 2, 6, 0 },
            { 0, 0, 3, 9, 12, 12,10, 3, 0, 0, 0, 3, 9, 12, 12, 12, 12, 10, 0, 0 },
            { 0, 0, 3, 15, 1, 1, 13, 3, 0, 0, 0, 3, 15, 1, 1, 1, 1, 13, 0, 9 },
            { 0, 0, 3, 15, 1, 1, 13, 3, 0, 0, 0, 3, 15, 1, 1, 18, 14, 11, 0, 15 },
            { 0, 0, 3, 15, 1, 1, 13, 7, 2, 2, 2, 6, 15, 1, 1, 13, 0, 0, 0, 15},
            { 0, 0, 3, 15, 1, 1, 17, 12, 12, 12, 12, 12, 16, 1, 1, 13, 0, 0, 9, 16},
            { 0, 0, 3, 15, 1, 1, 1, 18, 14, 14, 14, 19, 1, 1, 1, 13, 0, 0, 15, 1},
            { 0, 0, 3, 15, 1, 1, 1, 13, 0, 0, 0, 15, 1, 1, 1, 13, 0, 0, 15, 1},
            { 0, 0, 3, 15, 1, 1, 18, 11, 0, 0, 0, 8, 19, 1, 1, 13, 0, 0, 15, 1},
            { 0, 0, 3, 15, 1, 1, 13, 0, 0, 0, 0, 0, 15, 1, 1, 13, 0, 0, 15, 1},
            { 0, 0, 3, 15, 1, 1, 17, 12, 12, 12, 12, 12, 16, 1, 1, 13, 0, 0, 15, 1},
            { 20, 2, 6, 8, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 11, 0, 0, 8, 14}

        };

        return lvl;
    }

    public static PathPoint getStartPoint() {
        int[][] lvl = getLevelData();
        for (int y = 0; y < lvl.length; y++) {
            for (int x = 0; x < lvl[y].length; x++) {
                if (lvl[y][x] == 20) {
                    return new PathPoint(x, y);
                }
            }
        }
        return null;
    }

    public static PathPoint getEndPoint() {
        int[][] lvl = getLevelData();
        for (int y = 0; y < lvl.length; y++) {
            for (int x = 0; x < lvl[y].length; x++) {
                if (lvl[y][x] == 21) {
                    return new PathPoint(x, y);
                }
            }
        }
        return null;
    }
}