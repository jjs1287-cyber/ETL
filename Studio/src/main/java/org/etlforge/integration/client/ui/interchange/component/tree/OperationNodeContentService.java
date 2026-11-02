package org.etlforge.integration.client.ui.interchange.component.tree;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.eventbus.Subscribe;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.collections.Iterables2;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.event.UpdatedDependenciesEvent;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTreeModel;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityTypeAndNameSorter;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.uidelegate.entity.tree.IntegrationEntityTreeNode;
import org.etlforge.system.Disposable;
import org.etlforge.ui.widget.tree.KongaTreeNode;

final class OperationNodeContentService implements Disposable {
   private final IntegrationEntityTreeModel treeModel;
   private final ManagedProject project;
   private final OperationNodeContentService.Subsriber subscriber = new OperationNodeContentService.Subsriber();
   private boolean enabled;
   private boolean subscribing;

   public OperationNodeContentService(ManagedProject var1, IntegrationEntityTreeModel var2) {
      this.project = var1;
      this.treeModel = var2;
      this.subscribe();
   }

   private void subscribe() {
      this.project.getEventBus().register(this.subscriber);
      this.subscribing = true;
   }

   @Override
   public void dispose() {
      if (this.subscribing) {
         this.unsubscribe();
      }
   }

   private void unsubscribe() {
      this.project.getEventBus().unregister(this.subscriber);
      this.subscribing = false;
   }

   public void setEnabled(boolean var1) {
      if (var1 != this.enabled) {
         this.enabled = var1;
         if (this.enabled) {
            this.subscribe();
         } else {
            this.unsubscribe();
         }

         this.update();
      }
   }

   private void update() {
      OperationNodeContentService.Updater var1 = new OperationNodeContentService.Updater();
      var1.execute();
   }

   private class Subsriber {
      private Subsriber() {
      }

      @Subscribe
      public void handle(UpdatedDependenciesEvent var1) {
         OperationNodeContentService.this.update();
      }
   }

   private final class Updater extends KongaSwingWorker<Multimap<Operation, IntegrationEntity>, Void> {
      private Multimap<Operation, IntegrationEntity> dependencies;

      private Updater() {
      }

      protected Multimap<Operation, IntegrationEntity> doInBackground() throws Exception {
         HashMultimap var1 = HashMultimap.create();

         for (Operation var3 : OperationNodeContentService.this.project.getProject().getAllEntitiesOfClass(Operation.class)) {
            var1.putAll(var3, OperationNodeContentService.this.project.getDependencyStore().getRequiredObjectsRecursively(var3));
         }

         return var1;
      }

      protected void onSuccess(Multimap<Operation, IntegrationEntity> var1) {
         this.dependencies = var1;

         for (IntegrationEntityTreeNode var3 : this.collectOperationNodes()) {
            this.processOperation(var3);
         }
      }

      private List<IntegrationEntityTreeNode> collectOperationNodes() {
         ArrayList var1 = Lists.newArrayList();
         KongaTreeNode var2 = OperationNodeContentService.this.treeModel
            .findNodeForObject(OperationNodeContentService.this.project.getProject().getRootFolder(EntityType.Operation));
         if (var2 != null) {
            this.collectOperationNodes(var2, var1);
         }

         return var1;
      }

      private void collectOperationNodes(KongaTreeNode var1, List<IntegrationEntityTreeNode> var2) {
         if (var1.getUserObject() instanceof Operation) {
            var2.add((IntegrationEntityTreeNode)var1);
         } else {
            for (KongaTreeNode var4 : var1.children()) {
               this.collectOperationNodes(var4, var2);
            }
         }
      }

      private void processOperation(IntegrationEntityTreeNode var1) {
         if (OperationNodeContentService.this.enabled) {
            this.addChildrenToOperationNodes(var1);
         } else {
            this.hideChildren(var1);
         }
      }

      private void addChildrenToOperationNodes(IntegrationEntityTreeNode var1) {
         Set var2 = this.getCurrentChildren(var1);
         HashSet var3 = Sets.newHashSet(this.dependencies.get((Operation)var1.getUserObject()));
         if (!var2.equals(var3)) {
            this.setNewChildren(var1, var3);
         }
      }

      private void setNewChildren(IntegrationEntityTreeNode var1, Set<IntegrationEntity> var2) {
         var1.removeAllChildren();
         var1.setAllowsChildren(true);

         for (IntegrationEntity var5 : Iterables2.sortedCopy(var2, new EntityTypeAndNameSorter())) {
            IntegrationEntityTreeNode var6 = new IntegrationEntityTreeNode(var5);
            var1.add(var6);
         }

         this.fireOpNodeChanged(var1);
      }

      private void fireOpNodeChanged(IntegrationEntityTreeNode var1) {
         OperationNodeContentService.this.treeModel.fireTreeStructureChanged(this, OperationNodeContentService.this.treeModel.getPath(var1));
      }

      private Set<IntegrationEntity> getCurrentChildren(IntegrationEntityTreeNode var1) {
         HashSet var2 = Sets.newHashSet();

         for (KongaTreeNode var4 : var1.children()) {
            var2.add(((IntegrationEntityTreeNode)var4).getUserObject());
         }

         return var2;
      }

      private void hideChildren(IntegrationEntityTreeNode var1) {
         var1.removeAllChildren();
         var1.setAllowsChildren(false);
         this.fireOpNodeChanged(var1);
      }
   }
}
