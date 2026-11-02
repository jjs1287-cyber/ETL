package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import com.google.common.collect.ImmutableList;
import org.etlforge.xml.XmlStringBuilder;

final class MultiConditionValue extends ConditionValue {
   private final ImmutableList<String> values;

   public MultiConditionValue(Iterable<String> var1) {
      this.values = ImmutableList.copyOf(var1);
   }

   public ImmutableList<String> getValues() {
      return this.values;
   }

   @Override
   public void appendToXml(XmlStringBuilder var1) {
      for (String var3 : this.values) {
         var1.addElement("value");
         var1.setElementValue(var3);
         var1.closeElement();
      }
   }

   @Override
   public int hashCode() {
      return this.values.hashCode();
   }

   @Override
   public boolean equals(Object var1) {
      return var1 == this ? true : var1 instanceof MultiConditionValue && this.values.equals(((MultiConditionValue)var1).values);
   }

   @Override
   public String toString() {
      return this.values.toString();
   }
}
