package org.etlforge.integration.client.ui.admin.component.user2;

import com.google.common.base.Preconditions;
import javax.swing.Icon;
import javax.swing.tree.MutableTreeNode;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.server.ui.user.UserToolTip;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.ui.util.decorator.TreeNodeDecor;
import org.etlforge.ui.util.decorator.TreeNodeDecorator;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.NodeStatus;

public final class GroupMemberNode extends DefaultKongaTreeNode implements TreeNodeDecorator {
   private String tooltip;

   public GroupMemberNode(GroupMember var1) {
      this(null, var1);
   }

   public GroupMemberNode(GroupNode var1, GroupMember var2) {
      super(var1, var2, false);
   }

   @Override
   public void setParent(MutableTreeNode var1) {
      Preconditions.checkArgument(var1 == null || var1 instanceof GroupNode);
      super.setParent(var1);
   }

   public GroupMember getUserObject() {
      return (GroupMember)super.getUserObject();
   }

   @Override
   public Icon getNodeIcon(NodeStatus var1) {
      return ClientIcons.USER_16;
   }

   @Override
   public void decorate(TreeNodeDecor var1) {
      var1.setText(this.getDisplayName());
      var1.setIcon(ClientIcons.USER_16);
      var1.setToolTip(this.tooltip);
      var1.setTextStyle(TextStyles.TreeLeafText.asTextDecorator());
   }

   public void userChanged() {
      this.generateToolTip();
   }

   private void generateToolTip() {
      this.tooltip = new UserToolTip(this.getUserObject().getUser()).get();
   }
}
