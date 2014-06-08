package br.uff.ic.provviewer;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.NoMoreSolutionException;
import br.uff.ic.provviewer.Inference.PrologInference;
import br.uff.ic.provviewer.Edge.Edge;
import br.uff.ic.provviewer.Filter.Filters;
import br.uff.ic.provviewer.Filter.PreFilters;
import br.uff.ic.provviewer.Input.Config;
import br.uff.ic.provviewer.Input.XMLReader;
import br.uff.ic.provviewer.Layout.Temporal_Layout;
import br.uff.ic.provviewer.Stroke.EdgeStroke;
import br.uff.ic.provviewer.Stroke.VertexStroke;
import br.uff.ic.provviewer.Vertex.AgentVertex;
import br.uff.ic.provviewer.Vertex.ColorScheme.VertexPainter;
import br.uff.ic.provviewer.Vertex.EntityVertex;
import br.uff.ic.provviewer.Vertex.Vertex;
import br.uff.ic.provviewer.Vertex.VertexShape;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.PickedInfo;
import edu.uci.ics.jung.visualization.subLayout.GraphCollapser;
import edu.uci.ics.jung.visualization.util.PredicatedParallelEdgeIndexFunction;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;

/**
 * Prov Viewer GUI. Can be used as a Template.
 * @author kohwalter
 */
public class GraphFrame extends javax.swing.JFrame {
    final Set exclusions = new HashSet();
    
//    VisualizationViewer<Object, Edge> view;
//    Layout<Object, Edge> layout;
//    GraphCollapser gCollapser;
//    static DirectedGraph<Object,Edge> graph;
//    DirectedGraph<Object,Edge> collapsedGraph;
    
    DefaultModalGraphMouse mouse = new DefaultModalGraphMouse();
    boolean filterCredits = false;

    Variables variables = new Variables();
    Collapser collapser = new Collapser();
    Filters filter = new Filters();
    
    PrologInference testProlog = new PrologInference();

