package org.etlforge.ui.input;

import javax.swing.JLabel;
import javax.swing.JPanel;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.DialogLayout;

public final class InputFieldDialogLayout implements UiProvider {
   private final JPanel panel;

   public InputFieldDialogLayout() {
      this(5, 10);
   }

   public InputFieldDialogLayout(int var1, int var2) {
      this.panel = new JPanel(new DialogLayout(var1, var2));
   }

   public void addInputFields(InputField... var1) {
      for (InputField var5 : var1) {
         JLabel var6 = var5.getLabel();
         if (var6 == null) {
            var6 = new JLabel(" ");
         }

         this.panel.add(var6);
         this.panel.add(var5.getInputComponent());
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.panel);
   }

   @Override
   public void requestFocus() {
      this.panel.requestFocusInWindow();
   }
}
