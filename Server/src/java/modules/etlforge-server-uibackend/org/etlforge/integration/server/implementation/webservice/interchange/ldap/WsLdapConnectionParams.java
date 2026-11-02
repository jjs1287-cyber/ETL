package org.etlforge.integration.server.implementation.webservice.interchange.ldap;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsLdapConnectionParams implements Serializable {
   private String server;
   private String port;
   private String user;
   private String password;
   private int securityLevel;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsLdapConnectionParams.class, true);

   public WsLdapConnectionParams() {
   }

   public WsLdapConnectionParams(String var1, String var2, String var3, String var4, int var5) {
      this.server = var1;
      this.port = var2;
      this.user = var3;
      this.password = var4;
      this.securityLevel = var5;
   }

   public String getServer() {
      return this.server;
   }

   public void setServer(String var1) {
      this.server = var1;
   }

   public String getPort() {
      return this.port;
   }

   public void setPort(String var1) {
      this.port = var1;
   }

   public String getUser() {
      return this.user;
   }

   public void setUser(String var1) {
      this.user = var1;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String var1) {
      this.password = var1;
   }

   public int getSecurityLevel() {
      return this.securityLevel;
   }

   public void setSecurityLevel(int var1) {
      this.securityLevel = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsLdapConnectionParams)) {
         return false;
      }

      WsLdapConnectionParams var2 = (WsLdapConnectionParams)var1;
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
      boolean var3 = (this.server == null && var2.getServer() == null || this.server != null && this.server.equals(var2.getServer()))
         && (this.port == null && var2.getPort() == null || this.port != null && this.port.equals(var2.getPort()))
         && (this.user == null && var2.getUser() == null || this.user != null && this.user.equals(var2.getUser()))
         && (this.password == null && var2.getPassword() == null || this.password != null && this.password.equals(var2.getPassword()))
         && this.securityLevel == var2.getSecurityLevel();
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
      if (this.getServer() != null) {
         var1 += this.getServer().hashCode();
      }

      if (this.getPort() != null) {
         var1 += this.getPort().hashCode();
      }

      if (this.getUser() != null) {
         var1 += this.getUser().hashCode();
      }

      if (this.getPassword() != null) {
         var1 += this.getPassword().hashCode();
      }

      var1 += this.getSecurityLevel();
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
      typeDesc.setXmlType(new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsLdapConnectionParams"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("server");
      var0.setXmlName(new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "server"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("port");
      var0.setXmlName(new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "port"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("user");
      var0.setXmlName(new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "user"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("password");
      var0.setXmlName(new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "password"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("securityLevel");
      var0.setXmlName(new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "securityLevel"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
   }
}
