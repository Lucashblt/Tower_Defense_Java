package main;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Game extends JFrame implements Runnable {

	private GameScreen gameScreen;
	private BufferedImage img;
	private Thread gameThread;

	private final double FPS_SET = 120.0;
	private final double UPS_SET = 60.0;

	public Game() {
		importImg();

		setSize(640, 640);		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		gameScreen = new GameScreen(img);
		add(gameScreen);
		setVisible(true);
	}

	private void start() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	private void updateGame() {
		// Game logic updates would go here
	}

	private void importImg() {
		InputStream is = getClass().getResourceAsStream("/spriteatlas.png");

		// If not found on the classpath, try loading from a common 'res' folder on the filesystem (useful for simple setups)
		if (is == null) {
			try {
				is = new FileInputStream("res/spriteatlas.png");
			} catch (FileNotFoundException e) {
			}
		}

		try {
			img = ImageIO.read(is);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { is.close(); } catch (Exception ignore) {}
		}
	}

	public static void main(String[] args) {
		Game game = new Game();
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

			//FPS
			if(System.nanoTime() - lastFrame >= timePerFrame) {
				repaint();
				lastFrame = System.nanoTime();
				frames++;
			}

			//UPS
			if (System.nanoTime() - lastUpdate >= timePerUpdate) {
				updateGame();
				lastUpdate = System.nanoTime();
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
}
