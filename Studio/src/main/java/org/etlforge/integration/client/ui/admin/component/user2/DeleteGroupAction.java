package org.etlforge.integration.client.ui.admin.component.user2;

import java.awt.event.ActionEvent;
import org.etlforge.application.ui.Alert;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.user.Group;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.selection.SelectionSource;

public class DeleteGroupAction extends AbstractSingleObjectSelectionAction<Group> {
   private final UserConsoleModel model;

   public DeleteGroupAction(UserConsoleModel var1, SelectionSource var2) {
      super(Group.class, "Delete Group", ClientIcons.DELETE_GROUP_16);
      this.model = var1;
      this.putValue("SwingLargeIconKey", ClientIcons.DELETE_GROUP_32);
      this.setSelectionSource(var2);
   }

   protected boolean isEnabledFor(Group var1) {
      return !this.model.isReadOnly();
   }

   protected void run(Group var1, ActionEvent var2) {
      try {
         this.model.removeGroup(var1);
      } catch (VetoedModelChangeException var4) {
         Alert.error(var4.getMessage(), "Not Allowed");
      }
   }

   public void setReadOnly(boolean var1) {
      this.setEnabled(!var1 && this.getSelectedObject() != null);
   }
}
