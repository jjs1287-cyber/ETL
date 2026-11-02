package org.etlforge.integration.client.ui.deploy.option;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.tree.TreePath;
import org.etlforge.application.ui.widget.tree.TreePanel;
import org.etlforge.collections.IterableEnumeration;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTreeModel;
import org.etlforge.integration.client.ui.entity.tree.ProjectPathFilterFunction;
import org.etlforge.integration.client.ui.entity.tree.checked.CheckBoxEntityNode;
import org.etlforge.integration.client.ui.entity.tree.checked.CheckBoxNodeRenderer;
import org.etlforge.integration.client.ui.entity.tree.checked.CheckedTree;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.validation.ValidationModel;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.event.DoubleClick;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.check.CheckBoxNode;

final class DeployTree implements UiProvider {
   private final DeployTreeContainer container;
   private final ValidationModel validationModel;
   private final IntegrationEntityTreeModel protoTypeModel;
   private final CheckedTree tree;
   private final TreePanel treePanel;
   private Map<IntegrationEntity, CheckBoxNode> nodeMap;

   public DeployTree(DeployTreeContainer var1, ValidationModel var2, IntegrationEntityTreeModel var3) {
      this.container = var1;
      this.validationModel = var2;
      var1.setTree(this);
      this.protoTypeModel = var3;
      this.tree = this.createTree();
      this.treePanel = this.createTreePanel();
      DoubleClick.on(this.tree).toRun(new DeployTree.DoubleClickPageOpener());
   }

   private CheckedTree createTree() {
      CheckedTree var1 = CheckedTree.createCheckedTree(this.protoTypeModel, new DeployTree.Renderer());
      var1.setRootVisible(false);
      var1.setShowsRootHandles(true);
      var1.addCheckListener(this.container);
      var1.setRowHeight(0);
      var1.setVisibleRowCount(12);
      var1.expandAll();
      return var1;
   }

   private TreePanel createTreePanel() {
      TreePanel var1 = new TreePanel(Strings.get("Deploy.Tree.Title"), this.tree);
      var1.setRootVisible(false);
      var1.setEnabled(this.tree.isEnabled());
      var1.showFilterField(new ProjectPathFilterFunction(this.validationModel.getProject()), false);
      return var1;
   }

   public void setTreeModel(IntegrationEntityTreeModel var1) {
      this.tree.setModel(CheckedTree.createCheckedModel(var1));
      this.treePanel.clearFilter();
      this.nodeMap = null;
   }

   public IntegrationProject getProjectToDeploy() throws DeployTreeException {
      ArrayList var1 = Lists.newArrayList();
      ArrayList var2 = Lists.newArrayList();
      this.collectSelectedAndInvalidItems(var1, var2);
      if (!var2.isEmpty()) {
         throw DeployTreeException.invalidItems(var2);
      } else if (var1.isEmpty()) {
         throw DeployTreeException.emptySelection();
      } else {
         return this.container.createPartialProject(var1);
      }
   }

   private void collectSelectedAndInvalidItems(List<IntegrationEntity> var1, List<IntegrationEntity> var2) {
      IterableEnumeration var3 = this.tree.getModel().getRoot().breadthFirstEnumeration();

      while (var3.hasMoreElements()) {
         CheckBoxEntityNode var4 = (CheckBoxEntityNode)var3.nextElement();
         if (var4.isSelected()) {
            IntegrationEntity var5 = (IntegrationEntity)var4.getUserObject();
            EntityType var6 = var5.getEntityType();
            if (var6 != EntityType.Project && var6 != EntityType.RootFolder) {
               if (this.isValid(var5)) {
                  var1.add(var5);
               } else {
                  var2.add(var5);
               }
            }
         }
      }
   }

   private boolean isValid(IntegrationEntity var1) {
      if (var1.getEntityType().isFolder()) {
         return true;
      }

      ValidationStatus var2 = this.validationModel.getStatus(var1, true);
      return var2 != ValidationStatus.INVALID;
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.treePanel.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocusInWindow();
   }

