//This is complete and you do not need to edit it for this project.

import org.apache.commons.collections15.Factory;
import java.awt.Color;
import java.util.Random;

/**
 *  A host in the network.
 *  
 *  @author K. Raven Russell
 */
public class GraphEdge {
	/**
	 *  The last id given to a edge.
	 */
	public static int LAST_ID = -1;
	
	/**
	 *  A random number generator. Do not change the seed!
	 */
	protected static final Random r = new Random(0);
	
	/**
	 *  The unique id of this edge.
	 */
	protected final int id;
	
	/**
	 *  The cost of the edge.
	 */
	protected int cost;
	
	/**
	 *  The color of this edge in the visualization.
	 */
	protected Color c = Color.BLACK;
	
	/**
	 *  Makes a new edges with a random cost between
	 *  1 and 6.
	 */
	protected GraphEdge() {
		id = ++LAST_ID;
		cost = r.nextInt(5)+1;
	}
	
	/**
	 *  Returns the id of the edge.
	 *  @return the edge's unique identifier
	 */
	public int getId() {
		return id;
	}
	
	/**
	 *  Returns the color of the edge in the simulation.
	 *  
	 *  @return the edge's current color
	 */
	public Color getColor() {
		return c;
	}
	
	/**
	 *  Sets the color of the edge in the simulation.
	 *  @param c the new color to use
	 */
	public void setColor(Color c) {
		this.c = c;
	}
	
	/**
	 *  Returns the cost of the edge.
	 *  
	 *  @return the edge's cost
	 */
	public int getCost() {
		return cost;
	}
	
	/**
	 *  Updates the cost of the edge.
	 *  
	 *  @param cost the edge's new cost
	 */
	public void setCost(int cost) {
		this.cost = cost;
	}
	
	/**
	 *  The string representation of a edge
	 *  is just it's cost.
	 *  
	 *  @return the string representation of the edge
	 */
	@Override
	public String toString() {
		return ""+cost;
	}
	
	/**
	 *  Sets the hash code of the edge (cost * id).
	 *  
	 *  @return the hash code of the edge
	 */
	@Override
	public int hashCode() {
		return cost*id;
	}
	
	/**
	 *  Two edges are equal if they have the same id.
	 *  
	 *  @return whether two edges are equal
	 */
	@Override
	public boolean equals(Object o) {
		if(o instanceof GraphEdge) {
			return this.id == ((GraphEdge)o).id;
		}
		return false;
	}
}