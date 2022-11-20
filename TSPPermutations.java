//This is mostly done for you. See the "YOUR CODE HERE" spots below.

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 *  This algorithm attempts to solve the TSP problem by
 *  checking all possible travel plans and choosing the
 *  smallest one found.
 *  
 *  @author Katherine (Raven) Russell and YOUR_NAME_HERE
 */
class TSPPermutations extends TSPAlg {
	//*********************************************************************
	//*******                                                       *******
	//*******   You should not need to edit anything in this part.  *******
	//*******   However, you may need to read everything here.      *******
	//*******                                                       *******
	//*********************************************************************
	
	/**
	 *  A iterator over all permutations of cities (except
	 *  the first city which is "locked in").
	 */
	Iterator<List<City>> permutationItr;
	
	/**
	 *  The current permutation being examined by the algorithm.
	 */
	List<City> currentPermutation = null;
	
	/**
	 *  {@inheritDoc}
	 */
	public void reset(Graph<City, Flight> graph) {
		super.reset(graph);
		permutationItr = null;
		currentPermutation = null;
	}
	
	/**
	 *  {@inheritDoc}
	 */
	public void start() {
		super.start();
	}
	
	/**
	 *  {@inheritDoc}
	 */
	public void finish() {
		System.out.println("TSP Via Permutation:");
		if(visitOrder != null) {
			this.visitOrder.add(0, this.startingCity);
		}
		super.finish();
	}
	
	//*********************************************************************
	//*******                                                       *******
	//*******    You have a number of things to do in this part.    *******
	//*******                                                       *******
	//*********************************************************************

	/**
	 *  Get the next permutation. Note that a permutation does
	 *  NOT include the starting city since this should be "locked
	 *  in place". If you include the starting city in the permuations
	 *  you end up examining "rotations" of trips you have already
	 *  looked at.
	 *  
	 *  @return the next permutation of cities (other than the starting city)
	 */
	public List<City> permutate() {
		//YOUR CODE HERE
		// (1) get all the vertices in the graph
		if(permutationItr == null){
			ArrayList<City> vertices = new ArrayList<City>();
			for(City city: graph.getVertices()){
				vertices.add(city);
			}
			vertices.remove(startingCity);
			permutationItr = new PermutationIterator<>(vertices);
		}
		while(permutationItr.hasNext()){
			return permutationItr.next();
		}
		return null;
	}
	
	/**
	 *  {@inheritDoc}
	 */
	public boolean setupNextStep() {
		//YOUR CODE HERE
		currentPermutation = permutate();
		if(currentPermutation == null){
			return false;
		}
		return true;
	}
	
	/**
	 *  {@inheritDoc}
	 */
	public void doNextStep() {
		//YOUR CODE HERE
		double best_cost = Double.MAX_VALUE;
		double cost = 0;
		//start from starting city
		cost += (graph.findEdge(startingCity, currentPermutation.get(0))).getCost();

		for(int i = 0; i < currentPermutation.size()-1; i++){
			Flight edge = graph.findEdge(currentPermutation.get(i), currentPermutation.get(i+1));
			edge.setColor(COLOR_ACTIVE_EDGE);
			cost += edge.getCost();


			// at the second last element, we also need to go from last city back to starting city
			if(i == currentPermutation.size()-2){
				cost += (graph.findEdge(currentPermutation.get(currentPermutation.size()-1), startingCity)).getCost();
			}
		}
		if(cost < visitOrderCost){
			visitOrderCost = (int) cost;
			visitOrder = currentPermutation;
		}


		/*
		 * Basic instructions:
		 * - using the current permuation, determine the cost of the route
		 * - if it is better than the best route found so far, store it in
		 * the visit order list and visit order cost (from the parent
		 * TSPAlg class)
		 * - while you're doing this, highlight the edges for the current
		 * permutation
		 * 
		 * Details:
		 * - a permutation is just a list of cities (excluding the starting city)
		 * - to get the cost of a flight between two cities you need to use the graph
		 * - the graph is a JUNG graph so see
		 * http://jung.sourceforge.net/doc/api/edu/uci/ics/jung/graph/Graph.html,
		 * for documentation, especially the parent interface (HyperGraph) methods
		 * - an edge in the graph is a flight, so to find out how to get the cost of
		 * a flight see the Flight class (in Flight.java) and the parent class
		 * GraphEdge (in GraphEdge.java)
		 * - to set the color of an edge in the graph, see methods in those same Flight
		 * and GraphEdge classes (the color should be COLOR_ACTIVE_EDGE from TSPAlg)
		 * - DO NOT FORGET to add in the cost from the start city to the first city in
		 * the permutation AND back again!
		 */
	}
}
