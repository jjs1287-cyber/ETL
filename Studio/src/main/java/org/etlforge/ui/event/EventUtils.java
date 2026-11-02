package org.etlforge.ui.event;

import java.awt.AWTEvent;
import java.awt.Component;
import javax.swing.SwingUtilities;

public final class EventUtils {
   public static boolean isSourceDescendingFrom(Component var0, AWTEvent var1) {
      Object var2 = var1.getSource();
      return var2 instanceof Component && SwingUtilities.isDescendingFrom((Component)var2, var0);
   }

   private EventUtils() {
   }
}
