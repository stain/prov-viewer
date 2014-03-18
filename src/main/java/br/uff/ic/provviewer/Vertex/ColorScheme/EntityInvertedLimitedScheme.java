/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Vertex.ColorScheme;

import br.uff.ic.provviewer.Variables;
import br.uff.ic.provviewer.Vertex.EntityVertex;
import br.uff.ic.provviewer.Vertex.Vertex;
import java.awt.Paint;

/**
 *
 * @author Kohwalter
 */
public class EntityInvertedLimitedScheme extends ColorScheme {
    
    public EntityInvertedLimitedScheme(String attribute, String empty, double g, double y) {
        super(attribute, empty, g, y);
    }

    @Override
    public Paint Execute(Object v, final Variables variables) {

        ComputeValue(variables.graph);
        if (v instanceof EntityVertex) {
            return this.CompareValue(((EntityVertex) v).getAttributeValueInteger(this.attribute), this.givenMax, this.givenMin);
        }
        return ((Vertex) v).getColor();
    }

}
