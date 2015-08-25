/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Filter;

import br.uff.ic.provviewer.Edge.Edge;
import br.uff.ic.provviewer.GraphFrame;
import br.uff.ic.provviewer.Utils;
import br.uff.ic.provviewer.Vertex.AgentVertex;
import br.uff.ic.provviewer.Vertex.EntityVertex;
import br.uff.ic.provviewer.Vertex.Vertex;
import edu.uci.ics.jung.algorithms.filters.EdgePredicateFilter;
import edu.uci.ics.jung.algorithms.filters.Filter;
import edu.uci.ics.jung.algorithms.filters.VertexPredicateFilter;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import java.util.List;
import org.apache.commons.collections15.Predicate;

/**
 * Class to filter information
 *
 * @author Kohwalter
 */
public class Filters {

    /**
     * Variable FilteredGraph
     */
    public DirectedGraph<Object, Edge> filteredGraph;
    /**
     * Variable EdgeFilter
     */
    public Filter<Object, Edge> EdgeFilter;
    /**
     * Variable VertexFilter
     */
    public Filter<Object, Edge> VertexFilter;

    /**
     * Initialize filters
     */
    public void FilterInit() {
        //All vertices are visiable
        VertexFilter = new VertexPredicateFilter<Object, Edge>(new Predicate<Object>() {
            @Override
            public boolean evaluate(Object vertex) {
                return true;
            }
        });
        //All edges are visible
        EdgeFilter = new EdgePredicateFilter<Object, Edge>(new Predicate<Edge>() {
            @Override
            public boolean evaluate(Edge edge) {
                return true;
            }
        });
    }

    /**
     * Method to use filters (Vertex and Edge)
     *
     * @param view VisualizationViewer<Object, Edge> view
     * @param layout Layout<Object, Edge> layout
     * @param collapsedGraph DirectedGraph<Object,Edge> collapsedGraph
     * @param hiddenEdges Boolean (filter original edges that composes a
     * collapsed one or not?)
     */
    public void Filter(VisualizationViewer<Object, Edge> view,
            Layout<Object, Edge> layout,
            DirectedGraph<Object, Edge> collapsedGraph,
            boolean hiddenEdges) {
        filteredGraph = collapsedGraph;

        EdgeFilter = FilterEdges(hiddenEdges);
        VertexFilter = FilterVertex();

        filteredGraph = (DirectedGraph<Object, Edge>) EdgeFilter.transform(filteredGraph);
        filteredGraph = (DirectedGraph<Object, Edge>) VertexFilter.transform(filteredGraph);
        layout.setGraph(filteredGraph);
        view.repaint();
    }

    /**
     * Method for filtering edges
     *
     * @param hiddenEdges Boolean (consider hidden edges or not?)
     * @return new EdgePredicateFilter<Object, Edge>(new Predicate<Edge>()
     */
    public Filter<Object, Edge> FilterEdges(final boolean hiddenEdges) {
        Filter<Object, Edge> filterEdge = new EdgePredicateFilter<Object, Edge>(new Predicate<Edge>() {
            @Override
            public boolean evaluate(Edge edge) {
//                String[] line = edge.toString().split(" ");

                if (hiddenEdges) {
                    if (edge.isHidden()) {
                        return false;
                    }
                }

                List filtersL = GraphFrame.FilterList.getSelectedValuesList();

                boolean returnValue = false;
                for (int i = 0; i < filtersL.size(); i++) {
                    String filter = (String) filtersL.get(i);
                    if (edge.getEdgeInfluence().contains(filter)) {
                        if (!returnValue) {
                            returnValue = true;
                        }
                    }
                    if(filter.equalsIgnoreCase("All Edges"))
                    {
                        return true;
                    }
                }
                if (GraphFrame.FilterEdgeAgentButton.isSelected()) {
                    if (filteredGraph.getDest(edge) instanceof AgentVertex) {
                        return false;
                    }
                }

                return returnValue;
            }
        });
        return filterEdge;
    }

    /**
     * Method for filtering vertices
     *
     * @return new VertexPredicateFilter<Object, Edge>(new Predicate<Object>()
     */
    public Filter<Object, Edge> FilterVertex() {
        Filter<Object, Edge> filterVertex = new VertexPredicateFilter<Object, Edge>(new Predicate<Object>() {
            @Override
            public boolean evaluate(Object vertex) {
                final Graph test = filteredGraph;
                if (GraphFrame.FilterNodeAgentButton.isSelected()) {
                    if (vertex instanceof AgentVertex) {
                        return false;
                    }
                }
                if (GraphFrame.FilterNodeEntityButton.isSelected()) {
                    if (vertex instanceof EntityVertex) {
                        return false;
                    }
                }
                if (GraphFrame.FilterNodeLonelyButton.isSelected()) {
                    if (test.getNeighborCount(vertex) == 0) {
                        return false;
                    }
                }
                if(GraphFrame.TemporalFilterToggle.isSelected())
                {
                    while (vertex instanceof Graph)
                    {
                        vertex = ((Graph)vertex).getVertices().toArray()[0];
                    }
                    if(Utils.tryParseFloat(GraphFrame.FilterVertexMinValue.getText())) {
                        if (((Vertex)vertex).getTime() < Float.parseFloat(GraphFrame.FilterVertexMinValue.getText()))
                            return false;
                    }
                    if(Utils.tryParseFloat(GraphFrame.FilterVertexMaxValue.getText())) {
                        if (((Vertex)vertex).getTime() > Float.parseFloat(GraphFrame.FilterVertexMaxValue.getText()))
                            return false;
                    }
                }
                return true;
            }
        });
        return filterVertex;
    }
}
