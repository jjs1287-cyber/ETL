package org.etlforge.integration.client.ui.entity.tree;

import com.google.common.base.Predicates;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import org.etlforge.collections.IterableEnumeration;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.client.ui.entity.DeployDirtyIndicator;
import org.etlforge.integration.client.ui.entity.validation.ValidationStatusIndicator;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.validation.ValidationModel;
import org.etlforge.integration.uidelegate.entity.tree.IntegrationEntityTreeNode;
import org.etlforge.integration.uidelegate.project.ProjectNode;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public class IntegrationEntityTree extends KongaTree implements DeployDirtyIndicator, ValidationStatusIndicator {
   public static final String PROPERTY_DECORATING_DEPLOY_DIRTY_ITEMS = "IntegrationEntityTree#DecoratingDeployDirty";
   public static final String PROPERTY_DECORATING_INVALID_ITEMS = "IntegrationEntityTree#DecoratingInvalidItems";
   private IntegrationEntityTreeRenderer renderer = new IntegrationEntityTreeRenderer();
   private boolean decorateDeployDirtyItems;
   private boolean decorateInvalidItems;
   private TreeValidationService validationService;
   private ProjectNodeRenamer projectRenamer;

   public IntegrationEntityTree(KongaTreeModel var1) {
      super(var1);
      this.setCellRenderer(this.renderer);
      this.projectRenamer = ProjectNodeRenamer.READ_ONLY;
   }

   public final IntegrationEntityTreeModel getModel() {
      return (IntegrationEntityTreeModel)super.getModel();
   }

   @Override
   public void setCellRenderer(TreeCellRenderer var1) {
      if (var1 instanceof IntegrationEntityTreeRenderer) {
         this.renderer = (IntegrationEntityTreeRenderer)var1;
         this.renderer.setValidationService(this.validationService);
         this.renderer.setDecorateDeployDirtyNodes(this.decorateDeployDirtyItems);
         this.renderer.setDecorateInvalidNodes(this.decorateInvalidItems);
      } else {
         this.renderer = null;
      }

      super.setCellRenderer(var1);
      this.repaint();
   }

   public final void setProjectNodeRenamer(ProjectNodeRenamer var1) {
      this.projectRenamer = var1 != null ? var1 : ProjectNodeRenamer.READ_ONLY;
   }

   public final void renameIntegrationEntity(IntegrationEntity var1) {
      KongaTreeNode var2 = this.getNodeForEntity(var1);
      if (var2 != null) {
         this.onRenameNode(var2);
      }
   }

   public final KongaTreeNode getNodeForEntity(IntegrationEntity var1) {
      return this.getModel().findNodeForObject(var1);
   }

   @Override
   public final void onRenameNode(KongaTreeNode var1) {
      if (var1 instanceof ProjectNode) {
         this.projectRenamer.rename(((ProjectNode)var1).getUserObject());
      } else {
         super.onRenameNode(var1);
      }
   }

   public final void expandTopLevelNodes() {
      KongaTreeNode var1 = this.getModel().getRoot();

      for (KongaTreeNode var3 : var1.children()) {
         this.makeNodeExpanded(var3);
      }
   }

   public final void refresh(Folder var1) {
      KongaTreeNode var2 = this.getNodeForEntity(var1);
      if (var2 != null) {
         this.getModel().sortChildren(var2);
      }
   }

   public final void refresh() {
      Object var1 = this.getExpansionMemento(KongaTree.NodeKeyExtractor.TRIVIAL);
      this.getModel().fireTreeStructureChanged(this.getModel(), new TreePath(this.getModel().getRoot()));
      this.restoreExpansion(var1);
   }

   @Override
   public final void setDecorateDeployDirtyItems(boolean var1) {
      if (var1 != this.decorateDeployDirtyItems) {
         this.decorateDeployDirtyItems = var1;
         if (this.renderer != null) {
            this.renderer.setDecorateDeployDirtyNodes(var1);
         }

         if (var1) {
            this.getModel().invalidate();
         }

         this.firePropertyChange("IntegrationEntityTree#DecoratingDeployDirty", !var1, var1);
         this.refresh();
      }
   }

   @Override
   public final boolean isDecoratingDeployDirtyItems() {
      return this.decorateDeployDirtyItems;
   }

   public final void setValidationService(TreeValidationService var1) {
      if (var1 != this.validationService) {
         if (this.validationService != null) {
            this.validationService.dispose();
         }

         if (var1 != null) {
            var1.addTree(this);
         }

         this.validationService = var1;
         if (this.renderer != null) {
            this.renderer.setValidationService(var1);
         }

         this.refresh();
      }
   }

   public final TreeValidationService getValidationService() {
      return this.validationService;
   }

   public final ValidationModel getValidationModel() {
      return this.validationService != null ? this.validationService.getModel() : null;
   }

   @Override
   public final boolean isIndicatingValidationStatus() {
      return this.decorateInvalidItems;
   }

   @Override
   public final void setIndicateValidationStatus(boolean var1) {
      if (var1 != this.decorateInvalidItems) {
         this.decorateInvalidItems = var1;
         if (this.renderer != null) {
            this.renderer.setDecorateInvalidNodes(var1);
         }

         this.firePropertyChange("IntegrationEntityTree#DecoratingInvalidItems", !var1, var1);
         this.refresh();
      }
   }

   public final void displayIntegrationEntity(IntegrationEntity var1) {
      if (var1 != null) {
         KongaTreeNode var2 = this.getNodeToSelectForEntity(var1);
         if (var2 != null) {
            this.scrollPathToVisible(this.getPath(var2));
            this.makeNodeSelected(var2, false);
         }
      }
   }

   private KongaTreeNode getNodeToSelectForEntity(IntegrationEntity var1) {
      for (KongaTreeNode var4 : this.getSelectedNodes()) {
         if (var4.getUserObject() == var1) {
            return var4;
         }
      }

      return this.getNodeForEntity(var1);
   }

   public final List<IntegrationEntityTreeNode> getExpandedNodes() {
      ArrayList var1 = new ArrayList();
      TreePath var2 = this.getPath(this.getModel().getRoot());
      Enumeration var3 = this.getExpandedDescendants(var2);
      if (var3 != null) {
         while (var3.hasMoreElements()) {
            TreePath var4 = (TreePath)var3.nextElement();
            Object var5 = var4.getLastPathComponent();
            if (var5 instanceof IntegrationEntityTreeNode) {
               var1.add((IntegrationEntityTreeNode)var5);
            }
         }
      }

      return var1;
   }

   public final void makeExpanded(Set<IntegrationEntity> var1) {
      if (var1 != null && !var1.isEmpty()) {
         IterableEnumeration var2 = this.getModel().getRoot().depthFirstEnumeration();

         while (var2.hasMoreElements()) {
            KongaTreeNode var3 = (KongaTreeNode)var2.nextElement();
            if (var1.contains(var3.getUserObject())) {
               this.makeNodeExpanded(var3);
            }
         }
      }
   }

   public final void selectFirstRootChildNode() {
      IntegrationEntityTreeModel var1 = this.getModel();
      KongaTreeNode var2 = var1.getRoot();
      int var3 = var1.getChildCount(var2);
      if (var3 > 0) {
         if (!this.isNodeExpanded(var2)) {
            this.makeNodeExpanded(var2);
         }

         KongaTreeNode var4 = var1.getChild(var2, 0);
         this.makeNodeSelected(var4, false);
      }
   }

   public List<IntegrationEntity> getSelectedEntities() {
      return KongaListUtils.transform(this.getSelectedNodes(), new NodeToEntity(), Predicates.notNull());
   }
}
