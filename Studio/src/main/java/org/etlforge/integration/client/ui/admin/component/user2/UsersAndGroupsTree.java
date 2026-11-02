package org.etlforge.integration.client.ui.admin.component.user2;

import com.google.common.base.Supplier;
import java.beans.PropertyChangeEvent;
import javax.swing.Action;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.tree.TreeNode;
import org.etlforge.application.ui.widget.tree.TreePanel;
import org.etlforge.application.ui.widget.tree.TreeWaitNodeService;
import org.etlforge.integration.client.server.ui.ServerInfoNode;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.User;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.selection.Selection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.decorator.TreeNodeDecorRenderer;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeNodeObjectConverter;
import org.etlforge.ui.widget.tree.TreeSelectionSource;

public final class UsersAndGroupsTree implements UsersAndGroupsViewer {
   private final UserConsoleModel model;
   private final UserConsoleModelListener modelListener;
   private final KongaTree tree;
   private final UserAndGroupTreeModelBuilder treeModelBuilder;
   private final TreeWaitNodeService waitNodeService;
   private final TreeSelectionSource selectionSource;
   private Supplier<JPopupMenu> contextMenuProvider;

   public UsersAndGroupsTree(UserConsoleModel var1) {
      this.model = var1;
      this.treeModelBuilder = new UserAndGroupTreeModelBuilder(var1);
      this.tree = this.createTree();
      this.selectionSource = this.createSelectionSource();
      this.waitNodeService = new TreeWaitNodeService(this.tree, "Downloading user and group information from the server");
      var1.addModelListener(this.modelListener = new UsersAndGroupsTree.ModelListener());
      this.setDragAndDropEnabled(true);
   }

   @Override
   public void dispose() {
      this.model.removeModelListener(this.modelListener);
   }

   @Override
   public void setDefaultAction(SelectionAction var1) {
      this.selectionSource.setDefaultAction(var1);
   }

   @Override
   public void addKeyBinding(Object var1, Action var2, KeyStroke var3) {
      this.tree.getInputMap().put(var3 != null ? var3 : (KeyStroke)var2.getValue("AcceleratorKey"), var1);
      this.tree.getActionMap().put(var1, var2);
   }

   @Override
   public void setContextMenuProvider(Supplier<JPopupMenu> var1) {
      this.contextMenuProvider = var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(10, 10).nonOpaque();
      TreePanel var3 = new TreePanel("Users & Groups", this.tree);
      var3.setRootVisible(true);
      var2.center(var3);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocusInWindow();
   }

   @Override
   public Selection getSelection() {
      return this.selectionSource.getSelection();
   }

   @Override
   public void addSelectionListener(SelectionListener var1) {
      this.selectionSource.addSelectionListener(var1);
   }

   @Override
   public void removeSelectionListener(SelectionListener var1) {
      this.selectionSource.removeSelectionListener(var1);
   }

   private KongaTree createTree() {
      KongaTree var1 = new KongaTree(this.treeModelBuilder.build()) {
         @Override
         protected void onPopupTriggered() {
            super.onPopupTriggered();
            UsersAndGroupsTree.this.handleContextMenuRequest(this.getPopupX(), this.getPopupY());
         }
      };
      var1.setCellRenderer(new TreeNodeDecorRenderer());
      var1.setToggleClickCount(-1);
      return var1;
   }

   private void setDragAndDropEnabled(boolean var1) {
      this.tree.setDragEnabled(var1);
      this.tree.setTransferHandler(var1 ? new USerAndGroupTreeTransferHandler() : null);
   }

   private TreeSelectionSource createSelectionSource() {
      TreeSelectionSource var1 = new TreeSelectionSource(this.tree);
      var1.setNodeObjectConverter(new UsersAndGroupsTree.SelectionConverter(this.tree.getModel()));
      return var1;
   }

   private void handleContextMenuRequest(int var1, int var2) {
      if (this.contextMenuProvider != null) {
         JPopupMenu var3 = this.contextMenuProvider.get();
         if (var3 != null) {
            var3.show(this.tree, var1, var2);
         }
      }
   }

   private class ModelListener implements UserConsoleModelListener {
      private ModelListener() {
      }

