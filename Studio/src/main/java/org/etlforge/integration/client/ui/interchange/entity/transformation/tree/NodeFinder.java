package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;
import org.etlforge.string.StringMatcher;
import org.etlforge.string.StringMatchers;

public final class NodeFinder extends AbstractNodePathQuery {
   private final StringMatcher matcher = StringMatchers.EQUALS;

   public NodeFinder(TreeComponent var1) {
      super(var1);
   }

   public Node findNode(String var1) {
      if (this.isEmpty()) {
         return null;
      }

      if (var1.equals("_flat_")) {
         return this.getFlatRoot();
      }

      String[] var2 = var1.split("\\.|\\$");
      Node var3 = this.findStartPoint(this.tree.m_root, var2[0]);

      for (int var4 = 1; var4 < var2.length && var3 != null; var4++) {
         var3 = this.findMatchingChild(var3, var2[var4]);
      }

      return var3;
   }

   private Node getFlatRoot() {
      Node var1 = this.tree.m_root;
      return var1.m_deName.equals("[]") ? var1 : null;
   }

   private Node findStartPoint(Node var1, String var2) {
      String var3 = getName(var1);
      if (this.matcher.matches(var2, var3)) {
         return var1;
      }

      for (Node var5 : var1.getChildren()) {
         Node var6 = this.findStartPoint(var5, var2);
         if (var6 != null) {
            return var6;
         }
      }

      return null;
   }

   private Node findMatchingChild(Node var1, String var2) {
      for (Node var4 : var1.getChildren()) {
         String var5 = getName(var4);
         if (this.matcher.matches(var2, var5)) {
            return var4;
         }
      }

      return null;
   }
}
