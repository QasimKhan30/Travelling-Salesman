import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;

import edu.uci.ics.jung.algorithms.generators.random.ErdosRenyiGenerator;
import edu.uci.ics.jung.algorithms.generators.random.ErdosRenyiGeneratorDirected;

import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;

import java.util.*;

import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.FlowLayout;

import java.awt.geom.Ellipse2D;
import java.awt.Rectangle;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.ToolTipManager;
import javax.swing.JOptionPane;

/**
 *  GUI for the simulation.
 *  
 *  @author Katherine (Raven) Russell
 */
class SimGUI {
	/**
	 *  Frame for the GUI.
	 */
	private JFrame frame;
	
	/**
	 *  Current simulation.
	 */
	private FourEightyThreeAlg<City,Flight> alg = null;
	
	/**
	 *  Current simulation.
	 */
	private FourEightyThreeAlg<City,Flight> alg2 = null;
	
	/**
	 *  The panel containing the graph display.
	 */
	private Graph<City,Flight> graph = null;
	
	/**
	 *  The panel containing the graph display.
	 */
	private Graph<City,Flight> graph2 = null;
	
	/**
	 *  The panel containing the graph display.
	 */
	private VisualizationViewer<City, Flight> visServer = null;
	
	/**
	 *  The panel containing the graph display.
	 */
	private VisualizationViewer<City, Flight> visServer2 = null;
	
	/**
	 *  Editing model for mouse.
	 */
	private EditingModalGraphMouse<City, Flight> gm;
	
	/**
	 *  The panel containing the step, reset, and play buttons.
	 */
	private JPanel buttonPanel = null;
	
	/**
	 *  The panel containing the step, reset, and play buttons.
	 */
	private JPanel graphPanel = null;
	
	/**
	 *  Whether or not we are currently routing.
	 */
	private boolean routing = false;
	
	/**
	 *  Whether or not a simulation is currently playing with
	 *  the play button (i.e. automatically playing).
	 */
	private boolean playing = false;
	
	/**
	 *  The seed to use for the random number generator
	 *  associated with the algorithm simulation.
	 */
	private final Random rand;
	
	/**
	 *  The number of nodes in the simulation.
	 */
	private final int numNodes;
	
	/**
	 *  The size of one graph panel.
	 */
	private static final int GRAPH_DIM = 600;
	
	/**
	 *  The number of milliseconds to wait in between
	 *  calls to step.
	 */
	private int msWait = 100;
	
	/**
	 *  Load up the GUI.
	 *  
	 *  @param numNodes the number of nodes for the simulation
	 *  @param prob the probability of a connection between two nodes
	 *  @param seed seed for the random number generator in Alg
	 */
	public SimGUI(int numNodes, int seed, int msWait) {
		this.rand = new Random(seed);
		this.numNodes = numNodes;
		this.msWait = msWait;
		
		frame = new JFrame("Algorithm Simulation");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(GRAPH_DIM*2, 700);
		frame.getContentPane().setLayout(new FlowLayout());
		
		resetAlg();
		makeMenu(); //needs to go after so gm is set
		
		frame.setVisible(true);
	}
	
	/**
	 *  Makes the menu for the simulation.
	 */
	public void makeMenu() {
		frame.setJMenuBar(null);
		JMenuBar menuBar = new JMenuBar();
		
		//exit option
		JMenu simMenu = new JMenu("Simulation");
		simMenu.setPreferredSize(new Dimension(80,20)); // Change the size 
		
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		simMenu.add(exit);
		menuBar.add(simMenu);
		
		frame.setJMenuBar(menuBar);
	}
	
	/**
	 *  Makes both graphs for the graph panel.
	 */
	public void makeGraphPanel() {
		if(graphPanel != null) frame.remove(graphPanel);
		graphPanel = new JPanel();
		graphPanel.setLayout(new GridLayout(1, 2));
		visServer = makeGraphPanel(graph);
		visServer2 = makeGraphPanel(graph2);
		frame.add(graphPanel);
		frame.revalidate();
	}

