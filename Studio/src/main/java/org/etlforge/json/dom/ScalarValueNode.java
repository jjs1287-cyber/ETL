package org.etlforge.json.dom;

import com.google.common.base.Preconditions;
import org.etlforge.json.ScalarValueType;

public final class ScalarValueNode extends AbstractJsonNode implements ValueNode {
   public static final ScalarValueNode TRUE = new ScalarValueNode(ScalarValueType.BOOLEAN, Boolean.TRUE);
   public static final ScalarValueNode FALSE = new ScalarValueNode(ScalarValueType.BOOLEAN, Boolean.FALSE);
   public static final ScalarValueNode NULL = new ScalarValueNode(ScalarValueType.NULL, null);
   private final ScalarValueType type;
   private final Object value;

   public static ScalarValueNode stringValue(String var0) {
      Preconditions.checkNotNull(var0, "value");
      return new ScalarValueNode(ScalarValueType.STRING, var0);
   }

   public static ScalarValueNode numberValue(Number var0) {
      Preconditions.checkNotNull(var0, "value");
      return new ScalarValueNode(ScalarValueType.NUMBER, var0);
   }

   private ScalarValueNode(ScalarValueType var1, Object var2) {
      this.type = var1;
      this.value = var2;
   }

   public ScalarValueType getType() {
      return this.type;
   }

   public Object getValue() {
      return this.value;
   }

   @Override
   public int getFingerprint() {
      return this.type.hashCode();
   }

   public String formatForOutput() {
      return this.type.format(this.value);
   }

   @Override
   public JsonNode getChildAt(int var1) {
      throw new UnsupportedOperationException();
   }

   @Override
   public String toString() {
      String var1 = this.type.name();
      if (this.type != ScalarValueType.NULL) {
         var1 = var1 + "[" + this.value + "]";
      }

      return var1;
   }
}
