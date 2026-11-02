package org.etlforge.ui.event;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.etlforge.util.event.AbstractListenerManager;
import org.etlforge.util.event.ListenerManager;

public class ChangeListeners extends AbstractListenerManager<ChangeListener> implements ListenerManager<ChangeListener> {
   protected ChangeListener createNotifier() {
      return new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent var1) {
            for (ChangeListener var3 : ChangeListeners.this.getListeners()) {
               var3.stateChanged(var1);
            }
         }
      };
   }
}
