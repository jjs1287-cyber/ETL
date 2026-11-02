package org.etlforge.json.dom;

import com.google.common.base.Preconditions;
import org.etlforge.system.ArgCheck;

public final class FieldNode extends AbstractJsonNode {
   private final String name;
   private ValueNode value;

   public FieldNode(String var1) {
      this(var1, null);
   }

   public FieldNode(String var1, ValueNode var2) {
      ArgCheck.checkNotEmpty(var1, "name");
      this.name = var1;
      this.value = var2;
   }

   public String getName() {
      return this.name;
   }

   public ValueNode getValue() {
      return this.value;
   }

   public void setValue(ValueNode var1) {
      Preconditions.checkNotNull(var1, "value");
      this.value = var1;
      var1.setParent(this);
   }

   @Override
   public JsonNode getChildAt(int var1) {
      if (var1 == 0) {
         return this.value;
      } else {
         throw new IllegalArgumentException("No such child position: " + var1);
      }
   }

   @Override
   public int getFingerprint() {
      int var1 = 17;
      var1 = 31 * var1 + this.name.hashCode();
      if (this.value != null) {
         var1 = 31 * var1 + this.value.getFingerprint();
      }

      return var1;
   }

   @Override
   public String toString() {
      return "Field[" + this.name + "]";
   }
}
