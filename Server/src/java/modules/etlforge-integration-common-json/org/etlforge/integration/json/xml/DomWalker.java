package org.etlforge.integration.json.xml;

import java.util.Stack;
import org.etlforge.json.dom.ArrayNode;
import org.etlforge.json.dom.FieldNode;
import org.etlforge.json.dom.JsonNode;
import org.etlforge.json.dom.RootNode;

final class DomWalker {
   private final RootNode root;
   private final Stack<JsonNode> parentStack = new Stack<>();
   private final Stack<DomWalker.ChildIndex> childIndexStack = new Stack<>();

   public DomWalker(RootNode var1) {
      this.root = var1;
   }

   public JsonNode current() {
      return this.parentStack.isEmpty() ? null : this.parentStack.peek();
   }

   public boolean isSimpleArray() {
      JsonNode var1 = this.current();
      return var1 instanceof ArrayNode && ((ArrayNode)var1).isSimpleForm();
   }

   public JsonNode down() {
      if (this.parentStack.isEmpty()) {
         this.pushNewParent(this.root);
         return this.root;
      }

      JsonNode var1 = this.parentStack.peek();
      DomWalker.ChildIndex var2 = this.childIndexStack.peek();
      JsonNode var3 = var2.getChildAndIncrease(var1);
      if (var3 != null) {
         this.pushNewParent(var3);
      }

      return var3;
   }

   private void pushNewParent(JsonNode var1) {
      this.parentStack.push(var1);
      this.childIndexStack.push(new DomWalker.ChildIndex());
   }

   public void startField() {
      JsonNode var1 = this.parentStack.peek();
      DomWalker.ChildIndex var2 = this.childIndexStack.peek();
      JsonNode var3 = var2.getChildAndIncrease(var1);
      this.pushNewParent(var3);
   }

   public void endField() {
      this.parentStack.pop();
      this.childIndexStack.pop();
   }

   public void endObjectOrArray() {
      this.parentStack.pop();
      this.childIndexStack.pop();
      if (!this.parentStack.isEmpty() && this.parentStack.peek() instanceof FieldNode) {
         this.endField();
      }
   }

   public void scalarArrayItem() {
   }

   @Override
   public String toString() {
      JsonNode var1 = this.current();
      return var1 == null ? "[null]" : var1.toString();
   }

   private static class ChildIndex {
      private int index;

      private ChildIndex() {
      }

      public JsonNode getChildAndIncrease(JsonNode var1) {
         JsonNode var2 = var1.getChildAt(this.index);
         this.index++;
         return var2;
      }

      @Override
      public String toString() {
         return "Child index: " + this.index;
      }
   }
}
