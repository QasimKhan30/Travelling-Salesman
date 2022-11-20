//This is complete and you do not need to edit it for this project.

import org.apache.commons.collections15.Factory;
import java.awt.Color;

/**
 *  A generic graph node to be extended.
 *  @author K. Raven Russell
 */
public class GraphNode {
	/**
	 *  The last id given to a node.
	 */
	public static int LAST_ID = -1;
	
	/**
	 *  The unique id of this node.
	 */
	protected final int id;
	
	/**
	 *  The color of this node in the visualization.
	 */
	private Color c = Color.WHITE;
	
	/**
	 *  Sets the id of the City.
	 */
	protected GraphNode() {
		id = ++LAST_ID;
	}
	
	/**
	 *  Creates a graph node with a given ID, but
	 *  only if that graph node has previously been
	 *  generated with the standard constructor.
	 *  (This is to allow making graph node objects
	 *  that are equal to each other, but not graph nodes
	 *  with id numbers out of sequence.)
	 *  @param id the unique identifier of the node
	 */
	public GraphNode(int id) {
		if(id < 0 || id > LAST_ID) {
			throw new IllegalArgumentException("Cannot create a node with an arbitrary id.");
		}
		this.id = id;
	}
	
	/**
	 *  Returns the id of the graph node.
	 *  @return the graph node's unique identifier
	 */
	public int getId() {
		return id;
	}
	
	/**
	 *  Returns the color of the graph node in the simulation.
	 *  @return the graph node's current color
	 */
	public Color getColor() {
		return c;
	}
	
	/**
	 *  Sets the color of the graph node in the simulation.
	 *  @param c the new color to use
	 */
	public void setColor(Color c) {
		this.c = c;
	}
	
	/**
	 *  Sets the hashcode of the graph node
	 *  to be a hash of the string value
	 *  which contains the id.
	 *  @return the hash code of the graph node
	 */
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	/**
	 *  The string representation of a graph node.
	 *  @return the string representation of the graph node
	 */
	public String toString() {
		return ""+id;
	}
	
	/**
	 *  Two hosts are equal if they have the same id.
	 *  @return whether two hosts are equal
	 */
	@Override
	public boolean equals(Object o) {
		if(o instanceof GraphNode) {
			return this.id == ((GraphNode)o).id;
		}
		return false;
	}
}