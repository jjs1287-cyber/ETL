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

public class WsQueryRecord implements Serializable {
   private String name;
   private WsQueryField[] fields;
   private WsQueryRecord[] children;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsQueryRecord.class, true);

   public WsQueryRecord() {
   }

   public WsQueryRecord(String var1, WsQueryField[] var2, WsQueryRecord[] var3) {
      this.name = var1;
      this.fields = var2;
      this.children = var3;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public WsQueryField[] getFields() {
      return this.fields;
   }

   public void setFields(WsQueryField[] var1) {
      this.fields = var1;
   }

   public WsQueryRecord[] getChildren() {
      return this.children;
   }

   public void setChildren(WsQueryRecord[] var1) {
      this.children = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsQueryRecord)) {
         return false;
      }

      WsQueryRecord var2 = (WsQueryRecord)var1;
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
         && (this.fields == null && var2.getFields() == null || this.fields != null && Arrays.equals(this.fields, var2.getFields()))
         && (this.children == null && var2.getChildren() == null || this.children != null && Arrays.equals(this.children, var2.getChildren()));
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

      if (this.getFields() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getFields()); var2++) {
            Object var3 = Array.get(this.getFields(), var2);
            if (var3 != null && !var3.getClass().isArray()) {
               var1 += var3.hashCode();
            }
         }
      }

      if (this.getChildren() != null) {
         for (int var4 = 0; var4 < Array.getLength(this.getChildren()); var4++) {
            Object var5 = Array.get(this.getChildren(), var4);
            if (var5 != null && !var5.getClass().isArray()) {
               var1 += var5.hashCode();
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
      typeDesc.setXmlType(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsQueryRecord"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("name");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "name"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("fields");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "fields"));
      var0.setXmlType(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsQueryField"));
      var0.setNillable(true);
      var0.setItemQName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("children");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "children"));
      var0.setXmlType(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsQueryRecord"));
      var0.setNillable(true);
      var0.setItemQName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
