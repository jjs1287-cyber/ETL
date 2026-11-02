package org.etlforge.integration.server.backend.webservice.netsuite;

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
import org.etlforge.integration.server.backend.webservice.connector.WsStatus;

public class WsNetSuiteObjectCustomFieldsNew implements Serializable {
   private WsStatus status;
   private String customObjectId;
   private String[] customFieldCategory;
   private WsNetSuiteCustomFieldsNew[] customFields;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsNetSuiteObjectCustomFieldsNew.class, true);

   public WsNetSuiteObjectCustomFieldsNew() {
   }

   public WsNetSuiteObjectCustomFieldsNew(WsStatus var1, String var2, String[] var3, WsNetSuiteCustomFieldsNew[] var4) {
      this.status = var1;
      this.customObjectId = var2;
      this.customFieldCategory = var3;
      this.customFields = var4;
   }

   public WsStatus getStatus() {
      return this.status;
   }

   public void setStatus(WsStatus var1) {
      this.status = var1;
   }

   public String getCustomObjectId() {
      return this.customObjectId;
   }

   public void setCustomObjectId(String var1) {
      this.customObjectId = var1;
   }

   public String[] getCustomFieldCategory() {
      return this.customFieldCategory;
   }

   public void setCustomFieldCategory(String[] var1) {
      this.customFieldCategory = var1;
   }

   public WsNetSuiteCustomFieldsNew[] getCustomFields() {
      return this.customFields;
   }

   public void setCustomFields(WsNetSuiteCustomFieldsNew[] var1) {
      this.customFields = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsNetSuiteObjectCustomFieldsNew)) {
         return false;
      }

      WsNetSuiteObjectCustomFieldsNew var2 = (WsNetSuiteObjectCustomFieldsNew)var1;
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
      boolean var3 = (this.status == null && var2.getStatus() == null || this.status != null && this.status.equals(var2.getStatus()))
         && (
            this.customObjectId == null && var2.getCustomObjectId() == null
               || this.customObjectId != null && this.customObjectId.equals(var2.getCustomObjectId())
         )
         && (
            this.customFieldCategory == null && var2.getCustomFieldCategory() == null
               || this.customFieldCategory != null && Arrays.equals(this.customFieldCategory, var2.getCustomFieldCategory())
         )
         && (
            this.customFields == null && var2.getCustomFields() == null
               || this.customFields != null && Arrays.equals(this.customFields, var2.getCustomFields())
         );
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
      if (this.getStatus() != null) {
         var1 += this.getStatus().hashCode();
      }

      if (this.getCustomObjectId() != null) {
         var1 += this.getCustomObjectId().hashCode();
      }

      if (this.getCustomFieldCategory() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getCustomFieldCategory()); var2++) {
            Object var3 = Array.get(this.getCustomFieldCategory(), var2);
            if (var3 != null && !var3.getClass().isArray()) {
               var1 += var3.hashCode();
            }
         }
      }

      if (this.getCustomFields() != null) {
         for (int var4 = 0; var4 < Array.getLength(this.getCustomFields()); var4++) {
            Object var5 = Array.get(this.getCustomFields(), var4);
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
      typeDesc.setXmlType(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "WsNetSuiteObjectCustomFieldsNew"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("status");
      var0.setXmlName(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "Status"));
      var0.setXmlType(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsStatus"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("customObjectId");
      var0.setXmlName(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "CustomObjectId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("customFieldCategory");
      var0.setXmlName(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "CustomFieldCategory"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("customFields");
      var0.setXmlName(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "CustomFields"));
      var0.setXmlType(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "WsNetSuiteCustomFieldsNew"));
      var0.setNillable(true);
      var0.setItemQName(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
