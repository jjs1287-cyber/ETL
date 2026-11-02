package org.etlforge.integration.server.implementation.webservice.interchange.checkout.client;

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

public class CUserPermissions implements Serializable {
   private String userId;
   private CPermission[] permissions;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(CUserPermissions.class, true);

   public CUserPermissions() {
   }

   public CUserPermissions(String var1, CPermission[] var2) {
      this.userId = var1;
      this.permissions = var2;
   }

   public String getUserId() {
      return this.userId;
   }

   public void setUserId(String var1) {
      this.userId = var1;
   }

   public CPermission[] getPermissions() {
      return this.permissions;
   }

   public void setPermissions(CPermission[] var1) {
      this.permissions = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof CUserPermissions)) {
         return false;
      }

      CUserPermissions var2 = (CUserPermissions)var1;
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
      boolean var3 = (this.userId == null && var2.getUserId() == null || this.userId != null && this.userId.equals(var2.getUserId()))
         && (this.permissions == null && var2.getPermissions() == null || this.permissions != null && Arrays.equals(this.permissions, var2.getPermissions()));
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
      if (this.getUserId() != null) {
         var1 += this.getUserId().hashCode();
      }

      if (this.getPermissions() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getPermissions()); var2++) {
            Object var3 = Array.get(this.getPermissions(), var2);
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
      typeDesc.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "CUserPermissions")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("userId");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "UserId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("permissions");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "Permissions"));
      var0.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "CPermission"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
