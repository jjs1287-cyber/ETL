package org.etlforge.ui.dialog;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;

public final class ComponentDialogTrigger implements DialogTrigger, UiProvider {
   private final JComponent component;

   public ComponentDialogTrigger(JComponent var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("c cannot be null.");
      }

      this.component = var1;
   }

   @Override
   public Window getDialogOwner() {
      Window var1 = SwingUtilities.getWindowAncestor(this.component);
      return !(var1 instanceof Frame) && !(var1 instanceof Dialog) ? null : var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.component);
   }

   @Override
   public void requestFocus() {
      this.component.requestFocusInWindow();
   }
}
