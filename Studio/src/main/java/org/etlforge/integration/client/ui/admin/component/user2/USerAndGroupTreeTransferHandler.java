package org.etlforge.integration.client.ui.admin.component.user2;

import com.google.common.collect.Lists;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import javax.swing.tree.TreePath;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.server.ui.user.GroupUi;
import org.etlforge.integration.client.server.ui.user.TransferableGroups;
import org.etlforge.integration.client.server.ui.user.TransferableUsers;
import org.etlforge.integration.client.server.ui.user.UserUi;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeNode;

final class USerAndGroupTreeTransferHandler extends TransferHandler {
   private DataFlavor draggedFlavor;
   private List<UserUi> users;
   private List<GroupUi> groups;
   private List<GroupMember> groupMembers;

   @Override
   protected Transferable createTransferable(JComponent var1) {
      this.processSelectedNodes((KongaTree)var1);
      if (this.users != null) {
         this.draggedFlavor = TransferableUsers.FLAVOR;
         return new TransferableUsers(this.users);
      } else if (this.groups != null) {
         this.draggedFlavor = TransferableGroups.FLAVOR;
         return new TransferableGroups(this.groups);
      } else if (this.groupMembers != null) {
         this.draggedFlavor = TransferableGroupMembers.FLAVOR;
         return new TransferableGroupMembers(this.groupMembers);
      } else {
         this.draggedFlavor = null;
         return null;
      }
   }

   private void processSelectedNodes(KongaTree var1) {
      this.users = null;
      this.groups = null;
      this.groupMembers = null;

      for (KongaTreeNode var3 : var1.getSelectedNodes()) {
         if (var3 instanceof UserUi) {
            this.handleUser((UserUi)var3);
         } else if (var3 instanceof GroupUi) {
            this.handleGroup((GroupUi)var3);
         } else if (var3 instanceof GroupMemberNode) {
            this.handleGroupMember((GroupMemberNode)var3);
         }
      }
   }

   private void handleUser(UserUi var1) {
      if (this.groups == null && this.groupMembers == null) {
         if (this.users == null) {
            this.users = Lists.newArrayList();
         }

         this.users.add(var1);
      }
   }

   private void handleGroup(GroupUi var1) {
      if (this.users == null && this.groupMembers == null) {
         if (this.groups == null) {
            this.groups = Lists.newArrayList();
         }

         this.groups.add(var1);
      }
   }

   private void handleGroupMember(GroupMemberNode var1) {
      if (this.users == null && this.groups == null) {
         if (this.groupMembers == null) {
            this.groupMembers = Lists.newArrayList();
         }

         this.groupMembers.add(var1.getUserObject());
      }
   }

   @Override
   public int getSourceActions(JComponent var1) {
      return this.draggedFlavor == TransferableGroupMembers.FLAVOR ? 2 : 1073741824;
   }

   @Override
   public boolean canImport(TransferSupport var1) {
      Transferable var2 = var1.getTransferable();
      DataFlavor var3 = null;
      if (var2.isDataFlavorSupported(TransferableUsers.FLAVOR)) {
         var3 = TransferableUsers.FLAVOR;
      } else if (var2.isDataFlavorSupported(TransferableGroups.FLAVOR)) {
         var3 = TransferableGroups.FLAVOR;
      } else if (var2.isDataFlavorSupported(TransferableGroupMembers.FLAVOR)) {
         var3 = TransferableGroupMembers.FLAVOR;
      }

      return var3 != null ? this.getDropDestination(var1, var3) != null : false;
   }

   @Override
   public boolean importData(TransferSupport var1) {
      try {
         return this.doDrop(var1);
      } catch (Exception var3) {
         this.handleDropError(var3);
         return false;
      }
   }

   private boolean doDrop(TransferSupport var1) throws Exception {
      Transferable var2 = var1.getTransferable();
      if (var2.isDataFlavorSupported(TransferableUsers.FLAVOR)) {
         return this.handleUserDrop(var1);
      } else {
         return !var2.isDataFlavorSupported(TransferableGroups.FLAVOR) && var2.isDataFlavorSupported(TransferableGroupMembers.FLAVOR)
            ? this.handleGroupMemberDrop(var1)
            : false;
      }
   }

   private void handleDropError(Exception var1) {
      ErrorLog.attention(UsersAndGroupsTree.class, "An unexpected error occurred when completing the drop: " + var1.getMessage(), var1);
   }

   private boolean handleUserDrop(TransferSupport var1) throws Exception {
      GroupUi var2 = this.getDropDestination(var1, TransferableUsers.FLAVOR);
      if (var2 == null) {
         return false;
      }

      Transferable var3 = var1.getTransferable();

      for (Object __etlItem143 : (List)var3.getTransferData(TransferableUsers.FLAVOR)) {
         UserUi var6 = (UserUi) __etlItem143;
         var2.getGroup().addMember(var6.getUser());
      }

      return true;
   }

   private boolean handleGroupMemberDrop(TransferSupport var1) throws Exception {
      GroupUi var2 = this.getDropDestination(var1, TransferableGroupMembers.FLAVOR);
      if (var2 == null) {
         return false;
      }

      Transferable var3 = var1.getTransferable();

      for (Object __etlItem158 : (List)var3.getTransferData(TransferableGroupMembers.FLAVOR)) {
         GroupMember var6 = (GroupMember) __etlItem158;
         var6.leaveGroup();
         var2.getGroup().addMember(var6.getUser());
      }

      return true;
   }

   private GroupUi getDropDestination(TransferSupport var1, DataFlavor var2) {
      JTree.DropLocation var3 = (JTree.DropLocation)var1.getDropLocation();
      TreePath var4 = var3.getPath();
      if (var4 != null) {
         Object var5 = var4.getLastPathComponent();
         if (var2 != TransferableUsers.FLAVOR && var2 != TransferableGroupMembers.FLAVOR) {
            if (var2 == TransferableGroups.FLAVOR) {
            }
         } else if (var5 instanceof GroupUi) {
            return (GroupUi)var5;
         }
      }

      return null;
   }
}
