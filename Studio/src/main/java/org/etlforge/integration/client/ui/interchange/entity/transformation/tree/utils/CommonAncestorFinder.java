package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class CommonAncestorFinder {
   public static Node findCommonAncestor(Iterable<Node> var0) {
      List<List<Node>> var1 = createParentChains(var0);
      return findCommonAncestor(var1);
   }

   private static List<List<Node>> createParentChains(Iterable<Node> var0) {
      ArrayList var1 = Lists.newArrayList();

      for (Node var3 : var0) {
         LinkedList var4 = Lists.newLinkedList();

         for (Node var5 = var3.m_parent; var5 != null; var5 = var5.m_parent) {
            var4.addFirst(var5);
         }

         var1.add(var4);
      }

      return var1;
   }

   private static Node findCommonAncestor(List<List<Node>> var0) {
      int var1 = lengthOfShortestChain(var0);
      List var2 = toIterators(var0);
      Node var3 = null;

      for (int var4 = 0; var4 < var1; var4++) {
         Node var5 = null;

         for (Object __etlItem39 : var2) {
            Iterator var7 = (Iterator) __etlItem39;
            Node var8 = (Node)var7.next();
            if (var5 == null) {
               var5 = var8;
            } else if (var5 != var8) {
               return var3;
            }
         }

         var3 = var5;
      }

      return var3;
   }

   private static int lengthOfShortestChain(List<List<Node>> var0) {
      int var1 = Integer.MAX_VALUE;

      for (List var3 : var0) {
         var1 = Math.min(var1, var3.size());
      }

      return var1;
   }

   private static List<Iterator<Node>> toIterators(List<List<Node>> var0) {
      ArrayList var1 = Lists.newArrayList();

      for (List var3 : var0) {
         var1.add(var3.iterator());
      }

      return var1;
   }

   private CommonAncestorFinder() {
   }
}
