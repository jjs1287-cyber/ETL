package org.etlforge.ui.util.option;

public abstract class AbstractInputOption<T> implements InputOption<T> {
   private boolean active;

   @Override
   public boolean isActive() {
      return this.active;
   }

   @Override
   public void setActive(boolean var1) {
      this.active = var1;
      if (var1) {
         this.requestFocus();
      }
   }

   @Override
   public void requestFocus() {
   }
}
