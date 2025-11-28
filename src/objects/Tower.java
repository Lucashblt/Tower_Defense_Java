package objects;

import static helper.Constants.Towers.*;

public class Tower {

    private int x, y, id, towerType, cdTick = 0, damage;
    private float range, cooldown;
    private int tier;
    
    // Cache pour optimisation POO
    private int rangeSquared; // Range au carré pour comparaison rapide

    public Tower(int x, int y, int id, int towerType) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.towerType = towerType;
        this.tier = 1;
        setDefaultDamage();
        setDefaultRange();
        setCooldownTime();
        updateRangeSquared();
    }

    public void update() {
        cdTick++;
    }

    public boolean isCooldownOver() {
        return cdTick >= cooldown;
    }

    public void resetCooldown() {
        cdTick = 0;
    }

    private void setDefaultDamage() {
        this.damage = getStartDamage(towerType);
    }

    private void setDefaultRange() {
        this.range = getDefaultRange(towerType);
        updateRangeSquared();
    }
    
    private void updateRangeSquared() {
        this.rangeSquared = (int)(range * range);
    }

    private void setCooldownTime() {
        this.cooldown = getCooldownTime(towerType);
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public int getTowerType() {
        return towerType;
    }

    public int getId() {
        return id;
    }

    public int getDamage() {
        return damage;
    }

    public float getRange() {
        return range;
    }
    
    /**
     * Retourne le carré de la portée pour comparaison rapide sans sqrt()
     * Principe POO : Cache calculé pour optimisation
     */
    public int getRangeSquared() {
        return rangeSquared;
    }

    public float getCooldown() {
        return cooldown;
    }

    public void upgradeTower() {
        this.tier++;

        switch (towerType) {
            case ARCHER_TOWER:  
                this.damage += 2;
                this.range += 20;     
                this.cooldown -= 5;         
                break;
            case CANON_TOWER:
                this.damage += 5;
                this.range += 10;
                this.cooldown -= 15;
                break;
            case WIZARD_TOWER:
                this.damage += 1;
                this.range += 15;
                this.cooldown -= 10;
                break;        
            default:
                break;
        }
        updateRangeSquared(); // Mettre à jour le cache
    }

    public int getTier() {
        return tier;
    }
}
