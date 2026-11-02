package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;
import org.etlforge.lang.KongaStringUtils;

public final class NodePathCompleter extends AbstractNodePathQuery {
   public NodePathCompleter(TreeComponent var1) {
      super(var1);
   }

   public NodePathCompleter.Completion complete(String var1) {
      if (this.isEmpty()) {
         return null;
      } else if (var1.isEmpty()) {
         return null;
      } else {
         String[] var2 = var1.split("\\.|\\$");
         List var3 = this.getCandidates(var2);
         if (var3 != null) {
            return this.complete(var1, var2, var3);
         } else {
            return "_flat_".startsWith(var1) && !var1.equals("_flat_") ? new NodePathCompleter.Completion("_flat_", false) : null;
         }
      }
   }

   private List<Node> getCandidates(String[] var1) {
      List var2 = null;
      if (var1.length == 1) {
         var2 = findMatchingDescendants(this.tree.m_root, var1[0]);
      } else {
         var2 = this.collectMultiPartCandidates(var1);
      }

      return var2;
   }

   private static List<Node> findMatchingDescendants(Node var0, String var1) {
      List var2 = collectMatchingChildren(var0, var1);
      if (var2 != null) {
         return var2;
      }

      for (Node var4 : var0.getChildren()) {
         var2 = findMatchingDescendants(var4, var1);
         if (var2 != null) {
            return var2;
         }
      }

      return null;
   }

   private static List<Node> collectMatchingChildren(Node var0, String var1) {
      ArrayList var2 = null;

      for (Node var4 : var0.getChildren()) {
         String var5 = getName(var4);
         if (var5.startsWith(var1)) {
            if (var2 == null) {
               var2 = Lists.newArrayList();
            }

            var2.add(var4);
         }
      }

      return var2;
   }

   private List<Node> collectMultiPartCandidates(String[] var1) {
      Node var2 = findDescendant(this.tree.m_root, var1[0]);
      if (var2 == null) {
         return null;
      }

      for (int var3 = 1; var3 < var1.length - 1; var3++) {
         var2 = findChild(var2, var1[var3]);
         if (var2 == null) {
            return null;
         }
      }

      String var4 = var1[var1.length - 1];
      return collectMatchingChildren(var2, var4);
   }

   private static Node findDescendant(Node var0, String var1) {
      String var2 = getName(var0);
      if (var2.equals(var1)) {
         return var0;
      }

      for (Node var4 : var0.getChildren()) {
         Node var5 = findDescendant(var4, var1);
         if (var5 != null) {
            return var5;
         }
      }

      return null;
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

   private NodePathCompleter.Completion complete(String var1, String[] var2, List<Node> var3) {
      String var4 = var2[var2.length - 1];
      switch (var3.size()) {
         case 0:
            return null;
         case 1:
            return completeSingleNode(var1, var4, var3);
         default:
            return completeMultipleNodes(var1, var4, var3);
      }
   }

   private static NodePathCompleter.Completion completeSingleNode(String var0, String var1, List<Node> var2) {
      Node var3 = (Node)var2.get(0);
      String var4 = getName(var3);
      if (var4.length() > var1.length()) {
         String var5 = var0 + var4.substring(var1.length());
         if (var3.isFolder()) {
            if (var3.isLoopNode()) {
               var5 = var5 + ".";
            } else {
               var5 = var5 + "$";
            }
         }

         return new NodePathCompleter.Completion(var5, false);
      } else {
         return null;
      }
   }

   private static NodePathCompleter.Completion completeMultipleNodes(String var0, String var1, List<Node> var2) {
      ArrayList var3 = Lists.newArrayList();

      for (Node var5 : var2) {
         var3.add(getName(var5));
      }

      String var6 = KongaStringUtils.findLongestCommonStartFragment(var3, false);
      if (var6.length() > var1.length()) {
         String var7 = var0 + var6.substring(var1.length());
         return new NodePathCompleter.Completion(var7, true);
      } else {
         return null;
      }
   }

   public static final class Completion {
      public final String value;
      public final boolean multiMatch;

      private Completion(String var1, boolean var2) {
         this.value = var1;
         this.multiMatch = var2;
      }

      public NodePathCompleter.Completion substring(int var1) {
         return new NodePathCompleter.Completion(this.value.substring(var1), this.multiMatch);
      }
   }
}