	/**
	 *  Makes the graph components.
	 */
	public VisualizationViewer<City, Flight> makeGraphPanel(Graph<City,Flight> graphToMake) {
		if(alg == null) return null;
		
		Layout<City, Flight> layout = new StaticLayout<>(graphToMake, new Dimension(GRAPH_DIM,GRAPH_DIM));
		for(City c : graphToMake.getVertices()) {
			layout.setLocation(c, c.getLoc());
		}
		VisualizationViewer<City, Flight> serverToUse = new VisualizationViewer<City, Flight>(layout);
		serverToUse.setPreferredSize(new Dimension(GRAPH_DIM,GRAPH_DIM));
		
		serverToUse.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		RenderContext<City, Flight> context = serverToUse.getRenderContext();
		
		//label edges with toString()
		context.setEdgeLabelTransformer(
			new Transformer<Flight,String>(){
				/**
				 *  Transforms the connection into a string.
				 *  @param e the connection to print
				 *  @return string representation
				 */
				public String transform(Flight e) {
					return e.toString();
				}
			}
		);
		
		//color arrows with edge color
		context.setArrowFillPaintTransformer(
			new Transformer<Flight,Paint>() {
				/**
				 *  Transforms the connection into a color.
				 *  @param e the connection to print
				 *  @return color representation
				 */
				public Paint transform(Flight e) {
					return e.getColor();
				}
			}
		);
		
		//color lines with edge color
		context.setEdgeDrawPaintTransformer(
			new Transformer<Flight,Paint>() {
				/**
				 *  Transforms the connection into a color.
				 *  @param e the connection to print
				 *  @return color representation
				 */
				public Paint transform(Flight e) {
					return e.getColor();
				}
			}
		);
		
		//set edge line stroke to bolder
		context.setEdgeStrokeTransformer(
			new Transformer<Flight,Stroke>() {
				/**
				 *  Transforms the connection into a line.
				 *  @param e the connection to print
				 *  @return line representation
				 */
				public Stroke transform(Flight e) {
					return new BasicStroke(2);
				}
			}
		);
		
		//move edge labels off the lines
		context.setLabelOffset(-5);
		
		//make nodes bigger
		context.setVertexShapeTransformer(
			new Transformer<City,Shape>() {
				/**
				 *  Transforms the host into a shape.
				 *  @param v the host to print
				 *  @return shape representation
				 */
				public Shape transform(City v) {
					//int s = 30;
					//return new Ellipse2D.Double(-s/2.0, -s/2.0, s, s);
					int w = 80;
					int h = 20;
					return new Rectangle(-w/2, -h/2, w, h);
				}
			}
		);
		
		//label vertices with toString()
		context.setVertexLabelTransformer(
			new Transformer<City,String>() {
				/**
				 *  Transforms the host into a string.
				 *  @param v the host to transform
				 *  @return string representation
				 */
				public String transform(City v) {
					return v.toString();
				}
			}
		);
		
		//color vertices with node color
		context.setVertexFillPaintTransformer(
			new Transformer<City,Paint>() {
				/**
				 *  Transforms the host into a color.
				 *  @param v the host to transform
				 *  @return color representation
				 */
				public Paint transform(City v) {
					return v.getColor();
				}
			}
		);
		
		//deal with tooltips...
		ToolTipManager.sharedInstance().setDismissDelay(60000);
		serverToUse.setVertexToolTipTransformer(
			new Transformer<City,String>() {
				/**
				 *  Transforms the host into a string for a tooltip.
				 *  @param v the host to transform
				 *  @return string representation
				 */
				public String transform(City v) {
					return v.toString();
				}
			}
		);
		
		//Add user interactions
		gm = new EditingModalGraphMouse<>(context, City.getFactory(), Flight.getFactory()) {
			/**
			 *  Hack to prevent actually editing the graph for this project!
			 */
			public void mouseReleased(MouseEvent e) {
				if(e.getButton() != MouseEvent.BUTTON2) {
					super.mouseClicked(e);
				}
			}
		};
		gm.setMode(ModalGraphMouse.Mode.PICKING);
		serverToUse.setGraphMouse(gm);
		graphPanel.add(serverToUse, 0);
		
		return serverToUse;
	}
	
