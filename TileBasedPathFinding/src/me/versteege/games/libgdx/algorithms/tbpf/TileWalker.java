package me.versteege.games.libgdx.algorithms.tbpf;

import java.util.LinkedList;

public class TileWalker {

	private AStarPathFinder mPathFinder;
	private Tile [][] mTiles;
	private LinkedList<Tile> mPath;
	
	private int mX;
	private int mY;

	private final float mCooldownTime;
	private float mTimeSinceLastStep;
	
	public TileWalker(AStarPathFinder pathFinder, Tile [][] tiles, int startX, int startY, float tilesPerSec) {
		mPathFinder = pathFinder;
		mTiles = tiles;
		
		mX = startX;
		mY = startY;
		
		mCooldownTime = 1.0f / tilesPerSec;
		
		mPath = new LinkedList<Tile>();
	}
	
	public void walkToTile(Tile tile) {
		
		// clear the rest of last path
		for(Tile current : mPath) {
			current.setInPath(false);
		}
		
		mPath = mPathFinder.getPathToTile(mTiles[mX][mY], tile);
		
		// light up current path
		for(Tile current : mPath) {
			current.setInPath(true);
		}
		
		mTimeSinceLastStep = mCooldownTime;
	}
	
	public void update(float deltaTime) {
		
		mTimeSinceLastStep += deltaTime;
		
		if(mTimeSinceLastStep > mCooldownTime && mPath.size() > 0) {
			move(mTiles[mX][mY], mPath.get(0));
			
			mTimeSinceLastStep -= mCooldownTime;
		}
	}
	
	private void move(Tile from, Tile to) {
		from.setOccupied(false);
		to.setOccupied(true);
		
		mX = (int) to.position.x;
		mY = (int) to.position.y;
		
		to.setInPath(false);
		mPath.remove(to);
	}
}
