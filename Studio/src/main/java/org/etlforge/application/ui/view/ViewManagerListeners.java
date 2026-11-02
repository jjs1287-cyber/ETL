package org.etlforge.application.ui.view;

import org.etlforge.util.event.AbstractListenerManager;
import org.etlforge.util.event.ListenerManager;

public final class ViewManagerListeners extends AbstractListenerManager<ViewManagerListener> implements ListenerManager<ViewManagerListener> {
   protected ViewManagerListener createNotifier() {
      return new ViewManagerListener() {
         @Override
         public void viewIsDisplayed(ViewManagerEvent var1) {
            for (ViewManagerListener var3 : ViewManagerListeners.this.getListeners()) {
               var3.viewIsDisplayed(var1);
            }
         }

         @Override
         public void viewIsLoaded(ViewManagerEvent var1) {
            for (ViewManagerListener var3 : ViewManagerListeners.this.getListeners()) {
               var3.viewIsLoaded(var1);
            }
         }

         @Override
         public void viewWasRemoved(ViewManagerEvent var1) {
            for (ViewManagerListener var3 : ViewManagerListeners.this.getListeners()) {
               var3.viewWasRemoved(var1);
            }
         }
      };
   }
}
