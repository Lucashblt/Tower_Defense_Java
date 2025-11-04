package managers;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import helper.ImgFix;
import helper.LoadSave;
import static helper.Constants.Tiles.*;
import objects.Tile;

public class TileManager {
    
	public Tile GRASS, WATER, ROAD_LR, ROAD_TB, ROAD_B_TO_R, ROAD_L_TO_B, ROAD_L_TO_T, ROAD_T_TO_R, BL_WATER_CORNER, TL_WATER_CORNER, TR_WATER_CORNER, BR_WATER_CORNER, T_WATER, R_WATER, B_WATER,
			L_WATER, TL_ISLE, TR_ISLE, BR_ISLE, BL_ISLE, START_PATH, END_PATH;

    public BufferedImage atlas;
    public ArrayList<Tile> tiles = new ArrayList<>();

	public ArrayList<Tile> roadsS = new ArrayList<>();
	public ArrayList<Tile> roadsC = new ArrayList<>();
	public ArrayList<Tile> corners = new ArrayList<>();
	public ArrayList<Tile> beaches = new ArrayList<>();
	public ArrayList<Tile> islands = new ArrayList<>();

    public TileManager() {
        LoadAtlas();
        CreateTiles();
        
    }

    private void CreateTiles() {
        int id = 0;
        tiles.add(GRASS = new Tile(getSprite(9, 0), id++, GRASS_TILE));
        tiles.add(WATER = new Tile(getSprite(0, 0), id++, WATER_TILE));

        roadsS.add(ROAD_LR = new Tile(getSprite(8, 0), id++, ROAD_TILE));
        roadsS.add(ROAD_TB = new Tile(ImgFix.getRotImg(getSprite(8, 0), 90), id++, ROAD_TILE));

        roadsC.add(ROAD_B_TO_R = new Tile(getSprite(7, 0), id++, ROAD_TILE));
        roadsC.add(ROAD_L_TO_B = new Tile(ImgFix.getRotImg(getSprite(7,0), 90), id++, ROAD_TILE));
        roadsC.add(ROAD_L_TO_T = new Tile(ImgFix.getRotImg(getSprite(7, 0), 180), id++, ROAD_TILE));
        roadsC.add(ROAD_T_TO_R = new Tile(ImgFix.getRotImg(getSprite(7,0), 270), id++, ROAD_TILE));

        corners.add(BL_WATER_CORNER = new Tile(ImgFix.buildImg(getImgs(0, 0, 5, 0)), id++, WATER_TILE));
        corners.add(TL_WATER_CORNER = new Tile(ImgFix.getBuildRotImg(getImgs(0, 0, 5, 0), 90, 1), id++, WATER_TILE));
        corners.add(TR_WATER_CORNER = new Tile(ImgFix.getBuildRotImg(getImgs(0, 0, 5, 0), 180, 1), id++, WATER_TILE));
        corners.add(BR_WATER_CORNER = new Tile(ImgFix.getBuildRotImg(getImgs(0, 0, 5, 0), 270, 1), id++, WATER_TILE));

        beaches.add(T_WATER = new Tile(ImgFix.getBuildRotImg(getImgs(0, 0, 6, 0), 0, 1), id++, WATER_TILE));
        beaches.add(R_WATER = new Tile(ImgFix.getBuildRotImg(getImgs(0, 0, 6, 0), 90, 1), id++, WATER_TILE));
        beaches.add(B_WATER = new Tile(ImgFix.getBuildRotImg(getImgs(0, 0, 6, 0), 180, 1), id++, WATER_TILE));
        beaches.add(L_WATER = new Tile(ImgFix.getBuildRotImg(getImgs(0, 0, 6, 0), 270, 1), id++, WATER_TILE));

        islands.add(TL_ISLE = new Tile(ImgFix.getBuildRotImg(getImgs(0, 0, 4, 0), 0, 1), id++, WATER_TILE));
        islands.add(TR_ISLE = new Tile(ImgFix.getBuildRotImg(getImgs(0, 0, 4, 0), 90, 1), id++, WATER_TILE));
        islands.add(BR_ISLE = new Tile(ImgFix.getBuildRotImg(getImgs(0, 0, 4, 0), 180, 1), id++, WATER_TILE));
        islands.add(BL_ISLE = new Tile(ImgFix.getBuildRotImg(getImgs(0, 0, 4, 0), 270, 1), id++, WATER_TILE));

        tiles.addAll(roadsS);
		tiles.addAll(roadsC);
		tiles.addAll(corners);
		tiles.addAll(beaches);
		tiles.addAll(islands);

        tiles.add(START_PATH = new Tile(ImgFix.getBuildRotImg(getImgs(8, 0, 7, 2), 0, 1), id++, START_TILE));
        tiles.add(END_PATH = new Tile(ImgFix.getBuildRotImg(getImgs(8, 0, 8, 2), 0, 1), id++, END_TILE));
    }

    private BufferedImage[] getImgs(int firstX, int firstY, int secondX, int secondY) {
        BufferedImage[] imgs = new BufferedImage[2];
        imgs[0] = getSprite(firstX, firstY);
        imgs[1] = getSprite(secondX, secondY);
        return imgs;
    }

    private void LoadAtlas() {

        atlas = LoadSave.getSpriteAtlas();

    }

    public BufferedImage getSprite(int id) {
        return tiles.get(id).getSprite();
    }

    private BufferedImage getSprite(int xCord, int yCord) {
        return atlas.getSubimage(xCord * 32, yCord * 32, 32, 32);
    }

    public Tile getTile(int id) {
        return tiles.get(id);
    }
}
