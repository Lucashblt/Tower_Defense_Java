package main;

public enum GameStates {
    
    PLAYING,
    MENU,
    SIMULATION_PERFORMANCE,
    GAME_OVER;

    public static GameStates gameState = MENU;

    public static void setGameState(GameStates state) {
        gameState = state;
    }
}
