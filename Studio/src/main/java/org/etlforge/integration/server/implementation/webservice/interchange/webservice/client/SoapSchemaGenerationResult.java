package org.etlforge.integration.server.implementation.webservice.interchange.webservice.client;

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

public class SoapSchemaGenerationResult implements Serializable {
   private int result;
   private String[] fileNames;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(SoapSchemaGenerationResult.class, true);

   public SoapSchemaGenerationResult() {
   }

   public SoapSchemaGenerationResult(int var1, String[] var2) {
      this.result = var1;
      this.fileNames = var2;
   }

   public int getResult() {
      return this.result;
   }

   public void setResult(int var1) {
      this.result = var1;
   }

   public String[] getFileNames() {
      return this.fileNames;
   }

   public void setFileNames(String[] var1) {
      this.fileNames = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof SoapSchemaGenerationResult)) {
         return false;
      }

      SoapSchemaGenerationResult var2 = (SoapSchemaGenerationResult)var1;
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
      boolean var3 = this.result == var2.getResult()
         && (this.fileNames == null && var2.getFileNames() == null || this.fileNames != null && Arrays.equals(this.fileNames, var2.getFileNames()));
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
      var1 += this.getResult();
      if (this.getFileNames() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getFileNames()); var2++) {
            Object var3 = Array.get(this.getFileNames(), var2);
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "SoapSchemaGenerationResult")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("result");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "Result"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("fileNames");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "FileNames"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
