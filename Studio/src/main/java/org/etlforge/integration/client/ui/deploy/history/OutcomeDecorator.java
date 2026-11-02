package org.etlforge.integration.client.ui.deploy.history;

import java.awt.Color;
import javax.swing.Icon;
import javax.swing.JLabel;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.integration.client.deploy.serverapi.DeployResultVisitor;

final class OutcomeDecorator {
   private final String text;
   private final Icon icon;
   private final Color color;

   public static OutcomeDecorator fromResult(DeployResult var0) {
      OutcomeDecorator.Factory var1 = new OutcomeDecorator.Factory();
      return var1.visit(var0);
   }

   private OutcomeDecorator(String var1, Icon var2, Color var3) {
      this.text = var1;
      this.icon = var2;
      this.color = var3;
   }

   public Icon getIcon() {
      return this.icon;
   }

   public String getText() {
      return this.text;
   }

   public Color getColor() {
      return this.color;
   }

   public void decorate(JLabel var1) {
      var1.setText(this.getText());
      var1.setIcon(this.getIcon());
      var1.setForeground(this.getColor());
   }

   private static class Factory extends DeployResultVisitor<OutcomeDecorator> {
      private Factory() {
      }

      protected OutcomeDecorator onFailure(DeployResult var1) {
         return new OutcomeDecorator(Strings.get("Failure"), CommonIcons.ERROR_16, Colors.ERROR_TEXT_COLOR);
      }

      protected OutcomeDecorator onWarnings(DeployResult var1) {
         return new OutcomeDecorator(Strings.get("Warning"), CommonIcons.WARNING_16, Color.BLACK);
      }

      protected OutcomeDecorator onSuccess(DeployResult var1) {
         return new OutcomeDecorator(Strings.get("Success"), CommonIcons.SUCCESS_16, Color.BLACK);
      }
   }
}
