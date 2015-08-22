/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Input;

import br.uff.ic.provviewer.Attribute;
import br.uff.ic.provviewer.BasePath;
import br.uff.ic.provviewer.Edge.Edge;
import br.uff.ic.provviewer.Vertex.Vertex;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Kohwalter
 */
public abstract class InputReader {
    Map<String, Vertex> nodes;
    Collection<Edge> edges;
    File file;
    /**
     * Return edges
     *
     * @return
     */
    public Collection<Edge> getEdges() {
        return edges;
    }

    /**
     * Return vertices
     *
     * @return
     */
    public Collection<Vertex> getNodes() {
        return nodes.values();
    }

    public InputReader(File f){
        file = f;
        nodes = new HashMap<String, Vertex>();
        edges = new ArrayList<Edge>();
    }
    
//    public void addEdge(String id, String influence, String type, String value, String label, Map<String, Attribute> attributes, String target, String source)
//    {
//        Edge edge = new Edge(id, influence, type, value, label, attributes, nodes.get(target), nodes.get(source));
//        edges.add(edge);
//    }
    
    /**
     *  Overload method from AddEdge without Label
     * @param id
     * @param influence
     * @param type
     * @param value
     * @param target
     * @param source 
     */
    public void addEdge(String id, String type, String label, String value, String target, String source)
    {
        //AddEdge(id, type, label, value, target, source);
        Edge edge = new Edge(id, type, label, value, nodes.get(target), nodes.get(source));
        edges.add(edge);
    }
    
    /**
     * Method to add Vertex to the Map
     * @param node 
     */
    public void addNode(Vertex node)
    {
        nodes.put(node.getID(), node);
    }
    
    /**
     * Abstract method to read a XML file depending on the XML-Schema
     */
    public abstract void readFile()throws URISyntaxException, IOException;

}
