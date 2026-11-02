package org.etlforge.integration.server.implementation.webservice.interchange.checkout.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class CPermission implements Serializable {
   private String itemId;
   private int entityTypeId;
   private long allowBit;
   private long denyBits;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(CPermission.class, true);

   public CPermission() {
   }

   public CPermission(String var1, int var2, long var3, long var5) {
      this.itemId = var1;
      this.entityTypeId = var2;
      this.allowBit = var3;
      this.denyBits = var5;
   }

   public String getItemId() {
      return this.itemId;
   }

   public void setItemId(String var1) {
      this.itemId = var1;
   }

   public int getEntityTypeId() {
      return this.entityTypeId;
   }

   public void setEntityTypeId(int var1) {
      this.entityTypeId = var1;
   }

   public long getAllowBit() {
      return this.allowBit;
   }

   public void setAllowBit(long var1) {
      this.allowBit = var1;
   }

   public long getDenyBits() {
      return this.denyBits;
   }

   public void setDenyBits(long var1) {
      this.denyBits = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof CPermission)) {
         return false;
      }

      CPermission var2 = (CPermission)var1;
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
      boolean var3 = (this.itemId == null && var2.getItemId() == null || this.itemId != null && this.itemId.equals(var2.getItemId()))
         && this.entityTypeId == var2.getEntityTypeId()
         && this.allowBit == var2.getAllowBit()
         && this.denyBits == var2.getDenyBits();
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
      if (this.getItemId() != null) {
         var1 += this.getItemId().hashCode();
      }

      var1 += this.getEntityTypeId();
      var1 += new Long(this.getAllowBit()).hashCode();
      var1 += new Long(this.getDenyBits()).hashCode();
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "CPermission")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("itemId");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "ItemId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("entityTypeId");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "EntityTypeId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("allowBit");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "AllowBit"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("denyBits");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DenyBits"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
   }
}
