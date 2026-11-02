package org.etlforge.integration.server.implementation.webservice.interchange.authenticate.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;
import org.etlforge.integration.server.implementation.webservice.WsKeyValuePairs;

public class AuthenticateResult implements Serializable {
   private String serverGuid;
   private String userGuid;
   private String versionString;
   private WsKeyValuePairs keyValues;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(AuthenticateResult.class, true);

   public AuthenticateResult() {
   }

   public AuthenticateResult(String var1, String var2, String var3, WsKeyValuePairs var4) {
      this.serverGuid = var1;
      this.userGuid = var2;
      this.versionString = var3;
      this.keyValues = var4;
   }

   public String getServerGuid() {
      return this.serverGuid;
   }

   public void setServerGuid(String var1) {
      this.serverGuid = var1;
   }

   public String getUserGuid() {
      return this.userGuid;
   }

   public void setUserGuid(String var1) {
      this.userGuid = var1;
   }

   public String getVersionString() {
      return this.versionString;
   }

   public void setVersionString(String var1) {
      this.versionString = var1;
   }

   public WsKeyValuePairs getKeyValues() {
      return this.keyValues;
   }

   public void setKeyValues(WsKeyValuePairs var1) {
      this.keyValues = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof AuthenticateResult)) {
         return false;
      }

      AuthenticateResult var2 = (AuthenticateResult)var1;
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
      boolean var3 = (this.serverGuid == null && var2.getServerGuid() == null || this.serverGuid != null && this.serverGuid.equals(var2.getServerGuid()))
         && (this.userGuid == null && var2.getUserGuid() == null || this.userGuid != null && this.userGuid.equals(var2.getUserGuid()))
         && (this.versionString == null && var2.getVersionString() == null || this.versionString != null && this.versionString.equals(var2.getVersionString()))
         && (this.keyValues == null && var2.getKeyValues() == null || this.keyValues != null && this.keyValues.equals(var2.getKeyValues()));
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
      if (this.getServerGuid() != null) {
         var1 += this.getServerGuid().hashCode();
      }

      if (this.getUserGuid() != null) {
         var1 += this.getUserGuid().hashCode();
      }

      if (this.getVersionString() != null) {
         var1 += this.getVersionString().hashCode();
      }

      if (this.getKeyValues() != null) {
         var1 += this.getKeyValues().hashCode();
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
      typeDesc.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.authenticate.Authenticate", "AuthenticateResult")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("serverGuid");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.authenticate.Authenticate", "serverGuid"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("userGuid");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.authenticate.Authenticate", "userGuid"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("versionString");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.authenticate.Authenticate", "versionString"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("keyValues");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.authenticate.Authenticate", "keyValues"));
      var0.setXmlType(new QName("http://webservice.implementation.server.integration.etlforge.invalid", "WsKeyValuePairs"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
