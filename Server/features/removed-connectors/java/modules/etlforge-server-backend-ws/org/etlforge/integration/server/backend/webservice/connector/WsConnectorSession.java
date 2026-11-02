package org.etlforge.integration.server.backend.webservice.connector;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsConnectorSession implements Serializable {
   private String sessionId;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsConnectorSession.class, true);

   public WsConnectorSession() {
   }

   public WsConnectorSession(String var1) {
      this.sessionId = var1;
   }

   public String getSessionId() {
      return this.sessionId;
   }

   public void setSessionId(String var1) {
      this.sessionId = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsConnectorSession)) {
         return false;
      }

      WsConnectorSession var2 = (WsConnectorSession)var1;
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
      boolean var3 = this.sessionId == null && var2.getSessionId() == null || this.sessionId != null && this.sessionId.equals(var2.getSessionId());
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
      if (this.getSessionId() != null) {
         var1 += this.getSessionId().hashCode();
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
      typeDesc.setXmlType(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsConnectorSession"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("sessionId");
      var0.setXmlName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "SessionId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
