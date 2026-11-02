package org.etlforge.ui.widget.tree;

import com.google.common.collect.AbstractIterator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.tree.TreeModel;

public class BreadthFirstTraverser implements Iterable<Object> {
   private final TreeModel model;
   private final Queue<Object> queue = new LinkedList<>();

   public BreadthFirstTraverser(TreeModel var1) {
      this(var1, var1.getRoot());
   }

   public BreadthFirstTraverser(TreeModel var1, Object var2) {
      this.model = var1;
      this.queue.add(var2);
   }

   @Override
   public Iterator<Object> iterator() {
      return new AbstractIterator<Object>() {
         @Override
         protected Object computeNext() {
            if (BreadthFirstTraverser.this.queue.isEmpty()) {
               return this.endOfData();
            }

            Object var1 = BreadthFirstTraverser.this.queue.poll();
            int var2 = 0;

            for (int var3 = BreadthFirstTraverser.this.model.getChildCount(var1); var2 < var3; var2++) {
               BreadthFirstTraverser.this.queue.add(BreadthFirstTraverser.this.model.getChild(var1, var2));
            }

            return var1;
         }
      };
   }
}
