package org.etlforge.integration.server.backend.webservice.salesforce;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsSalesforceEngineLoginParams implements Serializable {
   private String authType; public String getAuthType() { return authType; } public void setAuthType(String value) { this.authType = value; }
private String instanceUrl; public String getInstanceUrl() { return instanceUrl; } public void setInstanceUrl(String value) { this.instanceUrl = value; }
private String clientId; public String getClientId() { return clientId; } public void setClientId(String value) { this.clientId = value; }
private String clientSecret; public String getClientSecret() { return clientSecret; } public void setClientSecret(String value) { this.clientSecret = value; }
private long salesforceConnectorId;
   private String version;
   private String loginUrl;
   private String userName;
   private String password;
   private String securityToken;
   private boolean sandbox;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsSalesforceEngineLoginParams.class, true);

   public WsSalesforceEngineLoginParams() {
   }

   public WsSalesforceEngineLoginParams(long var1, String var3, String var4, String var5, String var6, String var7, boolean var8) {
      this.salesforceConnectorId = var1;
      this.version = var3;
      this.loginUrl = var4;
      this.userName = var5;
      this.password = var6;
      this.securityToken = var7;
      this.sandbox = var8;
   }

   public long getSalesforceConnectorId() {
      return this.salesforceConnectorId;
   }

   public void setSalesforceConnectorId(long var1) {
      this.salesforceConnectorId = var1;
   }

   public String getVersion() {
      return this.version;
   }

   public void setVersion(String var1) {
      this.version = var1;
   }

   public String getLoginUrl() {
      return this.loginUrl;
   }

   public void setLoginUrl(String var1) {
      this.loginUrl = var1;
   }

   public String getUserName() {
      return this.userName;
   }

   public void setUserName(String var1) {
      this.userName = var1;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String var1) {
      this.password = var1;
   }

   public String getSecurityToken() {
      return this.securityToken;
   }

   public void setSecurityToken(String var1) {
      this.securityToken = var1;
   }

   public boolean isSandbox() {
      return this.sandbox;
   }

   public void setSandbox(boolean var1) {
      this.sandbox = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsSalesforceEngineLoginParams)) {
         return false;
      }

      WsSalesforceEngineLoginParams var2 = (WsSalesforceEngineLoginParams)var1;
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
      boolean var3 = this.salesforceConnectorId == var2.getSalesforceConnectorId()
         && (this.version == null && var2.getVersion() == null || this.version != null && this.version.equals(var2.getVersion()))
         && (this.loginUrl == null && var2.getLoginUrl() == null || this.loginUrl != null && this.loginUrl.equals(var2.getLoginUrl()))
         && (this.userName == null && var2.getUserName() == null || this.userName != null && this.userName.equals(var2.getUserName()))
         && (this.password == null && var2.getPassword() == null || this.password != null && this.password.equals(var2.getPassword()))
         && (this.securityToken == null && var2.getSecurityToken() == null || this.securityToken != null && this.securityToken.equals(var2.getSecurityToken()))
         && this.sandbox == var2.isSandbox();
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
      var1 += new Long(this.getSalesforceConnectorId()).hashCode();
      if (this.getVersion() != null) {
         var1 += this.getVersion().hashCode();
      }

      if (this.getLoginUrl() != null) {
         var1 += this.getLoginUrl().hashCode();
      }

      if (this.getUserName() != null) {
         var1 += this.getUserName().hashCode();
      }

      if (this.getPassword() != null) {
         var1 += this.getPassword().hashCode();
      }

      if (this.getSecurityToken() != null) {
         var1 += this.getSecurityToken().hashCode();
      }

      var1 += (this.isSandbox() ? Boolean.TRUE : Boolean.FALSE).hashCode();
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
      typeDesc.setXmlType(new QName("http://salesforce.webservice.backend.server.integration.etlforge.invalid", "WsSalesforceEngineLoginParams"));
 { ElementDesc field = new ElementDesc(); field.setFieldName("authType"); field.setXmlName(new QName("http://salesforce.webservice.backend.server.integration.etlforge.invalid", "AuthType")); field.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string")); field.setNillable(true); field.setMinOccurs(0); typeDesc.addFieldDesc(field); }
 { ElementDesc field = new ElementDesc(); field.setFieldName("instanceUrl"); field.setXmlName(new QName("http://salesforce.webservice.backend.server.integration.etlforge.invalid", "InstanceUrl")); field.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string")); field.setNillable(true); field.setMinOccurs(0); typeDesc.addFieldDesc(field); }
 { ElementDesc field = new ElementDesc(); field.setFieldName("clientId"); field.setXmlName(new QName("http://salesforce.webservice.backend.server.integration.etlforge.invalid", "ClientId")); field.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string")); field.setNillable(true); field.setMinOccurs(0); typeDesc.addFieldDesc(field); }
 { ElementDesc field = new ElementDesc(); field.setFieldName("clientSecret"); field.setXmlName(new QName("http://salesforce.webservice.backend.server.integration.etlforge.invalid", "ClientSecret")); field.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string")); field.setNillable(true); field.setMinOccurs(0); typeDesc.addFieldDesc(field); }
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("salesforceConnectorId");
      var0.setXmlName(new QName("http://salesforce.webservice.backend.server.integration.etlforge.invalid", "SalesforceConnectorId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("version");
      var0.setXmlName(new QName("http://salesforce.webservice.backend.server.integration.etlforge.invalid", "Version"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("loginUrl");
      var0.setXmlName(new QName("http://salesforce.webservice.backend.server.integration.etlforge.invalid", "LoginUrl"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("userName");
      var0.setXmlName(new QName("http://salesforce.webservice.backend.server.integration.etlforge.invalid", "UserName"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("password");
      var0.setXmlName(new QName("http://salesforce.webservice.backend.server.integration.etlforge.invalid", "Password"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("securityToken");
      var0.setXmlName(new QName("http://salesforce.webservice.backend.server.integration.etlforge.invalid", "SecurityToken"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("sandbox");
      var0.setXmlName(new QName("http://salesforce.webservice.backend.server.integration.etlforge.invalid", "Sandbox"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
   }
}
