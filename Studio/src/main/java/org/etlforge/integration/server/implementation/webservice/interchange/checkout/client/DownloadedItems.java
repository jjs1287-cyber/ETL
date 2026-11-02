package org.etlforge.integration.server.implementation.webservice.interchange.checkout.client;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class DownloadedItems implements Serializable {
   private long itemType;
   private DownloadedItemProperties[] items;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(DownloadedItems.class, true);

   public DownloadedItems() {
   }

   public DownloadedItems(long var1, DownloadedItemProperties[] var3) {
      this.itemType = var1;
      this.items = var3;
   }

   public long getItemType() {
      return this.itemType;
   }

   public void setItemType(long var1) {
      this.itemType = var1;
   }

   public DownloadedItemProperties[] getItems() {
      return this.items;
   }

   public void setItems(DownloadedItemProperties[] var1) {
      this.items = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof DownloadedItems)) {
         return false;
      }

      DownloadedItems var2 = (DownloadedItems)var1;
      if (var1 == null) {
         return false;
      }

      if (this == var1) {
         return true;
      }

      if (this.__equalsCalc != null) {
         return this.__equalsCalc == var1;
      }

      this.__equalsCalc = var1;
      boolean var3 = this.itemType == var2.getItemType()
         && (this.items == null && var2.getItems() == null || this.items != null && Arrays.equals(this.items, var2.getItems()));
      this.__equalsCalc = null;
      return var3;
   }

   @Override
   public synchronized int hashCode() {
      if (this.__hashCodeCalc) {
         return 0;
      }

      this.__hashCodeCalc = true;
      int var1 = 1;
      var1 += new Long(this.getItemType()).hashCode();
      if (this.getItems() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getItems()); var2++) {
            Object var3 = Array.get(this.getItems(), var2);
            if (var3 != null && !var3.getClass().isArray()) {
               var1 += var3.hashCode();
            }
         }
      }

      this.__hashCodeCalc = false;
      return var1;
   }

   public static TypeDesc getTypeDesc() {
      return typeDesc;
   }

   public static Serializer getSerializer(String var0, Class var1, QName var2) {
      return new BeanSerializer(var1, var2, typeDesc);
   }

   public static Deserializer getDeserializer(String var0, Class var1, QName var2) {
      return new BeanDeserializer(var1, var2, typeDesc);
   }

   static {
      typeDesc.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("itemType");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "ItemType"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("items");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "Items"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItemProperties")
      );
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
