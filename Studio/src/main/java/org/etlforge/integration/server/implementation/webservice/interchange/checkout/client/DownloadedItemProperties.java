package org.etlforge.integration.server.implementation.webservice.interchange.checkout.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;
import org.etlforge.integration.server.implementation.webservice.WsKeyValuePairs;

public class DownloadedItemProperties implements Serializable {
   private String itemGUID;
   private WsKeyValuePairs properties;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(DownloadedItemProperties.class, true);

   public DownloadedItemProperties() {
   }

   public DownloadedItemProperties(String var1, WsKeyValuePairs var2) {
      this.itemGUID = var1;
      this.properties = var2;
   }

   public String getItemGUID() {
      return this.itemGUID;
   }

   public void setItemGUID(String var1) {
      this.itemGUID = var1;
   }

   public WsKeyValuePairs getProperties() {
      return this.properties;
   }

   public void setProperties(WsKeyValuePairs var1) {
      this.properties = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof DownloadedItemProperties)) {
         return false;
      }

      DownloadedItemProperties var2 = (DownloadedItemProperties)var1;
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
      boolean var3 = (this.itemGUID == null && var2.getItemGUID() == null || this.itemGUID != null && this.itemGUID.equals(var2.getItemGUID()))
         && (this.properties == null && var2.getProperties() == null || this.properties != null && this.properties.equals(var2.getProperties()));
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
      if (this.getItemGUID() != null) {
         var1 += this.getItemGUID().hashCode();
      }

      if (this.getProperties() != null) {
         var1 += this.getProperties().hashCode();
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItemProperties")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("itemGUID");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "ItemGUID"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("properties");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "Properties"));
      var0.setXmlType(new QName("http://webservice.implementation.server.integration.etlforge.invalid", "WsKeyValuePairs"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
