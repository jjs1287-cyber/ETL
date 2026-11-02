package org.etlforge.integration.client.ui.deploy.history;

import java.awt.Color;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.integration.client.deploy.serverapi.DeployResultVisitor;
import org.etlforge.integration.client.ui.deploy.resources.DeployResources;
import org.etlforge.ui.util.decorator.ListDecor;
import org.etlforge.ui.util.decorator.ListDecorator;

final class MessageTableItem implements ListDecorator, Comparable<MessageTableItem> {
   private final String text;
   private final Color color;

   private MessageTableItem(String var1, Color var2) {
      this.text = var1;
      this.color = var2;
   }

   public static MessageTableItem create(DeployResult var0) {
      MessageTableItem.Factory var1 = new MessageTableItem.Factory();
      return var1.visit(var0);
   }

   @Override
   public void decorate(ListDecor var1, boolean var2) {
      var1.setText(this.text);
      var1.setIcon(null);
      var1.setToolTip(this.text);
      if (!var2) {
         var1.setTextColor(this.color);
      }
   }

   public int compareTo(MessageTableItem var1) {
      return this.text.compareToIgnoreCase(var1.text);
   }

   private static class Factory extends DeployResultVisitor<MessageTableItem> {
      private Factory() {
      }

      protected MessageTableItem onFailure(DeployResult var1) {
         String var2 = var1.getCause().getMessage();
         if (var2 == null || var2.length() == 0) {
            var2 = DeployResources.getString("UnknownError");
         }

         return new MessageTableItem(var2, Colors.ERROR_TEXT_COLOR);
      }

      protected MessageTableItem onWarnings(DeployResult var1) {
         return new MessageTableItem(Strings.get("Deploy.History.Table.Message.Warning"), Color.BLACK);
      }

      protected MessageTableItem onSuccess(DeployResult var1) {
         return new MessageTableItem(Strings.get("Deploy.History.Table.Message.Success"), Color.BLACK);
      }
   }
}
