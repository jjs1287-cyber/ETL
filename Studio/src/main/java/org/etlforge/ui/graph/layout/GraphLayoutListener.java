package org.etlforge.ui.graph.layout;

import java.util.EventListener;

public interface GraphLayoutListener extends EventListener {
   void layoutChanged(GraphLayoutEvent var1);

   void zOrderChanged(GraphLayoutEvent var1);
}
