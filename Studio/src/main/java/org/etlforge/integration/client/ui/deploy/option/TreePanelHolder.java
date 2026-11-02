package org.etlforge.integration.client.ui.deploy.option;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Action;
import javax.swing.JPanel;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class TreePanelHolder extends JPanel implements UiCanvas {
   private Component centerComponent;
   private final Component southComponent;
   private Dimension preferredSize;
   private boolean visible = true;

   TreePanelHolder(Action var1) {
      super(new BorderLayout(0, 5));
      this.setOpaque(false);
      BorderLayoutBuilder var2 = new BorderLayoutBuilder();
      var2.center(" ").east(InvisiblePanel.of(var1)).nonOpaque();
      this.southComponent = var2.container();
   }

   @Override
   public void display(Component var1) {
      this.centerComponent = var1;
      if (this.preferredSize == null) {
         this.preferredSize = this.centerComponent.getPreferredSize();
      }

      if (this.visible) {
         this.add(this.centerComponent, "Center");
         this.add(this.southComponent, "South");
      }
   }

   @Override
   public void setVisible(boolean var1) {
      if (var1 != this.visible) {
         if (var1) {
            if (this.centerComponent != null) {
               this.add(this.centerComponent, "Center");
            }

            this.add(this.southComponent, "South");
         } else {
            this.removeAll();
         }

         this.visible = var1;
      }
   }

   @Override
   public Dimension getPreferredSize() {
      int var1 = super.getPreferredSize().width;
      int var2 = 2;
      if (this.visible && this.centerComponent != null && this.preferredSize != null) {
         var1 = this.preferredSize.width;
         var2 = this.preferredSize.height;
      }

      var1 = Math.max(600, var1);
      return new Dimension(var1, var2);
   }

   @Override
   public Dimension getMaximumSize() {
      return this.getPreferredSize();
   }
}
