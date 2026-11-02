package org.etlforge.integration.server.backend.webservice.function;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsFunctionSessionId implements Serializable {
   private String etlforgeUser;
   private String md5Pwd;
   private String sessionGuid;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsFunctionSessionId.class, true);

   public WsFunctionSessionId() {
   }

   public WsFunctionSessionId(String var1, String var2, String var3) {
      this.etlforgeUser = var1;
      this.md5Pwd = var2;
      this.sessionGuid = var3;
   }

   public String getEtlforgeUser() {
      return this.etlforgeUser;
   }

   public void setEtlforgeUser(String var1) {
      this.etlforgeUser = var1;
   }

   public String getMd5Pwd() {
      return this.md5Pwd;
   }

   public void setMd5Pwd(String var1) {
      this.md5Pwd = var1;
   }

   public String getSessionGuid() {
      return this.sessionGuid;
   }

   public void setSessionGuid(String var1) {
      this.sessionGuid = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsFunctionSessionId)) {
         return false;
      }

      WsFunctionSessionId var2 = (WsFunctionSessionId)var1;
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
         && (this.md5Pwd == null && var2.getMd5Pwd() == null || this.md5Pwd != null && this.md5Pwd.equals(var2.getMd5Pwd()))
         && (this.sessionGuid == null && var2.getSessionGuid() == null || this.sessionGuid != null && this.sessionGuid.equals(var2.getSessionGuid()));
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

      if (this.getMd5Pwd() != null) {
         var1 += this.getMd5Pwd().hashCode();
      }

      if (this.getSessionGuid() != null) {
         var1 += this.getSessionGuid().hashCode();
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
      typeDesc.setXmlType(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsFunctionSessionId"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("etlforgeUser");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "ETLForgeUser"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("md5Pwd");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "Md5Pwd"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("sessionGuid");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "SessionGuid"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
