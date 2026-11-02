package org.etlforge.ui.graph2d.layout;

import java.awt.Rectangle;
import java.util.Map;

public interface LayoutAlgorithm {
   Map<Object, Rectangle> doLayout();
}
