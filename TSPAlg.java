//This is the base class for a TSP algorithm, both TSPPermutations and TSPApprox
//extend this class.

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;

import java.awt.Color;

import java.util.Iterator;
import java.util.List;

/**
 *  This describes the actual algorithm for TSP.
 *  
 *  @author Katherine (Raven) Russell
 */
abstract class TSPAlg implements FourEightyThreeAlg<City,Flight> {
	/**
	 *  The graph the algorithm will run on.
	 */
	protected Graph<City, Flight> graph;
	
	/**
	 *  The final order the TS should use according
	 *  to the algorithm.
	 */
	protected List<City> visitOrder = null;
	
	/**
	 *  The cost of the TS's travels.
	 */
	protected int visitOrderCost = Integer.MAX_VALUE;
	
	/**
	 *  The starting city for the algorithm. Various algorithms
	 *  use this in different ways. For example, TSPApprox uses
	 *  this as the starting point for Prim's algorithm and the
	 *  root of the spanning tree. TSPPermutations uses this as
	 *  the "rooted" city that is not part of the permutations.
	 */
	protected City startingCity = null;
	
	/**
	 *  Whether or not the algorithm has been started.
	 */
	protected boolean started = false;
	
	/**
	 *  Whether or not the algorithm has finished.
	 */
	protected boolean finished = false;
	
	/**
	 *  The time at which the algorithm was started.
	 */
	protected long startTime = 0l;
	
	/**
	 *  The color when an edge is done AND in used.
	 */
	public static final Color COLOR_DONE_EDGE_1 = Color.GREEN.darker();
	
	/**
	 *  The color when an edge is done AND NOT used.
	 */
	public static final Color COLOR_DONE_EDGE_2 = Color.LIGHT_GRAY;
	
	/**
	 *  The color when an edge currently being examined.
	 */
	public static final Color COLOR_ACTIVE_EDGE = Color.ORANGE;
	
	/**
	 *  The color when an edge has "no color".
	 */
	public static final Color COLOR_NONE_EDGE = Color.BLACK;
	
	/**
	 *  {@inheritDoc}
	 */
	abstract public boolean setupNextStep();
	
	/**
	 *  {@inheritDoc}
	 */
	abstract public void doNextStep();
	
	/**
	 *  {@inheritDoc}
	 */
	public EdgeType graphEdgeType() {
		return EdgeType.UNDIRECTED;
	}
	
	/**
	 *  {@inheritDoc}
	 */
	public void reset(Graph<City, Flight> graph) {
		this.graph = graph;
		started = false;
		finished = false;
		startingCity = null;
		visitOrder = null;
		visitOrderCost = Integer.MAX_VALUE;
	}
	
	/**
	 *  Determines the starting node for the TSP algorithm.
	 *  Currently chooses the north-most city to put it on
	 *  the edge of the map (for easy viewing).
	 *  
	 *  @return the north-most city
	 */
	protected City getStartingNode() {
		City most = null;
		
		for(City n : graph.getVertices()) {
			if(most == null || n.getLoc().getY() < most.getLoc().getY()) {
				most = n;
			}
		}
		
		return most;
	}
	
	/**
	 *  {@inheritDoc}
	 */
	public void start() {
		this.started = true;
		this.startingCity = getStartingNode();
		this.startTime = System.currentTimeMillis();
	}
	
	/**
	 *  {@inheritDoc}
	 */
	public void finish() {
		this.finished = true;
		if(visitOrder == null) return;
		
		colorAllEdges(COLOR_DONE_EDGE_2);
		
		Flight f = null;
		for(int i = 0; i < this.visitOrder.size()-1; i++) {
			f = graph.findEdge(this.visitOrder.get(i), this.visitOrder.get(i+1));
			f.setColor(COLOR_DONE_EDGE_1);
		}
		f = graph.findEdge(visitOrder.get(this.visitOrder.size()-1), this.visitOrder.get(0));
		f.setColor(COLOR_DONE_EDGE_1);
		
		System.out.print("\tFinal Path: ");
		printPath(visitOrder);
		System.out.println("\tFinal Path Cost: " + visitOrderCost);
		printClockTime();
		System.out.println();
	}
	
	/**
	 *  Helper method to print a path / permutation / traversal.
	 *  
	 *  @param cities a list of cities to print in the order provided
	 */
	protected void printPath(List<City> cities) {
		for(City c : cities) {
			System.out.print(c);
			System.out.print("-->");
		}
		System.out.println(cities.get(0));
	}
	
	/**
	 *  Helper method to print the time the algorithm took to run.
	 *  Reports in milliseconds, seconds, minutes, hours, or days
	 *  as long as the unit has at least two of them (e.g. for less
	 *  than two seconds prints in milliseconds, and for less than
	 *  two minutes prints in seconds).
	 */
	protected void printClockTime() {
		long clockTime = (System.currentTimeMillis()-startTime);
		if(clockTime < 2000) {
			System.out.println("\tSimulation Time: " + clockTime + " millisecond(s)");
		}
		else {
			clockTime /= 1000;
			if(clockTime < 120) {
				System.out.println("\tSimulation Time: " + clockTime + " second(s)");
			}
			else {
				clockTime /= 60;
				if(clockTime < 120) {
					System.out.println("\tSimulation Time: " + clockTime + " minute(s)");
				}
				else {
					clockTime /= 60;
					if(clockTime < 48) {
						System.out.println("\tSimulation Time: " + clockTime + " hour(s)");
					}
					else {
						clockTime /= 24;
						System.out.println("\tSimulation Time: " + clockTime + " day(s)");
					}
				}
			}
		}
	}
	
	/**
	 *  {@inheritDoc}
	 */
	public void cleanUpLastStep() {
		colorAllEdges(COLOR_NONE_EDGE);
	}
	
	/**
	 *  Helper methods to return all edges to a given color.
	 *  
	 *  @param c the color to change all edges to
	 */
	protected void colorAllEdges(Color c) {
		for(Flight f : graph.getEdges()) {
			f.setColor(c);
		}
	}
	
	/**
	 *  {@inheritDoc}
	 */
	public boolean step() {
		if(finished) return false;
		
		if(!started) {
			start();
			//return true;
		}
		
		cleanUpLastStep();
		if(!setupNextStep()) {
			finish();
			return false;
		}
		doNextStep();
		
		return true;
	}
}
