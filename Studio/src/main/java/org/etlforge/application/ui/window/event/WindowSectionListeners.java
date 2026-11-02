package org.etlforge.application.ui.window.event;

import org.etlforge.util.event.AbstractListenerManager;
import org.etlforge.util.event.ListenerManager;

public class WindowSectionListeners extends AbstractListenerManager<WindowSectionListener> implements ListenerManager<WindowSectionListener> {
   protected WindowSectionListener createNotifier() {
      return new WindowSectionListener() {
         @Override
         public void activeContentChanged(WindowSectionEvent var1) {
            for (WindowSectionListener var3 : WindowSectionListeners.this.getListeners()) {
               var3.activeContentChanged(var1);
            }
         }

         @Override
         public void contentAdded(WindowSectionEvent var1) {
            for (WindowSectionListener var3 : WindowSectionListeners.this.getListeners()) {
               var3.contentAdded(var1);
            }
         }

         @Override
         public void contentRemoved(WindowSectionEvent var1) {
            for (WindowSectionListener var3 : WindowSectionListeners.this.getListeners()) {
               var3.contentRemoved(var1);
            }
         }

         @Override
         public void ensureVisibleRequested(WindowSectionEvent var1) {
            for (WindowSectionListener var3 : WindowSectionListeners.this.getListeners()) {
               var3.ensureVisibleRequested(var1);
            }
         }

         @Override
         public void sectionActivated(WindowSectionEvent var1) {
            for (WindowSectionListener var3 : WindowSectionListeners.this.getListeners()) {
               var3.sectionActivated(var1);
            }
         }

         @Override
         public void sectionDeactivated(WindowSectionEvent var1) {
            for (WindowSectionListener var3 : WindowSectionListeners.this.getListeners()) {
               var3.sectionDeactivated(var1);
            }
         }
      };
   }
}
