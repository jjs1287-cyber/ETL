package org.etlforge.integration.server.implementation.webservice.sfdc.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsOrgIdentifier implements Serializable {
   private String authType; public String getAuthType() { return authType; } public void setAuthType(String value) { this.authType = value; }
private String instanceUrl; public String getInstanceUrl() { return instanceUrl; } public void setInstanceUrl(String value) { this.instanceUrl = value; }
private String clientId; public String getClientId() { return clientId; } public void setClientId(String value) { this.clientId = value; }
private String clientSecret; public String getClientSecret() { return clientSecret; } public void setClientSecret(String value) { this.clientSecret = value; }
private String url;
   private String salesforceVersion;
   private String username;
   private String password;
   private String securityToken;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsOrgIdentifier.class, true);

   public WsOrgIdentifier() {
   }

   public WsOrgIdentifier(String var1, String var2, String var3, String var4, String var5) {
      this.url = var1;
      this.salesforceVersion = var2;
      this.username = var3;
      this.password = var4;
      this.securityToken = var5;
   }

   public String getUrl() {
      return this.url;
   }

   public void setUrl(String var1) {
      this.url = var1;
   }

   public String getSalesforceVersion() {
      return this.salesforceVersion;
   }

   public void setSalesforceVersion(String var1) {
      this.salesforceVersion = var1;
   }

   public String getUsername() {
      return this.username;
   }

   public void setUsername(String var1) {
      this.username = var1;
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

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsOrgIdentifier)) {
         return false;
      }

      WsOrgIdentifier var2 = (WsOrgIdentifier)var1;
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
      boolean var3 = (this.url == null && var2.getUrl() == null || this.url != null && this.url.equals(var2.getUrl()))
         && (
            this.salesforceVersion == null && var2.getSalesforceVersion() == null
               || this.salesforceVersion != null && this.salesforceVersion.equals(var2.getSalesforceVersion())
         )
         && (this.username == null && var2.getUsername() == null || this.username != null && this.username.equals(var2.getUsername()))
         && (this.password == null && var2.getPassword() == null || this.password != null && this.password.equals(var2.getPassword()))
         && (this.securityToken == null && var2.getSecurityToken() == null || this.securityToken != null && this.securityToken.equals(var2.getSecurityToken()));
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
      if (this.getUrl() != null) {
         var1 += this.getUrl().hashCode();
      }

      if (this.getSalesforceVersion() != null) {
         var1 += this.getSalesforceVersion().hashCode();
      }

      if (this.getUsername() != null) {
         var1 += this.getUsername().hashCode();
      }

      if (this.getPassword() != null) {
         var1 += this.getPassword().hashCode();
      }

      if (this.getSecurityToken() != null) {
         var1 += this.getSecurityToken().hashCode();
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
      typeDesc.setXmlType(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsOrgIdentifier"));
      { ElementDesc field = new ElementDesc(); field.setFieldName("authType"); field.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "authType")); field.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string")); field.setNillable(true); field.setMinOccurs(0); typeDesc.addFieldDesc(field); }
      { ElementDesc field = new ElementDesc(); field.setFieldName("instanceUrl"); field.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "instanceUrl")); field.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string")); field.setNillable(true); field.setMinOccurs(0); typeDesc.addFieldDesc(field); }
      { ElementDesc field = new ElementDesc(); field.setFieldName("clientId"); field.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "clientId")); field.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string")); field.setNillable(true); field.setMinOccurs(0); typeDesc.addFieldDesc(field); }
      { ElementDesc field = new ElementDesc(); field.setFieldName("clientSecret"); field.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "clientSecret")); field.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string")); field.setNillable(true); field.setMinOccurs(0); typeDesc.addFieldDesc(field); }
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("url");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "url"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("salesforceVersion");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "salesforceVersion"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("username");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "username"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("password");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "password"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("securityToken");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "securityToken"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
