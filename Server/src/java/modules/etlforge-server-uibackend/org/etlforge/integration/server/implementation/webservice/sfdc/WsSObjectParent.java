package org.etlforge.integration.server.implementation.webservice.sfdc;

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

public class WsSObjectParent implements Serializable {
   private String name;
   private String[] objectTypes;
   private boolean nameObject;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsSObjectParent.class, true);

   public WsSObjectParent() {
   }

   public WsSObjectParent(String var1, String[] var2, boolean var3) {
      this.name = var1;
      this.objectTypes = var2;
      this.nameObject = var3;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String[] getObjectTypes() {
      return this.objectTypes;
   }

   public void setObjectTypes(String[] var1) {
      this.objectTypes = var1;
   }

   public boolean isNameObject() {
      return this.nameObject;
   }

   public void setNameObject(boolean var1) {
      this.nameObject = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsSObjectParent)) {
         return false;
      }

      WsSObjectParent var2 = (WsSObjectParent)var1;
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
      boolean var3 = (this.name == null && var2.getName() == null || this.name != null && this.name.equals(var2.getName()))
         && (this.objectTypes == null && var2.getObjectTypes() == null || this.objectTypes != null && Arrays.equals(this.objectTypes, var2.getObjectTypes()))
         && this.nameObject == var2.isNameObject();
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
      if (this.getName() != null) {
         var1 += this.getName().hashCode();
      }

      if (this.getObjectTypes() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getObjectTypes()); var2++) {
            Object var3 = Array.get(this.getObjectTypes(), var2);
            if (var3 != null && !var3.getClass().isArray()) {
               var1 += var3.hashCode();
            }
         }
      }

      var1 += (this.isNameObject() ? Boolean.TRUE : Boolean.FALSE).hashCode();
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
      typeDesc.setXmlType(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsSObjectParent"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("name");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "name"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("objectTypes");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "objectTypes"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("nameObject");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "nameObject"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
   }
}
