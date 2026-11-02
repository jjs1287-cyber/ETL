package org.etlforge.integration.client.ui.admin.component.user2;

import java.awt.event.ActionEvent;
import java.util.Set;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.user.User;
import org.etlforge.util.name.Nameables;

public final class AddNewUserAction extends ActiveModelAction {
   public AddNewUserAction(UserConsoleModel var1) {
      super(var1, Strings.get("UserConsole.AddUser.Name"), ClientIcons.ADD_USER_16);
      this.putValue("ShortDescription", Strings.get("UserConsole.AddUser.ToolTip"));
      this.putValue("SwingLargeIconKey", ClientIcons.ADD_USER_32);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      User var2 = this.createNewUser();
      this.getModel().addUser(var2);
   }

   private User createNewUser() {
      String var1 = getNewUserName(this.getModel());
      return new User(var1);
   }

   private static String getNewUserName(UserConsoleModel var0) {
      String var1 = Strings.get("UserConsole.NewUserName");
      Set var2 = Nameables.getUniqueNames(var0.getUsers());
      String var3 = var1;

      for (int var4 = 2; var2.contains(var3); var4++) {
         var3 = var1 + " (" + var4 + ")";
      }

      return var3;
   }
}