   public void activate() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            Object var1 = DeployTree.this.tree.getExpansionMemento(KongaTree.NodeKeyExtractor.TRIVIAL);
            DeployTree.this.tree.getModel().fireTreeStructureChanged(DeployTree.this.tree.getModel(), new TreePath(DeployTree.this.tree.getModel().getRoot()));
            DeployTree.this.tree.restoreExpansion(var1);
            DeployTree.this.requestFocus();
         }
      });
   }

   public void repaint() {
      this.tree.repaint();
   }

   public CheckBoxNode getRootNode() {
      return (CheckBoxNode)this.tree.getModel().getRoot();
   }

   private void createNodeMap() {
      this.nodeMap = Maps.newHashMap();
      KongaTreeNode var1 = this.tree.getModel().getRoot();
      IterableEnumeration var2 = var1.depthFirstEnumeration();

      while (var2.hasMoreElements()) {
         KongaTreeNode var3 = (KongaTreeNode)var2.nextElement();
         Object var4 = var3.getUserObject();
         if (var4 instanceof IntegrationEntity) {
            this.nodeMap.put((IntegrationEntity)var4, (CheckBoxNode)var3);
         }
      }
   }

   public CheckBoxNode getNodeForEntity(IntegrationEntity var1) {
      if (this.nodeMap == null) {
         this.createNodeMap();
      }

      return this.nodeMap.get(var1);
   }

   private class DoubleClickPageOpener implements Runnable {
      private DoubleClickPageOpener() {
      }

      @Override
      public void run() {
         IntegrationEntity var1 = this.getEntityClickedOn();
         if (var1 != null) {
            this.openPage(var1);
         }
      }

      private IntegrationEntity getEntityClickedOn() {
         List var1 = DeployTree.this.tree.getSelectedNodes();
         if (var1 != null && var1.size() == 1) {
            KongaTreeNode var2 = (KongaTreeNode)var1.get(0);
            Object var3 = var2.getUserObject();
            if (var3 instanceof IntegrationEntity) {
               return (IntegrationEntity)var3;
            }
         }

         return null;
      }

      private void openPage(IntegrationEntity var1) {
         DeployTree.this.container.openPage(var1);
      }
   }

   private class Renderer extends CheckBoxNodeRenderer {
      private Renderer() {
      }

      @Override
      protected String getText(Object var1) {
         if (var1 instanceof CheckBoxEntityNode) {
            Object var2 = ((CheckBoxEntityNode)var1).getUserObject();
            if (var2 instanceof IntegrationEntity) {
               return this.getTextFromEntity((IntegrationEntity)var2);
            }
         }

         return super.getText(var1);
      }

      @Override
      protected Icon getIcon(Object var1) {
         if (var1 instanceof CheckBoxEntityNode) {
            Object var2 = ((CheckBoxEntityNode)var1).getUserObject();
            if (var2 instanceof IntegrationEntity) {
               return this.getIconForEntity(var2);
            }

            if ("ETLForge Connect\u2122".equals(var2)) {
               return ClientIcons.ETLFORGE_CONNECT_24;
            }
         }

         return super.getIcon(var1);
      }

      private Icon getIconForEntity(Object var1) {
         IntegrationEntity var2 = (IntegrationEntity)var1;
         EntityType var3;
         EntityIcons.Size var4;
         if (var2 instanceof RootFolder) {
            var4 = EntityIcons.Size.MEDIUM;
            var3 = ((RootFolder)var2).getItemType();
         } else {
            var4 = EntityIcons.Size.SMALL;
            var3 = var2.getEntityType();
         }

         EntityIcons var5 = EntityIcons.forType(var3);
         ValidationStatus var6 = DeployTree.this.validationModel.getStatus(var2, true);
         return var5.getIcon(EntityIcons.Category.fromValidationStatus(var6), var4);
      }

      private String getTextFromEntity(IntegrationEntity var1) {
         EntityFilter var2 = EntityFilters.DEPLOY_DIRTY;
         return !var2.apply(var1) && !this.hasMatchingChild(var1, var2) ? var1.getName() : ">" + var1.getName();
      }

      private boolean hasMatchingChild(IntegrationEntity var1, EntityFilter var2) {
         for (IntegrationEntity var4 : var1.getAllChildren()) {
            if (var2.apply(var4) || this.hasMatchingChild(var4, var2)) {
               return true;
            }
         }

         return false;
      }
   }
}
