package me.versteege.games.libgdx.algorithms.tbpf;

import java.util.LinkedList;

import me.versteege.games.libgdx.algorithms.tbpf.Tile.Type;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class PathFindingSimulation implements ApplicationListener, InputProcessor {
	
	private OrthographicCamera mCamera;
	private ShapeRenderer mShapeRenderer;
	private TileWalker mTileWalker;
	
	private final Vector2 mPlayerPos = new Vector2();
	
	private Tile [][] mTiles;
	
	@Override
	public void create() {		
		mCamera = new OrthographicCamera(20, 20);
		mCamera.translate(10, 10);
		mShapeRenderer = new ShapeRenderer();
		
		Gdx.input.setInputProcessor(this);
		
		initTiles();
		//initWalls();
		
		// start player at 1, 1
		setPlayPos(1, 1);
		
		mTileWalker = new TileWalker(new AStarPathFinder(), mTiles, 1, 1, 6);
	}
	
	public void setPlayPos(int x, int y) {
		
		mTiles[(int)mPlayerPos.x][(int)mPlayerPos.y].setOccupied(false);
		
		mPlayerPos.x = x;
		mPlayerPos.y = y;
		
		mTiles[x][y].setOccupied(true);
	}
	
	private void initTiles() {
		mTiles = new Tile[(int) mCamera.viewportWidth][(int) mCamera.viewportHeight];
		for(int i = 0; i < mCamera.viewportWidth; i++) {
			for(int j = 0; j < mCamera.viewportHeight; j++) {
				Tile newTile = new Tile(Type.DEFAULT, Color.BLACK, i, j);
				mTiles[i][j] = newTile;
			}
		}
		
		for(int i = 0; i < mTiles.length; i++) {
			for(int j = 0; j < mTiles.length; j++) {
				// add left
				if(j > 0) {
					mTiles[i][j].addNeighbor(mTiles[i][j - 1]);
				}
				// add right
				if(j < mTiles[i].length - 1) {
					mTiles[i][j].addNeighbor(mTiles[i][j + 1]);
				}
				// add down
				if(i > 0) {
					mTiles[i][j].addNeighbor(mTiles[i - 1][j]);
				}
				// add up
				if(i < mTiles.length - 1) {
					mTiles[i][j].addNeighbor(mTiles[i + 1][j]);
				}
			}
		}
	}
	
	private PathFindingSimulation addWallTile(int x, int y) {
		mTiles[x][y].setType(Type.WALL);
		
		return this;
	}

	private void removeWallTile(int x, int y) {
		mTiles[x][y].setType(Type.DEFAULT);
	}
	
	private void clearWalls() {
		for(int i = 0; i < mTiles.length; i++) {
			for(int j = 0; j < mTiles[i].length; j++) {
				mTiles[i][j].setType(Type.DEFAULT);
			}
		}
	}
	
	@Override
	public void dispose() {
		mShapeRenderer.dispose();
	}
	
	private void update(float deltaTime) {
		mTileWalker.update(deltaTime);
	}

	@Override
	public void render() {		
		
		update(Gdx.graphics.getDeltaTime());
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		mCamera.update();

		mShapeRenderer.setProjectionMatrix(mCamera.combined);
		mShapeRenderer.begin(ShapeType.Filled);
		
		// tiles
		for(int i = 0; i < mTiles.length; i++) {
			for(int j = 0; j < mTiles[i].length; j++) {
				mShapeRenderer.setColor(mTiles[i][j].getColor());
				mShapeRenderer.rect(i, j, 1, 1);
				
				if(mTiles[i][j].isOccupied()) {
					mShapeRenderer.setColor(Color.YELLOW);
					mShapeRenderer.rect(i, j, 1, 1);
				}
				else if(mTiles[i][j].isInPath()) {
					mShapeRenderer.setColor(Color.GREEN);
					mShapeRenderer.rect(i, j, 1, 1);
				}
			}
		}
		
		mShapeRenderer.end();
		
		// grid lines
		mShapeRenderer.begin(ShapeType.Line);
		boolean drawVerticalLine = true;
		for(int i = 0; i < mCamera.viewportWidth; i++) {
			mShapeRenderer.setColor(Color.WHITE);
			mShapeRenderer.line(0, i, mCamera.viewportWidth, i);
			for(int j = 0; j < mCamera.viewportHeight; j++) {
				if(drawVerticalLine) {
					mShapeRenderer.setColor(Color.WHITE);
					mShapeRenderer.line(j, 0, j, mCamera.viewportHeight);
				}
			}
			drawVerticalLine = false;
		}
		mShapeRenderer.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	// user input methods
	
	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		if(character == 'r') {
			
			clearWalls();
			
			return true;
		}
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		
		Vector3 location = new Vector3(screenX, screenY, 0);
		mCamera.unproject(location);
		
		int tileX = (int) location.x;
		int tileY = (int) location.y;
		
		// left click
		if(button == 0) {
			if(mTiles[tileX][tileY].getType() != Type.WALL) {
				addWallTile(tileX, tileY);
			}
			else {
				removeWallTile(tileX, tileY);
			}
		}
		// right click
		else if(button == 1) {
			
			if(mTiles[tileX][tileY].getType() != Type.WALL) {
				mTileWalker.walkToTile(mTiles[tileX][tileY]);
			}
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