    /**
     * Creates new form GraphFrame
     * @param graph 
     */
    public GraphFrame(DirectedGraph<Object, Edge> graph) {
        initComponents();
//        Layouts.setSelectedItem("TemporalLayout");
        initGraphComponent(graph);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        CollapseAgent = new javax.swing.JButton();
        Reset = new javax.swing.JButton();
        Expand = new javax.swing.JButton();
        Collapse = new javax.swing.JButton();
        MouseModes = new javax.swing.JComboBox();
        FilterNodeAgentButton = new javax.swing.JCheckBox();
        FilterNodeLonelyButton = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        EdgeLineShapeSelection = new javax.swing.JComboBox();
        StatusFilterBox = new javax.swing.JComboBox();
        ShowEdgeTextButton = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        FilterList = new javax.swing.JList();
        Layouts = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Prov Viewer");

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        CollapseAgent.setText("CollapseAgent");
        CollapseAgent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CollapseAgentActionPerformed(evt);
            }
        });

        Reset.setText("Reset");
        Reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ResetActionPerformed(evt);
            }
        });

        Expand.setText("Expand");
        Expand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExpandActionPerformed(evt);
            }
        });

        Collapse.setText("Collapse");
        Collapse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CollapseActionPerformed(evt);
            }
        });

        MouseModes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Transforming", "Picking" }));
        MouseModes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MouseModesActionPerformed(evt);
            }
        });

        FilterNodeAgentButton.setText("Agents Vertices");
        FilterNodeAgentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FilterNodeAgentButtonActionPerformed(evt);
            }
        });

        FilterNodeLonelyButton.setText("Lonely Vertices");
        FilterNodeLonelyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FilterNodeLonelyButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Vertex Filter");

        jLabel2.setText("Display Edge");

        EdgeLineShapeSelection.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "QuadCurve", "Line" }));
        EdgeLineShapeSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EdgeLineShapeSelectionActionPerformed(evt);
            }
        });

        StatusFilterBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Default", "Morale", "Stamina", "Hours", "Weekend", "Credits", "Role" }));
        StatusFilterBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StatusFilterBoxActionPerformed(evt);
            }
        });

        ShowEdgeTextButton.setText("Edge Text");
        ShowEdgeTextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShowEdgeTextButtonActionPerformed(evt);
            }
        });

        jLabel3.setText("Attribute Status");

        jLabel4.setText("Edge Style");

        jLabel5.setText("Mouse Mode");

        FilterList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        FilterList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                FilterListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(FilterList);

        Layouts.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "CircleLayout", "FRLayout", "FRLayout2", "TemporalLayout", "ISOMLayout", "KKLayout" }));
        Layouts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LayoutsActionPerformed(evt);
            }
        });

        jLabel6.setText("Graph Layout");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(131, 131, 131)
                        .addComponent(CollapseAgent))
                    .addComponent(FilterNodeLonelyButton)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(FilterNodeAgentButton)
                        .addGap(18, 18, 18)
                        .addComponent(ShowEdgeTextButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(Collapse)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Expand)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Reset))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(StatusFilterBox, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(Layouts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(MouseModes, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(EdgeLineShapeSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))))
                .addGap(79, 79, 79))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CollapseAgent)
                    .addComponent(jLabel2)
                    .addComponent(Collapse)
                    .addComponent(Expand)
                    .addComponent(Reset))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel1))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(FilterNodeAgentButton)
                                    .addComponent(ShowEdgeTextButton))
                                .addComponent(FilterNodeLonelyButton)))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 1, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addGap(6, 6, 6)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(StatusFilterBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(EdgeLineShapeSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(MouseModes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Layouts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-639)/2, (screenSize.height-738)/2, 639, 738);
    }// </editor-fold>//GEN-END:initComponents
    /**
     * ================================================
     * Expand Button
     * ================================================
     */
    private void ExpandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExpandActionPerformed
        Collection picked = new HashSet(variables.view.getPickedVertexState().getPicked());
        collapser.Expander(variables, filter, picked);
    }//GEN-LAST:event_ExpandActionPerformed
    /**
     * ================================================
     * Collapse Button
     * ================================================
     */
    private void CollapseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CollapseActionPerformed
        //cannot use java generics type declarations with the graph collapser
        Collection picked = new HashSet(variables.view.getPickedVertexState().getPicked());
        collapser.Collapse(variables, filter, picked);
    }//GEN-LAST:event_CollapseActionPerformed
    /**
     * ================================================
     * Reset Button
     * ================================================
     */
    private void ResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ResetActionPerformed
        collapser.ResetGraph(variables, filter);
    }//GEN-LAST:event_ResetActionPerformed
    /**
     * ================================================
     * Select Mouse mode Button
     * ================================================
     */
    private void MouseModesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MouseModesActionPerformed
        String mode = (String)MouseModes.getSelectedItem();
        if(mode.equalsIgnoreCase("Picking"))
        {
             mouse.setMode(ModalGraphMouse.Mode.PICKING);
        }
        if(mode.equalsIgnoreCase("Transforming"))
        {
             mouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        }
    }//GEN-LAST:event_MouseModesActionPerformed
    /**
     * ================================================
     * Collapse Agent's processes
     * ================================================
     */
    private void CollapseAgentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CollapseAgentActionPerformed
        PickedInfo<Object> picked_state;
        picked_state = variables.view.getPickedVertexState();
        Object node = null;
        //Get the selected node
        for(Object z : variables.layout.getGraph().getVertices())
        {
            if (picked_state.isPicked(z))
            {
                node = z;
            }
        }
        //Select the node and its neighbors to be collapsed
        if(variables.layout.getGraph().getNeighbors(node) != null)
        {
            Collection picked = new HashSet(variables.layout.getGraph().getNeighbors(node));
            picked.add(node);
            if(!(node instanceof AgentVertex)) 
            {
                picked.removeAll(picked);
            }   
            collapser.Collapse(variables, filter, picked);
        }
    }//GEN-LAST:event_CollapseAgentActionPerformed

   /**
         * ================================================
         * Filtering agent vertices
         * ================================================
         */
    private void FilterNodeAgentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FilterNodeAgentButtonActionPerformed
        collapser.Filters(variables, filter);
    }//GEN-LAST:event_FilterNodeAgentButtonActionPerformed

   /**
         * ================================================
         * Filtering lonely vertices
         * ================================================
         */
    private void FilterNodeLonelyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FilterNodeLonelyButtonActionPerformed
        collapser.Filters(variables, filter);
    }//GEN-LAST:event_FilterNodeLonelyButtonActionPerformed

   /**
         * ================================================
         * Edge Shape: Make it to be a line instead of quadratic curves
         * ================================================
         */
    private void EdgeLineShapeSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EdgeLineShapeSelectionActionPerformed
        String mode = (String)EdgeLineShapeSelection.getSelectedItem();
        if(mode.equalsIgnoreCase("QuadCurve"))
        {
             variables.view.getRenderContext().setEdgeShapeTransformer(new EdgeShape.QuadCurve<Object,Edge>());
        }
        if(mode.equalsIgnoreCase("Line"))
        {
             variables.view.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Object,Edge>());
        }
        variables.view.repaint();
    }//GEN-LAST:event_EdgeLineShapeSelectionActionPerformed

    /**
         * ================================================
         * Status Filter box
         * ================================================
         */
    private void StatusFilterBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StatusFilterBoxActionPerformed
        collapser.ResetGraph(variables, filter);
        VertexPainter.VertexPainter((String)StatusFilterBox.getSelectedItem(), variables.view, variables);
        variables.view.repaint();
        String list;
        try {
            list = testProlog.QueryCollapse((String)StatusFilterBox.getSelectedItem(), "Neutral");
            collapser.CollapseIrrelevant(variables, filter, list, "Neutral");
        } catch (NoMoreSolutionException ex) {
            Logger.getLogger(GraphFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_StatusFilterBoxActionPerformed
    /**
         * ================================================
         * Show edge text button
         * ================================================
         */
    private void ShowEdgeTextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ShowEdgeTextButtonActionPerformed
        if(ShowEdgeTextButton.isSelected())
        {
           variables.view.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<Edge>()); 
        }
        else
        {
            //Show nothing
            variables.view.getRenderContext().setEdgeLabelTransformer(new Transformer<Edge, String>() {

                @Override
                public String transform(Edge i) {
                    return "";
                }
            });
        }
        variables.view.repaint();
    }//GEN-LAST:event_ShowEdgeTextButtonActionPerformed

   
    private void FilterListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_FilterListValueChanged
        // TODO add your handling code here:
        collapser.Filters(variables, filter);
    }//GEN-LAST:event_FilterListValueChanged

    private void LayoutsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LayoutsActionPerformed
        // TODO add your handling code here:
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
            variables.layout = new Temporal_Layout<Object, Edge>(variables.layout.getGraph());
        }
        if (layout.equalsIgnoreCase("ISOMLayout")) {
            variables.layout = new ISOMLayout<Object, Edge>(variables.layout.getGraph());
        }
        if (layout.equalsIgnoreCase("KKLayout")) {
            variables.layout = new KKLayout<Object, Edge>(variables.layout.getGraph());
        }
        variables.view.setGraphLayout(variables.layout);
        variables.view.repaint();
    }//GEN-LAST:event_LayoutsActionPerformed

    /**
     * ================================================
     * Init Graph Component
     * ================================================
     */
    boolean lay = true;
    private void initGraphComponent(DirectedGraph<Object, Edge> graph) {

        Config.Initialize();
        filter.filteredGraph = graph;
        variables.collapsedGraph = graph;
        filter.FilterInit();
        try {
            //Initialize Prolog
            testProlog.Init();
        } catch (IOException ex) {
            Logger.getLogger(GraphFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidTheoryException ex) {
            Logger.getLogger(GraphFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        /**
         * ================================================
         * Choosing layout
         * ================================================
         */
        if(lay)
        {
            variables.layout = new Temporal_Layout<Object, Edge>(graph);
            variables.view = new VisualizationViewer<Object, Edge>(variables.layout);
            Layouts.setSelectedItem("TemporalLayout");
            lay = false;
        }
//        layout.setSize(new Dimension(2000, 2000));
        /**
         * ================================================
         * VisualizationViewer<Node, Edge> view = new VisualizationViewer<Node, Edge>(layout);
         * ================================================
         */
        variables.view = new VisualizationViewer<Object, Edge>(variables.layout);
        final ScalingControl scaler = new CrossoverScalingControl();
        scaler.scale(variables.view, 1/2.1f, variables.view.getCenter());

        variables.gCollapser = new GraphCollapser(graph);
        
        final PredicatedParallelEdgeIndexFunction eif = PredicatedParallelEdgeIndexFunction.getInstance();
        // ================================================
        //        final Set exclusions = new HashSet();
        //testing for edge collapse
        eif.setPredicate(new Predicate() {
            @Override
            public boolean evaluate(Object e) {

                    return exclusions.contains(e);
            }});
        // ================================================
        variables.view.getRenderContext().setParallelEdgeIndexFunction(eif);
        
        variables.view.setBackground(Color.white);
        this.getContentPane().add(variables.view, BorderLayout.CENTER);
        /**
         * ================================================
         * Adding interaction via mouse
         * Commands: t for translate, p for picking
         * ================================================
         */
//        DefaultModalGraphMouse mouse = new DefaultModalGraphMouse();
        variables.view.setGraphMouse(mouse);
        variables.view.addKeyListener(mouse.getModeKeyListener());
        /**
         * ================================================
         * Add a listener for ToolTips
         * ================================================
         */
        variables.view.setVertexToolTipTransformer(new ToStringLabeller() {
            @Override
            public String transform(Object v) {
                    if(v instanceof Graph) {
                            return ("<html>" + ((Graph)v).getVertices().toString() + "</html>");
                    }
                    return ("<html>" + v.toString() + "</html>");
                    //return super.transform(v);
            }});
        /**
         * ================================================
         * Edge Tooltip
         * ================================================
         */
        variables.view.setEdgeToolTipTransformer(new Transformer<Edge,String>(){
        @Override
            public String transform(Edge n) 
            {
                return n.getInfluence();
            }
        });
        /**
         * ================================================
         * Labeling Vertex
         * ================================================
         */
//        view.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Node>());
        variables.view.getRenderContext().setVertexLabelTransformer(new Transformer<Object, String>() {

                @Override
                public String transform(Object v) {
                    if(v instanceof Graph) {
                        for(Object vertex : ((Graph)v).getVertices())
                        {
                            if(vertex instanceof AgentVertex) {
                                return "<html><font size=\"10\">" + ((Vertex)vertex).getName();
                            }
                        }    
                    }
                    if(v instanceof EntityVertex) {
                        return "<html><font size=\"10\">" + String.valueOf(((Vertex)v).getDate());
                    }
                    return "";
                }
            });
        
        /**
         * ================================================
         * Vertex Stroke
         * ================================================
         */
        Transformer<Object, Stroke> nodeStrokeTransformer =  new Transformer<Object, Stroke>() {
            @Override
            public Stroke transform(Object v) {
                return VertexStroke.VertexStroke(v, variables.view, variables.layout);
        }};
        variables.view.getRenderContext().setVertexStrokeTransformer(nodeStrokeTransformer);
        /**
         * ================================================
         * Edge Stroke
         * ================================================
         */
        variables.ComputeEdgeTypeValues(graph);
        Transformer<Edge, Stroke> edgeStrokeTransformer =  new Transformer<Edge, Stroke>() {
            @Override
            public Stroke transform(Edge e) {
                return EdgeStroke.StrokeByType(e, variables);
            }
        };
        variables.view.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
        /**
         * ================================================
         * Vertex Paint
         * ================================================
         */
        VertexPainter.VertexPainter("Default", variables.view, variables);
        /**
         * ================================================
         * Edge Paint
         * ================================================
         */
        Transformer edgePainter = new Transformer<Edge,Paint>() {
            @Override
            public Paint transform(Edge edge) {
                return edge.getColor();
            }
        };  
        variables.view.getRenderContext().setEdgeDrawPaintTransformer(edgePainter);
        variables.view.getRenderContext().setArrowDrawPaintTransformer(edgePainter);
        variables.view.getRenderContext().setArrowFillPaintTransformer(edgePainter);

        /**
         * ================================================
         * Node Shape
         * ================================================
         */
        variables.view.getRenderContext().setVertexShapeTransformer(new VertexShape());

        //graphicsContext.fill(shape);
        PreFilters.PreFilter();
        //Initialize selected filters from the GUI
        collapser.Filters(variables, filter);
    }
    
    /**
     * Get Graph from TSVReader
     * @param path
     * @return 
     */
    public static DirectedGraph<Object,Edge> getGraph(String path) {
        DirectedGraph<Object,Edge> g = new DirectedSparseMultigraph<Object,Edge>();
        try {
        //    try {
        //        TSVReader tsvReader = new TSVReader("log.txt");
//                TSVReader xmlReader = new TSVReader(path);
                XMLReader xmlReader = new XMLReader();
        //        for (Node node : tsvReader.getNodes()) {
        //            graph.addVertex(node.getID());
        //        }
                for (Edge edge : xmlReader.getEdges()) {
        //            g.addEdge(edge, edge.getSource().getID(), edge.getTarget().getID());
                    g.addEdge(edge, edge.getSource(), edge.getTarget());
                }
        //    } catch (URISyntaxException | IOException ex) {
        //        Logger.getLogger(GraphFrame.class.getName()).log(Level.SEVERE, null, ex);
        //    }
            
        } catch (URISyntaxException ex) {
            Logger.getLogger(GraphFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GraphFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return g;
}
    /**
     * Main
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional)">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    
                }
            }
        } catch (ClassNotFoundException ex) {
                Logger.getLogger(GraphFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                Logger.getLogger(GraphFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(GraphFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedLookAndFeelException ex) {
                Logger.getLogger(GraphFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(GraphFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
        //</editor-fold>

//        URL location = GraphFrame.class.getProtectionDomain().getCodeSource().getLocation();
        URL location = Config.class.getResource("/log.txt");
        Variables.graph = getGraph(location.getFile());
        java.awt.EventQueue.invokeLater(new Runnable() {
                
            @Override
                public void run() {
                    new GraphFrame(Variables.graph).setVisible(true);
                }
            });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Collapse;
    private javax.swing.JButton CollapseAgent;
    private javax.swing.JComboBox EdgeLineShapeSelection;
    private javax.swing.JButton Expand;
    public static javax.swing.JList FilterList;
    public static javax.swing.JCheckBox FilterNodeAgentButton;
    public static javax.swing.JCheckBox FilterNodeLonelyButton;
    private javax.swing.JComboBox Layouts;
    private javax.swing.JComboBox MouseModes;
    private javax.swing.JButton Reset;
    private javax.swing.JCheckBox ShowEdgeTextButton;
    public static javax.swing.JComboBox StatusFilterBox;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
