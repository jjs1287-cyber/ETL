package org.etlforge.application.ui.window.event;

import java.util.EnumSet;
import java.util.EventObject;
import org.etlforge.application.ui.window.WindowElement;
import org.etlforge.application.ui.window.WindowElementState;

public class WindowElementStateEvent extends EventObject {
   private final EnumSet<WindowElementState> oldState;
   private final EnumSet<WindowElementState> newState;

   public WindowElementStateEvent(WindowElement var1, EnumSet<WindowElementState> var2, EnumSet<WindowElementState> var3) {
      super(var1);
      if (var2 == null) {
         throw new NullPointerException("oldState");
      }

      if (var3 == null) {
         throw new NullPointerException("newState");
      }

      this.oldState = var2;
      this.newState = var3;
   }

   public WindowElement getSource() {
      return (WindowElement)super.getSource();
   }

   public EnumSet<WindowElementState> getOldState() {
      return this.oldState.clone();
   }

   public EnumSet<WindowElementState> getNewState() {
      return this.newState.clone();
   }
}
