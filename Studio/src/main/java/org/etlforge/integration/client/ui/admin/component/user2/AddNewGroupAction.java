package org.etlforge.integration.client.ui.admin.component.user2;

import java.awt.event.ActionEvent;
import java.util.Set;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.user.Group;
import org.etlforge.util.name.Nameables;

public class AddNewGroupAction extends ActiveModelAction {
   public AddNewGroupAction(UserConsoleModel var1) {
      super(var1, Strings.get("UserConsole.AddGroup.Name"), ClientIcons.ADD_GROUP_16);
      this.putValue("ShortDescription", Strings.get("UserConsole.AddGroup.ToolTip"));
      this.putValue("SwingLargeIconKey", ClientIcons.ADD_GROUP_32);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      Group var2 = this.createNewGroup();
      this.getModel().addGroup(var2);
   }

   private Group createNewGroup() {
      String var1 = getNewGroupName(this.getModel());
      return new Group(var1);
   }

   private static String getNewGroupName(UserConsoleModel var0) {
      String var1 = Strings.get("UserConsole.NewGroupName");
      Set var2 = Nameables.getUniqueNames(var0.getGroups());
      String var3 = var1;

      for (int var4 = 2; var2.contains(var3); var4++) {
         var3 = var1 + " (" + var4 + ")";
      }

      return var3;
   }
}
