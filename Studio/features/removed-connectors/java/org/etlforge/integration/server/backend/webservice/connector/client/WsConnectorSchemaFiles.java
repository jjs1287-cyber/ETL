package org.etlforge.integration.server.backend.webservice.connector.client;

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

public class WsConnectorSchemaFiles implements Serializable {
   private WsStatus status;
   private String requestMainSchema;
   private String requestRootElementExpandedName;
   private String responseMainSchema;
   private String responseRootElementExpandedName;
   private WsConnectorSchemaFile[] schemaFiles;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsConnectorSchemaFiles.class, true);

   public WsConnectorSchemaFiles() {
   }

   public WsConnectorSchemaFiles(WsStatus var1, String var2, String var3, String var4, String var5, WsConnectorSchemaFile[] var6) {
      this.status = var1;
      this.requestMainSchema = var2;
      this.requestRootElementExpandedName = var3;
      this.responseMainSchema = var4;
      this.responseRootElementExpandedName = var5;
      this.schemaFiles = var6;
   }

   public WsStatus getStatus() {
      return this.status;
   }

   public void setStatus(WsStatus var1) {
      this.status = var1;
   }

   public String getRequestMainSchema() {
      return this.requestMainSchema;
   }

   public void setRequestMainSchema(String var1) {
      this.requestMainSchema = var1;
   }

   public String getRequestRootElementExpandedName() {
      return this.requestRootElementExpandedName;
   }

   public void setRequestRootElementExpandedName(String var1) {
      this.requestRootElementExpandedName = var1;
   }

   public String getResponseMainSchema() {
      return this.responseMainSchema;
   }

   public void setResponseMainSchema(String var1) {
      this.responseMainSchema = var1;
   }

   public String getResponseRootElementExpandedName() {
      return this.responseRootElementExpandedName;
   }

   public void setResponseRootElementExpandedName(String var1) {
      this.responseRootElementExpandedName = var1;
   }

   public WsConnectorSchemaFile[] getSchemaFiles() {
      return this.schemaFiles;
   }

   public void setSchemaFiles(WsConnectorSchemaFile[] var1) {
      this.schemaFiles = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsConnectorSchemaFiles)) {
         return false;
      }

      WsConnectorSchemaFiles var2 = (WsConnectorSchemaFiles)var1;
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
            this.requestMainSchema == null && var2.getRequestMainSchema() == null
               || this.requestMainSchema != null && this.requestMainSchema.equals(var2.getRequestMainSchema())
         )
         && (
            this.requestRootElementExpandedName == null && var2.getRequestRootElementExpandedName() == null
               || this.requestRootElementExpandedName != null && this.requestRootElementExpandedName.equals(var2.getRequestRootElementExpandedName())
         )
         && (
            this.responseMainSchema == null && var2.getResponseMainSchema() == null
               || this.responseMainSchema != null && this.responseMainSchema.equals(var2.getResponseMainSchema())
         )
         && (
            this.responseRootElementExpandedName == null && var2.getResponseRootElementExpandedName() == null
               || this.responseRootElementExpandedName != null && this.responseRootElementExpandedName.equals(var2.getResponseRootElementExpandedName())
         )
         && (this.schemaFiles == null && var2.getSchemaFiles() == null || this.schemaFiles != null && Arrays.equals(this.schemaFiles, var2.getSchemaFiles()));
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

      if (this.getRequestMainSchema() != null) {
         var1 += this.getRequestMainSchema().hashCode();
      }

      if (this.getRequestRootElementExpandedName() != null) {
         var1 += this.getRequestRootElementExpandedName().hashCode();
      }

      if (this.getResponseMainSchema() != null) {
         var1 += this.getResponseMainSchema().hashCode();
      }

      if (this.getResponseRootElementExpandedName() != null) {
         var1 += this.getResponseRootElementExpandedName().hashCode();
      }

      if (this.getSchemaFiles() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getSchemaFiles()); var2++) {
            Object var3 = Array.get(this.getSchemaFiles(), var2);
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
      typeDesc.setXmlType(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsConnectorSchemaFiles"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("status");
      var0.setXmlName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "Status"));
      var0.setXmlType(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsStatus"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("requestMainSchema");
      var0.setXmlName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "RequestMainSchema"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("requestRootElementExpandedName");
      var0.setXmlName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "RequestRootElementExpandedName"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("responseMainSchema");
      var0.setXmlName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "ResponseMainSchema"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("responseRootElementExpandedName");
      var0.setXmlName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "ResponseRootElementExpandedName"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("schemaFiles");
      var0.setXmlName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "SchemaFiles"));
      var0.setXmlType(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsConnectorSchemaFile"));
      var0.setNillable(true);
      var0.setItemQName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
