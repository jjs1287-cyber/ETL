package org.etlforge.integration.server.implementation.webservice.interchange.xml.client;

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

public class CXmlStructureFileUploadResult implements Serializable {
   private CXmlStructureFile xmlStructureFile;
   private String[] missingStructureFiles;
   private CXmlStructureFile[] existingDependencies;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(CXmlStructureFileUploadResult.class, true);

   public CXmlStructureFileUploadResult() {
   }

   public CXmlStructureFileUploadResult(CXmlStructureFile var1, String[] var2, CXmlStructureFile[] var3) {
      this.xmlStructureFile = var1;
      this.missingStructureFiles = var2;
      this.existingDependencies = var3;
   }

   public CXmlStructureFile getXmlStructureFile() {
      return this.xmlStructureFile;
   }

   public void setXmlStructureFile(CXmlStructureFile var1) {
      this.xmlStructureFile = var1;
   }

   public String[] getMissingStructureFiles() {
      return this.missingStructureFiles;
   }

   public void setMissingStructureFiles(String[] var1) {
      this.missingStructureFiles = var1;
   }

   public CXmlStructureFile[] getExistingDependencies() {
      return this.existingDependencies;
   }

   public void setExistingDependencies(CXmlStructureFile[] var1) {
      this.existingDependencies = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof CXmlStructureFileUploadResult)) {
         return false;
      }

      CXmlStructureFileUploadResult var2 = (CXmlStructureFileUploadResult)var1;
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
            this.xmlStructureFile == null && var2.getXmlStructureFile() == null
               || this.xmlStructureFile != null && this.xmlStructureFile.equals(var2.getXmlStructureFile())
         )
         && (
            this.missingStructureFiles == null && var2.getMissingStructureFiles() == null
               || this.missingStructureFiles != null && Arrays.equals(this.missingStructureFiles, var2.getMissingStructureFiles())
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
      if (this.getXmlStructureFile() != null) {
         var1 += this.getXmlStructureFile().hashCode();
      }

      if (this.getMissingStructureFiles() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getMissingStructureFiles()); var2++) {
            Object var3 = Array.get(this.getMissingStructureFiles(), var2);
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.xml.XmlInfoProvider", "CXmlStructureFileUploadResult")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("xmlStructureFile");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.xml.XmlInfoProvider", "XmlStructureFile"));
      var0.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.xml.XmlInfoProvider", "CXmlStructureFile"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("missingStructureFiles");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.xml.XmlInfoProvider", "MissingStructureFiles"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.xml.XmlInfoProvider", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("existingDependencies");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.xml.XmlInfoProvider", "ExistingDependencies"));
      var0.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.xml.XmlInfoProvider", "CXmlStructureFile"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.xml.XmlInfoProvider", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
