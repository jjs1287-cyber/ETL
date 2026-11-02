package org.etlforge.integration.client.ui.deploy.history;

import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.ui.util.decorator.ListDecor;
import org.etlforge.ui.util.decorator.ListDecorator;

final class UserTableItem implements ListDecorator, Comparable<UserTableItem> {
   private final String name;

   private UserTableItem(String var1) {
      this.name = var1;
   }

   static UserTableItem create(DeployResult var0) {
      String var1 = var0.getUser();
      if (var1 == null || var1.length() == 0) {
         var1 = Strings.get("Deploy.Result.UnknownUser");
      }

      return new UserTableItem(var1);
   }

   @Override
   public String toString() {
      return this.name;
   }

   @Override
   public void decorate(ListDecor var1, boolean var2) {
      var1.setText(this.name);
      var1.setToolTip(this.name);
      var1.setIcon(ClientIcons.USER_16);
   }

   public int compareTo(UserTableItem var1) {
      return this.name.compareTo(var1.name);
   }
}
