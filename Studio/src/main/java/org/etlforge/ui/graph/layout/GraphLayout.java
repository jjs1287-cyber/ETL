package org.etlforge.ui.graph.layout;

import org.etlforge.ui.graph.model.GraphModelEvent;
import org.etlforge.ui.graph.model.NodesConnectedEvent;
import org.etlforge.ui.graph.model.NodesInsertedEvent;
import org.etlforge.ui.graph.model.NodesRemovedEvent;

public interface GraphLayout {
   GraphLayoutMemento getMemento();

   void addGraphLayoutListener(GraphLayoutListener var1);

   void removeGraphLayoutListener(GraphLayoutListener var1);

   void dataModelChanged(GraphModelEvent var1);

   void nodesInserted(NodesInsertedEvent var1);

   void nodesRemoved(NodesRemovedEvent var1);

   void nodesConnected(NodesConnectedEvent var1);
}
