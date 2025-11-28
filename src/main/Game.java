package main;

import javax.swing.JFrame;

import managers.TileManager;
import scenes.Menu;
import scenes.Playing;
import scenes.SimulationPerformance;
import scenes.GameOver;
import scenes.GameWin;

public class Game extends JFrame implements Runnable {

	private GameScreen gameScreen;
	private Thread gameThread;

	private final double FPS_SET = 160.0;
	private final double UPS_SET = 60.0;
		
	//Classes
    private Render render;
	private Menu menu;
	private SimulationPerformance simulationPerformance;
	private GameOver gameOver;
	private GameWin gameWin;
	private Playing playing;
	private TileManager tileManager;
	
	// FPS et UPS actuels
	private int currentFPS = 0;
	private int currentUPS = 0;

	public Game() {	
		initClasses();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Tower Defense");
		setResizable(false);
		add(gameScreen);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void initClasses() {
		tileManager = new TileManager();
		gameScreen = new GameScreen(this);
		render = new Render(this);
		menu = new Menu(this);
		simulationPerformance = new SimulationPerformance(this);
		gameOver = new GameOver(this);
		gameWin = new GameWin(this);
		playing = new Playing(this);
	}

	

	private void start() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	private void updateGame() {
		switch (GameStates.gameState) {
			case MENU:
				break;
			case PLAYING:
				playing.update();
				break;
			case SIMULATION_PERFORMANCE:
				simulationPerformance.update();
				break;
			default:
				break;
		}
	}


	public static void main(String[] args) {
		Game game = new Game();
		game.gameScreen.initInputs();
		game.start();
	}

	@Override
	public void run() {
		double timePerFrame = 1000000000 / FPS_SET;
		double timePerUpdate = 1000000000 / UPS_SET;

		long lastFrame = System.nanoTime();
		long lastUpdate = System.nanoTime();
		long lastTimeCheck	= System.currentTimeMillis();
		long lastGCCheck = System.currentTimeMillis();

		int frames = 0;
		int updates = 0;

		while (true) {

			long now = System.nanoTime();
			//FPS
			if(now - lastFrame >= timePerFrame) {
				repaint();
				lastFrame = now;
				frames++;
			}

			//UPS
			if (now - lastUpdate >= timePerUpdate) {
				updateGame();
				lastUpdate = now;
				updates++;
			}

			if(System.currentTimeMillis() - lastTimeCheck >= 1000) {
				System.out.println("FPS : " + frames + " | UPS : " + updates);
				currentFPS = frames;
				currentUPS = updates;
				frames = 0;
				updates = 0;
				lastTimeCheck = System.currentTimeMillis();	
			}
			
			// GC plus agressif toutes les 5 secondes pour stabilité
			if(System.currentTimeMillis() - lastGCCheck >= 5000) {
				Runtime.getRuntime().gc();
				lastGCCheck = System.currentTimeMillis();
			}
			
			// Micro-pause pour éviter 100% CPU (améliore stabilité)
			try {
				Thread.sleep(0, 100); // 100 nanosecondes
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	// Getters and Setters
	public Render getRender() {
		return render;
	}

	public Playing getPlaying() {
		return playing;
	}

	public Menu getMenu() {
		return menu;
	}

	public SimulationPerformance getSimulationPerformance() {
		return simulationPerformance;
	}

	public GameOver getGameOver() {
		return gameOver;
	}

	public GameWin getGameWin() {
		return gameWin;
	}

	public TileManager getTileManager() {
		return tileManager;
	}
	
	public int getCurrentFPS() {
		return currentFPS;
	}
	
	public int getCurrentUPS() {
		return currentUPS;
	}
}
