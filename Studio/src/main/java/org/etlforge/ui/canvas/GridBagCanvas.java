package org.etlforge.ui.canvas;

import java.awt.Component;
import java.awt.GridBagConstraints;
import javax.swing.JComponent;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.ComponentCustomizer;

final class GridBagCanvas implements ComponentCanvas<JComponent> {
   private JComponent decor;
   private GridBagConstraints constraints;

   GridBagCanvas(JComponent var1, GridBagConstraints var2) {
      this.decor = var1;
      this.constraints = var2;
   }

   GridBagCanvas(JComponent var1, GridBagConstraints var2, UiProvider var3) {
      this(var1, var2);
      var3.decorate(this);
   }

   @Override
   public void customize(ComponentCustomizer<JComponent> var1) {
      var1.customize(this.decor);
   }

   @Override
   public JComponent getUi() {
      return this.decor;
   }

   @Override
   public void display(Component var1) {
      this.decor.add(var1, this.constraints);
   }
}
