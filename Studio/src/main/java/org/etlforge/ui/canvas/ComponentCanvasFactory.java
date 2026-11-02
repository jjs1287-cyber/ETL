package org.etlforge.ui.canvas;

import java.awt.GridBagConstraints;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.ui.UiProvider;

public final class ComponentCanvasFactory {
   private ComponentCanvasFactory() {
      throw new UnsupportedOperationException();
   }

   public static ComponentCanvas<JPanel> eastBorder(UiProvider var0) {
      return BorderCanvas.createEastDecor(var0);
   }

   public static ComponentCanvas<JPanel> westBorder(UiProvider var0) {
      return BorderCanvas.createWestDecor(var0);
   }

   public static ComponentCanvas<JPanel> northBorder(UiProvider var0) {
      return BorderCanvas.createNorthDecor(var0);
   }

   public static ComponentCanvas<JPanel> southBorder(UiProvider var0) {
      return BorderCanvas.createSouthDecor(var0);
   }

   public static ComponentCanvas<JComponent> gridBag(JComponent var0, GridBagConstraints var1) {
      return new GridBagCanvas(var0, var1);
   }

   public static ComponentCanvas<JComponent> gridBag(JComponent var0, GridBagConstraints var1, UiProvider var2) {
      return new GridBagCanvas(var0, var1, var2);
   }
}
