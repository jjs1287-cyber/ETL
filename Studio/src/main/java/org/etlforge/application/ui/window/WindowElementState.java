package org.etlforge.application.ui.window;

import java.util.EnumSet;
import org.etlforge.application.ui.window.event.WindowElementStateEvent;

public enum WindowElementState {
   OPEN,
   CLOSED,
   ACTIVE,
   INACTIVE,
   ATTACHED,
   DETACHED;

   public WindowElementState getOppositeState() {
      switch (this) {
         case OPEN:
            return CLOSED;
         case CLOSED:
            return OPEN;
         case ACTIVE:
            return INACTIVE;
         case INACTIVE:
            return ACTIVE;
         case ATTACHED:
            return DETACHED;
         case DETACHED:
            return ATTACHED;
         default:
            return null;
      }
   }

   public boolean wasAdded(WindowElementStateEvent var1) {
      return this.wasAdded(var1.getOldState(), var1.getNewState());
   }

   public boolean wasAdded(EnumSet<WindowElementState> var1, EnumSet<WindowElementState> var2) {
      return var2.contains(this) && !var1.contains(this);
   }
}
