package br.uff.ic.provviewer.Layout;

import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.AgentVertex;
import br.uff.ic.utility.graph.EntityVertex;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.Graph;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.List;

/**
 * Template for a temporal graph layout. Lines represent each agent and his
 * activities. Columns represent passage of time
 *
 * @author Kohwalter
 * @param <V> JUNG's V (Vertex) type
 * @param <E> JUNG's E (Edge) type
 */
public class Timeline_Layout<V, E> extends ProvViewerLayout<V, E> {

    public Timeline_Layout(Graph<V, E> g, Variables variables) {
        super(g, variables);
    }

    @Override
    public void reset() {
        doInit();
    }

    @Override
    public void initialize() {
        doInit();
    }

    private Graph<V, E> layout_graph;

    /**
     * Initialize layout
     */
    private void doInit() {
        layout_graph = getGraph();
        //Compute position for all node-types (minus Agent)
        for (V v : layout_graph.getVertices()) {
            calcPositions(v);
        }
        //Check if there are nodes at the same place, if so apply repulsion
//         for(V v3 : layout_graph.getVertices()) 
//         {
//         calcRepulsion(v3);
//         }
    }

    /**
     * Calculate each entity and activity vertex position in the layout
     *
     * @param v Activity or entity vertex
     */
    protected synchronized void calcPositions(V v) {
        Point2D xyd = transform(v);
        double newXPos = 0;
        double newYPos = 0;

        // Use the middle vertex atribute for position
        if (v instanceof Graph) {
            int i = ((Graph) v).getVertexCount();
           
            //Sort vertices by ID
            List sorted = new ArrayList(((Graph) v).getVertices());
            Comparator comparator = new Comparator<Object>() {
                @Override
                public int compare(Object c1, Object c2) {
                    if(!(c1 instanceof Graph) && !(c2 instanceof Graph))
                        return ((Vertex)c1).getID().compareTo(((Vertex)c2).getID());
                    else
                        return 0;
                }
            };
            Collections.sort(sorted, comparator);
//             End sorting;
            Vertex middle;
            Object middleVertex = sorted.toArray()[(int) (i * 0.5)];
            while (middleVertex instanceof Graph) {
                middleVertex = ((Graph)middleVertex).getVertices().toArray()[0];
            }
            middle = (Vertex) middleVertex;
            calcPositions(xyd, ((Vertex) v).getNormalizedTime(), 0);
        }
        else {
            // Use vertex atribute for position
            if (v instanceof AgentVertex) {
                calcPositions(xyd, ((Vertex) v).getNormalizedTime(), 20);
            }
            else if (v instanceof EntityVertex) {
                calcPositions(xyd, ((Vertex) v).getNormalizedTime(), -20);
            }
            else {
                calcPositions(xyd, ((Vertex) v).getNormalizedTime(), 0);
            }
        }
        
                
    }
    
    protected synchronized void calcPositions(Point2D xyd, double t, double newYPos) {
        double time;//.getTime();
        time = Utils.convertTime(variables.config.timeScale, t, variables.selectedTimeScale);
        double newXPos = time;
        xyd.setLocation(newXPos, newYPos);
    }
    
   

    double variation = 1.0;

    //Check if 2 nodes are at the same position, if so add an offset
    /**
     * Method to check if there is any other vertex at the same position of this
     * one (x,y)
     *
     * @param v1 Vertex used to check if there is any other vertex at the same
     * position
     */
    protected synchronized void calcRepulsion(V v1) {
        //Only Process and Artifact types can have the same position, so lets check
        try {
            for (V v2 : layout_graph.getVertices()) {
                //A check to see if we are not comparing him with himself
                if (v1 != v2) {
                    Point2D p1 = transform(v1);
                    Point2D p2 = transform(v2);
                    if (p1 == null || p2 == null) {
                        continue;
                    }
                    //Need to check both X and Y positions
                    if (Equals(p1.getX(), p2.getX()) && Equals(p1.getY(), p2.getY())) {
                        p1.setLocation(p1.getX(), p1.getY() - variation);
                        //p2.setLocation(p2.getX(), p2.getY() + variation);
                        //Need to check again in case another node is at the same new position
                        calcRepulsion(v1);
                    }
                }
            }
        } catch (ConcurrentModificationException cme) {
        }
    }

    private double EPSILON = 1.0;

    /**
     * Check if a and b are equals
     *
     * @param a double
     * @param b double
     * @return if both values are equal
     */
    protected boolean Equals(double a, double b) {
        return Math.abs(a - b) < EPSILON;
    }

    /**
     * This one is an incremental visualization.
     *
     * @return true
     */
    public boolean isIncremental() {
        return true;
    }

    /**
     * Returns true once the current iteration has passed the maximum count,
     * <tt>MAX_ITERATIONS</tt>.
     *
     * @return true
     */
    @Override
    public boolean done() {
//        if (currentIteration > mMaxIterations || temperature < 1.0/max_dimension)
//        {
//            return true;
//        }
//        return false;
        return true;
    }

    @Override
    public void step() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }
}
