package org.etlforge.ui.util.option;

import org.etlforge.ui.UiCanvas;

public final class NoUiInputOption<T> implements InputOption<T> {
   private boolean active;
   private final T option;
   private final String selectorText;

   public NoUiInputOption(T var1, String var2) {
      this.option = (T)var1;
      this.selectorText = var2;
   }

   @Override
   public void decorate(UiCanvas var1) {
   }

   @Override
   public void requestFocus() {
   }

   @Override
   public void decorate(OptionSelector var1) {
      var1.setText(this.selectorText);
   }

   @Override
   public T getValue() {
      return this.option;
   }

   @Override
   public boolean isActive() {
      return this.active;
   }

   @Override
   public void setActive(boolean var1) {
      this.active = var1;
   }
}
