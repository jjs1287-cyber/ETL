package org.etlforge.json.dom;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public final class ArrayNode extends AbstractJsonNode implements RootNode, ValueNode {
   private final List<ValueNode> items = Lists.newArrayList();
   private final boolean skipDuplicateStructures;
   private final Set<Integer> itemFingerprints;

   public ArrayNode() {
      this(false);
   }

   public ArrayNode(boolean var1) {
      this.skipDuplicateStructures = var1;
      if (var1) {
         this.itemFingerprints = Sets.newHashSet();
      } else {
         this.itemFingerprints = null;
      }
   }

   public void addItem(ValueNode var1) {
      if (this.skipDuplicateStructures && (var1 instanceof ArrayNode || var1 instanceof ObjectNode)) {
         Integer var2 = var1.getFingerprint();
         if (!this.itemFingerprints.add(var2)) {
            return;
         }
      }

      var1.setParent(this);
      this.items.add(var1);
   }

   public ValueNode getChildAt(int var1) {
      return this.items.get(var1);
   }

   @Override
   public Iterable<ValueNode> getChildren() {
      return Collections.unmodifiableList(this.items);
   }

   @Override
   public int getFingerprint() {
      int var1 = 17;

      for (ValueNode var3 : this.items) {
         var1 = 31 * var1 + var3.getFingerprint();
      }

      return var1;
   }

   @Override
   public String toString() {
      return "array[" + this.items.size() + "]";
   }

   public boolean isSimpleForm() {
      if (this.items.isEmpty()) {
         return false;
      }

      for (ValueNode var2 : this.items) {
         if (!(var2 instanceof ObjectNode)) {
            return false;
         }

         ObjectNode var3 = (ObjectNode)var2;
         if (var3.getFieldCount() != 1) {
            return false;
         }
      }

      return true;
   }
}
