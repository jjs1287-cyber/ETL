package org.etlforge.integration.server.implementation.webservice.interchange.ldap;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsObjectClassInfo implements Serializable {
   private String oid;
   private String name;
   private String type;
   private String description;
   private boolean isObsolete;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsObjectClassInfo.class, true);

   public WsObjectClassInfo() {
   }

   public WsObjectClassInfo(String var1, String var2, String var3, String var4, boolean var5) {
      this.oid = var1;
      this.name = var2;
      this.type = var3;
      this.description = var4;
      this.isObsolete = var5;
   }

   public String getOid() {
      return this.oid;
   }

   public void setOid(String var1) {
      this.oid = var1;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getType() {
      return this.type;
   }

   public void setType(String var1) {
      this.type = var1;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String var1) {
      this.description = var1;
   }

   public boolean isIsObsolete() {
      return this.isObsolete;
   }

   public void setIsObsolete(boolean var1) {
      this.isObsolete = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsObjectClassInfo)) {
         return false;
      }

      WsObjectClassInfo var2 = (WsObjectClassInfo)var1;
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
      boolean var3 = (this.oid == null && var2.getOid() == null || this.oid != null && this.oid.equals(var2.getOid()))
         && (this.name == null && var2.getName() == null || this.name != null && this.name.equals(var2.getName()))
         && (this.type == null && var2.getType() == null || this.type != null && this.type.equals(var2.getType()))
         && (this.description == null && var2.getDescription() == null || this.description != null && this.description.equals(var2.getDescription()))
         && this.isObsolete == var2.isIsObsolete();
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
      if (this.getOid() != null) {
         var1 += this.getOid().hashCode();
      }

      if (this.getName() != null) {
         var1 += this.getName().hashCode();
      }

      if (this.getType() != null) {
         var1 += this.getType().hashCode();
      }

      if (this.getDescription() != null) {
         var1 += this.getDescription().hashCode();
      }

      var1 += (this.isIsObsolete() ? Boolean.TRUE : Boolean.FALSE).hashCode();
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
      typeDesc.setXmlType(new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsObjectClassInfo"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("oid");
      var0.setXmlName(new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "oid"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("name");
      var0.setXmlName(new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "name"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("type");
      var0.setXmlName(new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "type"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("description");
      var0.setXmlName(new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "description"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("isObsolete");
      var0.setXmlName(new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "isObsolete"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
   }
}
