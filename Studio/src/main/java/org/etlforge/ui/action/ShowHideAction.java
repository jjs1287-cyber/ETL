package org.etlforge.ui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public abstract class ShowHideAction extends AbstractAction {
   private static final String SHOW_SYMBOL = " >>";
   private static final String HIDE_SYMBOL = "<< ";
   private final String name;
   private boolean showing;

   public ShowHideAction(String var1) {
      this(var1, true);
   }

   public ShowHideAction(String var1, boolean var2) {
      super(var2 ? getNameWhenShowing(var1) : getNameWhenHiding(var1));
      this.name = var1;
      this.showing = var2;
   }

   @Override
   public final void actionPerformed(ActionEvent var1) {
      if (this.showing) {
         this.goToHiddenState();
      } else {
         this.goToShowingState();
      }
   }

   private void goToHiddenState() {
      this.putValue("Name", getNameWhenHiding(this.name));
      this.hide();
      this.showing = false;
   }

   private void goToShowingState() {
      this.putValue("Name", getNameWhenShowing(this.name));
      this.show();
      this.showing = true;
   }

   public final void setShowing(boolean var1) {
      if (this.showing != var1) {
         if (var1) {
            this.goToShowingState();
         } else {
            this.goToHiddenState();
         }
      }
   }

   public final boolean isShowing() {
      return this.showing;
   }

   protected abstract void hide();

   protected abstract void show();

   private static String getNameWhenShowing(String var0) {
      return "<< " + var0;
   }

   private static String getNameWhenHiding(String var0) {
      return var0 + " >>";
   }
}
