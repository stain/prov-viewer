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

package br.uff.ic.utility.graph;

import br.uff.ic.utility.GraphAttribute;
import br.uff.ic.provviewer.EdgeType;
import br.uff.ic.provviewer.VariableNames;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.Utils;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Edge Class
 *
 * @author Kohwalter
 */
public class Edge extends GraphObject {

    private String id;
    private Object source;
    private Object target;
    private final String type;           // Edge type (prov edges)
    //used to hide this edge when collapsing a group of edges
    private boolean hide;
    private Color defaultColor = new Color(0, 0, 255);
    private String influenceName = "Influence";

    /**
     * Constructor for testing purposes since it construct a mostly blank edge
     * @param id is the edge ID
     * @param target is the target vertex
     * @param source is the source vertex
     */
    public Edge(String id, Object target, Object source) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.type = "";
        hide = false;
        this.attributes = new HashMap<>();
        GraphAttribute att = new GraphAttribute(influenceName, "", "");
        this.attributes.put(att.getName(), att);
        if(!this.hasAttribute(VariableNames.edgeHiddenAttribute)) {
            GraphAttribute hide = new GraphAttribute(VariableNames.edgeHiddenAttribute, "false", "");
            this.attributes.put(hide.getName(), hide);
        }
        else {
            hide = Boolean.parseBoolean(this.getAttributeValue(VariableNames.edgeHiddenAttribute));
        }
//        this.attributes.putAll(attributes);
        setLabel("");
    }
    
    public Edge(String id, String type, Object target, Object source) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.type = type;
        hide = false;
        this.attributes = new HashMap<>();
        GraphAttribute att = new GraphAttribute(influenceName, "", "");
        this.attributes.put(att.getName(), att);
        if(!this.hasAttribute(VariableNames.edgeHiddenAttribute)) {
            GraphAttribute hide = new GraphAttribute(VariableNames.edgeHiddenAttribute, "false", "");
            this.attributes.put(hide.getName(), hide);
        }
        else {
            hide = Boolean.parseBoolean(this.getAttributeValue(VariableNames.edgeHiddenAttribute));
        }
//        this.attributes.putAll(attributes);
        setLabel(type);
    }
    
    /**
     * Constructor
     *
     * @param id
     * @param type
     * @param value
     * @param label
     * @param attributes
     * @param target
     * @param source
     */
    public Edge(String id, String type, String value,
            String label, Map<String, GraphAttribute> attributes, Object target, Object source) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.type = type;
        hide = false;
        this.attributes = new HashMap<>(attributes);
        GraphAttribute att = new GraphAttribute(influenceName, value, "");
        this.attributes.put(att.getName(), att);
        if(!this.hasAttribute(VariableNames.edgeHiddenAttribute)) {
            GraphAttribute hide = new GraphAttribute(VariableNames.edgeHiddenAttribute, "false", "");
            this.attributes.put(hide.getName(), hide);
        }
        else {
            hide = Boolean.parseBoolean(this.getAttributeValue(VariableNames.edgeHiddenAttribute));
        }
        setLabel(label);
    }

    /**
     * Constructor without extra attributes
     *
     * @param id
     * @param type
     * @param value
     * @param label
     * @param target
     * @param source
     */
    public Edge(String id, String type, String label, String value, Object target, Object source) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.type = type;
//        hide = false;
        this.attributes = new HashMap<>();
        GraphAttribute att = new GraphAttribute(influenceName, value, "");
        this.attributes.put(att.getName(), att);
        if(!this.hasAttribute(VariableNames.edgeHiddenAttribute)) {
            GraphAttribute hide = new GraphAttribute(VariableNames.edgeHiddenAttribute, "false", "");
            this.attributes.put(hide.getName(), hide);
        }
        else {
            hide = Boolean.parseBoolean(this.getAttributeValue(VariableNames.edgeHiddenAttribute));
        }
        setLabel(label);
