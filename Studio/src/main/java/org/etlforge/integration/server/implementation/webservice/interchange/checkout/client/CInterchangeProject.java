package org.etlforge.integration.server.implementation.webservice.interchange.checkout.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class CInterchangeProject implements Serializable {
   private String id;
   private String name;
   private String description;
   private String ownerUserId;
   private String ownerUserName;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(CInterchangeProject.class, true);

   public CInterchangeProject() {
   }

   public CInterchangeProject(String var1, String var2, String var3, String var4, String var5) {
      this.id = var1;
      this.name = var2;
      this.description = var3;
      this.ownerUserId = var4;
      this.ownerUserName = var5;
   }

   public String getId() {
      return this.id;
   }

   public void setId(String var1) {
      this.id = var1;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String var1) {
      this.description = var1;
   }

   public String getOwnerUserId() {
      return this.ownerUserId;
   }

   public void setOwnerUserId(String var1) {
      this.ownerUserId = var1;
   }

   public String getOwnerUserName() {
      return this.ownerUserName;
   }

   public void setOwnerUserName(String var1) {
      this.ownerUserName = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof CInterchangeProject)) {
         return false;
      }

      CInterchangeProject var2 = (CInterchangeProject)var1;
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
      boolean var3 = (this.id == null && var2.getId() == null || this.id != null && this.id.equals(var2.getId()))
         && (this.name == null && var2.getName() == null || this.name != null && this.name.equals(var2.getName()))
         && (this.description == null && var2.getDescription() == null || this.description != null && this.description.equals(var2.getDescription()))
         && (this.ownerUserId == null && var2.getOwnerUserId() == null || this.ownerUserId != null && this.ownerUserId.equals(var2.getOwnerUserId()))
         && (this.ownerUserName == null && var2.getOwnerUserName() == null || this.ownerUserName != null && this.ownerUserName.equals(var2.getOwnerUserName()));
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
      if (this.getId() != null) {
         var1 += this.getId().hashCode();
      }

      if (this.getName() != null) {
         var1 += this.getName().hashCode();
      }

      if (this.getDescription() != null) {
         var1 += this.getDescription().hashCode();
      }

      if (this.getOwnerUserId() != null) {
         var1 += this.getOwnerUserId().hashCode();
      }

      if (this.getOwnerUserName() != null) {
         var1 += this.getOwnerUserName().hashCode();
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "CInterchangeProject")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("id");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "Id"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("name");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "Name"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("description");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "Description"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("ownerUserId");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "OwnerUserId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("ownerUserName");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "OwnerUserName"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
