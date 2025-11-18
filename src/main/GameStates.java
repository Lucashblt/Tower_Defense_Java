package main;

public enum GameStates {
    
    PLAYING,
    MENU,
    SIMULATION_PERFORMANCE,
    GAME_OVER,
    GAME_WIN;

    public static GameStates gameState = MENU;

    public static void setGameState(GameStates state) {
        gameState = state;
    }
}
