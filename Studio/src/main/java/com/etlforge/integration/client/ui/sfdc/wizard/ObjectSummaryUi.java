package com.etlforge.integration.client.ui.sfdc.wizard;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.etlforge.integration.client.sfdc.data.ObjectSummary;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTreeRenderer;
import org.etlforge.integration.data.entity.BuiltInIntegrationEntity;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.uidelegate.entity.tree.IntegrationEntityTreeNode;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.text.TextDecorator;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeNodeComparators;

public final class ObjectSummaryUi<T extends IntegrationEntity & SalesforceWizardEntity> implements UiProvider {
   private final KongaTree tree = this.createTree();
   private String wizardName;
   private ObjectSummary<T> objects;
   private final Object objectLock = new Object();

   private KongaTree createTree() {
      KongaTree var1 = new KongaTree();
      var1.setRootVisible(false);
      var1.setShowsRootHandles(true);
      var1.setCellRenderer(new IntegrationEntityTreeRenderer() {
         @Override
         protected TextDecorator getNonEntityNodeStyle(KongaTreeNode var1) {
            return WizardStyleSheet.SMALL_CAPTION_STYLE.asTextDecorator();
         }
      });
      var1.setVisibleRowCount(4);
      return var1;
   }

   public void setObjects(ObjectSummary<T> var1) {
      this.objects = var1;
      this.buildTree(var1);
   }

   private void buildTree(ObjectSummary<T> var1) {
      ObjectSummaryUi.TreeModelProducer var2 = new ObjectSummaryUi.TreeModelProducer(var1, this.wizardName);
      ObjectSummaryUi.TreeModelConsumer var3 = new ObjectSummaryUi.TreeModelConsumer(this.tree);
      Computation var4 = Computation.create(var2, var3);
      var4.swingWorkerBuilder().buildAndExecute();
   }

   public void setNewWizardName(String var1) {
      this.wizardName = var1;
      if (this.objects != null) {
         ObjectSummaryUi.NameSetter var2 = new ObjectSummaryUi.NameSetter(this.objects, this.objectLock, var1);
         ObjectSummaryUi.TreeRefresher var3 = new ObjectSummaryUi.TreeRefresher();
         Computation var4 = Computation.create(var2, var3);
         var4.swingWorkerBuilder().buildAndExecute();
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.tree.asUiProvider().decorate(var1);
   }

   @Override
   public void requestFocus() {
   }

   private class NameSetter implements Callable<Void> {
      private final ObjectSummary<T> objects;
      private final Object objectLock;
      private final String newName;

      public NameSetter(ObjectSummary<T> var2, Object var3, String var4) {
         this.objects = var2;
         this.objectLock = var3;
         this.newName = var4;
      }

      public Void call() throws Exception {
         synchronized (this.objectLock) {
            this.objects.setNewWizardName(this.newName);
            return null;
         }
      }
   }

   private static class TreeModelConsumer implements Receiver<KongaTreeModel> {
      private final KongaTree tree;

      public TreeModelConsumer(KongaTree var1) {
         this.tree = var1;
      }

      public void handle(KongaTreeModel var1) {
         this.tree.setModel(var1);
         this.setExpansionState(var1);
      }

      private void setExpansionState(KongaTreeModel var1) {
         this.tree.collapseAll();
         KongaTreeNode var2 = var1.getRoot();
         this.tree.makeNodeExpanded(var2);
         this.tree.makeNodeExpanded(var2.getChildAt(0));
      }
   }

   private class TreeModelProducer implements Callable<KongaTreeModel> {
      private final ObjectSummary<T> objects;
      private final String wizardName;
      private final Map<EntityType, SortedSet<KongaTreeNode>> itemNodes = Maps.newHashMap();

      public TreeModelProducer(ObjectSummary<T> var2, String var3) {
         this.objects = var2;
         this.wizardName = var3;
      }

      public KongaTreeModel call() throws Exception {
         this.setWizardName();
         DefaultKongaTreeNode var1 = new DefaultKongaTreeNode("root", true);
         DefaultKongaTreeNode var2 = new DefaultKongaTreeNode(var1, "Summary", true);
         var2.addChild(new IntegrationEntityTreeNode(this.objects.getWizardObject()));
         synchronized (ObjectSummaryUi.this.objectLock) {
            this.createWizardOperation();
         }

         return this.buildModel(var1);
      }

      private void setWizardName() {
         if (this.wizardName != null) {
            synchronized (ObjectSummaryUi.this.objectLock) {
               this.objects.setNewWizardName(this.wizardName);
            }
         }
      }

      private void createWizardOperation() {
         for (IntegrationEntity var2 : this.objects.getItemsCreatedByWizard()) {
            if (this.includeInTree(var2)) {
               this.addNewNode(new IntegrationEntityTreeNode(var2));
            }
         }
      }

      private boolean includeInTree(IntegrationEntity var1) {
         return !(var1 instanceof BuiltInIntegrationEntity) && !(var1 instanceof Folder);
      }

      private void addNewNode(IntegrationEntityTreeNode var1) {
         EntityType var2 = var1.getUserObject().getEntityType();
         SortedSet var3 = this.itemNodes.get(var2);
         if (var3 == null) {
            var3 = Sets.newTreeSet(KongaTreeNodeComparators.byDisplayName());
            var3.add(var1);
            this.itemNodes.put(var2, var3);
         }

         var3.add(var1);
      }

      private KongaTreeModel buildModel(KongaTreeNode var1) {
         for (Entry var3 : this.itemNodes.entrySet()) {
            this.addItemsOfType(var1, var3);
         }

         return new DefaultKongaTreeModel(var1);
      }

      private void addItemsOfType(KongaTreeNode var1, Entry<EntityType, SortedSet<KongaTreeNode>> var2) {
         EntityType var3 = (EntityType)var2.getKey();
         Set var4 = (Set)var2.getValue();
         String var5 = var3.getPlural() + " (" + var4.size() + ")";
         DefaultKongaTreeNode var6 = new DefaultKongaTreeNode(var1, var5, true);

         for (Object __etlItem189 : var4) {
            KongaTreeNode var8 = (KongaTreeNode) __etlItem189;
            var6.addChild(var8);
         }
      }
   }

   private class TreeRefresher implements Receiver<Void> {
      private TreeRefresher() {
      }

      public void handle(Void var1) {
         DefaultKongaTreeModel var2 = (DefaultKongaTreeModel)ObjectSummaryUi.this.tree.getModel();

         for (KongaTreeNode var4 : var2.getRoot().depthFirstEnumeration()) {
            var2.nodeChanged(var4);
         }

         var2.fireTreeStructureChanged(ObjectSummaryUi.this.tree, null);
         ObjectSummaryUi.this.tree.repaint();
      }
   }
}
