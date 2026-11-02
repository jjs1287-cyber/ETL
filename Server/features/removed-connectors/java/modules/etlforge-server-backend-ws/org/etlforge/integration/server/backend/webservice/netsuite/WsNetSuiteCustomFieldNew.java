package org.etlforge.integration.server.backend.webservice.netsuite;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsNetSuiteCustomFieldNew implements Serializable {
   private String customLabel;
   private String internalId;
   private String fieldType;
   private String scriptId;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsNetSuiteCustomFieldNew.class, true);

   public WsNetSuiteCustomFieldNew() {
   }

   public WsNetSuiteCustomFieldNew(String var1, String var2, String var3, String var4) {
      this.customLabel = var1;
      this.internalId = var2;
      this.fieldType = var3;
      this.scriptId = var4;
   }

   public String getCustomLabel() {
      return this.customLabel;
   }

   public void setCustomLabel(String var1) {
      this.customLabel = var1;
   }

   public String getInternalId() {
      return this.internalId;
   }

   public void setInternalId(String var1) {
      this.internalId = var1;
   }

   public String getFieldType() {
      return this.fieldType;
   }

   public void setFieldType(String var1) {
      this.fieldType = var1;
   }

   public String getScriptId() {
      return this.scriptId;
   }

   public void setScriptId(String var1) {
      this.scriptId = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsNetSuiteCustomFieldNew)) {
         return false;
      }

      WsNetSuiteCustomFieldNew var2 = (WsNetSuiteCustomFieldNew)var1;
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
      boolean var3 = (this.customLabel == null && var2.getCustomLabel() == null || this.customLabel != null && this.customLabel.equals(var2.getCustomLabel()))
         && (this.internalId == null && var2.getInternalId() == null || this.internalId != null && this.internalId.equals(var2.getInternalId()))
         && (this.fieldType == null && var2.getFieldType() == null || this.fieldType != null && this.fieldType.equals(var2.getFieldType()))
         && (this.scriptId == null && var2.getScriptId() == null || this.scriptId != null && this.scriptId.equals(var2.getScriptId()));
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
      if (this.getCustomLabel() != null) {
         var1 += this.getCustomLabel().hashCode();
      }

      if (this.getInternalId() != null) {
         var1 += this.getInternalId().hashCode();
      }

      if (this.getFieldType() != null) {
         var1 += this.getFieldType().hashCode();
      }

      if (this.getScriptId() != null) {
         var1 += this.getScriptId().hashCode();
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
      typeDesc.setXmlType(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "WsNetSuiteCustomFieldNew"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("customLabel");
      var0.setXmlName(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "CustomLabel"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("internalId");
      var0.setXmlName(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "InternalId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("fieldType");
      var0.setXmlName(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "FieldType"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("scriptId");
      var0.setXmlName(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "ScriptId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
