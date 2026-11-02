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

public class CWsdlFileUploadResult implements Serializable {
   private CWsdlFile wsdlFile;
   private String[] missingWsdlFiles;
   private CWsdlFile[] existingDependencies;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(CWsdlFileUploadResult.class, true);

   public CWsdlFileUploadResult() {
   }

   public CWsdlFileUploadResult(CWsdlFile var1, String[] var2, CWsdlFile[] var3) {
      this.wsdlFile = var1;
      this.missingWsdlFiles = var2;
      this.existingDependencies = var3;
   }

   public CWsdlFile getWsdlFile() {
      return this.wsdlFile;
   }

   public void setWsdlFile(CWsdlFile var1) {
      this.wsdlFile = var1;
   }

   public String[] getMissingWsdlFiles() {
      return this.missingWsdlFiles;
   }

   public void setMissingWsdlFiles(String[] var1) {
      this.missingWsdlFiles = var1;
   }

   public CWsdlFile[] getExistingDependencies() {
      return this.existingDependencies;
   }

   public void setExistingDependencies(CWsdlFile[] var1) {
      this.existingDependencies = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof CWsdlFileUploadResult)) {
         return false;
      }

      CWsdlFileUploadResult var2 = (CWsdlFileUploadResult)var1;
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
      boolean var3 = (this.wsdlFile == null && var2.getWsdlFile() == null || this.wsdlFile != null && this.wsdlFile.equals(var2.getWsdlFile()))
         && (
            this.missingWsdlFiles == null && var2.getMissingWsdlFiles() == null
               || this.missingWsdlFiles != null && Arrays.equals(this.missingWsdlFiles, var2.getMissingWsdlFiles())
         )
         && (
            this.existingDependencies == null && var2.getExistingDependencies() == null
               || this.existingDependencies != null && Arrays.equals(this.existingDependencies, var2.getExistingDependencies())
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
      if (this.getWsdlFile() != null) {
         var1 += this.getWsdlFile().hashCode();
      }

      if (this.getMissingWsdlFiles() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getMissingWsdlFiles()); var2++) {
            Object var3 = Array.get(this.getMissingWsdlFiles(), var2);
            if (var3 != null && !var3.getClass().isArray()) {
               var1 += var3.hashCode();
            }
         }
      }

      if (this.getExistingDependencies() != null) {
         for (int var4 = 0; var4 < Array.getLength(this.getExistingDependencies()); var4++) {
            Object var5 = Array.get(this.getExistingDependencies(), var4);
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
      typeDesc.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "CWsdlFileUploadResult")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("wsdlFile");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "WsdlFile"));
      var0.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "CWsdlFile"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("missingWsdlFiles");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "MissingWsdlFiles")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("existingDependencies");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "ExistingDependencies")
      );
      var0.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "CWsdlFile"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
