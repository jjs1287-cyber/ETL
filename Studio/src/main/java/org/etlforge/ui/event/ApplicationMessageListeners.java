package org.etlforge.ui.event;

import org.etlforge.util.event.AbstractListenerManager;
import org.etlforge.util.event.ListenerManager;

public class ApplicationMessageListeners extends AbstractListenerManager<ApplicationMessageListener> implements ListenerManager<ApplicationMessageListener> {
   protected ApplicationMessageListener createNotifier() {
      return new ApplicationMessageListener() {
         @Override
         public void incomingMessage(ApplicationMessageEvent var1) {
            for (ApplicationMessageListener var3 : ApplicationMessageListeners.this.getListeners()) {
               var3.incomingMessage(var1);
            }
         }
      };
   }
}
