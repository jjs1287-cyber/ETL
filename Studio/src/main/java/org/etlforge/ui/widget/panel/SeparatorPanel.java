package org.etlforge.ui.widget.panel;

import java.awt.Component;
import java.awt.Container;
import javax.swing.JSeparator;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.OneRowPanel;

public class SeparatorPanel implements UiProvider {
   private final Component component;
   private final Container panel;

   public SeparatorPanel(Component var1) {
      this.component = var1;
      OneRowPanel var2 = new OneRowPanel();
      var2.setFillNone();
      var2.add(var1);
      var2.setFill(OneRowPanel.HORIZONTAL);
      var2.setWeightX(1.0);
      var2.withInsets(0, 3, 0, 0);
      var2.setAnchor(OneRowPanel.CENTER);
      var2.add(new JSeparator());
      this.panel = InvisiblePanel.newPanel();
      var2.addToParent(this.panel, null);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.panel);
   }

   @Override
   public void requestFocus() {
      this.component.requestFocusInWindow();
   }
}
