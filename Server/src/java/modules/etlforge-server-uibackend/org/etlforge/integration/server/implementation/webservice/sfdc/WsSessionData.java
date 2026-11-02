package org.etlforge.integration.server.implementation.webservice.sfdc;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsSessionData implements Serializable {
   private String sessionId;
   private String userId;
   private String endpoint;
   private int timeoutInSeconds;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsSessionData.class, true);

   public WsSessionData() {
   }

   public WsSessionData(String var1, String var2, String var3, int var4) {
      this.sessionId = var1;
      this.userId = var2;
      this.endpoint = var3;
      this.timeoutInSeconds = var4;
   }

   public String getSessionId() {
      return this.sessionId;
   }

   public void setSessionId(String var1) {
      this.sessionId = var1;
   }

   public String getUserId() {
      return this.userId;
   }

   public void setUserId(String var1) {
      this.userId = var1;
   }

   public String getEndpoint() {
      return this.endpoint;
   }

   public void setEndpoint(String var1) {
      this.endpoint = var1;
   }

   public int getTimeoutInSeconds() {
      return this.timeoutInSeconds;
   }

   public void setTimeoutInSeconds(int var1) {
      this.timeoutInSeconds = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsSessionData)) {
         return false;
      }

      WsSessionData var2 = (WsSessionData)var1;
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
      boolean var3 = (this.sessionId == null && var2.getSessionId() == null || this.sessionId != null && this.sessionId.equals(var2.getSessionId()))
         && (this.userId == null && var2.getUserId() == null || this.userId != null && this.userId.equals(var2.getUserId()))
         && (this.endpoint == null && var2.getEndpoint() == null || this.endpoint != null && this.endpoint.equals(var2.getEndpoint()))
         && this.timeoutInSeconds == var2.getTimeoutInSeconds();
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

      if (this.getUserId() != null) {
         var1 += this.getUserId().hashCode();
      }

      if (this.getEndpoint() != null) {
         var1 += this.getEndpoint().hashCode();
      }

      var1 += this.getTimeoutInSeconds();
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
      typeDesc.setXmlType(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsSessionData"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("sessionId");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "sessionId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("userId");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "userId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("endpoint");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "endpoint"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("timeoutInSeconds");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "timeoutInSeconds"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
   }
}
