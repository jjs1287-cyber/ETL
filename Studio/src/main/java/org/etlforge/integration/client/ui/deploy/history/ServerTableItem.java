package org.etlforge.integration.client.ui.deploy.history;

import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.ui.ServerToolTip;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.ui.util.decorator.ListDecor;
import org.etlforge.ui.util.decorator.ListDecorator;

final class ServerTableItem implements ListDecorator, Comparable<ServerTableItem> {
   private final String text;
   private final String tooltip;

   private ServerTableItem(ServerInfo var1) {
      this.text = var1.getName();
      this.tooltip = new ServerToolTip(var1).get();
   }

   static ServerTableItem create(DeployResult var0) {
      return new ServerTableItem(var0.getServer());
   }

   @Override
   public void decorate(ListDecor var1, boolean var2) {
      var1.setText(this.text);
      var1.setToolTip(this.tooltip);
      var1.setIcon(ClientIcons.SERVER_16);
   }

   public int compareTo(ServerTableItem var1) {
      return this.text.compareToIgnoreCase(var1.text);
   }
}
