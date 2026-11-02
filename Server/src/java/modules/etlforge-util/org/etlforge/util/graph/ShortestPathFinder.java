package org.etlforge.util.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public final class ShortestPathFinder<T> {
   private final ShortestPathFinder.Graph<T> graph;
   private Set<T> discovered;
   private Map<T, T> nodeToParent;
   private Queue<T> queue;

   public static <T> ShortestPathFinder<T> in(ShortestPathFinder.Graph<T> var0) {
      return new ShortestPathFinder<>(var0);
   }

   public ShortestPathFinder(ShortestPathFinder.Graph<T> var1) {
      this.graph = Preconditions.checkNotNull(var1, "graph");
   }

   public ImmutableList<T> findPath(T var1, T var2) {
      Preconditions.checkNotNull(var1, "from");
      Preconditions.checkNotNull(var2, "to");
      this.initSearch((T)var1);
      this.traverseGraph((T)var2);
      return this.constructPath((T)var1, (T)var2);
   }

   private void initSearch(T var1) {
      this.discovered = Sets.newHashSet();
      this.nodeToParent = Maps.newHashMap();
      this.queue = Lists.newLinkedList();
      this.queue.add((T)var1);
   }

   private void traverseGraph(T var1) {
      while (!this.queue.isEmpty()) {
         Object var2 = this.queue.remove();

         for (Object var4 : this.graph.neighbors((T)var2)) {
            if (!this.discovered.contains(var4)) {
               this.queue.add((T)var4);
               this.discovered.add((T)var4);
               this.nodeToParent.put((T)var4, (T)var2);
               if (var4 == var1) {
                  return;
               }
            }
         }
      }
   }

   private ImmutableList<T> constructPath(T var1, T var2) {
      if (!this.nodeToParent.containsKey(var2)) {
         return ImmutableList.of();
      }

      ArrayList var3 = Lists.newArrayList();
      Object var4 = var2;

      while (var4 != null) {
         var3.add(var4);
         var4 = this.nodeToParent.get(var4);
         if (var4 == var1 || var2 == var1 && var4 == var2) {
            var3.add(var4);
            break;
         }
      }

      Collections.reverse(var3);
      return ImmutableList.copyOf(var3);
   }

   public interface Graph<T> {
      Iterable<T> neighbors(T var1);
   }
}
