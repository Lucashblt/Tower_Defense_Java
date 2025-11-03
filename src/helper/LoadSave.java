package helper;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class LoadSave {
    
    public static BufferedImage getSpriteAtlas() {
        BufferedImage img = null;

        InputStream is = LoadSave.class.getClassLoader().getResourceAsStream("spriteatlas2.png");

		// If not found on the classpath, try loading from a common 'res' folder on the filesystem (useful for simple setups)
		if (is == null) {
			try {
				is = new FileInputStream("res/spriteatlas2.png");
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

        return img; 
    }

}
