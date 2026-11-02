package org.etlforge.ui.graph2d.layout;

import java.util.EventListener;

public interface NodeExpansionListener extends EventListener {
   void nodeExpanded(NodeExpandedEvent var1);

   void nodeCollapsed(NodeCollapsedEvent var1);
}
