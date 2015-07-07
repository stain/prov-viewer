/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.GUI;

import br.uff.ic.provviewer.Collapser;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.provviewer.Edge.Edge;
import br.uff.ic.provviewer.Filter.Filters;
import static br.uff.ic.provviewer.GraphFrame.StatusFilterBox;
import br.uff.ic.provviewer.Layout.Spatial_Layout;
import br.uff.ic.provviewer.Layout.Temporal_Layout;
import br.uff.ic.provviewer.Vertex.AgentVertex;
import br.uff.ic.provviewer.Vertex.ColorScheme.VertexPainter;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.PickedInfo;
import java.util.Collection;
import java.util.HashSet;
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
            Collection picked = new HashSet(variables.layout.getGraph().getNeighbors(node));
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
        variables.collapser.Filters(variables);
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
        if (layout.equalsIgnoreCase("CircleLayout")) {
            variables.layout = new CircleLayout<Object, Edge>(variables.layout.getGraph());
        }
        if (layout.equalsIgnoreCase("FRLayout")) {
            variables.layout = new FRLayout<Object, Edge>(variables.layout.getGraph());
        }
        if (layout.equalsIgnoreCase("FRLayout2")) {
            variables.layout = new FRLayout2<Object, Edge>(variables.layout.getGraph());
        }
        if (layout.equalsIgnoreCase("TemporalLayout")) {
            variables.layout = new Temporal_Layout<Object, Edge>(variables.layout.getGraph(), variables);
        }
        if (layout.equalsIgnoreCase("SpatialLayout")) {
            variables.layout = new Spatial_Layout<Object, Edge>(variables.layout.getGraph(), variables);
        }
        if (layout.equalsIgnoreCase("ISOMLayout")) {
            variables.layout = new ISOMLayout<Object, Edge>(variables.layout.getGraph());
        }
        if (layout.equalsIgnoreCase("KKLayout")) {
            variables.layout = new KKLayout<Object, Edge>(variables.layout.getGraph());
        }
        GuiBackground.InitBackground(variables, Layouts);
        variables.view.setGraphLayout(variables.layout);
        variables.view.repaint();
    }
}