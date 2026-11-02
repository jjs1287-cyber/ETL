package org.etlforge.ui.widget;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;

public abstract class PostRenderingComponentPainter implements ComponentPainter {
   @Override
   public Dimension getPreferredSize(Dimension var1) {
      return var1;
   }

   @Override
   public final boolean preRendering(Component var1, Graphics2D var2) {
      return false;
   }
}
