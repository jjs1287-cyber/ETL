package org.etlforge.integration.server.backend.webservice.salesforce;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsSalesforceEngineSessionParams implements Serializable {
   private String sessionId;
   private String serviceEndpoint;
   private String userId;
   private String version;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsSalesforceEngineSessionParams.class, true);

   public WsSalesforceEngineSessionParams() {
   }

   public WsSalesforceEngineSessionParams(String var1, String var2, String var3, String var4) {
      this.sessionId = var1;
      this.serviceEndpoint = var2;
      this.userId = var3;
      this.version = var4;
   }

   public String getSessionId() {
      return this.sessionId;
   }

   public void setSessionId(String var1) {
      this.sessionId = var1;
   }

   public String getServiceEndpoint() {
      return this.serviceEndpoint;
   }

   public void setServiceEndpoint(String var1) {
      this.serviceEndpoint = var1;
   }

   public String getUserId() {
      return this.userId;
   }

   public void setUserId(String var1) {
      this.userId = var1;
   }

   public String getVersion() {
      return this.version;
   }

   public void setVersion(String var1) {
      this.version = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsSalesforceEngineSessionParams)) {
         return false;
      }

      WsSalesforceEngineSessionParams var2 = (WsSalesforceEngineSessionParams)var1;
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
         && (
            this.serviceEndpoint == null && var2.getServiceEndpoint() == null
               || this.serviceEndpoint != null && this.serviceEndpoint.equals(var2.getServiceEndpoint())
         )
         && (this.userId == null && var2.getUserId() == null || this.userId != null && this.userId.equals(var2.getUserId()))
         && (this.version == null && var2.getVersion() == null || this.version != null && this.version.equals(var2.getVersion()));
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

      if (this.getServiceEndpoint() != null) {
         var1 += this.getServiceEndpoint().hashCode();
      }

      if (this.getUserId() != null) {
         var1 += this.getUserId().hashCode();
      }

      if (this.getVersion() != null) {
         var1 += this.getVersion().hashCode();
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
      typeDesc.setXmlType(new QName("http://salesforce.webservice.backend.server.integration.etlforge.invalid", "WsSalesforceEngineSessionParams"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("sessionId");
      var0.setXmlName(new QName("http://salesforce.webservice.backend.server.integration.etlforge.invalid", "SessionId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("serviceEndpoint");
      var0.setXmlName(new QName("http://salesforce.webservice.backend.server.integration.etlforge.invalid", "ServiceEndpoint"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("userId");
      var0.setXmlName(new QName("http://salesforce.webservice.backend.server.integration.etlforge.invalid", "UserId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("version");
      var0.setXmlName(new QName("http://salesforce.webservice.backend.server.integration.etlforge.invalid", "Version"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
