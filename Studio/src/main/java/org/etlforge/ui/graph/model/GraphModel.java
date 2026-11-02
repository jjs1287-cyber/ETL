package org.etlforge.ui.graph.model;

import java.util.Collection;
import org.etlforge.ui.graph.edge.GraphEdge;

public interface GraphModel {
   Collection<? extends Object> getNodes();

   boolean contains(Object var1);

   Collection<? extends GraphEdge> getEdges();

   Collection<? extends GraphEdge> getOutgoingEdges(Object var1);

   Collection<? extends GraphEdge> getIncomingEdges(Object var1);

   boolean isGroup(Object var1);

   Object getParentOf(Object var1);

   void addModelListener(GraphModelListener var1);

   void removeModelListener(GraphModelListener var1);
}
