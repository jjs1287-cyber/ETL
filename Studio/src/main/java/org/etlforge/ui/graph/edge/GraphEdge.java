package org.etlforge.ui.graph.edge;

public interface GraphEdge {
   Object from();

   Object to();

   boolean connects(Object var1);
}
