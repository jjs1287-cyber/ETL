package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import com.google.common.collect.ImmutableList;
import org.etlforge.xml.XmlStringBuilder;

final class RangeConditionValue extends ConditionValue {
   private final MultiConditionValue impl;

   public RangeConditionValue(String var1, String var2) {
      this.impl = new MultiConditionValue(ImmutableList.of(var1, var2));
   }

   public String lower() {
      return this.impl.getValues().get(0);
   }

   public String upper() {
      return this.impl.getValues().get(1);
   }

   @Override
   public void appendToXml(XmlStringBuilder var1) {
      this.impl.appendToXml(var1);
   }

   @Override
   public int hashCode() {
      return this.impl.hashCode();
   }

   @Override
   public boolean equals(Object var1) {
      return var1 == this ? true : var1 instanceof RangeConditionValue && this.impl.equals(((RangeConditionValue)var1).impl);
   }

   @Override
   public String toString() {
      return this.impl.toString();
   }
}
