package org.etlforge.ui.graph.model;

import java.util.EventListener;

public interface GraphModelListener extends EventListener {
   void modelChanged(GraphModelEvent var1);

   void nodesConnected(NodesConnectedEvent var1);

   void nodesDisconnected(NodesDisconnectedEvent var1);

   void nodesInserted(NodesInsertedEvent var1);

   void nodesRemoved(NodesRemovedEvent var1);

   void nodeChanged(NodeChangedEvent var1);
}
