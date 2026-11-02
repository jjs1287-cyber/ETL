package org.etlforge.integration.client.ui.jitterpack;

import org.etlforge.integration.client.project.jitterpack.JitterPack;
import org.etlforge.util.event.AbstractListenerManager;
import org.etlforge.util.event.ListenerManager;

public final class JitterPackSelectionListeners
   extends AbstractListenerManager<JitterPackSelectionListener>
   implements ListenerManager<JitterPackSelectionListener> {
   protected JitterPackSelectionListener createNotifier() {
      return new JitterPackSelectionListener() {
         @Override
         public void selectionChanged(JitterPack var1) {
            for (JitterPackSelectionListener var3 : JitterPackSelectionListeners.this.getListeners()) {
               var3.selectionChanged(var1);
            }
         }
      };
   }
}

