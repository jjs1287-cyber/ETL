package org.etlforge.ui.widget.tree;

import com.google.common.base.Predicate;
import javax.swing.tree.TreeModel;

public final class BreadthFirstSearcher {
   public static Object findFirst(TreeModel var0, Predicate<Object> var1) {
      BreadthFirstTraverser var2 = new BreadthFirstTraverser(var0);
      return traverse(var1, var2);
   }

   private static Object traverse(Predicate<Object> var0, Iterable<Object> var1) {
      for (Object var3 : var1) {
         if (var0.apply(var3)) {
            return var3;
         }
      }

      return null;
   }

   public static Object findFirst(TreeModel var0, Object var1, Predicate<Object> var2) {
      BreadthFirstTraverser var3 = new BreadthFirstTraverser(var0, var1);
      return traverse(var2, var3);
   }

   private BreadthFirstSearcher() {
   }
}
