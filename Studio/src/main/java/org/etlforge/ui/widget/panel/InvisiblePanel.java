package org.etlforge.ui.widget.panel;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.canvas.ContainerCanvas;
import org.etlforge.ui.widget.button.KongaButton;

public final class InvisiblePanel {
   public static JPanel newPanel() {
      JPanel var0 = new JPanel(getLayout());
      var0.setOpaque(false);
      return var0;
   }

   public static JPanel of(Component var0) {
      JPanel var1 = newPanel();
      var1.add(var0);
      return var1;
   }

   public static JPanel of(Action var0) {
      return of(new KongaButton(var0));
   }

   public static JPanel of(UiProvider var0) {
      JPanel var1 = newPanel();
      ContainerCanvas.decorate(var0, var1, null);
      return var1;
   }

   public static JComponent convert(JComponent var0) {
      var0.setLayout(getLayout());
      return var0;
   }

   public static JComponent convertAndAdd(JComponent var0, Component var1) {
      convert(var0);
      var0.removeAll();
      var0.add(var1);
      return var0;
   }

   private static LayoutManager getLayout() {
      return new FlowLayout(3, 0, 0);
   }

   private InvisiblePanel() {
   }
}
