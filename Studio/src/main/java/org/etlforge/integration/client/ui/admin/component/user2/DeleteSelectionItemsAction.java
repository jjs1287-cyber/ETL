package org.etlforge.integration.client.ui.admin.component.user2;

import java.util.List;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.Role;
import org.etlforge.integration.user.User;
import org.etlforge.ui.selection.AbstractSingleTypeSelectionAction;
import org.etlforge.ui.selection.SelectionSource;

public final class DeleteSelectionItemsAction extends AbstractSingleTypeSelectionAction<Role> {
   private final UserConsoleModel model;

   public DeleteSelectionItemsAction(UserConsoleModel var1, SelectionSource var2) {
      super(Role.class);
      this.model = var1;
      ActionDecorators.DELETE.decorate(this);
      this.setSelectionSource(var2);
   }

   protected boolean isEnabledFor(Role var1) {
      return !this.model.isReadOnly();
   }

   public void setReadOnly(boolean var1) {
      this.setEnabled(!var1 && !this.getSelectedObjects().isEmpty());
   }

   @Override
   protected void run(List<Role> var1) {
      for (Role var3 : var1) {
         this.delete(var3);
      }
   }

   private void delete(Role var1) {
      try {
         if (var1 instanceof User) {
            this.model.removeUser((User)var1);
         } else if (var1 instanceof Group) {
            this.model.removeGroup((Group)var1);
         } else if (var1 instanceof GroupMember) {
            ((GroupMember)var1).leaveGroup();
         }
      } catch (VetoedModelChangeException var3) {
         Alert.error(var3.getMessage(), "Not Allowed");
      }
   }
}
