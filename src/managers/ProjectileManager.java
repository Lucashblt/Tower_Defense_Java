package managers;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import enemies.Enemy;
import helper.LoadSave;
import helper.ObjectPool;
import objects.Projectile;
import objects.Tower;
import scenes.Playing;
import static helper.Constants.Towers.*;
import static helper.Constants.Projectiles.*;

public class ProjectileManager {

	private Playing playing;
	private EnemyManager enemyManager;
	
	// Object Pool pour les projectiles
	private ObjectPool<Projectile> projectilePool;
	private ArrayList<Projectile> activeProjectiles;
	
	private ArrayList<Explosion> explosions = new ArrayList<>();
	private BufferedImage[] proj_imgs, explo_imgs;

	public ProjectileManager(Playing playing) {
		this.playing = playing;
		
		// Pool de projectiles avec capacité adaptée
		projectilePool = new ObjectPool<>(
			() -> new Projectile(0, 0, 0, 0, 0, 0, 0, 0), 
			2000, 
			10000
		);
		activeProjectiles = new ArrayList<>();
		
		importImgs();
	}

	public void setEnemyManager(EnemyManager enemyManager) {
		this.enemyManager = enemyManager;
	}

	private void importImgs() {
		BufferedImage atlas = LoadSave.getSpriteAtlas();
		proj_imgs = new BufferedImage[3];

		for (int i = 0; i < 3; i++)
			proj_imgs[i] = atlas.getSubimage((7 + i) * 32, 32, 32, 32);
		importExplosion(atlas);
	}

	private void importExplosion(BufferedImage atlas) {
		explo_imgs = new BufferedImage[7];

		for (int i = 0; i < 7; i++)
			explo_imgs[i] = atlas.getSubimage(i * 32, 32 * 2, 32, 32);

	}

	public void newProjectile(Tower t, Enemy e) {
		int type = getProjType(t);

		int xDist = (int) (t.getX() - e.getX());
		int yDist = (int) (t.getY() - e.getY());
		int totDist = Math.abs(xDist) + Math.abs(yDist);

		float xPer = (float) Math.abs(xDist) / totDist;

		float xSpeed = xPer * helper.Constants.Projectiles.GetSpeed(type);
		float ySpeed = helper.Constants.Projectiles.GetSpeed(type) - xSpeed;

		if (t.getX() > e.getX())
			xSpeed *= -1;
		if (t.getY() > e.getY())
			ySpeed *= -1;

		float rotate = 0;

		if (type == ARROW) {
			float arcValue = (float) Math.atan(yDist / (float) xDist);
			rotate = (float) Math.toDegrees(arcValue);

			if (xDist < 0)
				rotate += 180;
		}

		// Utiliser le pool pour obtenir ou créer un projectile
		Projectile p = projectilePool.obtain();
		if (p != null) {
			p.reuse((int)t.getX() + 16, (int)t.getY() + 16, xSpeed, ySpeed, t.getDamage(), rotate, type);
			activeProjectiles.add(p);
		}
	}

	public void update() {
		// Mise à jour des projectiles actifs
		for (int i = activeProjectiles.size() - 1; i >= 0; i--) {
			Projectile p = activeProjectiles.get(i);
			if (p.isActive()) {
				p.move();
				if (isProjHittingEnemy(p)) {
					// Sauvegarder le type AVANT de désactiver (pour l'explosion)
					int projType = p.getProjectileType();
					Point2D.Float projPos = new Point2D.Float(p.getPos().x, p.getPos().y);
					
					p.setActive(false);
					
					// Créer l'explosion si c'est une bombe
					if (projType == BOMB) {
						explosions.add(new Explosion(projPos));
						explodeOnEnemies(p);
					}
				} else if (isProjOutsideBounds(p)) {
					p.setActive(false);
				}
			}
			
			// Retourner au pool si inactif
			if (!p.isActive()) {
				projectilePool.free(p);
				activeProjectiles.remove(i);
			}
		}

		// Mise à jour des explosions
		for (int i = explosions.size() - 1; i >= 0; i--) {
			Explosion e = explosions.get(i);
			if (e.getIndex() < 7) {
				e.update();
			} else {
				explosions.remove(i);
			}
		}
	}

