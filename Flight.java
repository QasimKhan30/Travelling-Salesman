//This is complete and you do not need to edit it for this project.

//If you are interested, this class contains two "patterns" common in
//software development: (1) generating unique ids per instance and
//(2) creating a "factory" that creates instances.

import org.apache.commons.collections15.Factory;
import java.awt.Color;
import java.util.Random;

/**
 *  A flight between two cities.
 *  
 *  @author K. Raven Russell
 */
public class Flight extends GraphEdge {
	/**
	 *  Makes a new flight using the default GraphEdge
	 *  constructor.
	 */
	private Flight() {
		
	}
	
	/**
	 *  Two flights are equal if they have the same id.
	 *  
	 *  @return whether two flights are equal
	 */
	@Override
	public boolean equals(Object o) {
		if(o instanceof Flight) {
			return this.id == ((Flight)o).id;
		}
		return false;
	}
	
	/**
	 *  This is a code pattern called a "factory".
	 *  A factory for flights makes instances of
	 *  flights!
	 *  
	 *  @return a factory that can make flights
	 */
	public static Factory<Flight> getFactory() { 
		return new Factory<Flight> () {
			public Flight create() {
				return new Flight();
			}
		};
	}
}