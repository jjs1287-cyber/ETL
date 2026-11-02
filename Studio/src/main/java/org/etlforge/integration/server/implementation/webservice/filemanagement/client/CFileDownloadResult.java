package org.etlforge.integration.server.implementation.webservice.filemanagement.client;

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

public class CFileDownloadResult implements Serializable {
   private CDownloadedFile downloadedFile;
   private CDownloadedFile[] dependencies;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(CFileDownloadResult.class, true);

   public CFileDownloadResult() {
   }

   public CFileDownloadResult(CDownloadedFile var1, CDownloadedFile[] var2) {
      this.downloadedFile = var1;
      this.dependencies = var2;
   }

   public CDownloadedFile getDownloadedFile() {
      return this.downloadedFile;
   }

   public void setDownloadedFile(CDownloadedFile var1) {
      this.downloadedFile = var1;
   }

   public CDownloadedFile[] getDependencies() {
      return this.dependencies;
   }

   public void setDependencies(CDownloadedFile[] var1) {
      this.dependencies = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof CFileDownloadResult)) {
         return false;
      }

      CFileDownloadResult var2 = (CFileDownloadResult)var1;
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
            this.downloadedFile == null && var2.getDownloadedFile() == null
               || this.downloadedFile != null && this.downloadedFile.equals(var2.getDownloadedFile())
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
      if (this.getDownloadedFile() != null) {
         var1 += this.getDownloadedFile().hashCode();
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
      typeDesc.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.filemanagement.FileManager", "CFileDownloadResult"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("downloadedFile");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.filemanagement.FileManager", "DownloadedFile"));
      var0.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.filemanagement.FileManager", "CDownloadedFile"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("dependencies");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.filemanagement.FileManager", "Dependencies"));
      var0.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.filemanagement.FileManager", "CDownloadedFile"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.filemanagement.FileManager", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
