package org.etlforge.integration.uidelegate.entity.renderer;

import java.awt.Container;
import javax.swing.JLabel;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.uidelegate.entity.IconStore;
import org.etlforge.integration.uidelegate.entity.tooltip.ToolTipStringFactory;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.style.TextStyle;

public class IntegrationEntityLabel implements UiProvider {
   private final JLabel label;

   public IntegrationEntityLabel(IntegrationEntity var1) {
      this(var1, TextStyles.DefaultText);
   }

   public IntegrationEntityLabel(IntegrationEntity var1, TextStyle var2) {
      this.label = var2.makeLabel(var1.getName());
      this.label.setToolTipText(ToolTipStringFactory.getToolTip(var1));
      this.label.setIcon(IconStore.defaultIcon(var1));
   }

   public void addToParent(Container var1, Object var2) {
      var1.add(this.label, var2);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.label);
   }

   @Override
   public void requestFocus() {
   }
}
