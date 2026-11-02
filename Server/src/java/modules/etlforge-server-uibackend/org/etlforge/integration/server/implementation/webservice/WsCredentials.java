package org.etlforge.integration.server.implementation.webservice;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsCredentials implements Serializable {
   private String user;
   private String md5Pwd;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsCredentials.class, true);

   public WsCredentials() {
   }

   public WsCredentials(String var1, String var2) {
      this.user = var1;
      this.md5Pwd = var2;
   }

   public String getUser() {
      return this.user;
   }

   public void setUser(String var1) {
      this.user = var1;
   }

   public String getMd5Pwd() {
      return this.md5Pwd;
   }

   public void setMd5Pwd(String var1) {
      this.md5Pwd = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsCredentials)) {
         return false;
      }

      WsCredentials var2 = (WsCredentials)var1;
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
      boolean var3 = (this.user == null && var2.getUser() == null || this.user != null && this.user.equals(var2.getUser()))
         && (this.md5Pwd == null && var2.getMd5Pwd() == null || this.md5Pwd != null && this.md5Pwd.equals(var2.getMd5Pwd()));
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
      if (this.getUser() != null) {
         var1 += this.getUser().hashCode();
      }

      if (this.getMd5Pwd() != null) {
         var1 += this.getMd5Pwd().hashCode();
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
      typeDesc.setXmlType(new QName("http://webservice.implementation.server.integration.etlforge.invalid", "WsCredentials"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("user");
      var0.setXmlName(new QName("http://webservice.implementation.server.integration.etlforge.invalid", "user"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("md5Pwd");
      var0.setXmlName(new QName("http://webservice.implementation.server.integration.etlforge.invalid", "md5Pwd"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
