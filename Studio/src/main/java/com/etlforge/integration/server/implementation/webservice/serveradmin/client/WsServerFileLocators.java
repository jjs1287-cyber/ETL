package com.etlforge.integration.server.implementation.webservice.serveradmin.client;

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

public class WsServerFileLocators implements Serializable {
   private WsServerFileLocator[] locators;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsServerFileLocators.class, true);

   public WsServerFileLocators() {
   }

   public WsServerFileLocators(WsServerFileLocator[] var1) {
      this.locators = var1;
   }

   public WsServerFileLocator[] getLocators() {
      return this.locators;
   }

   public void setLocators(WsServerFileLocator[] var1) {
      this.locators = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsServerFileLocators)) {
         return false;
      }

      WsServerFileLocators var2 = (WsServerFileLocators)var1;
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
      boolean var3 = this.locators == null && var2.getLocators() == null || this.locators != null && Arrays.equals(this.locators, var2.getLocators());
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
      if (this.getLocators() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getLocators()); var2++) {
            Object var3 = Array.get(this.getLocators(), var2);
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
      typeDesc.setXmlType(new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "WsServerFileLocators"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("locators");
      var0.setXmlName(new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "locators"));
      var0.setXmlType(new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "WsServerFileLocator"));
      var0.setNillable(true);
      var0.setItemQName(new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
