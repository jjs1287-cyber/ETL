package org.etlforge.ui.widget;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;

public interface ComponentPainter {
   boolean preRendering(Component var1, Graphics2D var2);

   void postRendering(Component var1, Graphics2D var2);

   Dimension getPreferredSize(Dimension var1);
}
