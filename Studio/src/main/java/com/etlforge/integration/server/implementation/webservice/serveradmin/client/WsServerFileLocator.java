package com.etlforge.integration.server.implementation.webservice.serveradmin.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsServerFileLocator implements Serializable {
   private String path;
   private String lastModified;
   private long sizeInBytes;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsServerFileLocator.class, true);

   public WsServerFileLocator() {
   }

   public WsServerFileLocator(String var1, String var2, long var3) {
      this.path = var1;
      this.lastModified = var2;
      this.sizeInBytes = var3;
   }

   public String getPath() {
      return this.path;
   }

   public void setPath(String var1) {
      this.path = var1;
   }

   public String getLastModified() {
      return this.lastModified;
   }

   public void setLastModified(String var1) {
      this.lastModified = var1;
   }

   public long getSizeInBytes() {
      return this.sizeInBytes;
   }

   public void setSizeInBytes(long var1) {
      this.sizeInBytes = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsServerFileLocator)) {
         return false;
      }

      WsServerFileLocator var2 = (WsServerFileLocator)var1;
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
      boolean var3 = (this.path == null && var2.getPath() == null || this.path != null && this.path.equals(var2.getPath()))
         && (this.lastModified == null && var2.getLastModified() == null || this.lastModified != null && this.lastModified.equals(var2.getLastModified()))
         && this.sizeInBytes == var2.getSizeInBytes();
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
      if (this.getPath() != null) {
         var1 += this.getPath().hashCode();
      }

      if (this.getLastModified() != null) {
         var1 += this.getLastModified().hashCode();
      }

      var1 += new Long(this.getSizeInBytes()).hashCode();
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
      typeDesc.setXmlType(new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "WsServerFileLocator"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("path");
      var0.setXmlName(new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "path"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("lastModified");
      var0.setXmlName(new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "lastModified"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("sizeInBytes");
      var0.setXmlName(new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "sizeInBytes"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
   }
}