//        if (label.equalsIgnoreCase("") || label == null || "-".equals(label) || label.equalsIgnoreCase("Neutral")) {
//            setLabel("Neutral");
//            value = "0";
//        }
        
    }

    /**
     * Constructor without influence value, label, type (type=influence) and no
     * extra attribute
     *
     * @param id Edge's ID
     * @deprecated Used only on outdated TSVReader
     * @param target Vertex target
     * @param source Vertex source
     * @param influence Influence value and name (i.e. "+9 damage")
     */
    public Edge(String id, Object target, Object source, String influence) {
        this.id = id;
        this.source = source;
        this.target = target;
        influence = "0";
        this.type = getLabel();
        hide = false;
        this.attributes = new HashMap<>();
        GraphAttribute att = new GraphAttribute(influenceName, influence, "");
        this.attributes.put(att.getName(), att);
        GraphAttribute hide = new GraphAttribute(VariableNames.edgeHiddenAttribute, "false", "");
        this.attributes.put(hide.getName(), hide);
        if (influence.equalsIgnoreCase("")) {
            setLabel("Neutral");
        } else {
            setLabel(influence);
        }
    }

    /**
     * Return Edge id
     *
     * @return
     */
    public String getID() {
        return id;
    }

    /**
     * Set the edge ID
     * @param t is the new value
     */
    public void setID(String t) {
        id = t;
    }

    /**
     * Method to get the edge source
     *
     * @return vertex source
     */
    public Object getSource() {
        return source;
    }

    /**
     * Set the edge's source
     * @param t is the new source
     */
    public void setSource(Object t) {
        source = t;
    }

    /**
     * Method to get the edge target
     *
     * @return vertex target
     */
    public Object getTarget() {
        return target;
    }

    /**
     * Set the edge's target/destionation
     * @param t  is the new target
     */
    public void setTarget(Object t) {
        target = t;
    }

    /**
     * Method for returning the edge's numeric value (converted from String)
     *
     * @return (float) edge influence value
     */
    public float getValue() {
        if (Utils.tryParseFloat(this.getAttributeValue(influenceName))) {
            return Float.parseFloat(this.getAttributeValue(influenceName));
        } else if (Utils.tryParseFloat(this.getAttributeValue(influenceName).split(" ")[0])) {
            return Float.parseFloat(this.getAttributeValue(influenceName).split(" ")[0]);
        } else {
            return 0;
        }
    }

    /**
     * Returns the edge's value as the original String instead of converting to numeric
     * @return 
     */
    public String getStringValue() {
        return this.getAttributeValue(influenceName);
    }
    
    /**
     * Updates the edge's value
     * @param t  is the string with the new value
     */
    public void setValue(String t) {
        this.getAttribute(influenceName).setValue(t);
    }

    /**
     * Method for returning the edge type
     *
     * @return
     */
    public String getType() {
        return this.type;
    }

    /**
     * Method to get the edge influence + value
     *
     * @param nGraphs is the number of different graphFiles values
     * @return (String) influence
     */
    public String getEdgeTooltip(int nGraphs) {
        return "<br>ID: " + this.id
                + "<br>Label: " + getLabel()
                + "<br>"
                + "<br>Type: " + getType()
                + "<br>Frequency: " + getFrequency(nGraphs)
                + "<br>" + printAttributes();
    }

    /**
     * Method to check if the edge is hidden (due to collapses)
     *
     * @return (boolean) if the edge is hidden or not
     */
    public boolean isHidden() {
        return hide;
//        return Boolean.parseBoolean(this.attributes.get(VariableNames.edgeHiddenAttribute).getValue());
    }

    /**
     * Method to set the hide parameter
     *
     * @param t (boolean) Hide = t
     */
    public void setHide(boolean t) {
        hide = t;
        GraphAttribute hide = new GraphAttribute(VariableNames.edgeHiddenAttribute, Boolean.toString(t), "");
        this.attributes.put(hide.getName(), hide);
    }
    
