package org.etlforge.integration.client.server.ui.user;

import com.google.common.base.Preconditions;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.user.User;
import org.etlforge.ui.util.decorator.ListDecor;
import org.etlforge.ui.util.decorator.ListDecorator;

public final class UserListUi implements ListDecorator, UserUi {
   private final User user;
   private UserToolTip tooltip;

   public UserListUi(User var1) {
      this.user = Preconditions.checkNotNull(var1, "user");
      this.tooltip = new UserToolTip(var1);
   }

   @Override
   public User getUser() {
      return this.user;
   }

   @Override
   public void decorate(ListDecor var1, boolean var2) {
      var1.setText(RoleUiUtils.getDisplayName(this.user));
      var1.setIcon(ClientIcons.USER_16);
      var1.setToolTip(this.tooltip.get());
   }

   @Override
   public void update() {
      this.tooltip = new UserToolTip(this.user);
   }
}
