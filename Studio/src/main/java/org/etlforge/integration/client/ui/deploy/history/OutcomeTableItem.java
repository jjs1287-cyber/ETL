package org.etlforge.integration.client.ui.deploy.history;

import java.awt.Color;
import javax.swing.Icon;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.ui.util.decorator.ListDecor;
import org.etlforge.ui.util.decorator.ListDecorator;

final class OutcomeTableItem implements ListDecorator, Comparable<OutcomeTableItem> {
   private final String text;
   private final Icon icon;
   private final Color color;

   private OutcomeTableItem(String var1, Icon var2, Color var3) {
      this.text = var1;
      this.icon = var2;
      this.color = var3;
   }

   public static OutcomeTableItem create(DeployResult var0) {
      OutcomeDecorator var1 = OutcomeDecorator.fromResult(var0);
      return new OutcomeTableItem(var1.getText(), var1.getIcon(), var1.getColor());
   }

   @Override
   public void decorate(ListDecor var1, boolean var2) {
      var1.setText(this.text);
      var1.setToolTip(this.text);
      var1.setIcon(this.icon);
      if (!var2) {
         var1.setTextColor(this.color);
      }
   }

   public int compareTo(OutcomeTableItem var1) {
      return this.text.compareTo(var1.text);
   }
}
