package org.etlforge.ui.widget.tree;

import com.google.common.collect.AbstractIterator;
import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;
import javax.swing.tree.TreeModel;

public class PreOrderTraverser implements Iterable<Object> {
   private final TreeModel model;
   private final Stack<Iterator<Object>> stack;

   public PreOrderTraverser(TreeModel var1) {
      this(var1, var1.getRoot());
   }

   public PreOrderTraverser(TreeModel var1, Object var2) {
      this.model = var1;
      this.stack = new Stack<>();
      this.stack.add(Collections.singletonList(var2).iterator());
   }

   @Override
   public Iterator<Object> iterator() {
      return new AbstractIterator<Object>() {
         @Override
         protected Object computeNext() {
            if (PreOrderTraverser.this.stack.isEmpty()) {
               return this.endOfData();
            }

            Iterator var1 = PreOrderTraverser.this.stack.peek();
            Object var2 = var1.next();
            if (!var1.hasNext()) {
               PreOrderTraverser.this.stack.pop();
            }

            if (PreOrderTraverser.this.model.getChildCount(var2) > 0) {
               PreOrderTraverser.this.stack.push(new PreOrderTraverser.Children(PreOrderTraverser.this.model, var2));
            }

            return var2;
         }
      };
   }

   private static class Children extends AbstractIterator<Object> {
      private final TreeModel model;
      private final Object parent;
      private final int childCount;
      private int currentChild;

      public Children(TreeModel var1, Object var2) {
         this.model = var1;
         this.parent = var2;
         this.childCount = var1.getChildCount(var2);
      }

      @Override
      protected Object computeNext() {
         if (this.currentChild < this.childCount) {
            Object var1 = this.model.getChild(this.parent, this.currentChild);
            this.currentChild++;
            return var1;
         } else {
            return this.endOfData();
         }
      }
   }
}
