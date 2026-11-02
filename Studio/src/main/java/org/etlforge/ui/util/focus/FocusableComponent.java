package org.etlforge.ui.util.focus;

import com.google.common.base.Preconditions;
import java.awt.Component;
import java.awt.event.FocusListener;

public final class FocusableComponent implements Focusable {
   private final Component adaptee;

   public FocusableComponent(Component var1) {
      this.adaptee = Preconditions.checkNotNull(var1);
   }

   @Override
   public void requestFocus() {
      this.adaptee.requestFocusInWindow();
   }

   @Override
   public void addFocusListener(FocusListener var1) {
      this.adaptee.addFocusListener(var1);
   }

   @Override
   public void removeFocusListener(FocusListener var1) {
      this.adaptee.removeFocusListener(var1);
   }
}
