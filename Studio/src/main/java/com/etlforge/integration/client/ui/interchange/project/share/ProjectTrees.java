package com.etlforge.integration.client.ui.interchange.project.share;

import java.awt.datatransfer.Transferable;
import java.util.Collections;
import java.util.List;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import javax.swing.event.TreeModelEvent;
import javax.swing.tree.TreePath;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.widget.tree.TreePanel;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.ui.entity.EntityDropHandler;
import org.etlforge.integration.client.ui.entity.TransferableIntegrationEntities;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTree;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTreeModel;
import org.etlforge.integration.client.ui.entity.tree.NodeToEntity;
import org.etlforge.integration.client.ui.entity.tree.ProjectPathFilterFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.uidelegate.entity.tree.IntegrationEntityTreeNode;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.TreeModelAdapter;
import org.etlforge.ui.widget.tree.TreeSelectionSource;
import org.etlforge.ui.widget.tree.event.KongaTreeListener;

public class ProjectTrees {
   private ProjectTrees.SourceTreeDropHandler sourceTreeDropHandler;
   private final ProjectTrees.TargetTreeDropHandler targetTreeDropHandler;
   private final ShareProjectDataModel model;
   private final IntegrationEntityTree sourceTree;
   private final TreePanel sourceTreePanel;
   private final IntegrationEntityTree targetTree;
   private final TreePanel targetTreePanel;
   private boolean sourceProjectDraggable;

   public ProjectTrees(ShareProjectDataModel var1, ProjectTrees.TargetTreeDropHandler var2) {
      this.model = var1;
      var1.setEmptySourceTreeText("[Select a project to import from...]");
      this.targetTreeDropHandler = var2;
      this.sourceTree = new IntegrationEntityTree(var1.getSourceProjectTreeModel());
      this.sourceTree.setTransferHandler(new ProjectTrees.SourceTreeTransferHandler());
      this.sourceProjectDraggable = true;
      TextStyles.DefaultItalic.makeOver(this.sourceTree);
      this.sourceTreePanel = this.createTreePanel(null, this.sourceTree);
      this.targetTree = new IntegrationEntityTree(var1.getTargetProjectTreeModel());
      this.targetTree.getModel().addTreeModelListener(new ProjectTrees.TargetTreeExpansionHandler());
      this.targetTree.expandTopLevelNodes();
      this.targetTreePanel = this.createTreePanel(null, this.targetTree);
      this.bindTreeTitles();
      this.bindSourceTree();
   }

   private void bindTreeTitles() {
      ProjectTrees.TitleBinding var1 = new ProjectTrees.TitleBinding(this.model, "sourceProjectTitle", this.sourceTreePanel);
      var1.syncUi();
      ProjectTrees.TitleBinding var2 = new ProjectTrees.TitleBinding(this.model, "targetProjectTitle", this.targetTreePanel);
      var2.syncUi();
   }

   private void bindSourceTree() {
      new ProjectTrees.SourceTreeBinding();
   }

   public void setSourceDropHandler(ProjectTrees.SourceTreeDropHandler var1) {
      this.sourceTreeDropHandler = var1;
   }

   public void setSourceProjectDraggable(boolean var1) {
      this.sourceProjectDraggable = var1;
   }

   public void setExportAction(SelectionAction var1) {
      TreeSelectionSource var2 = new TreeSelectionSource(this.sourceTree);
      var2.setNodeObjectConverter(new NodeToEntity());
      var1.setSelectionSource(var2);
   }

   public void clearSelection() {
      this.sourceTree.clearSelection();
      this.targetTree.clearSelection();
   }

   public JComponent layout() {
      Grid var1 = new Grid(1, 2);
      var1.addAll(this.sourceTreePanel, this.targetTreePanel);
      return var1.container();
   }

   public void addSourceTreeListener(KongaTreeListener var1) {
      this.sourceTree.addKongaTreeListener(var1);
   }

   public void removeSourceTreeListener(KongaTreeListener var1) {
      this.sourceTree.removeKongaTreeListener(var1);
   }

   private TreePanel createTreePanel(String var1, KongaTree var2) {
      TreePanel var3 = new TreePanel(var1, var2);
      var3.setLayoutFilterFieldBelowTheTree(true);
      return var3.setRootVisible(true);
   }

   private class SourceTreeBinding extends AbstractBinding<IntegrationEntityTreeModel> {
      public SourceTreeBinding() {
         super(ProjectTrees.this.model, "sourceProjectTreeModel");
      }

