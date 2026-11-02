package org.etlforge.integration.client.ui.admin.component.user2;

import java.awt.event.ActionEvent;
import org.etlforge.application.ui.Alert;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.user.User;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.selection.SelectionSource;

public class DeleteUserAction extends AbstractSingleObjectSelectionAction<User> {
   private final UserConsoleModel model;

   public DeleteUserAction(UserConsoleModel var1, SelectionSource var2) {
      super(User.class, "Delete User", ClientIcons.DELETE_USER_16);
      this.model = var1;
      this.putValue("SwingLargeIconKey", ClientIcons.DELETE_USER_32);
      this.setSelectionSource(var2);
   }

   protected boolean isEnabledFor(User var1) {
      return !this.model.isReadOnly();
   }

   protected void run(User var1, ActionEvent var2) {
      try {
         this.model.removeUser(var1);
      } catch (VetoedModelChangeException var4) {
         Alert.error(var4.getMessage(), "Not Allowed");
      }
   }

   public void setReadOnly(boolean var1) {
      this.setEnabled(!var1 && this.getSelectedObject() != null);
   }
}
