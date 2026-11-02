package org.etlforge.integration.server.backend.webservice.connector;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsConnectorSchemas implements Serializable {
   private WsStatus status;
   private String requestXsdSchema;
   private String requestRootElementExpandedName;
   private String responseXsdSchema;
   private String responseRootElementExpandedName;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsConnectorSchemas.class, true);

   public WsConnectorSchemas() {
   }

   public WsConnectorSchemas(WsStatus var1, String var2, String var3, String var4, String var5) {
      this.status = var1;
      this.requestXsdSchema = var2;
      this.requestRootElementExpandedName = var3;
      this.responseXsdSchema = var4;
      this.responseRootElementExpandedName = var5;
   }

   public WsStatus getStatus() {
      return this.status;
   }

   public void setStatus(WsStatus var1) {
      this.status = var1;
   }

   public String getRequestXsdSchema() {
      return this.requestXsdSchema;
   }

   public void setRequestXsdSchema(String var1) {
      this.requestXsdSchema = var1;
   }

   public String getRequestRootElementExpandedName() {
      return this.requestRootElementExpandedName;
   }

   public void setRequestRootElementExpandedName(String var1) {
      this.requestRootElementExpandedName = var1;
   }

   public String getResponseXsdSchema() {
      return this.responseXsdSchema;
   }

   public void setResponseXsdSchema(String var1) {
      this.responseXsdSchema = var1;
   }

   public String getResponseRootElementExpandedName() {
      return this.responseRootElementExpandedName;
   }

   public void setResponseRootElementExpandedName(String var1) {
      this.responseRootElementExpandedName = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsConnectorSchemas)) {
         return false;
      }

      WsConnectorSchemas var2 = (WsConnectorSchemas)var1;
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
            this.requestXsdSchema == null && var2.getRequestXsdSchema() == null
               || this.requestXsdSchema != null && this.requestXsdSchema.equals(var2.getRequestXsdSchema())
         )
         && (
            this.requestRootElementExpandedName == null && var2.getRequestRootElementExpandedName() == null
               || this.requestRootElementExpandedName != null && this.requestRootElementExpandedName.equals(var2.getRequestRootElementExpandedName())
         )
         && (
            this.responseXsdSchema == null && var2.getResponseXsdSchema() == null
               || this.responseXsdSchema != null && this.responseXsdSchema.equals(var2.getResponseXsdSchema())
         )
         && (
            this.responseRootElementExpandedName == null && var2.getResponseRootElementExpandedName() == null
               || this.responseRootElementExpandedName != null && this.responseRootElementExpandedName.equals(var2.getResponseRootElementExpandedName())
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

      if (this.getRequestXsdSchema() != null) {
         var1 += this.getRequestXsdSchema().hashCode();
      }

      if (this.getRequestRootElementExpandedName() != null) {
         var1 += this.getRequestRootElementExpandedName().hashCode();
      }

      if (this.getResponseXsdSchema() != null) {
         var1 += this.getResponseXsdSchema().hashCode();
      }

      if (this.getResponseRootElementExpandedName() != null) {
         var1 += this.getResponseRootElementExpandedName().hashCode();
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
      typeDesc.setXmlType(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsConnectorSchemas"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("status");
      var0.setXmlName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "Status"));
      var0.setXmlType(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsStatus"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("requestXsdSchema");
      var0.setXmlName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "RequestXsdSchema"));
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
      var0.setFieldName("responseXsdSchema");
      var0.setXmlName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "ResponseXsdSchema"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("responseRootElementExpandedName");
      var0.setXmlName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "ResponseRootElementExpandedName"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
