package org.etlforge.ui.graph2d.node;

import java.awt.Dimension;
import java.util.EventListener;

public interface NodeSizeListener extends EventListener {
   void nodeSizeChanged(GraphNodeUi2D var1, Dimension var2);
}
