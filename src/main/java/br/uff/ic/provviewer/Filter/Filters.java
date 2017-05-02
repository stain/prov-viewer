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
package br.uff.ic.provviewer.Filter;

import br.uff.ic.utility.graph.Edge;
import br.uff.ic.provviewer.GraphFrame;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.AgentVertex;
import br.uff.ic.utility.graph.EntityVertex;
import br.uff.ic.utility.graph.Vertex;
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
//    public void FilterInit() {
    public void Filters() {
        //All vertices are visiable
        VertexFilter = new VertexPredicateFilter<>(new Predicate<Object>() {
            @Override
            public boolean evaluate(Object vertex) {
                return true;
            }
        });
        //All edges are visible
        EdgeFilter = new EdgePredicateFilter<>(new Predicate<Edge>() {
            @Override
            public boolean evaluate(Edge edge) {
                return true;
            }
        });
    }
    
    /**
     * Method for filtering the Graph
     *
     * @param variables Variables type
     * @param hiddenEdges Boolean used to decide if hidden (original edges that
     * composes the collapsed edge) edges will be filtered
     */
    public void Filters(Variables variables, boolean hiddenEdges) {
        variables.filter.filterVerticesAndEdges(variables.view,
                variables.layout,
                variables.collapsedGraph,
                hiddenEdges, variables.config.timeScale, variables.selectedTimeScale);
    }

    /**
     * Overload of the Filters method. hiddenEdges is always set as true,
     * filtering the edges that composes the collapsed one
     *
     * @param variables Variables type
     */
    public void Filters(Variables variables) {
        Filters(variables, true);
    }
    
    /**
     * Method to apply filters after an operation
     *
     * @param variables
     */
    public void AddFilters(Variables variables) {
        GraphFrame.edgeFilterList.setSelectionInterval(0, variables.config.edgetype.size() - 1);
        GraphFrame.vertexFilterList.setSelectionInterval(0, variables.config.vertexLabelFilter.size() - 1);
        Filters(variables, false);
    }

    /**
     * Method to remove filters before an operation, avoiding the loss of
     * information
     *
     * @param variables
     */
    public void RemoveFilters(Variables variables) {
        GraphFrame.edgeFilterList.setSelectedIndex(0);
        GraphFrame.vertexFilterList.setSelectedIndex(0);
        GraphFrame.FilterEdgeAgentButton.setSelected(false);
        GraphFrame.FilterNodeAgentButton.setSelected(false);
        GraphFrame.FilterNodeEntityButton.setSelected(false);
        GraphFrame.FilterNodeLonelyButton.setSelected(false);
        GraphFrame.TemporalFilterToggle.setSelected(false);
        Filters(variables);
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
    public void filterVerticesAndEdges(VisualizationViewer<Object, Edge> view,
            Layout<Object, Edge> layout,
            DirectedGraph<Object, Edge> collapsedGraph,
            boolean hiddenEdges, String timeScale, String selectedTimeScale) {
        filteredGraph = collapsedGraph;

        EdgeFilter = filterEdges(hiddenEdges);
        VertexFilter = filterVertex(timeScale, selectedTimeScale);

        filteredGraph = (DirectedGraph<Object, Edge>) EdgeFilter.transform(filteredGraph);
        filteredGraph = (DirectedGraph<Object, Edge>) VertexFilter.transform(filteredGraph);
        
        // Filter Lonely Vertices. Had to be seperated otherwise it would not work
        VertexFilter = filterLonelyVertex();
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
    private Filter<Object, Edge> filterEdges(final boolean hiddenEdges) {
        Filter<Object, Edge> filterEdge = new EdgePredicateFilter<>(new Predicate<Edge>() {
            @Override
            public boolean evaluate(Edge edge) {
                if (hiddenEdges) {
                    if (edge.isHidden()) {
                        return false;
                    }
                }
                if (edgeTypeFilter(edge)) {
                    return false;
                }

                return !edgeAgentFilter(edge);
            }
        });
        return filterEdge;
    }

    /**
     * Edge filter to show only edges from the selected type or label
     * @param edge
     * @return if the edge will be hidden
     */
    private boolean edgeTypeFilter(Edge edge) {
        List filtersL = GraphFrame.edgeFilterList.getSelectedValuesList();
        for (Object filtersL1 : filtersL) {
            String filter = (String) filtersL1;
            if (filter.equalsIgnoreCase("All Edges")) {
                return false;
            }
            if (edge.getLabel().contains(filter) || edge.getType().contains(filter)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method to filter edges that connect to agents
     *
     * @param edge
     * @return if the edge will be hidden
     */
    private boolean edgeAgentFilter(Edge edge) {
        if (GraphFrame.FilterEdgeAgentButton.isSelected()) {
            if (filteredGraph.getDest(edge) instanceof AgentVertex) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vertex filter method
     *
     * @return if the vertex will be hidden
     */
    private Filter<Object, Edge> filterVertex(final String timeScale, final String selectedTimeScale) {

        Filter<Object, Edge> filterVertex = new VertexPredicateFilter<>(new Predicate<Object>() {
            @Override
            public boolean evaluate(Object vertex) {
                if (vertexTypeFilter(vertex)) {
                    return false;
                }
//                if (vertexLonelyFilter(vertex)) {
//                    return false;
//                }
                if (vertexAttributeFilter(vertex)) {
                    return false;
                }
                return !vertexTemporalFilter(vertex, timeScale, selectedTimeScale);
            }
        });
        return filterVertex;
    }
    private Filter<Object, Edge> filterLonelyVertex() {

        Filter<Object, Edge> filterVertex = new VertexPredicateFilter<>(new Predicate<Object>() {
            @Override
            public boolean evaluate(Object vertex) {
                return !vertexLonelyFilter(vertex);
            }
        });
        return filterVertex;
    }

    /**
     * Vertex filter for filtering lonely vertices (vertices without edges)
     *
     * @param vertex
     * @return if the vertex will be hidden
     */
    private boolean vertexLonelyFilter(Object vertex) {
        if (GraphFrame.FilterNodeLonelyButton.isSelected()) {
            final Graph test = filteredGraph;
            if (test.getNeighborCount(vertex) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vertex filter to filter vertices of the selected type
     *
     * @param vertex
     * @return if the vertex will be hidden
     */
    private boolean vertexTypeFilter(Object vertex) {
        if (GraphFrame.FilterNodeAgentButton.isSelected()) {
            if (vertex instanceof AgentVertex) {
                return true;
            }
        }
        if (GraphFrame.FilterNodeEntityButton.isSelected()) {
            if (vertex instanceof EntityVertex) {
                return true;
            }
        }
        return false;
    }
    
    private boolean vertexAttributeFilter(Object vertex) {
        List filtersL = GraphFrame.vertexFilterList.getSelectedValuesList();
        for (Object filtersL1 : filtersL) {
            String filter = (String) filtersL1;
            if (filter.equalsIgnoreCase("All Vertices")) {
                return false;
            }
            if(vertex instanceof Vertex) {
                String name;
                String value;
                name = filter.split(": ")[0];
                value = filter.split(": ")[1];
                if (((Vertex)vertex).getAttributeValue(name).equalsIgnoreCase(value)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Vertex filter to filter vertices that is outside the desired temporal
     * range
     *
     * @param vertex
     * @return if the vertex will be hidden
     */
    private boolean vertexTemporalFilter(Object vertex, String timeScale, String selectedTimeScale) {
        if (GraphFrame.TemporalFilterToggle.isSelected()) {
            if (!(vertex instanceof AgentVertex)) {
                while (vertex instanceof Graph) {
                    vertex = ((Graph) vertex).getVertices().toArray()[0];
                }
                double timeDate = ((Vertex) vertex).getTime();
                double time = ((Vertex) vertex).getNormalizedTime();

                time = Utils.convertTime(timeScale, time, selectedTimeScale);

                if (Utils.tryParseFloat(GraphFrame.FilterVertexMinValue.getText())) {
                    double minTime = Float.parseFloat(GraphFrame.FilterVertexMinValue.getText());
                    if (time < minTime) {
                        return true;
                    }
                } else if (Utils.tryParseDate(GraphFrame.FilterVertexMinValue.getText())) {
                    double minTime = Utils.convertStringDateToFloat(GraphFrame.FilterVertexMinValue.getText());
                    if (timeDate < minTime) {
                        return true;
                    }
                }
                if (Utils.tryParseFloat(GraphFrame.FilterVertexMaxValue.getText())) {
                    double maxTime;
                    maxTime = Float.parseFloat(GraphFrame.FilterVertexMaxValue.getText());
                    if (time > maxTime) {
                        return true;
                    }
                } else if (Utils.tryParseDate(GraphFrame.FilterVertexMaxValue.getText())) {
                    double maxTime = Utils.convertStringDateToFloat(GraphFrame.FilterVertexMaxValue.getText());
                    if (timeDate > maxTime) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
