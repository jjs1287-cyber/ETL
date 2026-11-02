package org.etlforge.integration.server.backend.webservice.connector.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsConnectorExecutionResponse implements Serializable {
   private WsStatus status;
   private String connectorResponse;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsConnectorExecutionResponse.class, true);

   public WsConnectorExecutionResponse() {
   }

   public WsConnectorExecutionResponse(WsStatus var1, String var2) {
      this.status = var1;
      this.connectorResponse = var2;
   }

   public WsStatus getStatus() {
      return this.status;
   }

   public void setStatus(WsStatus var1) {
      this.status = var1;
   }

   public String getConnectorResponse() {
      return this.connectorResponse;
   }

   public void setConnectorResponse(String var1) {
      this.connectorResponse = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsConnectorExecutionResponse)) {
         return false;
      }

      WsConnectorExecutionResponse var2 = (WsConnectorExecutionResponse)var1;
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
            this.connectorResponse == null && var2.getConnectorResponse() == null
               || this.connectorResponse != null && this.connectorResponse.equals(var2.getConnectorResponse())
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

      if (this.getConnectorResponse() != null) {
         var1 += this.getConnectorResponse().hashCode();
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
      typeDesc.setXmlType(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsConnectorExecutionResponse"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("status");
      var0.setXmlName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "Status"));
      var0.setXmlType(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsStatus"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("connectorResponse");
      var0.setXmlName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "ConnectorResponse"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
