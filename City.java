//This is complete and you do not need to edit it for this project.

import org.apache.commons.collections15.Factory;
import java.awt.Color;
import java.awt.Point;

/**
 *  A city in the world.
 *  
 *  @author K. Raven Russell
 */
public class City extends GraphNode {
	/**
	 *  Some city names to rotate through.
	 */
	public static String[] names = { "New York City","Los Angeles","Chicago","Houston","Phoenix","San Antonio","Philadelphia","San Diego","Dallas","Austin","San Jose","Fort Worth","Jacksonville","Charlotte","Columbus","Indianapolis","San Francisco","Seattle","Denver","Washington","Boston","El Paso","Nashville","Oklahoma City","Las Vegas","Portland","Detroit","Memphis","Louisville","Milwaukee","Baltimore","Albuquerque","Tucson","Mesa","Fresno","Atlanta","Sacramento","Kansas City","Colorado Springs","Raleigh","Miami","Omaha","Long Beach","Virginia Beach","Oakland","Minneapolis","Tampa","Tulsa","Arlington","Aurora","Wichita","Bakersfield","New Orleans","Cleveland","Henderson","Anaheim","Honolulu","Riverside","Santa Ana","Corpus Christi","Lexington","San Juan","Stockton","St. Paul","Cincinnati","Irvine","Greensboro","Pittsburgh","Lincoln","Durham","Orlando","St. Louis","Chula Vista","Plano","Newark","Anchorage","Fort Wayne","Chandler","Reno","Las Vegas","Scottsdale","St. Petersburg","Laredo","Gilbert","Toledo","Lubbock","Madison","Glendale","Jersey City","Buffalo","Chesapeake","Winston-Salem","Fremont","Norfolk","Frisco","Paradise","Irving","Garland","Richmond","Arlington" };
	
	/**
	 *  Track the last name used by any city.
	 */
	public static int LAST_NAME_ID = -1;
	
	/**
	 *  The name id of this particular city.
	 */
	private final int NAME_ID = ++LAST_NAME_ID % names.length;
	
	/**
	 *  The physical location of the point
	 */
	private Point loc;
	
	/**
	 *  Sets the id of the city using the GraphNode constructor.
	 */
	private City() {
		
	}
	
	/**
	 *  Extends the GraphNode constructor GraphNode(int)
	 *  to accept a location.
	 *  
	 *  @param id the unique identifier of the node
	 *  @param loc the location of the city in space
	 */
	public City(int id, Point loc) {
		super(id);
		this.loc = loc;
	}
	
	/**
	 *  Accessors for the city's location.
	 *  
	 *  @return the city's location
	 */
	public Point getLoc() {
		return this.loc;
	}
	
	/**
	 *  Mutator for the city's location.
	 *  
	 *  @param loc the city's location
	 */
	public void setLoc(Point loc) {
		this.loc = loc;
	}
	
	/**
	 *  The string representation of a city.
	 *  
	 *  @return the string representation of the city
	 */
	public String toString() {
		return names[NAME_ID];
	}
	
	/**
	 *  Two cities are equal if they have the same id.
	 *  
	 *  @return whether two cities are equal
	 */
	@Override
	public boolean equals(Object o) {
		if(o instanceof City) {
			return this.id == ((City)o).id;
		}
		return false;
	}
	
	/**
	 *  This is a code pattern called a "factory".
	 *  A factory for cities makes instances of cities!
	 *  
	 *  @return a factory that can make cities
	 */
	public static Factory<City> getFactory() { 
		return new Factory<City> () {
			public City create() {
				return new City();
			}
		};
	}
}