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
package br.uff.ic.provviewer.GUI;

import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.graph.Edge;
import static br.uff.ic.provviewer.GraphFrame.StatusFilterBox;
import br.uff.ic.provviewer.Layout.Temporal_Layout;
import br.uff.ic.provviewer.Layout.Timeline_AttributeValue_Layout;
import br.uff.ic.utility.graph.AgentVertex;
import br.uff.ic.provviewer.Vertex.ColorScheme.VertexPainter;
import br.uff.ic.utility.IO.PROVNWriter;
import br.uff.ic.utility.IO.XMLWriter;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.PickedInfo;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import org.apache.commons.collections15.Transformer;

/**
 * Class responsible for implementing each GUI button
 * @author Kohwalter
 */
public class GuiButtons {

    /**
     * Method to exit the application
     */
    public static void Exit() {
        System.exit(0);
    }

    /**
     * Method to expand a previously collapsed vertex
     * @param variables 
     */
    public static void Expand(Variables variables) {
        Collection picked = new HashSet(variables.view.getPickedVertexState().getPicked());
        variables.collapser.Expander(variables, picked);
    }

    /**
     * Method to collapse a set of selected vertices
     * @param variables 
     */
    public static void Collapse(Variables variables) {
        Collection picked = new HashSet(variables.view.getPickedVertexState().getPicked());
        variables.collapser.Collapse(variables, picked, true);
    }

    /**
     * Method to rollback to the original graph
     * @param variables 
     */
    public static void Reset(Variables variables) {
        variables.collapser.ResetGraph(variables);
    }

    /**
     * Method to select the mouse modes (Picking or Transforming)
     * @param mouse
     * @param MouseModes 
     */
    public static void MouseModes(DefaultModalGraphMouse mouse, JComboBox MouseModes) {
        String mode = (String) MouseModes.getSelectedItem();
        if (mode.equalsIgnoreCase("Picking")) {
            mouse.setMode(ModalGraphMouse.Mode.PICKING);
        }
        if (mode.equalsIgnoreCase("Transforming")) {
            mouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        }
    }

    /**
     * Method to collapse all vertices from each agent
     * @param variables 
     */
    public static void CollapseAgent(Variables variables) {
        PickedInfo<Object> picked_state;
        picked_state = variables.view.getPickedVertexState();
        Object node = null;
        //Get the selected node
        for (Object z : variables.layout.getGraph().getVertices()) {
            if (picked_state.isPicked(z)) {
                node = z;
            }
        }
        //Select the node and its neighbors to be collapsed
        if (variables.layout.getGraph().getNeighbors(node) != null) {
            Collection picked = new HashSet();
            for(Object v : variables.layout.getGraph().getNeighbors(node)) {
                if(v instanceof Graph) {
                    boolean hasAgent = false;
                    for (Object g : ((Graph) v).getVertices()) {
                        if(g instanceof AgentVertex) {
                            hasAgent = true;
                        }
                    }
                    if(!hasAgent)
                        picked.add(v);
                }
                else if(!(v instanceof AgentVertex))
                    picked.add(v);
            }
//            Collection picked = new HashSet(variables.layout.getGraph().getNeighbors(node));
            picked.add(node);
            if (!(node instanceof AgentVertex)) {
                picked.removeAll(picked);
            }
            variables.collapser.Collapse(variables, picked, true);
        }
    }

    /**
     * Method to apply edge filters
     * @param variables 
     */
    public static void Filter(Variables variables) {
        variables.filter.Filters(variables);
    }

