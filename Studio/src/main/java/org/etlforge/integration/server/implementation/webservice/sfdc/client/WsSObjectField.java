package org.etlforge.integration.server.implementation.webservice.sfdc.client;

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

public class WsSObjectField implements Serializable {
   private String name;
   private String label;
   private int type;
   private int soapType;
   private int booleans;
   private String refTo;
   private String relName;
   private WsPicklistEntry[] picklist;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsSObjectField.class, true);

   public WsSObjectField() {
   }

   public WsSObjectField(String var1, String var2, int var3, int var4, int var5, String var6, String var7, WsPicklistEntry[] var8) {
      this.name = var1;
      this.label = var2;
      this.type = var3;
      this.soapType = var4;
      this.booleans = var5;
      this.refTo = var6;
      this.relName = var7;
      this.picklist = var8;
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

   public int getType() {
      return this.type;
   }

   public void setType(int var1) {
      this.type = var1;
   }

   public int getSoapType() {
      return this.soapType;
   }

   public void setSoapType(int var1) {
      this.soapType = var1;
   }

   public int getBooleans() {
      return this.booleans;
   }

   public void setBooleans(int var1) {
      this.booleans = var1;
   }

   public String getRefTo() {
      return this.refTo;
   }

   public void setRefTo(String var1) {
      this.refTo = var1;
   }

   public String getRelName() {
      return this.relName;
   }

   public void setRelName(String var1) {
      this.relName = var1;
   }

   public WsPicklistEntry[] getPicklist() {
      return this.picklist;
   }

   public void setPicklist(WsPicklistEntry[] var1) {
      this.picklist = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsSObjectField)) {
         return false;
      }

      WsSObjectField var2 = (WsSObjectField)var1;
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
         && this.type == var2.getType()
         && this.soapType == var2.getSoapType()
         && this.booleans == var2.getBooleans()
         && (this.refTo == null && var2.getRefTo() == null || this.refTo != null && this.refTo.equals(var2.getRefTo()))
         && (this.relName == null && var2.getRelName() == null || this.relName != null && this.relName.equals(var2.getRelName()))
         && (this.picklist == null && var2.getPicklist() == null || this.picklist != null && Arrays.equals(this.picklist, var2.getPicklist()));
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

      var1 += this.getType();
      var1 += this.getSoapType();
      var1 += this.getBooleans();
      if (this.getRefTo() != null) {
         var1 += this.getRefTo().hashCode();
      }

      if (this.getRelName() != null) {
         var1 += this.getRelName().hashCode();
      }

      if (this.getPicklist() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getPicklist()); var2++) {
            Object var3 = Array.get(this.getPicklist(), var2);
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
      typeDesc.setXmlType(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsSObjectField"));
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
      var0.setFieldName("type");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "type"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("soapType");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "soapType"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("booleans");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "booleans"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("refTo");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "refTo"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("relName");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "relName"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("picklist");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "picklist"));
      var0.setXmlType(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsPicklistEntry"));
      var0.setNillable(true);
      var0.setItemQName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
