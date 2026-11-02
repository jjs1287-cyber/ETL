package org.etlforge.ui.graph2d;

import java.awt.Point;
import javax.swing.CellEditor;
import javax.swing.JComponent;

public interface GraphNodeEditor extends CellEditor {
   Object getNode();

   JComponent getComponent(Graph2D var1);

   Point getOffset();
}
