package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import com.google.common.base.Strings;
import org.etlforge.xml.XmlStringBuilder;

final class SingleConditionValue extends ConditionValue {
   private final String value;

   public SingleConditionValue(String var1) {
      this.value = Strings.nullToEmpty(var1);
   }

   public String getValue() {
      return this.value;
   }

   @Override
   public void appendToXml(XmlStringBuilder var1) {
      var1.addAttribute("value", this.value);
   }

   @Override
   public int hashCode() {
      return this.value.hashCode();
   }

   @Override
   public boolean equals(Object var1) {
      return var1 == this ? true : var1 instanceof SingleConditionValue && this.value.equals(((SingleConditionValue)var1).value);
   }

   @Override
   public String toString() {
      return this.value;
   }
}