//    public String getHide() {
//        return this.attributes.get(VariableNames.edgeHiddenAttribute).getValue();
//    }

    /**
     * Method to check if the edge is of the neutral type (Empty influence or
     * value equals zero
     *
     * @return (boolean) is neutral or not
     */
    public boolean isNeutral() {
        return (getLabel().equalsIgnoreCase(""))
                || (getLabel().isEmpty())
                || (getLabel().equalsIgnoreCase("Neutral"));
    }

    /**
     * Method to override JUNG's default toString method
     *
     * @return edge details
     */
    @Override
    public String toString() {
        String font = VariableNames.FontConfiguration;
        if (getLabel().isEmpty()) {
            return font + this.type;
        } else if (getLabel().equals(this.type)) {
            return font + this.type;
        } else {
            return font + this.type + " (" + getLabel() + ")";
        }
    }
    
    private Color defaultColor() {
        if(this.getLabel().contains("Crash"))
            return new Color(204, 0, 204);
        if(((Vertex)this.getTarget()).getTime() < 100)
            return Color.RED;
        else if (((Vertex)this.getTarget()).getTime() < 180)
            return Color.GREEN;
        else
            return Color.BLUE;
//        return defaultColor;
    }

    /**
     * Method to get the edge color (red, black, or green), defined by value
     *
     * @param variables
     * @return
     */
    public Color getColor(Variables variables) {
        if(variables.isEdgeColorByGraphs) {
            String[] graphs = getAttributeValues(VariableNames.GraphFile);
            return Utils.getGrayscaleColor(graphs.length, variables.numberOfGraphs);
        } else {
            float v = getValue();
            if (v == 0) {
                return defaultColor;
            } else {
                int j = 0;
                for (int i = 0; i < variables.config.edgetype.size(); i++) {
                    if (this.getLabel().contains(variables.config.edgetype.get(i).type)) {
                        j = i;
                    }
                }
                // TODO add inverted color scheme for increase in value to be red and decrease to be green
                if (variables.config.edgetype.get(j).isInverted) {
                    if (v > 0) {
                        return compareValueRed(v, 0, variables.config.edgetype.get(j).max, variables.config.edgetype.get(j).isInverted);
                    } else {
                        return compareValueGreen(v, variables.config.edgetype.get(j).min, 0, variables.config.edgetype.get(j).isInverted);
                    }
                } else {
                // DO
                    // else
                    if (v > 0) {
                        return compareValueGreen(v, 0, variables.config.edgetype.get(j).max, variables.config.edgetype.get(j).isInverted);
                    } else {
                        return compareValueRed(v, variables.config.edgetype.get(j).min, 0, variables.config.edgetype.get(j).isInverted);
                    }
                }
            }
        }
    }

    /**
     * Method that calculates the green gradient based on the edge's value and the maximum and minimum for the same edge type
     * @param value is the edge's value
     * @param min is the minimum value for the edge type
     * @param max is the maximum value for the edge type
     * @param isInverted tells if the color gradient will be increasing or decreasing
     * @return 
     */
    public Color compareValueGreen(float value, double min, double max, boolean isInverted) {
        if(!isInverted) {
            int proportion = (int) Math.round(510 * Math.abs(value - min) / (float) Math.abs(max - min));
            proportion = Math.max(proportion, 0);
            return new Color(0, Math.min(255, proportion), 0);
        }
        else {
            int proportion = (int) Math.round(510 * Math.abs(value - min) / (float) Math.abs(max - min));
            proportion = Math.min(proportion, 510);
            return new Color(0, Math.min(255, 510 - proportion), 0);
        }
    }

    /**
     * Method that calculates the red gradient based on the edge's value and the maximum and minimum for the same edge type
     * @param value is the edge's value
     * @param min is the minimum value for the edge type
     * @param max is the maximum value for the edge type
     * @param isInverted tells if the color gradient will be increasing or decreasing
     * @return 
     */
    public Color compareValueRed(float value, double min, double max, boolean isInverted) {
        if(!isInverted) {
            int proportion = (int) Math.round(510 * Math.abs(value - min) / (float) Math.abs(max - min));
            proportion = Math.min(proportion, 510);
            return new Color(Math.min(255, 510 - proportion), 0, 0);
        } else
        {
            int proportion = (int) Math.round(510 * Math.abs(value - min) / (float) Math.abs(max - min));
            proportion = Math.max(proportion, 0);
            return new Color(Math.min(255, proportion), 0, 0);
        }
    }

    /**
     * Method used during the collapse of edges. This method defines if the
     * collapsed edge influence value will be the sum of the edges values or the
     * average
     *
     * @param variables
     * @return (boolean) Return true if influence from collapsed edges are
     * added. Return false if average
     */
    public boolean addInfluence(Variables variables) {
        for (EdgeType edgetype : variables.config.edgetype) {
            if (this.getType().contains(edgetype.type)) {
                if (edgetype.collapse.equalsIgnoreCase("AVERAGE")) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Function to merge two edges
     * @param edge is the edge that we want to merge with
     * @return the updated edge after merging with "edge"
     */
    public Edge merge(Edge edge, String mergeCode) {
        if(this.getType().equalsIgnoreCase(edge.getType())) {
            this.updateAllAttributes(edge.getAttributes());
            // We have a problem here when merging many graphs: StringBuilder.append(StringBuilder.java:132) java.lang.OutOfMemoryError: Java heap space
//            this.id = this.id + ", " + edge.id; 
            this.id = this.id + " "; 
            if(!this.getLabel().contains(edge.getLabel()))
                this.setLabel(this.getLabel() + ", " + edge.getLabel());
            edge.setHide(true);
            GraphAttribute merged = new GraphAttribute(VariableNames.MergedEdgeAttribute, mergeCode, "");
            this.addAttribute(merged);
            GraphAttribute hide = new GraphAttribute(VariableNames.edgeHiddenAttribute, "false", "");
            this.addAttribute(hide);
        }
        return this;
    }
    
//    /**
//     * Method that calculates the edge frequency and returns a human-readable value (String)
//     * @param nGraphs is the total number of existing graphs used during the merge
//     * @return the edge's frequency between 0% and 100%
//     */
//    public String getEdgeFrequency(float nGraphs) {
//        if(this.hasAttribute(VariableNames.GraphFile)) {
//            float frequency = ((float) this.getAttributeValues(VariableNames.GraphFile).length / (float)nGraphs) * 100;
//            return String.format("%.02f", frequency) + "%";
//        } else
//            return "Frequency Unavailable";
//    }
//    
//    /**
//     * Method that calculates the edge frequency and returns the probability between 0 and 1
//     * @param nGraphs is the total number of existing graphs used during the merge
//     * @return the edge's frequency
//     */
//    public float getEdgeFrequencyValue(float nGraphs) {
//        if(this.hasAttribute(VariableNames.GraphFile)) {
//            return ((float) this.getAttributeValues(VariableNames.GraphFile).length / (float)nGraphs);
//        } else
//            return 0;
//    }
}
