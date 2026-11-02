package org.etlforge.ui.input;

import org.etlforge.util.event.AbstractListenerManager;
import org.etlforge.util.event.ListenerManager;

public class InputChangeListeners extends AbstractListenerManager<InputChangeListener> implements ListenerManager<InputChangeListener> {
   protected InputChangeListener createNotifier() {
      return new InputChangeListener() {
         @Override
         public void inputChanged(InputChangeEvent var1) {
            for (InputChangeListener var3 : InputChangeListeners.this.getListeners()) {
               var3.inputChanged(var1);
            }
         }
      };
   }
}