	private void explodeOnEnemies(Projectile p) {
		ArrayList<Enemy> enemies = null;
		if (playing != null) {
			enemies = playing.getEnemyManager().getEnemies();
		} else if (enemyManager != null) {
			enemies = enemyManager.getEnemies();
		}
		
		if (enemies == null) {
			return;
		}
		
		for (Enemy e : enemies) {
			if (e.isAlive()) {
				float radius = 40.0f;

				float xDist = Math.abs(p.getPos().x - e.getX());
				float yDist = Math.abs(p.getPos().y - e.getY());

				float realDist = (float) Math.hypot(xDist, yDist);

				if (realDist <= radius)
					e.hurt(p.getDmg());
			}

		}

	}

	private boolean isProjHittingEnemy(Projectile p) {
		ArrayList<Enemy> enemies = null;
		if (playing != null) {
			enemies = playing.getEnemyManager().getEnemies();
		} else if (enemyManager != null) {
			enemies = enemyManager.getEnemies();
		}
		
		if (enemies == null) {
			return false;
		}
		
		for (Enemy e : enemies) {
			if (e.isAlive())
				if (e.getBounds().contains(p.getPos())) {
					e.hurt(p.getDmg());
					if (p.getProjectileType() == CHAINS)
						e.slow();

					return true;
				}
		}
		return false;
	}

	private boolean isProjOutsideBounds(Projectile p) {
		if (p.getPos().x >= 0)
			if (p.getPos().x <= 640)
				if (p.getPos().y >= 0)
					if (p.getPos().y <= 800)
						return false;
		return true;
	}

	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		for (int i = 0; i < activeProjectiles.size(); i++) {
			Projectile p = activeProjectiles.get(i);
			if (p.isActive()) {
				if (p.getProjectileType() == ARROW) {
					g2d.translate(p.getPos().x, p.getPos().y);
					g2d.rotate(Math.toRadians(p.getRotation()));
					g2d.drawImage(proj_imgs[p.getProjectileType()], -16, -16, null);
					g2d.rotate(-Math.toRadians(p.getRotation()));
					g2d.translate(-p.getPos().x, -p.getPos().y);
				} else {
					g2d.drawImage(proj_imgs[p.getProjectileType()], (int) p.getPos().x - 16, (int) p.getPos().y - 16, null);
				}
			}
		}

		drawExplosions(g2d);
	}

	private void drawExplosions(Graphics2D g2d) {
		// Itération directe sans copie
		for (int i = 0; i < explosions.size(); i++) {
			Explosion e = explosions.get(i);
			if (e.getIndex() < 7)
				g2d.drawImage(explo_imgs[e.getIndex()], (int) e.getPos().x - 16, (int) e.getPos().y - 16, null);
		}
	}

	private int getProjType(Tower t) {
		switch (t.getTowerType()) {
		case ARCHER_TOWER:
			return ARROW;
		case CANON_TOWER:
			return BOMB;
		case WIZARD_TOWER:
			return CHAINS;
		}
		return 0;
	}

	public class Explosion {

		private Point2D.Float pos;
		private int exploTick, exploIndex;

		public Explosion(Point2D.Float pos) {
			this.pos = pos;
		}

		public void update() {
			exploTick++;
			if (exploTick >= 6) {
				exploTick = 0;
				exploIndex++;
			}
		}

		public int getIndex() {
			return exploIndex;
		}

		public Point2D.Float getPos() {
			return pos;
		}
	}

	public int getProjectileCount() {
		return activeProjectiles.size();
	}

	public void reset() {
		// Retourner tous les projectiles au pool
		for (Projectile p : activeProjectiles) {
			projectilePool.free(p);
		}
		activeProjectiles.clear();
		explosions.clear();
	}
}