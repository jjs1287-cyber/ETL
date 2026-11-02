package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Objects.ToStringHelper;
import org.etlforge.integration.mscrm.metadata.MSCrmAttribute;
import org.etlforge.xml.XmlStringBuilder;

final class Condition implements FilterEntry {
   private final MSCrmAttribute attribute;
   private final ConditionOperator operator;
   private final ConditionValue value;

   public Condition(MSCrmAttribute var1, ConditionOperator var2, ConditionValue var3) {
      this.attribute = Preconditions.checkNotNull(var1, "attributeName");
      this.operator = Preconditions.checkNotNull(var2, "operator");
      this.value = Preconditions.checkNotNull(var3, "value");
   }

   public MSCrmAttribute getAttribute() {
      return this.attribute;
   }

   public ConditionOperator getOperator() {
      return this.operator;
   }

   public ConditionValue getValue() {
      return this.value;
   }

   @Override
   public void appendToXml(XmlStringBuilder var1) {
      var1.addElement("condition");
      var1.addAttribute("attribute", this.attribute.getName());
      var1.addAttribute("operator", this.operator.getXml());
      this.value.appendToXml(var1);
      var1.closeElement();
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.operator, this.attribute, this.value);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof Condition)) {
         return false;
      }

      Condition var2 = (Condition)var1;
      return this.operator == var2.operator && this.attribute.equals(var2.attribute) && this.value.equals(var2.value);
   }

   @Override
   public String toString() {
      ToStringHelper var1 = Objects.toStringHelper(this).add("attribute", this.attribute.getName()).add("operator", this.operator);
      if (this.value != ConditionValue.NONE) {
         var1.add("value", this.value);
      }

      return var1.toString();
   }
}
