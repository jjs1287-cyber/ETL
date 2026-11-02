package org.etlforge.integration.server.backend.webservice.function;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsGetSalesforceServerTimeStampInput implements Serializable {
   private String serviceEndpoint;
   private String sessionId;
   private String timeZone;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsGetSalesforceServerTimeStampInput.class, true);

   public WsGetSalesforceServerTimeStampInput() {
   }

   public WsGetSalesforceServerTimeStampInput(String var1, String var2, String var3) {
      this.serviceEndpoint = var1;
      this.sessionId = var2;
      this.timeZone = var3;
   }

   public String getServiceEndpoint() {
      return this.serviceEndpoint;
   }

   public void setServiceEndpoint(String var1) {
      this.serviceEndpoint = var1;
   }

   public String getSessionId() {
      return this.sessionId;
   }

   public void setSessionId(String var1) {
      this.sessionId = var1;
   }

   public String getTimeZone() {
      return this.timeZone;
   }

   public void setTimeZone(String var1) {
      this.timeZone = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsGetSalesforceServerTimeStampInput)) {
         return false;
      }

      WsGetSalesforceServerTimeStampInput var2 = (WsGetSalesforceServerTimeStampInput)var1;
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
      boolean var3 = (
            this.serviceEndpoint == null && var2.getServiceEndpoint() == null
               || this.serviceEndpoint != null && this.serviceEndpoint.equals(var2.getServiceEndpoint())
         )
         && (this.sessionId == null && var2.getSessionId() == null || this.sessionId != null && this.sessionId.equals(var2.getSessionId()))
         && (this.timeZone == null && var2.getTimeZone() == null || this.timeZone != null && this.timeZone.equals(var2.getTimeZone()));
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
      if (this.getServiceEndpoint() != null) {
         var1 += this.getServiceEndpoint().hashCode();
      }

      if (this.getSessionId() != null) {
         var1 += this.getSessionId().hashCode();
      }

      if (this.getTimeZone() != null) {
         var1 += this.getTimeZone().hashCode();
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
      typeDesc.setXmlType(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsGetSalesforceServerTimeStampInput"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("serviceEndpoint");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "ServiceEndpoint"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("sessionId");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "SessionId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("timeZone");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "TimeZone"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
