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

public class RequiredFile implements Serializable {
   private String clientPath;
   private String zippedBase64EncodedContents;
   private String[] dependencies;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(RequiredFile.class, true);

   public RequiredFile() {
   }

   public RequiredFile(String var1, String var2, String[] var3) {
      this.clientPath = var1;
      this.zippedBase64EncodedContents = var2;
      this.dependencies = var3;
   }

   public String getClientPath() {
      return this.clientPath;
   }

   public void setClientPath(String var1) {
      this.clientPath = var1;
   }

   public String getZippedBase64EncodedContents() {
      return this.zippedBase64EncodedContents;
   }

   public void setZippedBase64EncodedContents(String var1) {
      this.zippedBase64EncodedContents = var1;
   }

   public String[] getDependencies() {
      return this.dependencies;
   }

   public void setDependencies(String[] var1) {
      this.dependencies = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof RequiredFile)) {
         return false;
      }

      RequiredFile var2 = (RequiredFile)var1;
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
      boolean var3 = (this.clientPath == null && var2.getClientPath() == null || this.clientPath != null && this.clientPath.equals(var2.getClientPath()))
         && (
            this.zippedBase64EncodedContents == null && var2.getZippedBase64EncodedContents() == null
               || this.zippedBase64EncodedContents != null && this.zippedBase64EncodedContents.equals(var2.getZippedBase64EncodedContents())
         )
         && (
            this.dependencies == null && var2.getDependencies() == null
               || this.dependencies != null && Arrays.equals(this.dependencies, var2.getDependencies())
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
      if (this.getClientPath() != null) {
         var1 += this.getClientPath().hashCode();
      }

      if (this.getZippedBase64EncodedContents() != null) {
         var1 += this.getZippedBase64EncodedContents().hashCode();
      }

      if (this.getDependencies() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getDependencies()); var2++) {
            Object var3 = Array.get(this.getDependencies(), var2);
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "RequiredFile")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("clientPath");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "ClientPath"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("zippedBase64EncodedContents");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "ZippedBase64EncodedContents")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("dependencies");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "Dependencies"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
