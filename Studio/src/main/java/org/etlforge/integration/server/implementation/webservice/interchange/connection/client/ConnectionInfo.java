package org.etlforge.integration.server.implementation.webservice.interchange.connection.client;

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

public class ConnectionInfo implements Serializable {
   private String[] keys;
   private String[] keyValues;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(ConnectionInfo.class, true);

   public ConnectionInfo() {
   }

   public ConnectionInfo(String[] var1, String[] var2) {
      this.keys = var1;
      this.keyValues = var2;
   }

   public String[] getKeys() {
      return this.keys;
   }

   public void setKeys(String[] var1) {
      this.keys = var1;
   }

   public String[] getKeyValues() {
      return this.keyValues;
   }

   public void setKeyValues(String[] var1) {
      this.keyValues = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof ConnectionInfo)) {
         return false;
      }

      ConnectionInfo var2 = (ConnectionInfo)var1;
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
      boolean var3 = (this.keys == null && var2.getKeys() == null || this.keys != null && Arrays.equals(this.keys, var2.getKeys()))
         && (this.keyValues == null && var2.getKeyValues() == null || this.keyValues != null && Arrays.equals(this.keyValues, var2.getKeyValues()));
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
      if (this.getKeys() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getKeys()); var2++) {
            Object var3 = Array.get(this.getKeys(), var2);
            if (var3 != null && !var3.getClass().isArray()) {
               var1 += var3.hashCode();
            }
         }
      }

      if (this.getKeyValues() != null) {
         for (int var4 = 0; var4 < Array.getLength(this.getKeyValues()); var4++) {
            Object var5 = Array.get(this.getKeyValues(), var4);
            if (var5 != null && !var5.getClass().isArray()) {
               var1 += var5.hashCode();
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
      typeDesc.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "ConnectionInfo"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("keys");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "Keys"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("keyValues");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "KeyValues"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
