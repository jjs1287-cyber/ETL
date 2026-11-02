package org.etlforge.integration.client.ui.admin.component.user2;

import javax.swing.Icon;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.server.ui.user.RoleUiUtils;
import org.etlforge.integration.client.server.ui.user.UserToolTip;
import org.etlforge.integration.client.server.ui.user.UserUi;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.user.User;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.decorator.TreeNodeDecor;
import org.etlforge.ui.util.decorator.TreeNodeDecorator;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.NodeStatus;

public class UserNode extends DefaultKongaTreeNode implements UserUi, TreeNodeDecorator {
   private String tooltip;

   public UserNode(User var1) {
      this(null, var1);
   }

   public UserNode(KongaTreeNode var1, User var2) {
      super(var1, var2, false);
      this.generateToolTip();
   }

   @Override
   public User getUser() {
      return this.getUserObject();
   }

   @Override
   public void update() {
      this.generateToolTip();
   }

   public User getUserObject() {
      return (User)super.getUserObject();
   }

   @Override
   public String getDisplayName() {
      return RoleUiUtils.getDisplayName(this.getUser());
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
      if (StringUtils.isNotBlank(this.getUser().getName())) {
         var1.setTextStyle(TextStyles.TreeLeafText.asTextDecorator());
      } else {
         var1.setTextStyle(TextStyle.newStyle(TextStyles.TreeLeafText, 2).asTextDecorator());
      }
   }

   private void generateToolTip() {
      this.tooltip = new UserToolTip(this.getUserObject()).get();
   }
}
