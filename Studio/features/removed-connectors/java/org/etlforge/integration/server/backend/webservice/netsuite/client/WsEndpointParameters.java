package org.etlforge.integration.server.backend.webservice.netsuite.client;

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

public class WsEndpointParameters implements Serializable {
   private String etlforgeUser;
   private String etlforgeMd5Pwd;
   private String endpointGUID;
   private String endpointTypeName;
   private String host;
   private String user;
   private String password;
   private boolean isPasswordEncrypted;
   private WsNameValuePair[] additionalParameters;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsEndpointParameters.class, true);

   public WsEndpointParameters() {
   }

   public WsEndpointParameters(String var1, String var2, String var3, String var4, String var5, String var6, String var7, boolean var8, WsNameValuePair[] var9) {
      this.etlforgeUser = var1;
      this.etlforgeMd5Pwd = var2;
      this.endpointGUID = var3;
      this.endpointTypeName = var4;
      this.host = var5;
      this.user = var6;
      this.password = var7;
      this.isPasswordEncrypted = var8;
      this.additionalParameters = var9;
   }

   public String getEtlforgeUser() {
      return this.etlforgeUser;
   }

   public void setEtlforgeUser(String var1) {
      this.etlforgeUser = var1;
   }

   public String getEtlforgeMd5Pwd() {
      return this.etlforgeMd5Pwd;
   }

   public void setEtlforgeMd5Pwd(String var1) {
      this.etlforgeMd5Pwd = var1;
   }

   public String getEndpointGUID() {
      return this.endpointGUID;
   }

   public void setEndpointGUID(String var1) {
      this.endpointGUID = var1;
   }

   public String getEndpointTypeName() {
      return this.endpointTypeName;
   }

   public void setEndpointTypeName(String var1) {
      this.endpointTypeName = var1;
   }

   public String getHost() {
      return this.host;
   }

   public void setHost(String var1) {
      this.host = var1;
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

   public boolean isIsPasswordEncrypted() {
      return this.isPasswordEncrypted;
   }

   public void setIsPasswordEncrypted(boolean var1) {
      this.isPasswordEncrypted = var1;
   }

   public WsNameValuePair[] getAdditionalParameters() {
      return this.additionalParameters;
   }

   public void setAdditionalParameters(WsNameValuePair[] var1) {
      this.additionalParameters = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsEndpointParameters)) {
         return false;
      }

      WsEndpointParameters var2 = (WsEndpointParameters)var1;
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
            this.etlforgeUser == null && var2.getEtlforgeUser() == null || this.etlforgeUser != null && this.etlforgeUser.equals(var2.getEtlforgeUser())
         )
         && (
            this.etlforgeMd5Pwd == null && var2.getEtlforgeMd5Pwd() == null
               || this.etlforgeMd5Pwd != null && this.etlforgeMd5Pwd.equals(var2.getEtlforgeMd5Pwd())
         )
         && (this.endpointGUID == null && var2.getEndpointGUID() == null || this.endpointGUID != null && this.endpointGUID.equals(var2.getEndpointGUID()))
         && (
            this.endpointTypeName == null && var2.getEndpointTypeName() == null
               || this.endpointTypeName != null && this.endpointTypeName.equals(var2.getEndpointTypeName())
         )
         && (this.host == null && var2.getHost() == null || this.host != null && this.host.equals(var2.getHost()))
         && (this.user == null && var2.getUser() == null || this.user != null && this.user.equals(var2.getUser()))
         && (this.password == null && var2.getPassword() == null || this.password != null && this.password.equals(var2.getPassword()))
         && this.isPasswordEncrypted == var2.isIsPasswordEncrypted()
         && (
            this.additionalParameters == null && var2.getAdditionalParameters() == null
               || this.additionalParameters != null && Arrays.equals(this.additionalParameters, var2.getAdditionalParameters())
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
      if (this.getEtlforgeUser() != null) {
         var1 += this.getEtlforgeUser().hashCode();
      }

      if (this.getEtlforgeMd5Pwd() != null) {
         var1 += this.getEtlforgeMd5Pwd().hashCode();
      }

      if (this.getEndpointGUID() != null) {
         var1 += this.getEndpointGUID().hashCode();
      }

      if (this.getEndpointTypeName() != null) {
         var1 += this.getEndpointTypeName().hashCode();
      }

      if (this.getHost() != null) {
         var1 += this.getHost().hashCode();
      }

      if (this.getUser() != null) {
         var1 += this.getUser().hashCode();
      }

      if (this.getPassword() != null) {
         var1 += this.getPassword().hashCode();
      }

      var1 += (this.isIsPasswordEncrypted() ? Boolean.TRUE : Boolean.FALSE).hashCode();
      if (this.getAdditionalParameters() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getAdditionalParameters()); var2++) {
            Object var3 = Array.get(this.getAdditionalParameters(), var2);
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
      typeDesc.setXmlType(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsEndpointParameters"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("etlforgeUser");
      var0.setXmlName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "ETLForgeUser"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("etlforgeMd5Pwd");
      var0.setXmlName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "ETLForgeMd5Pwd"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("endpointGUID");
      var0.setXmlName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "EndpointGUID"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("endpointTypeName");
      var0.setXmlName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "EndpointTypeName"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("host");
      var0.setXmlName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "Host"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("user");
      var0.setXmlName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "User"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("password");
      var0.setXmlName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "Password"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("isPasswordEncrypted");
      var0.setXmlName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "IsPasswordEncrypted"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("additionalParameters");
      var0.setXmlName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "AdditionalParameters"));
      var0.setXmlType(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsNameValuePair"));
      var0.setNillable(true);
      var0.setItemQName(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
