package org.etlforge.integration.client.server.ui.user;

import com.google.common.base.Preconditions;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.user.Group;
import org.etlforge.ui.util.decorator.ListDecor;
import org.etlforge.ui.util.decorator.ListDecorator;

public final class GroupListUi implements ListDecorator, GroupUi {
   private final Group group;
   private GroupToolTip tooltip;

   public GroupListUi(Group var1) {
      this.group = Preconditions.checkNotNull(var1, "group");
      this.tooltip = new GroupToolTip(var1);
   }

   @Override
   public Group getGroup() {
      return this.group;
   }

   @Override
   public void decorate(ListDecor var1, boolean var2) {
      var1.setText(RoleUiUtils.getDisplayName(this.group));
      var1.setIcon(ClientIcons.GROUP_16);
      var1.setToolTip(this.tooltip.get());
   }

   @Override
   public void update() {
      this.tooltip = new GroupToolTip(this.group);
   }
}
