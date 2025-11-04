package helper;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ImgFix {
    

    public static BufferedImage getRotImg(BufferedImage img, int rotAngle) {
        int width = img.getWidth();
        int height = img.getHeight();

        BufferedImage newImg = new BufferedImage(width, height, img.getType());
        Graphics2D g2d = newImg.createGraphics();

        g2d.rotate(Math.toRadians(rotAngle), width / 2, height / 2);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        return newImg;
    }

    public static BufferedImage buildImg(BufferedImage[] imgs){
        int width = imgs[0].getWidth();
        int height = imgs[0].getHeight();

        BufferedImage newImg = new BufferedImage(width, height, imgs[0].getType());
        Graphics2D g2d = newImg.createGraphics();

        for(BufferedImage img : imgs) {
            g2d.drawImage(img, 0, 0, null);
        }

        g2d.dispose();
        return newImg;
    }
}
