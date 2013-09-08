package me.versteege.games.libgdx.algorithms.tbpf;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Tile {
	
	public enum Type {
		DEFAULT,
		WALL
	}
	
	private Type mType;
	private Color mColor;
	private boolean mOccupied;
	private boolean mInPath;
	public Vector2 position;
	
	private Set<Tile> mNeighbors;
	
	public Tile(Type type, Color color, int x, int y) {
		mType = type;
		mColor = color;
		mOccupied = false;
		
		position = new Vector2();
		
		position.x = x;
		position.y = y;
		
		mNeighbors = new HashSet<Tile>();
	}
	
	public void setColor(Color color) {
		mColor = color;
	}
	
	public Color getColor() {
		return mColor;
	}
	
	public void setType(Type type) {
		mType = type;
		
		if(type == Type.WALL) {
			mColor = Color.RED;
		}
		else if(type == Type.DEFAULT) {
			mColor = Color.BLACK;
		}
	}
	
	public Type getType() {
		return mType;
	}
	
	public boolean isOccupied() {
		return mOccupied;
	}
	
	public boolean isInPath() {
		return mInPath;
	}
	
	public void setOccupied(boolean occupied) {
		mOccupied = occupied;
	}
	
	public void setInPath(boolean inPath) {
		mInPath = inPath;
	}
	
	public void addNeighbor(Tile tile) {
		mNeighbors.add(tile);
	}
	
	public Set<Tile> getNeighbors() {
		return mNeighbors;
	}
	
	public Tile removeNeighbor(Tile tile) {
		mNeighbors.remove(tile);	
		return tile;
	}
	
	public String toString() {
		return String.format("(%d, %d)", (int)position.x, (int)position.y);
	}
}
