package org.etlforge.integration.client.ui.entity.tree;

import com.google.common.collect.Lists;
import java.util.List;
import javax.swing.event.TreeModelListener;
import org.etlforge.application.ui.ApplicationUiUtils;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.event.IntegrationProjectEvent;
import org.etlforge.integration.data.project.event.ProjectListener;
import org.etlforge.integration.uidelegate.entity.tree.IntegrationEntityTreeNode;
import org.etlforge.system.Disposable;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.util.UiUtils;

public final class DynamicProjectTreeModel implements Disposable {
   private IntegrationEntityTreeModel model;
   private IntegrationProject project;
   private final ProjectListener contentListener = new DynamicProjectTreeModel.ContentListener();
   private Receiver<List<IntegrationEntityTreeNode>> newItemsHandler = NoReceiver.create();
   private NodesMovedMonitor nodesMovedMonitor;
   private final List<TreeModelListener> modelListeners = Lists.newArrayList();

   public void setProject(IntegrationProject var1) {
      this.stopListeningToProject();
      if (var1 != null) {
         var1.addProjectListener(this.contentListener);
      }

      this.project = var1;
   }

   public void setTreeModel(IntegrationEntityTreeModel var1) {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "setTreeModel");
      this.removeListenersFromExistingModel();
      this.model = var1;
      this.addListenersToNewModel();
   }

   private void removeListenersFromExistingModel() {
      if (this.model != null) {
         for (TreeModelListener var2 : this.modelListeners) {
            this.model.removeTreeModelListener(var2);
         }
      }
   }

   private void addListenersToNewModel() {
      for (TreeModelListener var2 : this.modelListeners) {
         this.model.addTreeModelListener(var2);
      }
   }

   public IntegrationEntityTreeModel getTreeModel() {
      return this.model;
   }

   public void setNewItemsHandler(Receiver<List<IntegrationEntityTreeNode>> var1) {
      this.newItemsHandler = var1;
   }

   public void setNodesMovedMonitor(NodesMovedMonitor var1) {
      this.nodesMovedMonitor = var1;
   }

   @Override
   public void dispose() {
      this.stopListeningToProject();
   }

   private void stopListeningToProject() {
      if (this.project != null) {
         this.project.removeProjectListener(this.contentListener);
         this.project = null;
      }
   }

   public void addTreeModelListener(TreeModelListener var1) {
      this.modelListeners.add(var1);
      if (this.model != null) {
         this.model.addTreeModelListener(var1);
      }
   }

   public void removeTreeModelListener(TreeModelListener var1) {
      this.modelListeners.remove(var1);
      if (this.model != null) {
         this.model.removeTreeModelListener(var1);
      }
   }

   private class ContentListener implements ProjectListener {
      private ContentListener() {
      }

      @Override
      public void entitiesAdded(IntegrationProjectEvent var1) {
         this.handleItemsCreatedOrRevived(var1);
      }

      @Override
      public void entitiesRevived(IntegrationProjectEvent var1) {
         this.handleItemsCreatedOrRevived(var1);
      }

      @Override
      public void entitiesDeleted(final IntegrationProjectEvent var1) {
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               ContentListener.this.removeItems(var1);
            }
         });
      }

      private void removeItems(IntegrationProjectEvent var1) {
         if (DynamicProjectTreeModel.this.model != null) {
            DynamicProjectTreeModel.this.model.removeIntegrationEntities(var1.getEntities());
         }
      }

      private void handleItemsCreatedOrRevived(IntegrationProjectEvent var1) {
         final List var2 = var1.getEntities();
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               ContentListener.this.insertItems(var2);
            }
         });
      }

      private void insertItems(List<IntegrationEntity> var1) {
         if (DynamicProjectTreeModel.this.model != null) {
            List var2 = DynamicProjectTreeModel.this.model.insertIntegrationEntities(var1);
            DynamicProjectTreeModel.this.newItemsHandler.handle(var2);
         }
      }

      @Override
      public void entitiesMoved(final IntegrationProjectEvent var1) {
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               if (DynamicProjectTreeModel.this.model != null) {
                  DynamicProjectTreeModel.this.new NodeMover().moveNodes(var1.getEntities());
               }
            }
         });
      }
   }

   private class NodeMover {
      private NodeMover() {
      }

      public void moveNodes(List<IntegrationEntity> var1) {
         this.preMoveNotification();
         boolean var2 = DynamicProjectTreeModel.this.model.moveIntegrationEntities(var1);
         this.postMoveNotification(var2);
      }

      private void preMoveNotification() {
         if (DynamicProjectTreeModel.this.nodesMovedMonitor != null) {
            DynamicProjectTreeModel.this.nodesMovedMonitor.preMove();
         }
      }

      private void postMoveNotification(boolean var1) {
         if (DynamicProjectTreeModel.this.nodesMovedMonitor != null) {
            DynamicProjectTreeModel.this.nodesMovedMonitor.postMove(var1);
         }
      }
   }
}
