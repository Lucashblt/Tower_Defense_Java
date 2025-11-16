package main;

public enum GameStates {
    
    PLAYING,
    MENU,
    SIMULATION_PERFORMANCE;

    public static GameStates gameState = MENU;

    public static void setGameState(GameStates state) {
        gameState = state;
    }
}