	/**
	 *  Makes the panel containing the step, reset, and play buttons.
	 */
	public void makeBottomButtons() {
		if(alg == null || alg2 == null) return;
		if(buttonPanel != null) frame.remove(buttonPanel);
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 2));
		
		//step button
		JButton step = new JButton("Step");
		step.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				step();
			}
		});
		buttonPanel.add(step);
		
		//reset button
		JButton reset = new JButton("Reset");
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				resetAlg();
			}
		});
		buttonPanel.add(reset);
		
		//play button
		JButton play = new JButton("Play");
		play.addActionListener(new ActionListener() {
			private void toggle() {
				//toggle playing and not playing
				playing = !playing;
				buttonPanel.getComponent(0).setEnabled(!playing);
				buttonPanel.getComponent(1).setEnabled(!playing);
				((JButton)buttonPanel.getComponent(2)).setText((playing ? "Stop" : "Play"));
			}
			
			public void actionPerformed(ActionEvent event) {
				toggle();
				
				//if playing, kick off a timer
				if(playing) {
					new javax.swing.Timer(msWait, new ActionListener() {
						public void actionPerformed(ActionEvent event) {
							//someone hit the stop button
							if(!playing) {
								((javax.swing.Timer)event.getSource()).stop();
								return;
							}
							else {
								if(!step()) toggle();
							}
						}
					}).start();
				}
			}
		});
		buttonPanel.add(play);
		
		frame.add(buttonPanel, 1);
		frame.revalidate();
	}
	
	/**
	 *  Calls the step button on the simulation and updates
	 *  the GUI to display the result.
	 *  
	 *  @param source the source for the message
	 *  @param dest the destination for the message
	 *  @return whether or not the simulation was able to step
	 */
	public boolean step() {
		boolean ret = alg.step();
		ret = (alg2.step() || ret);
		visServer.repaint();
		visServer2.repaint();
		return ret;
	}
	
	/**
	 *  Starts the simulation.
	 */
	public void start() {
		alg.start();
		alg2.start();
		visServer.repaint();
		visServer2.repaint();
	}
	
	/**
	 *  Picks a point in the grid to place a city (helper
	 *  for genGraph()).
	 *  
	 *  @param grid the grid of "used" parts
	 *  @return a new point on the graph panel display
	 */
	public Point genPoint(boolean[][] grid) {
		//don't use the edge of the space
		int buffer = 50;
		
		//how much space each square of the grid has
		int validLoc = (GRAPH_DIM-(buffer*2))/grid.length;
		
		//pick an unused area of the grid
		int square = Math.abs(this.rand.nextInt()) % (grid.length*grid.length);
		int gridX = square/grid.length;
		int gridY = square%grid.length;
		while(grid[gridX][gridY]) {
			square = Math.abs(this.rand.nextInt()) % (grid.length*grid.length);
			gridX = square/grid.length;
			gridY = square%grid.length;
		}
		
		//mark it as used
		grid[gridX][gridY] = true;
		
		int x = buffer + (gridX*validLoc) + (Math.abs(this.rand.nextInt())%validLoc);
		int y = buffer + (gridY*validLoc) + (Math.abs(this.rand.nextInt())%validLoc);
		
		return new Point(x, y);
	}
	
	/**
	 *  Generates a new graph.
	 */
	public void genGraph() {
		//only one city in each part of the world to space them out
		boolean[][] grid = new boolean[4][4];
		
		//factories for generating new cities and flights
		Factory<City> nodeFactory = City.getFactory();
		Factory<Flight> edgeFactory = Flight.getFactory();
		
		//the actual graphs
		graph = new UndirectedSparseGraph<>();
		graph2 = new UndirectedSparseGraph<>();
		
		//generate each city and it's location
		for(int i = 0; i < this.numNodes; i++) {
			City c = nodeFactory.create();
			c.setLoc(genPoint(grid));
			graph.addVertex(c);
			graph2.addVertex(c);
		}
		
		//add in flights between cities
		for(City c1 : graph.getVertices()) {
			for(City c2 : graph.getVertices()) {
				if(!c1.equals(c2)) {
					int cost = (int) c1.getLoc().distance(c2.getLoc());
					
					Flight f = edgeFactory.create();
					f.setCost(cost);
					graph.addEdge(f, c1, c2);
					
					f = edgeFactory.create();
					f.setCost(cost);
					graph2.addEdge(f, c1, c2);
				}
			}
		}
		
	}
	
	/**
	 *  Load a new simulation.
	 */
	public void resetAlg() {
		if(alg == null) alg = new TSPPermutations();
		if(alg2 == null) alg2 = new TSPApprox();
		
		City.LAST_ID = -1;
		Flight.LAST_ID = -1;
		genGraph();
		alg.reset(graph);
		alg2.reset(graph2);
		
		makeGraphPanel();
		makeMenu();
		makeBottomButtons();
	}
	
	/**
	 *  A main method to run the simulation with GUI.
	 *  
	 *  @param args [0] = the seed for the alg's random number generator
	 */
	public static void main(String[] args) {
		if(args.length == 0) {
			new SimGUI(5,0,100);
			return;
		}
		
		int numNodes = Math.max(2,Math.min(10,Integer.parseInt(args[0])));
		if(args.length == 1) {
			new SimGUI(numNodes,0,100);
			return;
		}
		
		int seed = Math.min(0,Integer.parseInt(args[1]));
		if(args.length == 2) {
			new SimGUI(numNodes,seed,100);
			return;
		}
		
		int msWait = Math.max(1,Integer.parseInt(args[2]));
		if(args.length == 3) {
			new SimGUI(numNodes,seed,msWait);
			return;
		}
		
		System.out.println("Call with one of the following:\njava SIMGui\njava SIMGui [numNodes]\njava SIMGui [numNodes] [seed]\njava SIMGui [numNodes] [seed] [waitInMS]");
	}
}