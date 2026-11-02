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

public class WsSObjectSchema implements Serializable {
   private String name;
   private String label;
   private String plural;
   private int booleans;
   private WsSObjectField[] fields;
   private WsSObjectParent[] parents;
   private WsSObjectChild[] children;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsSObjectSchema.class, true);

   public WsSObjectSchema() {
   }

   public WsSObjectSchema(String var1, String var2, String var3, int var4, WsSObjectField[] var5, WsSObjectParent[] var6, WsSObjectChild[] var7) {
      this.name = var1;
      this.label = var2;
      this.plural = var3;
      this.booleans = var4;
      this.fields = var5;
      this.parents = var6;
      this.children = var7;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getLabel() {
      return this.label;
   }

   public void setLabel(String var1) {
      this.label = var1;
   }

   public String getPlural() {
      return this.plural;
   }

   public void setPlural(String var1) {
      this.plural = var1;
   }

   public int getBooleans() {
      return this.booleans;
   }

   public void setBooleans(int var1) {
      this.booleans = var1;
   }

   public WsSObjectField[] getFields() {
      return this.fields;
   }

   public void setFields(WsSObjectField[] var1) {
      this.fields = var1;
   }

   public WsSObjectParent[] getParents() {
      return this.parents;
   }

   public void setParents(WsSObjectParent[] var1) {
      this.parents = var1;
   }

   public WsSObjectChild[] getChildren() {
      return this.children;
   }

   public void setChildren(WsSObjectChild[] var1) {
      this.children = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsSObjectSchema)) {
         return false;
      }

      WsSObjectSchema var2 = (WsSObjectSchema)var1;
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
         && (this.label == null && var2.getLabel() == null || this.label != null && this.label.equals(var2.getLabel()))
         && (this.plural == null && var2.getPlural() == null || this.plural != null && this.plural.equals(var2.getPlural()))
         && this.booleans == var2.getBooleans()
         && (this.fields == null && var2.getFields() == null || this.fields != null && Arrays.equals(this.fields, var2.getFields()))
         && (this.parents == null && var2.getParents() == null || this.parents != null && Arrays.equals(this.parents, var2.getParents()))
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

      if (this.getLabel() != null) {
         var1 += this.getLabel().hashCode();
      }

      if (this.getPlural() != null) {
         var1 += this.getPlural().hashCode();
      }

      var1 += this.getBooleans();
      if (this.getFields() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getFields()); var2++) {
            Object var3 = Array.get(this.getFields(), var2);
            if (var3 != null && !var3.getClass().isArray()) {
               var1 += var3.hashCode();
            }
         }
      }

      if (this.getParents() != null) {
         for (int var5 = 0; var5 < Array.getLength(this.getParents()); var5++) {
            Object var7 = Array.get(this.getParents(), var5);
            if (var7 != null && !var7.getClass().isArray()) {
               var1 += var7.hashCode();
            }
         }
      }

      if (this.getChildren() != null) {
         for (int var6 = 0; var6 < Array.getLength(this.getChildren()); var6++) {
            Object var8 = Array.get(this.getChildren(), var6);
            if (var8 != null && !var8.getClass().isArray()) {
               var1 += var8.hashCode();
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
      typeDesc.setXmlType(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsSObjectSchema"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("name");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "name"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("label");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "label"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("plural");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "plural"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("booleans");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "booleans"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("fields");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "fields"));
      var0.setXmlType(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsSObjectField"));
      var0.setNillable(true);
      var0.setItemQName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("parents");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "parents"));
      var0.setXmlType(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsSObjectParent"));
      var0.setNillable(true);
      var0.setItemQName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("children");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "children"));
      var0.setXmlType(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsSObjectChild"));
      var0.setNillable(true);
      var0.setItemQName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
