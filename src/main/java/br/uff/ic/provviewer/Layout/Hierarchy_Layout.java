/*
 * The MIT License
 *
 * Copyright 2017 Kohwalter.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.uff.ic.provviewer.Layout;

import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.AgentVertex;
import br.uff.ic.utility.graph.EntityVertex;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Graph;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Template for a temporal graph layout. Lines represent each agent and his
 * activities. Columns represent passage of time
 *
 * @author Kohwalter
 * @param <V> JUNG's V (Vertex) type
 * @param <E> JUNG's E (Edge) type
 */
public class Hierarchy_Layout<V, E> extends ProvViewerLayout<V, E> {

    private List<V> vertex_ordered_list;
    private List<V> entity_ordered_list;
    private DirectedGraph<V, E> graph;

    public Hierarchy_Layout(Graph<V, E> g, Variables variables) {
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

    public void setVertexOrder(Comparator<V> comparator) {
        if (vertex_ordered_list == null) {
//            vertex_ordered_list = new ArrayList<V>(getGraph().getVertices());
            vertex_ordered_list = new ArrayList<>();
            entity_ordered_list = new ArrayList<>();
            for (V v : getGraph().getVertices()) {
                if(v instanceof EntityVertex) {
                    entity_ordered_list.add(v);
                }
                else
                    vertex_ordered_list.add(v);
            }
        }
        Collections.sort(vertex_ordered_list, comparator);
        Collections.sort(entity_ordered_list, comparator);
    }

    private Graph<V, E> layout_graph;

    /**
     * Initialize layout
     */
    private void doInit() {
        Comparator comparator = new Comparator<Object>() {
            @Override
            public int compare(Object c1, Object c2) {
                if (!(c1 instanceof Graph) && !(c2 instanceof Graph)) {
                    double c1t = ((Vertex) c1).getTime();
                    double c2t = ((Vertex) c2).getTime();
                    if (c1t != c2t) {
                        return Double.compare(c1t, c2t);
                    } else {
                        return ((Vertex) c1).getNodeType().compareTo(((Vertex) c2).getNodeType());
                    }
                    //TODO make agent lose priority to appear after the activity
                } else {
                    return 0;
                }
            }
        };
        setVertexOrder(comparator);
        graph = (DirectedGraph<V, E>) variables.graph;
        int i = 0;
        int agentY = 0;
        double yPos = 0;
        double xPos = 0;
        int entityXPos = (int) (vertex_ordered_list.size() * 0.5 - (entity_ordered_list.size() * 0.5));
        int scale = 2 * variables.config.vertexSize;
        entityXPos = entityXPos * scale;
        for (V v : vertex_ordered_list) {
            Point2D coord = transform(v);
            if (v instanceof AgentVertex) {
                yPos = agentY;
                agentY = agentY + 2 * scale;
//                i = i + scale;
                xPos = i - scale;
            } else if (v instanceof ActivityVertex) {
                if (graph.getOutEdges(v) != null) {
                    for (E neighbor : graph.getOutEdges(v)) {
                        //if the edge link to an Agent-node
                        if (graph.getDest(neighbor) instanceof AgentVertex) {
                            Point2D agentPos = transform(graph.getDest(neighbor));
                            yPos = agentPos.getY() + scale;
                        }
                        
                        xPos = i;
                        i = i + scale;
                    }
                }
            } else {
                yPos = 0;
                i = i + scale;
                xPos = i;
            }
            coord.setLocation(xPos, yPos);
        }
        for(V v : entity_ordered_list) {
            Point2D coord = transform(v);
            // Position then in the middle
            xPos = entityXPos;
            entityXPos = entityXPos + scale;
            yPos = -10 * scale;
            coord.setLocation(xPos, yPos);
        }
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
