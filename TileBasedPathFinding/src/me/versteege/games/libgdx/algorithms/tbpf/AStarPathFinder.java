package me.versteege.games.libgdx.algorithms.tbpf;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class AStarPathFinder {
	
	public AStarPathFinder() {
		
	}
	
	public LinkedList<Tile> getPathToTile(Tile from, Tile to) {
		return doAStar(from, to);
	}
	
	private double heuristic(Tile tile, Tile goal) {
		float dx = Math.abs(tile.position.x - goal.position.x);
		float dy = Math.abs(tile.position.y - goal.position.y);
		
		return dx + dy;
	}
	
	private Tile getMinFScoreTile(Map<Tile, Double> fScore, Set<Tile> openSet) {
		
		double minScore = Double.MAX_VALUE;
		Tile minScoreTile = null;
		
		for(Tile tile : fScore.keySet()) {
			if(openSet.contains(tile)) {
				double score = fScore.get(tile);
				if(score < minScore) {
					minScore = score;
					minScoreTile = tile;
				}
			}
		}
		
		return minScoreTile;
	}
	
	private LinkedList<Tile> doAStar(Tile start, Tile goal) {
		
		Set<Tile> closedSet = new HashSet<Tile>();
		
		Set<Tile> openSet = new HashSet<Tile>();
		openSet.add(start);
		
		Map<Tile, Tile> cameFrom = new HashMap<Tile, Tile>();
		
		Map<Tile, Double> gScore = new HashMap<Tile, Double>();
		gScore.put(start, 0d);
		
		Map<Tile, Double> fScore = new HashMap<Tile, Double>();
		fScore.put(start, gScore.get(start) + heuristic(start, goal));
		
		
		while(!openSet.isEmpty()) {
			
			Tile current = getMinFScoreTile(fScore, openSet);
			if(current.equals(goal)) {
				return reconstructPath(cameFrom, goal);
			}
			
			openSet.remove(current);
			closedSet.add(current);
			
			for(Tile neighbor : current.getNeighbors()) {
				
				double travelCostToNeighbor = 1;
				
				if(neighbor.getType() == Tile.Type.WALL) {
					travelCostToNeighbor = 100000;						// way to expensive to even consider
				}

				double tentativeGScore = gScore.get(current) + travelCostToNeighbor;
				if(closedSet.contains(neighbor) && tentativeGScore >= gScore.get(neighbor)) {
					continue;
				}
				
				if(!closedSet.contains(neighbor) || tentativeGScore < gScore.get(neighbor)) {
					cameFrom.put(neighbor, current);
					gScore.put(neighbor, tentativeGScore);
					fScore.put(neighbor, tentativeGScore + heuristic(neighbor, goal));
					if(!openSet.contains(neighbor)) {
						openSet.add(neighbor);
					}
				}
			}
		}
		
		return null;
	}
	
	public LinkedList<Tile> reconstructPath(Map<Tile, Tile> cameFrom, Tile current) {
		
		LinkedList<Tile> path = null;
		
		if(cameFrom.containsKey(current)) {
			path = reconstructPath(cameFrom, cameFrom.get(current));
			path.add(current);
		}
		else {
			path = new LinkedList<Tile>();
			path.add(current);
		}
		
		return path;
	}
}
