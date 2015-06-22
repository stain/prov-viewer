/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.GUI;

import br.uff.ic.provviewer.Attribute;
import br.uff.ic.provviewer.Edge.Edge;
import br.uff.ic.provviewer.GraphAttribute;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.provviewer.Vertex.ActivityVertex;
import br.uff.ic.provviewer.Vertex.AgentVertex;
import br.uff.ic.provviewer.Vertex.EntityVertex;
import br.uff.ic.provviewer.Vertex.Vertex;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections15.Transformer;

/**
 *
 * @author Kohwalter
 */
public class GuiTooltip {

    Map<String, GraphAttribute> attributes;

    public static void Tooltip(Variables variables) {
        variables.view.setVertexToolTipTransformer(new ToStringLabeller() {
            @Override
            public String transform(Object v) {
                if (v instanceof Graph) {
//                    return "<html>" + ((Graph) v).getVertices().toString() + "</html>";
                    return "<html>" + GraphTooltip(v) + "</html>";
                }
                return "<html>" + v.toString() + "</html>";
            }
        });
        variables.view.setEdgeToolTipTransformer(new Transformer<Edge, String>() {
            @Override
            public String transform(Edge n) {
                return n.getEdgeInfluence();
            }
        });
    }

    static int agents = 0;
    static int activities = 0;
    static int entities = 0;
    public static String GraphTooltip(Object v) {
        String nodeTypes = "";
        
        agents = 0;
        activities = 0;
        entities = 0;
        Map<String, String> ids = new HashMap<String, String>();
        Map<String, String> labels = new HashMap<String, String>();
        Map<String, String> times = new HashMap<String, String>();
        Map<String, GraphAttribute> attributes = new HashMap<String, GraphAttribute>(); 
        
        GraphTooltip(v, ids, labels, times, attributes);

        if (agents > 0) {
            nodeTypes = "Agents: " + agents + "<br>";
        }
        if (activities > 0) {
            nodeTypes += "Activities: " + activities + "<br>";
        }
        if (entities > 0) {
            nodeTypes += "Entities: " + entities + "<br>";
        }

        return "<b>Collapsed Vertex" + "</b>" + "<br>" + nodeTypes
                + "<br>IDs: " + labels.values().toString() + "<br>"
                + "<b>Labels: " + labels.values().toString() + "</b>"
                + " <br>" + "Times: " + labels.values().toString()
                + " <br>" + PrintAttributes(attributes);
    }

    public static void GraphTooltip(Object v, 
            Map<String, String> ids,
            Map<String, String> labels,
            Map<String, String> times,
            Map<String, GraphAttribute> attributes){
        
        Collection vertices = ((Graph) v).getVertices();
        for (Object vertex : vertices) {
            if (!(vertex instanceof Graph))
            {
                ids.put(((Vertex) vertex).getID(), ((Vertex) vertex).getID());
                labels.put(((Vertex) vertex).getLabel(), ((Vertex) vertex).getLabel());
                times.put(((Vertex) vertex).getTimeString(), ((Vertex) vertex).getTimeString());
                
                if (vertex instanceof AgentVertex) {
                    agents++;
                } else if (vertex instanceof ActivityVertex) {
                    activities++;
                } else if (vertex instanceof EntityVertex) {
                    entities++;
                }
                
                for (Attribute att : ((Vertex) vertex).getAttributes()) {
                    if (attributes.containsKey(att.getName())) {
                        GraphAttribute temporary = attributes.get(att.getName());
                        temporary.updateAttribute(att.getValue());
                        attributes.put(att.getName(), temporary);
                    } else {
                        attributes.put(att.getName(), new GraphAttribute(att.getName(), att.getValue()));
                    }
                }
            }
            else //(vertex instanceof Graph) 
            {
                GraphTooltip(vertex, ids, labels, times, attributes);
            }
        }
    }
    public static String PrintAttributes(Map<String, GraphAttribute> attributes) {
        String attributeList = "";
        if (!attributes.values().isEmpty()) {
            for (GraphAttribute att : attributes.values()) {
                attributeList += att.printAttribute();
            }
        }
        return attributeList;
    }
}
