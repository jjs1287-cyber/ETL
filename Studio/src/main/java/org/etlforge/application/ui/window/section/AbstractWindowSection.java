package org.etlforge.application.ui.window.section;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.application.ui.window.event.WindowSectionEvent;
import org.etlforge.application.ui.window.event.WindowSectionListener;
import org.etlforge.application.ui.window.event.WindowSectionListeners;

public abstract class AbstractWindowSection implements WindowSection {
   private final ApplicationWindow window;
   private final WindowSectionListeners sectionListeners;

   public AbstractWindowSection(ApplicationWindow var1) {
      this.window = var1;
      this.sectionListeners = new WindowSectionListeners();
   }

   @Override
   public final ApplicationWindow getWindow() {
      return this.window;
   }

   @Override
   public void ensureVisible() {
      this.fireEnureVisibleRequested();
   }

   @Override
   public final void addWindowSectionListener(WindowSectionListener var1) {
      this.sectionListeners.add(var1);
   }

   @Override
   public final void removeWindowSectionListener(WindowSectionListener var1) {
      this.sectionListeners.remove(var1);
   }

   protected final void fireEnureVisibleRequested() {
      this.sectionListeners.call().ensureVisibleRequested(new WindowSectionEvent(this));
   }

   protected final void fireContentAdded(WindowSectionContent var1) {
      this.sectionListeners.call().contentAdded(new WindowSectionEvent(this, var1));
   }

   protected final void fireContentRemoved(WindowSectionContent var1) {
      this.sectionListeners.call().contentRemoved(new WindowSectionEvent(this, var1));
   }

   protected final void fireActiveContentChanged(WindowSectionContent var1) {
      this.sectionListeners.call().activeContentChanged(new WindowSectionEvent(this, var1));
   }
}
