package org.etlforge.application.ui.window.event;

import java.util.EventObject;
import java.util.List;

public class ActiveObjectProviderEvent extends EventObject {
   private final List<?> activeObjects;

   public ActiveObjectProviderEvent(ActiveObjectProvider var1, List<?> var2) {
      super(var1);
      if (var2 == null) {
         throw new IllegalArgumentException("activeObjects can't be null.");
      }

      this.activeObjects = var2;
   }

   public ActiveObjectProvider getSource() {
      return (ActiveObjectProvider)super.getSource();
   }

   public List<?> getActiveObjects() {
      return this.activeObjects;
   }
}