    /**
     * Method to define the overall edge arrow format
     * @param EdgeLineShapeSelection
     * @param variables 
     */
    public static void EdgeLineMode(JComboBox EdgeLineShapeSelection, Variables variables) {
        String mode = (String) EdgeLineShapeSelection.getSelectedItem();
        if (mode.equalsIgnoreCase("QuadCurve")) {
            variables.view.getRenderContext().setEdgeShapeTransformer(new EdgeShape.QuadCurve<Object, Edge>());
        }
        if (mode.equalsIgnoreCase("Line")) {
            variables.view.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Object, Edge>());
        }
        variables.view.repaint();
    }

    /**
     * Method to change the vertex coloring according to the selected attribute
     * @param variables 
     */
    public static void StatusFilter(Variables variables) {
        VertexPainter.VertexPainter((String) StatusFilterBox.getSelectedItem(), variables.view, variables);
        variables.view.repaint();
    }

    /**
     * method to display edge label
     * @param variables
     * @param ShowEdgeTextButton 
     */
    public static void EdgeTextDisplay(Variables variables, Boolean ShowEdgeTextButton) {
        if (ShowEdgeTextButton) {
            variables.view.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<Edge>());
        } else {
            //Show nothing
            variables.view.getRenderContext().setEdgeLabelTransformer(new Transformer<Edge, String>() {

                @Override
                public String transform(Edge i) {
                    return "";
                }
            });
        }
        variables.view.repaint();
    }

    /**
     * Method to change the graph layout
     * @param variables
     * @param Layouts 
     */
    public static void LayoutSelection(Variables variables, JComboBox Layouts) {
        String layout = (String) Layouts.getSelectedItem();
        variables.newLayout(layout);
        variables.guiBackground.InitBackground(variables, Layouts);
        variables.view.setGraphLayout(variables.layout);
        variables.view.repaint();
    }
    
    /**
     * Method to update the layouts that use the vertex's time as X position since the user can alter the timescale
     * @param variables
     * @param Layouts 
     */
    public static void UpdateTemporalLayout(Variables variables, JComboBox Layouts) {
        String layout = (String) Layouts.getSelectedItem();
        if (layout.equalsIgnoreCase(variables.layout_temporal)) {
            variables.layout = new Temporal_Layout<>(variables.layout.getGraph(), variables);
            variables.guiBackground.InitBackground(variables, Layouts);
            variables.view.setGraphLayout(variables.layout);
            variables.view.repaint();
        }
        if (layout.equalsIgnoreCase(variables.layout_timeline_AttributeValue)) {
            variables.layout = new Timeline_AttributeValue_Layout<>(variables.layout.getGraph(), variables);
            variables.guiBackground.InitBackground(variables, Layouts);
            variables.view.setGraphLayout(variables.layout);
            variables.view.repaint();
        }
        
    }

    public static void AutoDetectEdge(Variables variables, boolean selected) {
        if(selected)
            variables.config.DetectEdges(variables.graph.getEdges());
        variables.ComputeEdgeTypeValues();
    }
    
    public static void AutoDetectVertexModes(Variables variables, boolean selected) {
        if(selected)
            variables.config.DetectVertexModes(variables.graph.getVertices());
    }
    
//    public static void AutoDetectVertexLabels(Variables variables, boolean selected) {
//        if(selected)
//            variables.config.DetectVertexAttributeFilterValues(variables.graph.getVertices());
//    }

    public static void VertexLabel(Variables variables, boolean agentLabel, boolean activityLabel, boolean entityLabel, boolean timeLabel, boolean showID) {
        GuiFunctions.VertexLabel(variables, agentLabel, activityLabel, entityLabel, timeLabel, showID);
        variables.view.repaint();
    }

    public static void ExportPROVN(Variables variables) {
//        Collection<Vertex> vertices = new ArrayList<Vertex>();
//        for(Object v : variables.graph.getVertices()) {
//            vertices.add((Vertex)v);
//        }
//        
        PROVNWriter provnWriter = new PROVNWriter(variables.graph.getVertices(), variables.graph.getEdges());
        XMLWriter xmlWriter = new XMLWriter(variables.graph.getVertices(), variables.graph.getEdges());
        XMLWriter xmlWriter_collapsed = new XMLWriter(variables.collapsedGraph.getVertices(), variables.graph.getEdges());
        try {
            provnWriter.saveToProvn("PROVN_Export_Test");
            xmlWriter.saveToXML("XML_Export_Test");
            xmlWriter_collapsed.saveToXML("XML_Collapsed_Export_Test");
        } catch (IOException ex) {
            Logger.getLogger(GuiButtons.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void TemporalLayoutGranularity(String selectedTime, Variables variables, 
            boolean nanoseconds, 
            boolean microseconds, 
            boolean milliseconds, 
            boolean seconds, 
            boolean minutes, 
            boolean hours, 
            boolean days, 
            boolean weeks, 
            JComboBox Layouts, 
            JCheckBoxMenuItem temporalNanosecondsButton, 
            JCheckBoxMenuItem temporalMicrosecondsButton, 
            JCheckBoxMenuItem temporalMillisecondsButton, 
            JCheckBoxMenuItem temporalSecondsButton, 
            JCheckBoxMenuItem temporalMinutesButton, 
            JCheckBoxMenuItem temporalHoursButton, 
            JCheckBoxMenuItem temporalDaysButton, 
            JCheckBoxMenuItem temporalWeeksButton) {
        variables.selectedTimeScale = selectedTime;
        temporalNanosecondsButton.setSelected(nanoseconds);
        temporalMicrosecondsButton.setSelected(microseconds);
        temporalMillisecondsButton.setSelected(milliseconds);
        temporalSecondsButton.setSelected(seconds);
        temporalMinutesButton.setSelected(minutes);
        temporalHoursButton.setSelected(hours);
        temporalDaysButton.setSelected(days);
        temporalWeeksButton.setSelected(weeks);
        
        GuiButtons.UpdateTemporalLayout(variables, Layouts);
    }
    
    public static void derivateScheme(Variables variables, boolean doDerivate) {
        variables.doDerivate = doDerivate;
        StatusFilter(variables);
        
    }
    
    public static void removeOutliersDerivateScheme(Variables variables, boolean removeOutliers) {
        variables.removeDerivateOutliers = removeOutliers;
        variables.changedOutliersOption = true;
        StatusFilter(variables);
        
    }
    
    public static void selectVertexShape(Variables variables, String selectedMode, String attribute) {
        GuiFunctions.VertexShape(variables, selectedMode, attribute);
    }

}
