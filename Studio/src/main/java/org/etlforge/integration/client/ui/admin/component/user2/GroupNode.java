package org.etlforge.integration.client.ui.admin.component.user2;

import javax.swing.Icon;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.server.ui.user.GroupToolTip;
import org.etlforge.integration.client.server.ui.user.GroupUi;
import org.etlforge.integration.client.server.ui.user.RoleUiUtils;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.user.Group;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.decorator.TreeNodeDecor;
import org.etlforge.ui.util.decorator.TreeNodeDecorator;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.NodeStatus;

public class GroupNode extends DefaultKongaTreeNode implements GroupUi, TreeNodeDecorator {
   private String tooltip;

   public GroupNode(Group var1) {
      this(null, var1);
   }

   public GroupNode(KongaTreeNode var1, Group var2) {
      super(var1, var2, true);
      this.generateToolTip();
   }

   @Override
   public Group getGroup() {
      return this.getUserObject();
   }

   @Override
   public void update() {
      this.generateToolTip();
   }

   public Group getUserObject() {
      return (Group)super.getUserObject();
   }

   @Override
   public String getDisplayName() {
      return RoleUiUtils.getDisplayName(this.getGroup());
   }

   @Override
   public Icon getNodeIcon(NodeStatus var1) {
      return ClientIcons.GROUP_16;
   }

   @Override
   public void decorate(TreeNodeDecor var1) {
      var1.setText(this.getDisplayName());
      var1.setIcon(ClientIcons.GROUP_16);
      var1.setToolTip(this.tooltip);
      if (StringUtils.isNotBlank(this.getGroup().getName())) {
         var1.setTextStyle(TextStyles.TreeLeafText.asTextDecorator());
      } else {
         var1.setTextStyle(TextStyle.newStyle(TextStyles.TreeLeafText, 2).asTextDecorator());
      }
   }

   private void generateToolTip() {
      this.tooltip = new GroupToolTip(this.getUserObject()).get();
   }
}
