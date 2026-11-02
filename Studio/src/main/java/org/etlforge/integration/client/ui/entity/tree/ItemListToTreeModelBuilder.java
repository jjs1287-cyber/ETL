package org.etlforge.integration.client.ui.entity.tree;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Set;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.uidelegate.entity.tree.IntegrationEntityTreeNode;

public final class ItemListToTreeModelBuilder {
   private IntegrationEntityTreeNode projectNode;
   private Map<IntegrationEntity, IntegrationEntityTreeNode> folderNodes;
   private Set<IntegrationEntity> processed;

   public IntegrationEntityTreeModel createModel(Iterable<? extends IntegrationEntity> var1) {
      this.reset();

      for (IntegrationEntity var3 : var1) {
         this.process(var3);
      }

      if (this.projectNode == null) {
         throw new RuntimeException("No project");
      }

      this.sortFolders();
      return new IntegrationEntityTreeModel(this.projectNode);
   }

   private void reset() {
      this.projectNode = null;
      this.folderNodes = Maps.newHashMap();
      this.processed = Sets.newHashSet();
   }

   private void process(IntegrationEntity var1) {
      if (this.processed.add(var1)) {
         IntegrationEntityTreeNode var2 = new IntegrationEntityTreeNode(var1);
         if (var1 instanceof IntegrationProject) {
            if (this.projectNode != null) {
               throw new RuntimeException("Multiple projects");
            }

            this.projectNode = var2;
         } else {
            if (var1 instanceof Folder) {
               this.folderNodes.put(var1, var2);
            }

            this.processParents(var1, var2);
         }
      }
   }

   private void processParents(IntegrationEntity var1, IntegrationEntityTreeNode var2) {
      IntegrationEntity var3 = var1.getParent();

      while (var3 != null) {
         IntegrationEntityTreeNode var4 = this.getParentNode(var3);
         if (var2.getParent() != var4) {
            var4.addChild(var2);
         }

         if (var3 instanceof IntegrationProject) {
            break;
         }

         var3 = var3.getParent();
         var2 = var4;
      }
   }

   private IntegrationEntityTreeNode getParentNode(IntegrationEntity var1) {
      if (var1 instanceof IntegrationProject) {
         if (this.projectNode == null) {
            this.projectNode = new IntegrationEntityTreeNode(var1);
            return this.projectNode;
         } else if (this.projectNode.getUserObject() == var1) {
            return this.projectNode;
         } else {
            throw new RuntimeException("Multiple projects");
         }
      } else {
         assert var1 instanceof Folder;
         IntegrationEntityTreeNode var2 = this.folderNodes.get(var1);
         if (var2 == null) {
            var2 = new IntegrationEntityTreeNode(var1);
            this.folderNodes.put(var1, var2);
         }

         return var2;
      }
   }

   private void sortFolders() {
      this.projectNode.sortChildren();

      for (IntegrationEntityTreeNode var2 : this.folderNodes.values()) {
         var2.sortChildren();
      }
   }
}
