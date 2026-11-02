package org.etlforge.integration.client.ui.deploy.option;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.etlforge.integration.data.entity.BuiltInIntegrationEntity;
import org.etlforge.integration.data.entity.DependencyStore;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.check.CheckBoxNode;
import org.etlforge.ui.widget.tree.check.CheckBoxTreeEvent;
import org.etlforge.ui.widget.tree.check.CheckBoxTreeListener;

final class DeployTreeCheckHandler implements CheckBoxTreeListener {
   private final DeployTree tree;
   private final DependencyStore dependencies;
   private IntegrationEntity changedEntity;
   private final Set<IntegrationEntity> explicitlySelected = Sets.newHashSet();
   private final Table<IntegrationEntity, IntegrationEntity, Boolean> implicitlySelected = HashBasedTable.create();

   public DeployTreeCheckHandler(DeployTree var1, IntegrationProject var2, DependencyStore var3) {
      this.tree = var1;
      this.dependencies = var3;
   }

   @Override
   public void nodeToggled(CheckBoxTreeEvent var1) {
      CheckBoxNode var2 = var1.getNode();
      Object var3 = var2.getUserObject();
      if (var3 instanceof IntegrationEntity) {
         this.changedEntity = (IntegrationEntity)var3;
         if (var2.isSelected()) {
            this.explicitlySelected.add(this.changedEntity);
            this.processSelect(this.changedEntity, var2);
            if (this.changedEntity instanceof Folder) {
               ArrayList var4 = Lists.newArrayList();
               this.getAllDescendantsRecursively(var2, var4);
               this.selectManually(var4);
            }

            this.setRootFoldersSelected();
         } else {
            this.processUnselect(this.changedEntity);
            this.refreshSelection();
         }

         this.tree.repaint();
      }
   }

   @Override
   public void overallCheckedStateChanged(CheckBoxTreeEvent var1) {
   }

   private void selectManually(List<KongaTreeNode> var1) {
      for (KongaTreeNode var3 : var1) {
         this.changedEntity = (IntegrationEntity)var3.getUserObject();
         this.explicitlySelected.add(this.changedEntity);
         ((CheckBoxNode)var3).setSelected(true);
         this.processSelect(this.changedEntity, (CheckBoxNode)var3);
      }
   }

   private void processSelect(IntegrationEntity var1, CheckBoxNode var2) {
      this.processSelectImpl(var1, var2, new HashSet<>());
   }

   private void processSelectImpl(IntegrationEntity var1, CheckBoxNode var2, Set<IntegrationEntity> var3) {
      if (var3.add(var1)) {
         for (IntegrationEntity var5 : this.dependencies.getRequiredObjects(var1)) {
            if (!(var5 instanceof BuiltInIntegrationEntity)) {
               CheckBoxNode var6 = this.tree.getNodeForEntity(var5);
               var6.setSelected(true);
               if (!this.explicitlySelected.contains(var5)) {
                  this.implicitlySelected.put(this.changedEntity, var5, Boolean.TRUE);
               }

               this.processSelectImpl(var5, var6, var3);
            }
         }

         IntegrationEntity var7 = var1.getParent();
         if (var7 != null) {
            CheckBoxNode var8 = this.tree.getNodeForEntity(var7);
            var8.setSelected(true);
            if (!this.explicitlySelected.contains(var7)) {
               this.implicitlySelected.put(this.changedEntity, var7, Boolean.TRUE);
            }

            this.processSelectImpl(var7, var8, var3);
         } else {
            KongaTreeNode var9 = var2.getParent();
            if (var9 instanceof CheckBoxNode) {
               ((CheckBoxNode)var9).setSelected(true);
            }
         }
      }
   }

   private void processUnselect(IntegrationEntity var1) {
      this.processUnselectImpl(var1, new HashSet<>());
   }

   private void processUnselectImpl(IntegrationEntity var1, Set<IntegrationEntity> var2) {
      if (!var2.contains(var1)) {
         var2.add(var1);
         this.explicitlySelected.remove(var1);
         this.implicitlySelected.row(var1).clear();

         for (IntegrationEntity var4 : this.implicitlySelected.rowKeySet()) {
            this.implicitlySelected.remove(var4, var1);
         }

         for (IntegrationEntity var7 : this.dependencies.getDependingObjects(var1)) {
            this.implicitlySelected.remove(var7, var1);
            if (!var1.hasBeenDeployed()) {
               this.processUnselectImpl(var7, var2);
            }
         }

         for (IntegrationEntity var8 : var1.getAllChildren()) {
            this.processUnselectImpl(var8, var2);
         }
      }
   }

   private void refreshSelection() {
      this.unselectAll();

      for (IntegrationEntity var2 : this.explicitlySelected) {
         CheckBoxNode var3 = this.tree.getNodeForEntity(var2);
         if (var3 != null) {
            var3.setSelected(true);
         }
      }

      for (IntegrationEntity var8 : this.implicitlySelected.rowKeySet()) {
         Map var9 = this.implicitlySelected.row(var8);

         for (Object __etlItem149 : var9.keySet()) {
            IntegrationEntity var5 = (IntegrationEntity) __etlItem149;
            CheckBoxNode var6 = this.tree.getNodeForEntity(var5);
            if (var6 != null) {
               var6.setSelected(true);
            }
         }
      }

      this.setRootFoldersSelected();
   }

   private void unselectAll() {
      this.unselectNode(this.tree.getRootNode());
   }

   private void unselectNode(CheckBoxNode var1) {
      var1.setSelected(false);

      for (KongaTreeNode var3 : var1.children()) {
         this.unselectNode((CheckBoxNode)var3);
      }
   }

   private void setRootFoldersSelected() {
      CheckBoxNode var1 = this.tree.getRootNode();
      var1.setSelected(this.hasSelectedChild(var1));
   }

   private boolean hasSelectedChild(KongaTreeNode var1) {
      boolean var2 = false;

      for (KongaTreeNode var4 : var1.children()) {
         boolean var5 = var4.getUserObject() instanceof RootFolder;
         if (var5) {
            boolean var6 = this.hasSelectedChild(var4);
            ((CheckBoxNode)var4).setSelected(var6);
            if (var6) {
               var2 = true;
            }
         } else {
            if (((CheckBoxNode)var4).isSelected()) {
               return true;
            }

            if (this.hasSelectedChild(var4)) {
               return true;
            }
         }
      }

      return var2;
   }

   private void getAllDescendantsRecursively(KongaTreeNode var1, List<KongaTreeNode> var2) {
      for (KongaTreeNode var4 : var1.children()) {
         var2.add(var4);
         this.getAllDescendantsRecursively(var4, var2);
      }
   }
}
