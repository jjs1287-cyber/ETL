package org.etlforge.integration.client.ui.admin.component.user2;

import java.util.List;
import org.etlforge.application.ui.Alert;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.ui.selection.AbstractSingleTypeSelectionAction;
import org.etlforge.ui.selection.SelectionSource;

public class RemoveGroupMemberAction extends AbstractSingleTypeSelectionAction<GroupMember> {
   private boolean readOnly;

   public RemoveGroupMemberAction(SelectionSource var1) {
      this(var1, "Remove Group Member(s)");
   }

   public RemoveGroupMemberAction(SelectionSource var1, String var2) {
      super(GroupMember.class, var2, ClientIcons.getIcon("GroupMember.Delete.16"));
      this.putValue("SwingLargeIconKey", ClientIcons.getIcon("GroupMember.Delete.32"));
      this.setSelectionSource(var1);
   }

   protected boolean isEnabledFor(GroupMember var1) {
      return !this.readOnly;
   }

   @Override
   protected void run(List<GroupMember> var1) {
      for (GroupMember var3 : var1) {
         try {
            var3.leaveGroup();
         } catch (VetoedModelChangeException var5) {
            Alert.error(var5.getMessage(), "Not Allowed");
         }
      }
   }

   public void setReadOnly(boolean var1) {
      this.readOnly = var1;
      List var2 = this.getSelectedObjects();
      this.setEnabled(!var1 && var2 != null && !var2.isEmpty());
   }
}
