package org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client;

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

public class WsDataElement implements Serializable {
   private String name;
   private String type;
   private String value;
   private String scope;
   private boolean isArray;
   private WsDataElement[] arrValue;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsDataElement.class, true);

   public WsDataElement() {
   }

   public WsDataElement(String var1, String var2, String var3, String var4, boolean var5, WsDataElement[] var6) {
      this.name = var1;
      this.type = var2;
      this.value = var3;
      this.scope = var4;
      this.isArray = var5;
      this.arrValue = var6;
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

   public String getValue() {
      return this.value;
   }

   public void setValue(String var1) {
      this.value = var1;
   }

   public String getScope() {
      return this.scope;
   }

   public void setScope(String var1) {
      this.scope = var1;
   }

   public boolean isIsArray() {
      return this.isArray;
   }

   public void setIsArray(boolean var1) {
      this.isArray = var1;
   }

   public WsDataElement[] getArrValue() {
      return this.arrValue;
   }

   public void setArrValue(WsDataElement[] var1) {
      this.arrValue = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsDataElement)) {
         return false;
      }

      WsDataElement var2 = (WsDataElement)var1;
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
         && (this.type == null && var2.getType() == null || this.type != null && this.type.equals(var2.getType()))
         && (this.value == null && var2.getValue() == null || this.value != null && this.value.equals(var2.getValue()))
         && (this.scope == null && var2.getScope() == null || this.scope != null && this.scope.equals(var2.getScope()))
         && this.isArray == var2.isIsArray()
         && (this.arrValue == null && var2.getArrValue() == null || this.arrValue != null && Arrays.equals(this.arrValue, var2.getArrValue()));
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

      if (this.getType() != null) {
         var1 += this.getType().hashCode();
      }

      if (this.getValue() != null) {
         var1 += this.getValue().hashCode();
      }

      if (this.getScope() != null) {
         var1 += this.getScope().hashCode();
      }

      var1 += (this.isIsArray() ? Boolean.TRUE : Boolean.FALSE).hashCode();
      if (this.getArrValue() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getArrValue()); var2++) {
            Object var3 = Array.get(this.getArrValue(), var2);
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "WsDataElement")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("name");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "Name"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("type");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "Type"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("value");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "Value"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("scope");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "Scope"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("isArray");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "IsArray"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("arrValue");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "ArrValue"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "WsDataElement")
      );
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
