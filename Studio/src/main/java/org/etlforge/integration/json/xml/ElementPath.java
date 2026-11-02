package org.etlforge.integration.json.xml;

import com.google.common.collect.Lists;
import java.util.LinkedList;
import org.etlforge.collections.Collections2;

final class ElementPath {
   private final LinkedList<String> stack = Lists.newLinkedList();

   public void push(String var1) {
      this.stack.addLast(var1);
   }

   public int size() {
      return this.stack.size();
   }

   public String get() {
      return Collections2.join(this.stack, "/");
   }

   public void pop() {
      this.stack.removeLast();
   }

   @Override
   public String toString() {
      return this.get();
   }
}
