package org.etlforge.integration.data.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.etlforge.collections.Iterables2;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectDependencies;
import org.etlforge.integration.data.project.ProjectUtils;
import org.etlforge.system.callback.Receiver;

public final class RequiredObjectsTree {
   private final RequiredObjectsTree.Node root;

   public static RequiredObjectsTree fromStartPoint(IntegrationEntity var0) {
      DependencyStore var1 = lookupDependencies(var0);

      try {
         return new RequiredObjectsTree(var1, var0);
      } finally {
         var1.dispose();
      }
   }

   private static DependencyStore lookupDependencies(IntegrationEntity var0) {
      IntegrationProject var1 = ProjectUtils.getProject(var0);
      if (var1 == null) {
         throw new IllegalArgumentException("startPoint must belong to a project");
      } else {
         return var1.getCurrentDependencies();
      }
   }

   public RequiredObjectsTree(ProjectDependencies var1, IntegrationEntity var2) {
      this.root = new RequiredObjectsTree.Node(null, var2);
      this.buildBranch(var1, var2, this.root);
   }

   private void buildBranch(ProjectDependencies var1, IntegrationEntity var2, RequiredObjectsTree.Node var3) {
      for (IntegrationEntity var6 : Iterables2.sortedCopy(var1.getRequiredObjects(var2), new EntityTypeAndNameSorter())) {
         RequiredObjectsTree.Node var7 = var3.addChild(var6);
         if (!var7.isCycle()) {
            this.buildBranch(var1, var6, var7);
         }
      }
   }

   public RequiredObjectsTree.Node getRoot() {
      return this.root;
   }

   public int size() {
      return this.getAllItems().size();
   }

   public Set<IntegrationEntity> getAllItems() {
      return this.getItemsExcluding(new HashSet<>());
   }

   public Set<IntegrationEntity> getItemsExcluding(Collection<IntegrationEntity> var1) {
      HashSet var2 = Sets.newHashSet();
      this.root.collectItems(var2, var1);
      return var2;
   }

   public Set<IntegrationEntity> collectAncestors(IntegrationEntity var1) {
      RequiredObjectsTree.Node var2 = this.findNode(var1);
      HashSet var3 = Sets.newHashSet();
      var2.collectAncestors(var3);
      return var3;
   }

   public Set<IntegrationEntity> collectDescendants(IntegrationEntity var1) {
      RequiredObjectsTree.Node var2 = this.findNode(var1);
      HashSet var3 = Sets.newHashSet();
      var2.collectDescendants(var3);
      return var3;
   }

   private RequiredObjectsTree.Node findNode(IntegrationEntity var1) {
      RequiredObjectsTree.Node var2 = this.root.find(var1);
      if (var2 == null) {
         throw new IllegalArgumentException("item is not part of this tree");
      } else {
         return var2;
      }
   }

   public static final class Node {
      public final IntegrationEntity item;
      public final RequiredObjectsTree.Node parent;
      private final List<RequiredObjectsTree.Node> children;

      public Node(RequiredObjectsTree.Node var1, IntegrationEntity var2) {
         this.parent = var1;
         this.item = var2;
         this.children = Lists.newArrayList();
         if (var1 != null) {
            var1.children.add(this);
         }
      }

      public List<RequiredObjectsTree.Node> children() {
         return Collections.unmodifiableList(this.children);
      }

      public void traverseDepthFirst(Receiver<RequiredObjectsTree.Node> var1) {
         var1.handle(this);

         for (RequiredObjectsTree.Node var3 : this.children) {
            var3.traverseDepthFirst(var1);
         }
      }

      private RequiredObjectsTree.Node addChild(IntegrationEntity var1) {
         return new RequiredObjectsTree.Node(this, var1);
      }

      private boolean isCycle() {
         for (RequiredObjectsTree.Node var1 = this.parent; var1 != null; var1 = var1.parent) {
            if (var1.item == this.item) {
               return true;
            }
         }

         return false;
      }

      private void collectAncestors(Set<IntegrationEntity> var1) {
         for (RequiredObjectsTree.Node var2 = this.parent; var2 != null; var2 = var2.parent) {
            var1.add(var2.item);
         }
      }

      private void collectDescendants(Set<IntegrationEntity> var1) {
         for (RequiredObjectsTree.Node var3 : this.children) {
            var1.add(var3.item);
            var3.collectDescendants(var1);
         }
      }

      private void collectItems(Set<IntegrationEntity> var1, Collection<IntegrationEntity> var2) {
         if (!var2.contains(this.item)) {
            var1.add(this.item);

            for (RequiredObjectsTree.Node var4 : this.children) {
               var4.collectItems(var1, var2);
            }
         }
      }

      private RequiredObjectsTree.Node find(IntegrationEntity var1) {
         if (var1 == this.item) {
            return this;
         }

         for (RequiredObjectsTree.Node var3 : this.children) {
            RequiredObjectsTree.Node var4 = var3.find(var1);
            if (var4 != null) {
               return var4;
            }
         }

         return null;
      }
   }
}
