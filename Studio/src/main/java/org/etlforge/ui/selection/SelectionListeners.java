package org.etlforge.ui.selection;

import org.etlforge.util.event.AbstractListenerManager;
import org.etlforge.util.event.ListenerManager;

public class SelectionListeners extends AbstractListenerManager<SelectionListener> implements ListenerManager<SelectionListener> {
   protected SelectionListener createNotifier() {
      return new SelectionListener() {
         @Override
         public void selectionChanged(SelectionChangedEvent var1) {
            for (SelectionListener var3 : SelectionListeners.this.getListeners()) {
               var3.selectionChanged(var1);
            }
         }
      };
   }
}
