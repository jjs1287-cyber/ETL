package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;

public final class NodeCollector extends AbstractNodePathQuery {
   public NodeCollector(TreeComponent var1) {
      super(var1);
   }

   public List<Node> findMatchingNodes(String var1) {
      if (this.isEmpty()) {
         return Collections.emptyList();
      }

      if (var1.isEmpty()) {
         return this.collectMatchesForEmptyFragment();
      }

      String[] var2 = var1.split("\\.|\\$");
      List var3 = this.collectCandidates(var2);
      return analyzeCandidates(var3, var2);
   }

   private List<Node> collectMatchesForEmptyFragment() {
      Node var1 = this.tree.m_root;
      if (!var1.hasChildren()) {
         return Lists.newArrayList();
      }

      var1 = var1.getChildAt(0);
      return this.isFlat() ? Lists.newArrayList(var1.getChildren()) : Lists.newArrayList(var1);
   }

   private List<Node> collectCandidates(String[] var1) {
      if (var1.length == 0) {
         return Collections.emptyList();
      }

      String var2 = var1[0];
      if (var2.isEmpty()) {
         return Collections.emptyList();
      }

      if (var1.length == 1) {
         return this.isFlat() ? this.collectMatchingNodesFromFlatTree(var2) : this.collectRootIfNameMatch(var2);
      }

      Node var3 = this.findStartNode(var2, this.isFlat());
      if (var3 == null) {
         return Collections.emptyList();
      }

      for (int var4 = 1; var4 < var1.length - 1; var4++) {
         var3 = findChild(var3, var1[var4]);
         if (var3 == null) {
            return Lists.newArrayList();
         }
      }

      return collectMatchingChildNodes(var3, var1[var1.length - 1]);
   }

   private List<Node> collectMatchingNodesFromFlatTree(String var1) {
      return !this.tree.m_root.hasChildren() ? Lists.newArrayList() : collectMatchingChildNodes(this.tree.m_root.getChildAt(0), var1);
   }

   private static List<Node> collectMatchingChildNodes(Node var0, String var1) {
      ArrayList var2 = Lists.newArrayList();

      for (Node var4 : var0.getChildren()) {
         String var5 = getName(var4);
         if (var5.startsWith(var1)) {
            var2.add(var4);
         }
      }

      return var2;
   }

   private List<Node> collectRootIfNameMatch(String var1) {
      ArrayList var2 = Lists.newArrayList();
      if (this.tree.m_root.hasChildren()) {
         Node var3 = this.tree.m_root.getChildAt(0);
         String var4 = getName(var3);
         if (var4.startsWith(var1)) {
            var2.add(var3);
         }
      }

      return var2;
   }

   private Node findStartNode(String var1, boolean var2) {
      if (!this.tree.m_root.hasChildren()) {
         return null;
      }

      if (var2) {
         for (Node var4 : this.tree.m_root.getChildren()) {
            String var5 = getName(var4);
            if (var1.equals(var5)) {
               return var4;
            }
         }
      }

      Node var6 = this.tree.m_root.getChildAt(0);
      String var7 = getName(var6);
      return var1.equals(var7) ? var6 : null;
   }

   private static Node findChild(Node var0, String var1) {
      for (Node var3 : var0.getChildren()) {
         String var4 = getName(var3);
         if (var4.equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   private static List<Node> analyzeCandidates(List<Node> var0, String[] var1) {
      if (var0.size() == 1) {
         Node var2 = (Node)var0.get(0);
         if (var2.isFolder()) {
            String var3 = getName(var2);
            if (var3.equals(var1[var1.length - 1])) {
               return Lists.newArrayList(var2.getChildren());
            }
         }
      }

      return var0;
   }
}
