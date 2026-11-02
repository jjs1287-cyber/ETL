package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.etlforge.system.ArgCheck;
import org.etlforge.util.SortOrder;
import org.etlforge.xml.XmlStringBuilder;

final class Order {
   private final String attribute;
   private final SortOrder direction;

   public Order(String var1, SortOrder var2) {
      this.attribute = ArgCheck.checkNotEmpty(var1, "attribute");
      this.direction = Preconditions.checkNotNull(var2, "direction") == SortOrder.NONE ? SortOrder.ASCENDING : var2;
   }

   public String getAttribute() {
      return this.attribute;
   }

   public SortOrder getDirection() {
      return this.direction;
   }

   public void appendToXml(XmlStringBuilder var1) {
      var1.addElement("order");
      var1.addAttribute("attribute", this.attribute);
      var1.addAttribute("descending", this.direction == SortOrder.DESCENDING ? "true" : "false");
      var1.closeElement();
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.direction, this.attribute);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof Order)) {
         return false;
      }

      Order var2 = (Order)var1;
      return this.direction == var2.direction && this.attribute.equals(var2.attribute);
   }
}
