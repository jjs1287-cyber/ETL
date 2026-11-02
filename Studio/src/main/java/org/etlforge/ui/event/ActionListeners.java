package org.etlforge.ui.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.etlforge.util.event.AbstractListenerManager;
import org.etlforge.util.event.ListenerManager;

public class ActionListeners extends AbstractListenerManager<ActionListener> implements ListenerManager<ActionListener> {
   protected ActionListener createNotifier() {
      return new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            for (ActionListener var3 : ActionListeners.this.getListeners()) {
               var3.actionPerformed(var1);
            }
         }
      };
   }
}
