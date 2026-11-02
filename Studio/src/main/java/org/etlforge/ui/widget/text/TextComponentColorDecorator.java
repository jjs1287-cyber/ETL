package org.etlforge.ui.widget.text;

import java.awt.Color;
import org.etlforge.util.Interval;

public interface TextComponentColorDecorator {
   void addColoredRegion(Interval var1, Color var2);

   void clearColoredRegions();

   void repaint();

   void dispose();
}
