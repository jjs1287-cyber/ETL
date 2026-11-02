package org.etlforge.application.ui.window.event;

import java.util.EventListener;

public interface WindowSectionListener extends EventListener {
   void contentAdded(WindowSectionEvent var1);

   void contentRemoved(WindowSectionEvent var1);

   void ensureVisibleRequested(WindowSectionEvent var1);

   void sectionActivated(WindowSectionEvent var1);

   void sectionDeactivated(WindowSectionEvent var1);

   void activeContentChanged(WindowSectionEvent var1);

   abstract class Adapter implements WindowSectionListener {
      @Override
      public void contentAdded(WindowSectionEvent var1) {
      }

      @Override
      public void contentRemoved(WindowSectionEvent var1) {
      }

      @Override
      public void ensureVisibleRequested(WindowSectionEvent var1) {
      }

      @Override
      public void sectionActivated(WindowSectionEvent var1) {
      }

      @Override
      public void sectionDeactivated(WindowSectionEvent var1) {
      }

      @Override
      public void activeContentChanged(WindowSectionEvent var1) {
      }
   }
}
