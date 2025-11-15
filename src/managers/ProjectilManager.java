package managers;

import java.util.ArrayList;

import enemies.Enemy;
import objects.Projectile;
import objects.Tower;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage; 

import static helper.Constants.Towers.*;
import static helper.Constants.Projectiles.*;

import scenes.Playing;

public class ProjectilManager {
    
    private Playing playing;
    private ArrayList<Projectile> projectiles = new ArrayList<>();
    private BufferedImage[] projectileImgs;
    private int projectileId = 0;

    public ProjectilManager(Playing playing) {
        this.playing = playing;
        importImgs();
    }

    private void importImgs() {
        BufferedImage atlas = helper.LoadSave.getSpriteAtlas();
        projectileImgs = new BufferedImage[3];

        for (int i = 0; i < 3; i++) {
            projectileImgs[i] = atlas.getSubimage((7 + i) * 32, 32, 32, 32);
        }
    }

    public void newProjectile(Tower t, Enemy e){
        int type = getProjectileType(t);

        int xDist = (int)Math.abs(t.getX() - e.getX());
        int yDist = (int)Math.abs(t.getY() - e.getY());
        int totDist = Math.abs(xDist) + Math.abs(yDist);

		float xPer = (float) Math.abs(xDist) / totDist;

        float xSpeed = xPer * helper.Constants.Projectiles.GetSpeed(type);
        float ySpeed = helper.Constants.Projectiles.GetSpeed(type)  - xSpeed;

        if(t.getX() > e.getX()) xSpeed *= -1;
        if(t.getY() > e.getY()) ySpeed *= -1;

        float arcValue = (float) Math.atan(yDist / (float) xDist);
        float rotate = (float) Math.toDegrees(arcValue);

        if(xDist < 0 ){
            rotate += 180;
        }

        projectiles.add(new Projectile(t.getX() + 16, t.getY() + 16, xSpeed, ySpeed,  t.getDamage(), rotate, projectileId++, type));
    }

    private int getProjectileType(Tower t) {
        switch (t.getTowerType()) {
            case ARCHER_TOWER:
                return ARROW;
            case CANON_TOWER:
                return BOMB;
            case WIZARD_TOWER:
                return CHAINS;
            default:
                return 0;
        }
    }

    public void update() {
        for (Projectile p : projectiles) {
            if(p.isActive()){
                p.move();
                if(isProjHittingEnemy(p)){
                    p.setActive(false);
                }
            } 
        }
    }
    private boolean isProjHittingEnemy(Projectile p) {
        for(Enemy e : playing.getEnemyManager().getEnemies()){
            if(e.getBounds().contains(p.getPos())){
                e.hurt(p.getDmg());
                return true;
            }
        }
        return false;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        for (Projectile p : projectiles) {
            if(p.isActive()){
                g2d.translate(p.getPos().x, p.getPos().y);
                g2d.rotate(Math.toRadians(p.getRotation()));
                g2d.drawImage(projectileImgs[p.getProjectileType()], -16, -16, null);
                g2d.rotate(-Math.toRadians(p.getRotation()));
                g2d.translate(-p.getPos().x, -p.getPos().y);
            }
        }
    }
}
