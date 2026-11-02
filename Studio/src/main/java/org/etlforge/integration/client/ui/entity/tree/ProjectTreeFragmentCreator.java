package org.etlforge.integration.client.ui.entity.tree;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.uidelegate.entity.tree.IntegrationEntityTreeNode;
import org.etlforge.integration.uidelegate.project.ProjectNode;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public class ProjectTreeFragmentCreator {
   private static final Comparator<IntegrationEntity> FOLDER_CONTENT_ORDER = new ProjectTreeFragmentCreator.FolderContentSorter();
   private final IntegrationProject project;
   private EntityFilter filter;
   private boolean removeEmptyFolders;

   public static IntegrationEntityTreeModel createTreeModel(IntegrationProject var0, EntityType var1) {
      ProjectTreeFragmentCreator var2 = new ProjectTreeFragmentCreator(var0);
      RootFolder var3 = var0.getRootFolder(var1);
      IntegrationEntityTreeNode var4 = new IntegrationEntityTreeNode(var3);
      var2.createFragment(var4, var1);
      var4.sortChildren();
      return new IntegrationEntityTreeModel(var4);
   }

   public static IntegrationEntityTreeModel createTreeModel(IntegrationProject var0, Class<?> var1) {
      return createTreeModel(var0, var1, false);
   }

   public static IntegrationEntityTreeModel createTreeModel(IntegrationProject var0, Class<?> var1, boolean var2) {
      ProjectTreeFragmentCreator var3 = new ProjectTreeFragmentCreator(var0);
      ArrayList var4 = Lists.newArrayList();

      for (RootFolder var6 : var0.getRootFoldersOfType(var1)) {
         IntegrationEntityTreeNode var7 = new IntegrationEntityTreeNode(var6);
         var3.createFragment(var7, var6);
         var7.sortChildren();
         var4.add(var7);
      }

      if (var4.size() == 1 && var2) {
         return new IntegrationEntityTreeModel((KongaTreeNode)var4.get(0));
      }

      ProjectNode var8 = new ProjectNode(var0);

      for (Object __etlItem56 : var4) {
         KongaTreeNode var10 = (KongaTreeNode) __etlItem56;
         var8.addChild(var10);
      }

      return new IntegrationEntityTreeModel(var8);
   }

   public ProjectTreeFragmentCreator(IntegrationProject var1) {
      this.project = Preconditions.checkNotNull(var1);
      this.filter = EntityFilters.ACCEPTS_ALL;
   }

   public void setFilter(EntityFilter var1) {
      this.filter = var1 != null ? var1 : EntityFilters.ACCEPTS_ALL;
   }

   public void setRemoveEmptyFolders(boolean var1) {
      this.removeEmptyFolders = var1;
   }

   public void createFragment(KongaTreeNode var1, EntityType var2) {
      RootFolder var3 = this.project.getRootFolder(var2);
      this.createFragment(var1, var3);
   }

   public void createFragment(KongaTreeNode var1, Folder var2) {
      for (IntegrationEntity var5 : Ordering.from(FOLDER_CONTENT_ORDER).sortedCopy(var2.getAllChildren())) {
         if (var5 instanceof Folder) {
            this.processFolder(var1, var5);
         } else if (this.filter.apply(var5)) {
            this.createEntityNode(var5, var1);
         }
      }
   }

   private void processFolder(KongaTreeNode var1, IntegrationEntity var2) {
      Folder var3 = (Folder)var2;
      IntegrationEntityTreeNode var4 = new IntegrationEntityTreeNode(var1, var2);
      this.createFragment(var4, var3);
      if (this.removeEmptyFolders && var4.getChildCount() == 0) {
         var1.remove(var4);
      }

      var4.sortChildren();
   }

   protected IntegrationEntityTreeNode createEntityNode(IntegrationEntity var1, KongaTreeNode var2) {
      return new IntegrationEntityTreeNode(var2, var1);
   }

   private static class FolderContentSorter implements Comparator<IntegrationEntity>, Serializable {
      private FolderContentSorter() {
      }

      public int compare(IntegrationEntity var1, IntegrationEntity var2) {
         EntityType var3 = var1.getEntityType();
         EntityType var4 = var2.getEntityType();
         int var5 = var4.compareTo(var3);
         if (var5 == 0) {
            var5 = var1.getName().compareToIgnoreCase(var2.getName());
         }

         return var5;
      }
   }
}
