package main;

import javax.swing.JFrame;

import inputs.MyMouseListener;
import scenes.Menu;
import scenes.Playing;
import scenes.Settings;
import inputs.KeyboardListener;

public class Game extends JFrame implements Runnable {

	private GameScreen gameScreen;
	private Thread gameThread;

	private final double FPS_SET = 120.0;
	private final double UPS_SET = 60.0;

	private MyMouseListener myMouseListener;
	private KeyboardListener keyboardListener;
	
	//Classes
    private Render render;
	private Menu menu;
	private Settings settings;
	private Playing playing;

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
		gameScreen = new GameScreen(this);
		render = new Render(this);
		menu = new Menu(this);
		settings = new Settings(this);
		playing = new Playing(this);
	}

	private void initInputs() {
		myMouseListener = new MyMouseListener();
		keyboardListener = new KeyboardListener();

		this.addMouseListener(myMouseListener);
		this.addMouseMotionListener(myMouseListener);
		this.addKeyListener(keyboardListener);

		requestFocus();
	}

	private void start() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	private void updateGame() {
		// Game logic updates would go here
	}


	public static void main(String[] args) {
		Game game = new Game();
		game.initInputs();
		game.start();
	}

	@Override
	public void run() {
		double timePerFrame = 1000000000 / FPS_SET;
		double timePerUpdate = 1000000000 / UPS_SET;

		long lastFrame = System.nanoTime();
		long lastUpdate = System.nanoTime();
		long lastTimeCheck	= System.currentTimeMillis();

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
				frames = 0;
				updates = 0;
				lastTimeCheck = System.currentTimeMillis();	
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

	public Settings getSettings() {
		return settings;
	}
}