      protected void updateUiWithValue(IntegrationEntityTreeModel var1) {
         ProjectTrees.this.sourceTree.setModel(var1);
         ProjectTrees.this.sourceTree.setFont(ProjectTrees.this.targetTree.getFont());
         ProjectTrees.this.sourceTree.expandTopLevelNodes();
         ProjectTrees.this.sourceTree.setDragEnabled(true);
         ProjectTrees.this.targetTree.setTransferHandler(ProjectTrees.this.new TargetTreeTransferHandler());
         ProjectTrees.this.targetTree.setDropMode(DropMode.ON);
         ProjectTrees.this.sourceTree.requestFocusInWindow();
         ProjectTrees.this.sourceTree.makeNodeSelected(ProjectTrees.this.sourceTree.getModel().getRoot(), false);
         IntegrationProject var2 = this.getSourceProject();
         if (var2 != null) {
            ProjectPathFilterFunction var3 = new ProjectPathFilterFunction(var2);
            ProjectTrees.this.sourceTreePanel.showFilterField(var3, false);
         } else {
            ProjectTrees.this.sourceTreePanel.hideFilterField();
         }
      }

      private IntegrationProject getSourceProject() {
         ManagedProject var1 = ProjectTrees.this.model.getSourceProject();
         return var1 != null ? var1.getProject() : null;
      }
   }

   public interface SourceTreeDropHandler {
      boolean canImport(TransferSupport var1);

      boolean importData(TransferSupport var1);
   }

   private class SourceTreeTransferHandler extends TransferHandler {
      private SourceTreeTransferHandler() {
      }

      @Override
      protected Transferable createTransferable(JComponent var1) {
         List var2 = ((KongaTree)var1).getSelectedNodes();
         if (this.checkSelectedNodesCanBeDragged(var2)) {
            assert var2.size() == 1;
            IntegrationEntityTreeNode var3 = (IntegrationEntityTreeNode)var2.get(0);
            return new TransferableIntegrationEntities(Collections.singletonList(var3.getUserObject()));
         } else {
            return null;
         }
      }

      @Override
      public int getSourceActions(JComponent var1) {
         return 1;
      }

      @Override
      public boolean canImport(TransferSupport var1) {
         return ProjectTrees.this.sourceTreeDropHandler != null ? ProjectTrees.this.sourceTreeDropHandler.canImport(var1) : false;
      }

      @Override
      public boolean importData(TransferSupport var1) {
         return ProjectTrees.this.sourceTreeDropHandler != null ? ProjectTrees.this.sourceTreeDropHandler.importData(var1) : false;
      }

      private boolean checkSelectedNodesCanBeDragged(List<KongaTreeNode> var1) {
         if (var1 != null && var1.size() == 1) {
            KongaTreeNode var2 = (KongaTreeNode)var1.get(0);
            if (!(var2 instanceof IntegrationEntityTreeNode)) {
               return false;
            }

            IntegrationEntityTreeNode var3 = (IntegrationEntityTreeNode)var2;
            EntityType var4 = var3.getUserObject().getEntityType();
            return var4 != EntityType.RootFolder && (ProjectTrees.this.sourceProjectDraggable || var4 != EntityType.Project);
         } else {
            return false;
         }
      }
   }

   public interface TargetTreeDropHandler {
      boolean isValidParent(IntegrationEntity var1, IntegrationEntity var2);

      boolean handleDrop(IntegrationEntity var1, IntegrationEntity var2);
   }

   private class TargetTreeExpansionHandler extends TreeModelAdapter {
      private TargetTreeExpansionHandler() {
      }

      @Override
      public void treeNodesInserted(TreeModelEvent var1) {
         TreePath var2 = var1.getTreePath();
         Object var3 = var2.getLastPathComponent();
         ProjectTrees.this.targetTree.makeNodeExpanded(var3);
      }
   }

   private class TargetTreeTransferHandler extends EntityDropHandler {
      private TargetTreeTransferHandler() {
      }

      @Override
      protected boolean handleDrop(IntegrationEntity var1, TransferSupport var2) {
         IntegrationEntity var3 = this.getParent(var2);
         return ProjectTrees.this.targetTreeDropHandler.handleDrop(var1, var3);
      }

      @Override
      protected boolean acceptsDropAction(int var1) {
         return var1 == 1;
      }

      @Override
      protected boolean accepts(IntegrationEntity var1, TransferSupport var2) {
         return ProjectTrees.this.targetTreeDropHandler.isValidParent(this.getParent(var2), var1);
      }

      private IntegrationEntity getParent(TransferSupport var1) {
         JTree.DropLocation var2 = (JTree.DropLocation)var1.getDropLocation();
         TreePath var3 = var2.getPath();
         if (var3 != null) {
            Object var4 = var3.getLastPathComponent();
            if (var4 instanceof IntegrationEntityTreeNode) {
               return ((IntegrationEntityTreeNode)var4).getUserObject();
            }
         }

         return null;
      }
   }

   private static class TitleBinding extends AbstractBinding<String> {
      private final TreePanel treePanel;

      public TitleBinding(ShareProjectDataModel var1, String var2, TreePanel var3) {
         super(var1, var2);
         this.treePanel = var3;
      }

      protected void updateUiWithValue(String var1) {
         this.treePanel.setTitle(var1);
      }
   }
}
