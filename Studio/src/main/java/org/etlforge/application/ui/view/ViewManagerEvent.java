package org.etlforge.application.ui.view;

import java.util.EventObject;

public final class ViewManagerEvent extends EventObject {
   private final View view;

   public ViewManagerEvent(ViewManager var1, View var2) {
      super(var1);
      this.view = var2;
   }

   public ViewManager getSource() {
      return (ViewManager)super.getSource();
   }

   public View getView() {
      return this.view;
   }
}
