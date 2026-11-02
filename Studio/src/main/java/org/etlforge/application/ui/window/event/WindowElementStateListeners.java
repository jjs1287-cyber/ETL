package org.etlforge.application.ui.window.event;

import org.etlforge.util.event.AbstractListenerManager;
import org.etlforge.util.event.ListenerManager;

public class WindowElementStateListeners extends AbstractListenerManager<WindowElementStateListener> implements ListenerManager<WindowElementStateListener> {
   protected WindowElementStateListener createNotifier() {
      return new WindowElementStateListener() {
         @Override
         public void stateChanged(WindowElementStateEvent var1) {
            for (WindowElementStateListener var3 : WindowElementStateListeners.this.getListeners()) {
               var3.stateChanged(var1);
            }
         }
      };
   }
}
