package me.versteege.games.libgdx.algorithms.tbpf;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

public class Wall {
	
	private final List<Vector2> mTiles;
	
	public Wall() {
		mTiles = new LinkedList<Vector2>();
	}
	
	public Wall addTile(float x, float y) {
		mTiles.add(new Vector2(x, y));
		return this;
	}
	
	public List<Vector2> getTiles() {
		return mTiles;
	}
}
