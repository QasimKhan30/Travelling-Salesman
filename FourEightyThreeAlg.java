//TODO: Nothing, all done.

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;

/**
 *  Interface for graph algorithms in the simulator.
 *  
 *  @author Katherine (Raven) Russell
 */
interface FourEightyThreeAlg<V extends GraphNode,E extends GraphEdge> {
	/**
	 *  Indicates the edge type required by the algotithm.
	 *  
	 *  @return the edge type expected by the algorithm
	 */
	public EdgeType graphEdgeType();
	
	/**
	 *  What to do before the simulator begins.
	 */
	public void start();
	
	/**
	 *  What to do when the simulator is stepped.
	 *  
	 *  @return whether or not there are more steps.
	 */
	public boolean step();
	
	/**
	 *  What to do after the simulator finishes all steps.
	 */
	public void finish();
	
	/**
	 *  What to do to clean up after the last step performed.
	 */
	public void cleanUpLastStep();
	
	/**
	 *  What to do before the next step is performed.
	 *  
	 *  @return false if there is no next step to perform
	 */
	public boolean setupNextStep();
	
	/**
	 *  Actually execute the next step.
	 */
	public void doNextStep();
	
	/**
	 *  Resets the algorithm for a new graph.
	 *  
	 *  @param graph the new graph
	 */
	public void reset(Graph<V,E> graph);
}