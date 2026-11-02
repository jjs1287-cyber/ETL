package org.etlforge.integration.client.server.ui;

import javax.swing.Icon;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.ui.resources.ServerUiResources;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.ui.util.decorator.TreeNodeDecor;
import org.etlforge.ui.util.decorator.TreeNodeDecorator;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.NodeStatus;

public final class ServerInfoNode extends DefaultKongaTreeNode implements TreeNodeDecorator {
   private static final String DISCONNECTED = ServerUiResources.getString("ServerInfoNode.Disconnected");

   public ServerInfoNode(ServerInfo var1) {
      super(var1, true);
   }

   @Override
   public String getDisplayName() {
      ServerInfo var1 = this.getUserObject();
      return var1 != null ? var1.getName() : DISCONNECTED;
   }

   public void setServerInfo(ServerInfo var1) {
      this.setUserObject(var1);
   }

   @Override
   public void setUserObject(Object var1) {
      if (var1 != null && !(var1 instanceof ServerInfo)) {
         throw new IllegalArgumentException("userObject must be null or a ServerInfo instance");
      }

      super.setUserObject(var1);
   }

   public ServerInfo getUserObject() {
      return (ServerInfo)super.getUserObject();
   }

   @Override
   public Icon getNodeIcon(NodeStatus var1) {
      return ClientIcons.SERVER_16;
   }

   @Override
   public void decorate(TreeNodeDecor var1) {
      var1.setText(this.getDisplayName());
      var1.setIcon(this.getNodeIcon(NodeStatus.CLOSED));
      var1.setToolTip(this.getUserObject() != null ? new ServerToolTip(this.getUserObject()).get() : null);
      var1.setTextStyle(TextStyles.TreeRootFolderText.asTextDecorator());
   }
}
