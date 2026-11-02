package com.etlforge.integration.client.ui.admin.project;

import com.google.common.collect.ImmutableMap;
import com.etlforge.integration.client.admin.project.DeleteIntegrationProjectResult;
import com.etlforge.integration.client.ui.admin.resources.EnterpriseAdminResources;
import java.awt.Component;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.NodeObjectConverter;
import org.etlforge.ui.widget.tree.NodeStatus;
import org.etlforge.ui.widget.tree.TreeSelectionSource;
import org.etlforge.util.string.StringLimiter;

final class DeleteProjectResultTree implements UiProvider {
   private final ImmutableMap<IntegrationProjectId, String> projectNames;
   private final KongaTree tree;
   private final TreeSelectionSource selectionSource;

   public DeleteProjectResultTree(Map<IntegrationProjectId, String> var1, Iterable<DeleteIntegrationProjectResult> var2) {
      this.projectNames = ImmutableMap.copyOf(var1);
      this.tree = this.createTree(var2);
      this.selectionSource = this.createSelectionSource();
   }

   private KongaTree createTree(Iterable<DeleteIntegrationProjectResult> var1) {
      KongaTreeModel var2 = this.buildModel(var1);
      KongaTree var3 = new KongaTree(var2);
      var3.expandAll();
      var3.setRootVisible(false);
      var3.setCellRenderer(new DeleteProjectResultTree.Renderer());
      return var3;
   }

   private KongaTreeModel buildModel(Iterable<DeleteIntegrationProjectResult> var1) {
      DefaultKongaTreeNode var2 = new DefaultKongaTreeNode("root", true);

      for (DeleteIntegrationProjectResult var4 : var1) {
         this.createProjectNode(var2, var4);
      }

      return new DefaultKongaTreeModel(var2);
   }

   private void createProjectNode(KongaTreeNode var1, DeleteIntegrationProjectResult var2) {
      String var3 = this.projectNames.get(var2.getProjectId());
      if (var3 == null) {
         var3 = "[" + EnterpriseAdminResources.getString("DeleteProjects.NoName") + "] [ID = " + var2.getProjectId().toString() + "]";
      }

      DeleteProjectResultTree.ProjectNode var4 = new DeleteProjectResultTree.ProjectNode(var1, var3);
      if (!var2.success()) {
         DeleteProjectResultTree.MessageNode.newErrorNode(var4, var2.getError());
      }

      for (String var6 : var2.getWarnings()) {
         DeleteProjectResultTree.MessageNode.newWarningNode(var4, var6);
      }
   }

   private TreeSelectionSource createSelectionSource() {
      TreeSelectionSource var1 = new TreeSelectionSource(this.tree);
      var1.setNodeObjectConverter(new NodeObjectConverter.Adapter() {
         @Override
         public Object toObject(TreeNode var1) {
            return var1 instanceof DeleteProjectResultTree.MessageNode ? ((DeleteProjectResultTree.MessageNode)var1).getMessage() : null;
         }
      });
      return var1;
   }

   public void addMessageSelectionListener(SelectionListener var1) {
      this.selectionSource.addSelectionListener(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.tree.asUiProvider().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocusInWindow();
      KongaTreeNode var1 = this.tree.getModel().getRoot();
      if (var1.getChildCount() > 0) {
         this.tree.makeNodeSelected(var1.getChildAt(0), false);
      }
   }

   private static final class MessageNode extends DefaultKongaTreeNode {
      private final String display;
      private final Icon icon;

      public static DeleteProjectResultTree.MessageNode newErrorNode(DeleteProjectResultTree.ProjectNode var0, String var1) {
         return new DeleteProjectResultTree.MessageNode(var0, var1, CommonIcons.ERROR_16);
      }

      public static DeleteProjectResultTree.MessageNode newWarningNode(DeleteProjectResultTree.ProjectNode var0, String var1) {
         return new DeleteProjectResultTree.MessageNode(var0, var1, CommonIcons.WARNING_16);
      }

      private MessageNode(DeleteProjectResultTree.ProjectNode var1, String var2, Icon var3) {
         super(var1, var2, false);
         this.icon = var3;
         this.display = new StringLimiter(var2).getLimitedString(100);
      }

      public String getMessage() {
         return (String)this.getUserObject();
      }

      @Override
      public Icon getNodeIcon(NodeStatus var1) {
         return this.icon;
      }

      @Override
      public String getDisplayName() {
         return this.display;
      }

      @Override
      public String toString() {
         return this.getMessage();
      }
   }

   private static final class ProjectNode extends DefaultKongaTreeNode {
      public ProjectNode(KongaTreeNode var1, String var2) {
         super(var1, var2, true);
      }

      @Override
      public Icon getNodeIcon(NodeStatus var1) {
         return EntityIcons.forType(EntityType.Project).getDefaultIcon(EntityIcons.Size.SMALL);
      }
   }

   private static final class Renderer extends DefaultTreeCellRenderer {
      private Renderer() {
      }

      @Override
      public Component getTreeCellRendererComponent(JTree var1, Object var2, boolean var3, boolean var4, boolean var5, int var6, boolean var7) {
         super.getTreeCellRendererComponent(var1, var2, var3, var4, var5, var6, var7);
         KongaTreeNode var8 = (KongaTreeNode)var2;
         this.setText(var8.getDisplayName());
         this.setIcon(var8.getNodeIcon(NodeStatus.CLOSED));
         return this;
      }
   }
}
