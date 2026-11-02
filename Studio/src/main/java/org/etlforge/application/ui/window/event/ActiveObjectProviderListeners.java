package org.etlforge.application.ui.window.event;

import org.etlforge.util.event.AbstractListenerManager;
import org.etlforge.util.event.ListenerManager;

public class ActiveObjectProviderListeners
   extends AbstractListenerManager<ActiveObjectProviderListener>
   implements ListenerManager<ActiveObjectProviderListener> {
   protected ActiveObjectProviderListener createNotifier() {
      return new ActiveObjectProviderListener() {
         @Override
         public void activeObjectsChanged(ActiveObjectProviderEvent var1) {
            for (ActiveObjectProviderListener var3 : ActiveObjectProviderListeners.this.getListeners()) {
               var3.activeObjectsChanged(var1);
            }
         }
      };
   }
}