      @Override
      public void newServerConnectedTo(final UserConsoleModel var1) {
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               UsersAndGroupsTree.this.treeModelBuilder.setNewServerAsRoot(var1.getCurrentServer());
            }
         });
      }

      @Override
      public void allDataRemoved(UserConsoleModel var1) {
         this.rebuildTreeModel();
      }

      @Override
      public void newUserDataDownloaded(UserConsoleModel var1) {
         this.rebuildTreeModel();
      }

      @Override
      public void downloadStatusChanged(UserConsoleModel var1, boolean var2) {
         if (var2) {
            UsersAndGroupsTree.this.waitNodeService.startWait();
         } else {
            UsersAndGroupsTree.this.waitNodeService.stopWait();
         }
      }

      @Override
      public void groupAdded(UserConsoleModel var1, Group var2) {
         GroupNode var3 = UsersAndGroupsTree.this.treeModelBuilder.insertNewGroup(var2);
         UsersAndGroupsTree.this.tree.makeNodeSelected(var3, false);
      }

      @Override
      public void groupRemoved(UserConsoleModel var1, Group var2) {
         UsersAndGroupsTree.this.treeModelBuilder.deleteGroup(var2);
      }

      @Override
      public void userAdded(UserConsoleModel var1, User var2) {
         UserNode var3 = UsersAndGroupsTree.this.treeModelBuilder.insertNewUser(var2);
         UsersAndGroupsTree.this.tree.makeNodeSelected(var3, false);
      }

      @Override
      public void userRemoved(UserConsoleModel var1, User var2) {
         UsersAndGroupsTree.this.treeModelBuilder.deleteUser(var2);
      }

      @Override
      public void memberAdded(Group var1, User var2) {
         GroupMemberNode var3 = UsersAndGroupsTree.this.treeModelBuilder.addGroupMember(var1, var2);
         UsersAndGroupsTree.this.tree.makeNodeSelected(var3, false);
      }

      @Override
      public void memberRemoved(Group var1, User var2) {
         UsersAndGroupsTree.this.treeModelBuilder.deleteGroupMember(var1, var2);
      }

      @Override
      public void dirtyStatusChanged(UserConsoleModel var1, boolean var2) {
      }

      @Override
      public void activeStatusChanged(UserConsoleModel var1, boolean var2) {
      }

      @Override
      public void readOnlyStateChanged(UserConsoleModel var1, boolean var2) {
         UsersAndGroupsTree.this.setDragAndDropEnabled(!var2);
      }

      @Override
      public void uploadStatusChanged(UserConsoleModel var1, boolean var2) {
      }

      @Override
      public void groupPropertyChanged(UserConsoleModel var1, PropertyChangeEvent var2) {
         UsersAndGroupsTree.this.treeModelBuilder.groupChanged((Group)var2.getSource());
      }

      @Override
      public void userPropertyChanged(UserConsoleModel var1, PropertyChangeEvent var2) {
         UsersAndGroupsTree.this.treeModelBuilder.userChanged((User)var2.getSource());
      }

      private void rebuildTreeModel() {
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               DefaultKongaTreeModel var1 = UsersAndGroupsTree.this.treeModelBuilder.build();
               UsersAndGroupsTree.this.tree.setModel(var1);
               UsersAndGroupsTree.this.selectionSource.setNodeObjectConverter(new UsersAndGroupsTree.SelectionConverter(var1));

               for (KongaTreeNode var3 : UsersAndGroupsTree.this.treeModelBuilder.getNodesToExpand()) {
                  UsersAndGroupsTree.this.tree.makeNodeExpanded(var3);
               }

               ServerInfoNode var4 = UsersAndGroupsTree.this.treeModelBuilder.getCurrentRoot();
               if (var4.getUserObject() != null) {
                  UsersAndGroupsTree.this.tree.makeNodeSelected(UsersAndGroupsTree.this.treeModelBuilder.getCurrentRoot(), false);
               }

               UsersAndGroupsTree.this.tree.requestFocusInWindow();
            }
         });
      }
   }

   private static class SelectionConverter extends KongaTreeNodeObjectConverter {
      public SelectionConverter(KongaTreeModel var1) {
         super(var1);
      }

      @Override
      public Object toObject(TreeNode var1) {
         if (var1 instanceof ServerInfoNode) {
            ServerInfoNode var2 = (ServerInfoNode)var1;
            if (var2.getUserObject() == null) {
               return DisconnectedMarker.MARKER;
            }
         } else if (var1 instanceof UserRoot || var1 instanceof GroupRoot) {
            return var1;
         }

         return super.toObject(var1);
      }

      @Override
      public KongaTreeNode toNode(Object var1) {
         return super.toNode(var1);
      }
   }
}
