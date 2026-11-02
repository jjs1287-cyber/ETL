package org.etlforge.integration.server.implementation.webservice.interchange.ldap;

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

public class WsObjectClassInfoReturn implements Serializable {
   private WsObjectClassInfo[] returnedClasses;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsObjectClassInfoReturn.class, true);

   public WsObjectClassInfoReturn() {
   }

   public WsObjectClassInfoReturn(WsObjectClassInfo[] var1) {
      this.returnedClasses = var1;
   }

   public WsObjectClassInfo[] getReturnedClasses() {
      return this.returnedClasses;
   }

   public void setReturnedClasses(WsObjectClassInfo[] var1) {
      this.returnedClasses = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsObjectClassInfoReturn)) {
         return false;
      }

      WsObjectClassInfoReturn var2 = (WsObjectClassInfoReturn)var1;
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
      boolean var3 = this.returnedClasses == null && var2.getReturnedClasses() == null
         || this.returnedClasses != null && Arrays.equals(this.returnedClasses, var2.getReturnedClasses());
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
      if (this.getReturnedClasses() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getReturnedClasses()); var2++) {
            Object var3 = Array.get(this.getReturnedClasses(), var2);
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
      typeDesc.setXmlType(new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsObjectClassInfoReturn"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("returnedClasses");
      var0.setXmlName(new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "returnedClasses"));
      var0.setXmlType(new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsObjectClassInfo"));
      var0.setNillable(true);
      var0.setItemQName(new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
