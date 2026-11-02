package org.etlforge.integration.server.implementation.webservice.admin.client;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsProxySettings implements Serializable {
   private boolean enabled;
   private String host;
   private int port;
   private String userName;
   private String password;
   private boolean trustAllCertificates;
   private String[] exceptions;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsProxySettings.class, true);

   public WsProxySettings() {
   }

   public WsProxySettings(boolean var1, String var2, int var3, String var4, String var5, boolean var6, String[] var7) {
      this.enabled = var1;
      this.host = var2;
      this.port = var3;
      this.userName = var4;
      this.password = var5;
      this.trustAllCertificates = var6;
      this.exceptions = var7;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }

   public String getHost() {
      return this.host;
   }

   public void setHost(String var1) {
      this.host = var1;
   }

   public int getPort() {
      return this.port;
   }

   public void setPort(int var1) {
      this.port = var1;
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

   public boolean isTrustAllCertificates() {
      return this.trustAllCertificates;
   }

   public void setTrustAllCertificates(boolean var1) {
      this.trustAllCertificates = var1;
   }

   public String[] getExceptions() {
      return this.exceptions;
   }

   public void setExceptions(String[] var1) {
      this.exceptions = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsProxySettings)) {
         return false;
      }

      WsProxySettings var2 = (WsProxySettings)var1;
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
      boolean var3 = this.enabled == var2.isEnabled()
         && (this.host == null && var2.getHost() == null || this.host != null && this.host.equals(var2.getHost()))
         && this.port == var2.getPort()
         && (this.userName == null && var2.getUserName() == null || this.userName != null && this.userName.equals(var2.getUserName()))
         && (this.password == null && var2.getPassword() == null || this.password != null && this.password.equals(var2.getPassword()))
         && this.trustAllCertificates == var2.isTrustAllCertificates()
         && (this.exceptions == null && var2.getExceptions() == null || this.exceptions != null && Arrays.equals(this.exceptions, var2.getExceptions()));
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
      var1 += (this.isEnabled() ? Boolean.TRUE : Boolean.FALSE).hashCode();
      if (this.getHost() != null) {
         var1 += this.getHost().hashCode();
      }

      var1 += this.getPort();
      if (this.getUserName() != null) {
         var1 += this.getUserName().hashCode();
      }

      if (this.getPassword() != null) {
         var1 += this.getPassword().hashCode();
      }

      var1 += (this.isTrustAllCertificates() ? Boolean.TRUE : Boolean.FALSE).hashCode();
      if (this.getExceptions() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getExceptions()); var2++) {
            Object var3 = Array.get(this.getExceptions(), var2);
            if (var3 != null && !var3.getClass().isArray()) {
               var1 += var3.hashCode();
            }
         }
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
      typeDesc.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.admin.Admin", "WsProxySettings"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("enabled");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.admin.Admin", "Enabled"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("host");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.admin.Admin", "Host"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("port");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.admin.Admin", "Port"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("userName");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.admin.Admin", "UserName"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("password");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.admin.Admin", "Password"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("trustAllCertificates");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.admin.Admin", "TrustAllCertificates"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("exceptions");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.admin.Admin", "Exceptions"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.admin.Admin", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
