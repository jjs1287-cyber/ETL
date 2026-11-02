package org.etlforge.application.ui.window.event;

import java.util.EventObject;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.WindowSectionContent;

public class WindowSectionEvent extends EventObject {
   private final WindowSectionContent content;

   public WindowSectionEvent(WindowSection var1) {
      this(var1, null);
   }

   public WindowSectionEvent(WindowSection var1, WindowSectionContent var2) {
      super(var1);
      this.content = var2;
   }

   public WindowSection getSource() {
      return (WindowSection)super.getSource();
   }

   public WindowSectionContent getContent() {
      return this.content;
   }
}
