package org.etlforge.integration.client.ui.entity.tree;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.event.TreeModelListener;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectPropertyChangeSupport;
import org.etlforge.integration.uidelegate.entity.tree.IntegrationEntityTreeNode;
import org.etlforge.system.Disposable;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeFilter;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public class DynamicProjectTree implements Disposable, UiProvider {
   private final IntegrationEntityTree tree;
   private IntegrationProject project;
   private final DynamicProjectTreeModel dynamicTreeModel;
   private ProjectPropertyChangeSupport changeListener;

   public DynamicProjectTree() {
      IntegrationEntityTreeModel var1 = this.createEmptyModel();
      this.dynamicTreeModel = this.createDynamicTreeModel();
      this.dynamicTreeModel.setTreeModel(var1);
      this.tree = this.createTree(var1);
   }

   protected IntegrationEntityTreeModel createEmptyModel() {
      return new IntegrationEntityTreeModel("No project is open");
   }

   private DynamicProjectTreeModel createDynamicTreeModel() {
      DynamicProjectTreeModel var1 = new DynamicProjectTreeModel();
      var1.setNewItemsHandler(new DynamicProjectTree.NewItemsHandler());
      var1.setNodesMovedMonitor(new DynamicProjectTree.MoveHandler());
      return var1;
   }

   protected IntegrationEntityTree createTree(IntegrationEntityTreeModel var1) {
      return new IntegrationEntityTree(var1);
   }

   public final IntegrationEntityTreeModel getModel() {
      return this.tree.getModel();
   }

   public final void setProject(IntegrationProject var1) {
      if (var1 != this.project) {
         if (this.project != null) {
            this.dispose();
         }

         this.dynamicTreeModel.setProject(var1);
         IntegrationEntityTreeModel var2 = null;
         if (var1 != null) {
            var2 = this.createModelForProject(var1);
            this.changeListener = new ProjectPropertyChangeSupport(var1, false);
            this.changeListener.addPropertyChangeListener(new PropertyChangeListener() {
               @Override
               public void propertyChange(PropertyChangeEvent var1) {
                  DynamicProjectTree.this.updateTree(var1);
               }
            });
         } else {
            var2 = this.createEmptyModel();
         }

         this.project = var1;
         this.dynamicTreeModel.setTreeModel(var2);
         this.tree.setModel(var2);
      }
   }

   protected IntegrationEntityTreeModel createModelForProject(IntegrationProject var1) {
      IntegrationEntityTreeModelFactory var2 = new IntegrationEntityTreeModelFactory();
      return var2.newModel(var1);
   }

   public final IntegrationEntityTree getAdaptee() {
      return this.tree;
   }

   public final void setFilter(KongaTreeFilter var1) {
      this.tree.setFilter(var1);
   }

   public final void expandAll() {
      this.tree.expandAll();
   }

   public final void collapseAll() {
      this.tree.collapseAll();
   }

   @Override
   public final void dispose() {
      this.dynamicTreeModel.dispose();
      if (this.project != null) {
         this.changeListener.dispose();
         this.project = null;
      }
   }

   @Override
   public final void decorate(UiCanvas var1) {
      this.tree.asUiProvider().decorate(var1);
   }

   @Override
   public final void requestFocus() {
      this.tree.requestFocusInWindow();
   }

   public final void addTreeModelListener(TreeModelListener var1) {
      this.dynamicTreeModel.addTreeModelListener(var1);
   }

   public final void removeTreeModelListener(TreeModelListener var1) {
      this.dynamicTreeModel.removeTreeModelListener(var1);
   }

   private void updateTree(PropertyChangeEvent var1) {
      String var2 = var1.getPropertyName();
      if ("Name".equals(var2)) {
         this.updateNodeForEventSource(var1);
      } else if ("IntegrationEntity#DeployDirty".equals(var2) && this.tree.isDecoratingDeployDirtyItems()) {
         this.updateNodeForEventSource(var1);
      }
   }

   private void updateNodeForEventSource(final PropertyChangeEvent var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            KongaTreeNode var1x = DynamicProjectTree.this.findNodeForEventSource(var1);
            if (var1x != null) {
               DynamicProjectTree.this.tree.getModel().nodeChanged(var1x);
               DynamicProjectTree.this.tree.repaint();
            }
         }
      });
   }

   private KongaTreeNode findNodeForEventSource(PropertyChangeEvent var1) {
      Object var2 = var1.getSource();
      return var2 instanceof IntegrationEntity ? this.tree.getNodeForEntity((IntegrationEntity)var2) : null;
   }

   private class MoveHandler implements NodesMovedMonitor {
      private Object expansionMemento;

      private MoveHandler() {
      }

      @Override
      public void preMove() {
         this.expansionMemento = DynamicProjectTree.this.tree.getExpansionMemento(KongaTree.NodeKeyExtractor.TRIVIAL);
      }

      @Override
      public void postMove(boolean var1) {
         if (var1) {
            DynamicProjectTree.this.tree.restoreExpansion(this.expansionMemento);
            DynamicProjectTree.this.tree.repaint();
         }

         this.expansionMemento = null;
      }
   }

   private class NewItemsHandler implements Receiver<List<IntegrationEntityTreeNode>> {
      private NewItemsHandler() {
      }

      public void handle(List<IntegrationEntityTreeNode> var1) {
         for (IntegrationEntityTreeNode var3 : var1) {
            this.expandAndSelectNewNode(var3);
         }
      }

      private void expandAndSelectNewNode(KongaTreeNode var1) {
         DynamicProjectTree.this.tree.expandPath(DynamicProjectTree.this.tree.getPath(var1));
         DynamicProjectTree.this.tree.makeNodeSelected(var1, false);
      }
   }
}
