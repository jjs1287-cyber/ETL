package org.etlforge.integration.client.ui.interchange.component.tree;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import java.awt.EventQueue;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTreeModel;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.event.IntegrationProjectEvent;
import org.etlforge.integration.data.project.event.ProjectListener;
import org.etlforge.integration.uidelegate.entity.tree.IntegrationEntityTreeNode;
import org.etlforge.system.Disposable;
import org.etlforge.ui.widget.tree.KongaTreeNode;

final class ConnectOperationNodeService implements Disposable {
   private final IntegrationProject project;
   private final IntegrationEntityTreeModel treeModel;
   private final ProjectListener projectListener;
   private final ConnectOperationNodeService.OperationFilter opFilter;

   public ConnectOperationNodeService(IntegrationProject var1, IntegrationEntityTreeModel var2) {
      this.project = var1;
      this.treeModel = var2;
      this.projectListener = new ConnectOperationNodeService.ProjectListenerImpl();
      var1.addProjectListener(this.projectListener);
      this.opFilter = new ConnectOperationNodeService.OperationFilter();
   }

   public void insertOperationNodes() {
      for (Operation var3 : Iterables.filter(this.project.getAllEntitiesOfClass(Operation.class), this.opFilter)) {
         this.insertOperation(var3);
      }
   }

   private void insertOperation(Operation var1) {
      EntityType var2 = var1.getConnectType();
      KongaTreeNode var3 = this.findConnectRoot(var2);
      if (var3 != null) {
         IntegrationEntityTreeNode var4 = new IntegrationEntityTreeNode(var1);
         this.treeModel.insertNode(var4, var3);
      }
   }

   private KongaTreeNode findConnectRoot(EntityType var1) {
      assert var1 != null : "The filter that accepts only Connect Wizard operations is broken.";
      RootFolder var2 = this.project.getRootFolder(var1);
      KongaTreeNode var3 = this.treeModel.getRoot();

      for (KongaTreeNode var5 : var3.breadthFirstEnumeration()) {
         Object var6 = var5.getUserObject();
         if (var6 == var2) {
            return var5;
         }
      }

      return null;
   }

   @Override
   public void dispose() {
      this.project.removeProjectListener(this.projectListener);
   }

   private static class OperationFilter implements EntityFilter {
      private OperationFilter() {
      }

      @Override
      public boolean apply(IntegrationEntity var1) {
         if (!(var1 instanceof Operation)) {
            return false;
         }

         EntityType var2 = ((Operation)var1).getConnectType();
         return var2 != null && !var2.canBeInstantiated();
      }
   }

   private class ProjectListenerImpl implements ProjectListener {
      private ProjectListenerImpl() {
      }

      @Override
      public void entitiesAdded(IntegrationProjectEvent var1) {
         this.handleItemsAdded(var1);
      }

      private void handleItemsAdded(IntegrationProjectEvent var1) {
         final Iterable var2 = this.getOperationsOfInterest(var1);
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               for (Object __etlItem97 : var2) {
                  Operation var2x = (Operation) __etlItem97;
                  ConnectOperationNodeService.this.insertOperation(var2x);
               }
            }
         });
      }

      private Iterable<Operation> getOperationsOfInterest(IntegrationProjectEvent var1) {
         return FluentIterable.from(var1.getEntities()).filter(ConnectOperationNodeService.this.opFilter).filter(Operation.class);
      }

      @Override
      public void entitiesRevived(IntegrationProjectEvent var1) {
         this.handleItemsAdded(var1);
      }

      @Override
      public void entitiesDeleted(IntegrationProjectEvent var1) {
         final Iterable var2 = this.getOperationsOfInterest(var1);
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               for (Object __etlItem119 : var2) {
                  Operation var2x = (Operation) __etlItem119;
                  ProjectListenerImpl.this.removeOperation(var2x);
               }
            }
         });
      }

      private void removeOperation(Operation var1) {
         EntityType var2 = var1.getConnectType();
         KongaTreeNode var3 = ConnectOperationNodeService.this.findConnectRoot(var2);
         if (var3 != null) {
            this.removeOperationNodeFromParent(var1, var3);
         }
      }

      private void removeOperationNodeFromParent(Operation var1, KongaTreeNode var2) {
         for (KongaTreeNode var4 : var2.children()) {
            if (var4.getUserObject() == var1) {
               ConnectOperationNodeService.this.treeModel.removeNode(var4);
               return;
            }
         }
      }

      @Override
      public void entitiesMoved(IntegrationProjectEvent var1) {
      }
   }
}
