package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public final class BreadthFirstTraverser implements Iterable<Node> {
   private final LinkedList<Node> process;

   public BreadthFirstTraverser(Node var1) {
      Preconditions.checkNotNull(var1, "root");
      this.process = Lists.newLinkedList();
      this.process.add(var1);
   }

   @Override
   public Iterator<Node> iterator() {
      return new Iterator<Node>() {
         @Override
         public boolean hasNext() {
            return !BreadthFirstTraverser.this.process.isEmpty();
         }

         public Node next() {
            if (BreadthFirstTraverser.this.process.isEmpty()) {
               throw new NoSuchElementException();
            }

            Node var1 = BreadthFirstTraverser.this.process.poll();
            int var2 = 0;

            for (int var3 = var1.getChildCount(); var2 < var3; var2++) {
               BreadthFirstTraverser.this.process.addLast(var1.getChildAt(var2));
            }

            return var1;
         }

         @Override
         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }
}
