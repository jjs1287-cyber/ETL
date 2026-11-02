package org.etlforge.application.ui.window;

import java.util.EnumSet;
import org.etlforge.application.ui.window.event.WindowElementStateEvent;

public enum WindowElementStateSpan {
   WHEN_OPEN(WindowElementState.OPEN, WindowElementState.CLOSED),
   WHEN_ACTIVE(WindowElementState.ACTIVE, WindowElementState.INACTIVE),
   WHEN_ATTACHED(WindowElementState.ATTACHED, WindowElementState.DETACHED);

   private final WindowElementState start;
   private final WindowElementState end;

   WindowElementStateSpan(WindowElementState var3, WindowElementState var4) {
      this.start = var3;
      this.end = var4;
   }

   public boolean isEntering(WindowElementStateEvent var1) {
      return !var1.getOldState().contains(this.start) && var1.getNewState().contains(this.start);
   }

   public boolean isLeaving(WindowElementStateEvent var1) {
      return var1.getOldState().contains(this.start) && var1.getNewState().contains(this.end);
   }

   public boolean isAlive(EnumSet<WindowElementState> var1) {
      return var1.contains(this.start);
   }
}
