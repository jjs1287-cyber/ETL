package org.etlforge.integration.client.ui.admin.component.user2;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.ui.ServerInfoNode;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.Role;
import org.etlforge.integration.user.User;
import org.etlforge.ui.util.decorator.TreeNodeDecor;
import org.etlforge.ui.util.decorator.TreeNodeDecorator;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.util.name.NameSorter;

final class UserAndGroupTreeModelBuilder {
   private final UserConsoleModel consoleModel;
   private ServerInfoNode root;
   private UserAndGroupTreeModelBuilder.UserRootNode usersRoot;
   private UserAndGroupTreeModelBuilder.GroupRootNode groupsRoot;
   private DefaultKongaTreeModel treeModel;

   public UserAndGroupTreeModelBuilder(UserConsoleModel var1) {
      this.consoleModel = var1;
   }

   public ServerInfoNode getCurrentRoot() {
      return this.root;
   }

   public KongaTreeModel getCurrentTreeModel() {
      return this.treeModel;
   }

   public DefaultKongaTreeModel build() {
      this.root = new ServerInfoNode(this.consoleModel.getCurrentServer());
      if (!this.consoleModel.isEmpty()) {
         this.buildUserBranch();
         this.buildGroupBranch();
      }

      this.treeModel = new DefaultKongaTreeModel(this.root);
      return this.treeModel;
   }

   public List<? extends KongaTreeNode> getNodesToExpand() {
      return Lists.newArrayList(this.usersRoot, this.groupsRoot);
   }

   public void setNewServerAsRoot(ServerInfo var1) {
      this.root.setServerInfo(var1);
      this.treeModel.nodeChanged(this.root);
   }

   public UserNode insertNewUser(User var1) {
      UserNode var2 = new UserNode(var1);
      this.treeModel.insertNode(var2, this.usersRoot);
      return var2;
   }

   public void deleteUser(User var1) {
      KongaTreeNode var2 = findChildNode(this.usersRoot, var1);
      if (var2 != null) {
         this.treeModel.removeNode(var2);
      }
   }

   public void userChanged(User var1) {
      UserNode var2 = (UserNode)findChildNode(this.usersRoot, var1);
      if (var2 != null) {
         var2.update();
         this.treeModel.nodeChanged(var2);
      }

      for (KongaTreeNode var4 : this.groupsRoot.children()) {
         GroupMemberNode var5 = (GroupMemberNode)findGroupMemberNode((GroupNode)var4, var1);
         if (var5 != null) {
            var5.userChanged();
            this.treeModel.nodeChanged(var5);
         }
      }
   }

   public GroupNode insertNewGroup(Group var1) {
      GroupNode var2 = new GroupNode(var1);
      this.treeModel.insertNode(var2, this.groupsRoot);
      return var2;
   }

   public void deleteGroup(Group var1) {
      KongaTreeNode var2 = findChildNode(this.groupsRoot, var1);
      if (var2 != null) {
         this.treeModel.removeNode(var2);
      }
   }

   public void groupChanged(Group var1) {
      GroupNode var2 = (GroupNode)findChildNode(this.groupsRoot, var1);
      if (var2 != null) {
         var2.update();
         this.treeModel.nodeChanged(var2);
      }
   }

   public GroupMemberNode addGroupMember(Group var1, User var2) {
      KongaTreeNode var3 = findChildNode(this.groupsRoot, var1);
      if (var3 != null) {
         GroupMemberNode var4 = new GroupMemberNode(new GroupMember(var2, var1));
         this.treeModel.insertNode(var4, var3);
         return var4;
      } else {
         return null;
      }
   }

   public void deleteGroupMember(Group var1, User var2) {
      GroupNode var3 = (GroupNode)findChildNode(this.groupsRoot, var1);
      if (var3 != null) {
         KongaTreeNode var4 = findGroupMemberNode(var3, var2);
         if (var4 != null) {
            this.treeModel.removeNode(var4);
         }
      }
   }

   private void buildUserBranch() {
      this.usersRoot = new UserAndGroupTreeModelBuilder.UserRootNode(this.root);

      for (User var2 : this.consoleModel.getUsers()) {
         new UserNode(this.usersRoot, var2);
      }
   }

   private void buildGroupBranch() {
      this.groupsRoot = new UserAndGroupTreeModelBuilder.GroupRootNode(this.root);

      for (Group var2 : this.consoleModel.getGroups()) {
         GroupNode var3 = new GroupNode(this.groupsRoot, var2);
         List var4 = var2.getMembers();
         Collections.sort(var4, NameSorter.ignoreCase());

         for (Object __etlItem146 : var4) {
            User var6 = (User) __etlItem146;
            GroupMember var7 = new GroupMember(var6, var2);
            new GroupMemberNode(var3, var7);
         }
      }
   }

   private static KongaTreeNode findChildNode(KongaTreeNode var0, Role var1) {
      for (KongaTreeNode var3 : var0.children()) {
         if (var3.getUserObject() == var1) {
            return var3;
         }
      }

      return null;
   }

   private static KongaTreeNode findGroupMemberNode(GroupNode var0, User var1) {
      GroupMember var2 = new GroupMember(var1, var0.getUserObject());

      for (KongaTreeNode var4 : var0.children()) {
         if (var4.getUserObject().equals(var2)) {
            return var4;
         }
      }

      return null;
   }

   private static class FolderNode extends DefaultKongaTreeNode implements TreeNodeDecorator {
      public FolderNode(KongaTreeNode var1, String var2) {
         super(var1, var2, true);
      }

      @Override
      public void decorate(TreeNodeDecor var1) {
         var1.setText(this.getDisplayName());
         var1.setIcon(null);
         var1.setToolTip(null);
         var1.setTextStyle(TextStyles.DefaultBold.asTextDecorator());
      }
   }

   private static class GroupRootNode extends UserAndGroupTreeModelBuilder.FolderNode implements GroupRoot {
      public GroupRootNode(KongaTreeNode var1) {
         super(var1, "Groups");
      }
   }

   private static class UserRootNode extends UserAndGroupTreeModelBuilder.FolderNode implements UserRoot {
      public UserRootNode(KongaTreeNode var1) {
         super(var1, "Users");
      }
   }
}
