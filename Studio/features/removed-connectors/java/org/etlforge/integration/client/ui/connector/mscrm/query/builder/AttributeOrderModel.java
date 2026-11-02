package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import com.google.common.collect.ImmutableList;
import org.etlforge.integration.mscrm.metadata.MSCrmAttribute;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.ListProperty;
import org.etlforge.xml.XmlStringBuilder;

final class AttributeOrderModel extends AbstractPropertyBasedModel {
   public static final String ORDER = "order";
   private final ImmutableList<MSCrmAttribute> availableAttributes;
   private final ListProperty<Order> orderList = this.newListProperty("order");

   public AttributeOrderModel(Iterable<MSCrmAttribute> var1) {
      this.availableAttributes = ImmutableList.copyOf(var1);
   }

   AttributeOrderModel(AttributeOrderModel var1) {
      this.availableAttributes = var1.availableAttributes;
      this.orderList.setSilently(var1.orderList.get());
   }

   public ImmutableList<MSCrmAttribute> getAvailableAttributes() {
      return this.availableAttributes;
   }

   public void appendToXml(XmlStringBuilder var1) {
      for (Order var3 : this.orderList) {
         var3.appendToXml(var1);
      }
   }

   public void add(Order var1) {
      this.orderList.add(var1);
   }

   public void remove(Iterable<Order> var1) {
      this.orderList.removeAll(var1);
   }

   public void replace(Order var1, Order var2) {
      this.orderList.identityReplace(var1, var2);
   }
}
