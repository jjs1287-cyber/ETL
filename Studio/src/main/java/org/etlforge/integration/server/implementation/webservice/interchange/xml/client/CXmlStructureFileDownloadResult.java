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

public class CXmlStructureFileDownloadResult implements Serializable {
   private CXmlStructureFile structureFile;
   private CXmlStructureFile[] dependencies;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(CXmlStructureFileDownloadResult.class, true);

   public CXmlStructureFileDownloadResult() {
   }

   public CXmlStructureFileDownloadResult(CXmlStructureFile var1, CXmlStructureFile[] var2) {
      this.structureFile = var1;
      this.dependencies = var2;
   }

   public CXmlStructureFile getStructureFile() {
      return this.structureFile;
   }

   public void setStructureFile(CXmlStructureFile var1) {
      this.structureFile = var1;
   }

   public CXmlStructureFile[] getDependencies() {
      return this.dependencies;
   }

   public void setDependencies(CXmlStructureFile[] var1) {
      this.dependencies = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof CXmlStructureFileDownloadResult)) {
         return false;
      }

      CXmlStructureFileDownloadResult var2 = (CXmlStructureFileDownloadResult)var1;
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
            this.structureFile == null && var2.getStructureFile() == null || this.structureFile != null && this.structureFile.equals(var2.getStructureFile())
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
      if (this.getStructureFile() != null) {
         var1 += this.getStructureFile().hashCode();
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.xml.XmlInfoProvider", "CXmlStructureFileDownloadResult")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("structureFile");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.xml.XmlInfoProvider", "StructureFile"));
      var0.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.xml.XmlInfoProvider", "CXmlStructureFile"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("dependencies");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.xml.XmlInfoProvider", "Dependencies"));
      var0.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.xml.XmlInfoProvider", "CXmlStructureFile"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.xml.XmlInfoProvider", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
