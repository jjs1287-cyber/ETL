package org.etlforge.ui.canvas;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.ComponentCustomizer;

final class BorderCanvas implements ComponentCanvas<JPanel> {
   private final JPanel decor = new JPanel(new BorderLayout());
   private final Object position;

   private BorderCanvas(Object var1, UiProvider var2) {
      this.decor.setOpaque(false);
      this.position = var1;
      if (var2 != null) {
         var2.decorate(this);
      }
   }

   @Override
   public void display(Component var1) {
      this.decor.add(var1, this.position);
   }

   @Override
   public JComponent getUi() {
      return this.decor;
   }

   @Override
   public void customize(ComponentCustomizer<JPanel> var1) {
      var1.customize(this.decor);
   }

   static BorderCanvas createEastDecor(UiProvider var0) {
      return new BorderCanvas("East", var0);
   }

   static BorderCanvas createWestDecor(UiProvider var0) {
      return new BorderCanvas("West", var0);
   }

   static BorderCanvas createNorthDecor(UiProvider var0) {
      return new BorderCanvas("North", var0);
   }

   static BorderCanvas createSouthDecor(UiProvider var0) {
      return new BorderCanvas("South", var0);
   }

   static BorderCanvas createCenterDecor(UiProvider var0) {
      return new BorderCanvas("Center", var0);
   }
}
