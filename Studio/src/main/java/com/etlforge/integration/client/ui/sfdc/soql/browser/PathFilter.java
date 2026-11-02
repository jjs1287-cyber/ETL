package com.etlforge.integration.client.ui.sfdc.soql.browser;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeFilter2;
import org.etlforge.ui.widget.tree.KongaTreeNode;

final class PathFilter implements KongaTreeFilter2 {
   private final List<Predicate<KongaTreeNode>> predicates = Lists.newArrayList();
   private final List<String> objectPath = Lists.newArrayList();
   private Set<KongaTreeNode> deepestNodes = Sets.newHashSet();

   public PathFilter(List<String> var1, boolean var2) {
      for (int var3 = 0; var3 < var1.size(); var3++) {
         boolean var4 = var3 == var1.size() - 1;
         boolean var5 = var4;
         boolean var6 = var4 ? var2 : true;
         PathFilter.NodeParticle var7 = new PathFilter.NodeParticle((String)var1.get(var3), var5, var6);
         this.predicates.add(var7);
         if (var6) {
            this.objectPath.add((String)var1.get(var3));
         }
      }

      if (var2) {
         this.predicates.add(Predicates.alwaysTrue());
      }
   }

   @Override
   public boolean apply(KongaTreeNode var1) {
      return this.checkNode(var1);
   }

   private boolean checkNode(KongaTreeNode var1) {
      int var2 = var1.getLevel();
      if (var2 == 0) {
         return true;
      }

      if (var2 > this.predicates.size()) {
         return true;
      }

      Predicate var3 = this.predicates.get(var2 - 1);
      boolean var4 = var3.apply(var1);
      if (var4 && var2 == this.predicates.size()) {
         this.deepestNodes.add(var1);
      }

      return var4;
   }

   @Override
   public void hasBeenApplied(KongaTree var1) {
      if (!this.deepestNodes.isEmpty()) {
         PathFilter.LastLevelExpander var2 = new PathFilter.LastLevelExpander(var1, this.deepestNodes);
         var2.expand();
         this.deepestNodes = Sets.newHashSet();
      }
   }

   private static class LastLevelExpander {
      private final KongaTree tree;
      private final Set<KongaTreeNode> lastLevel;
      private final Set<KongaTreeNode> alreadyExpanded = Sets.newHashSet();

      public LastLevelExpander(KongaTree var1, Set<KongaTreeNode> var2) {
         this.tree = var1;
         this.lastLevel = var2;
      }

      public void expand() {
         for (KongaTreeNode var2 : this.lastLevel) {
            if (var2.isFolder()) {
               this.expandFolder(var2);
            } else {
               this.expandParent(var2);
            }
         }
      }

      private void expandFolder(KongaTreeNode var1) {
         if (this.lastLevel.size() == 1) {
            this.tree.makeNodeExpanded(var1);
         }
      }

      private void expandParent(KongaTreeNode var1) {
         KongaTreeNode var2 = var1.getParent();
         if (!this.alreadyExpanded.contains(var2)) {
            this.tree.makeNodeExpanded(var2);
            this.alreadyExpanded.add(var2);
         }
      }
   }

   private static class NodeParticle implements Predicate<KongaTreeNode> {
      private final String name;
      private final boolean acceptsField;
      private final boolean exactMatch;

      public NodeParticle(String var1, boolean var2, boolean var3) {
         this.name = var1.toLowerCase();
         this.acceptsField = var2;
         this.exactMatch = var3;
      }

      public boolean apply(KongaTreeNode var1) {
         String var2 = var1.getDisplayName().toLowerCase();
         boolean var3 = this.exactMatch ? var2.equals(this.name) : var2.startsWith(this.name);
         if (var3) {
            return var1 instanceof FieldNode ? this.acceptsField : true;
         } else {
            return false;
         }
      }
   }
}
