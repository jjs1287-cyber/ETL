package org.etlforge.integration.server.implementation.webservice.interchange.deploy.client;

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

public class CFileCheckResult implements Serializable {
   private int fileCheckStatus;
   private CRequiredFile[] availableRequiredFiles;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(CFileCheckResult.class, true);

   public CFileCheckResult() {
   }

   public CFileCheckResult(int var1, CRequiredFile[] var2) {
      this.fileCheckStatus = var1;
      this.availableRequiredFiles = var2;
   }

   public int getFileCheckStatus() {
      return this.fileCheckStatus;
   }

   public void setFileCheckStatus(int var1) {
      this.fileCheckStatus = var1;
   }

   public CRequiredFile[] getAvailableRequiredFiles() {
      return this.availableRequiredFiles;
   }

   public void setAvailableRequiredFiles(CRequiredFile[] var1) {
      this.availableRequiredFiles = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof CFileCheckResult)) {
         return false;
      }

      CFileCheckResult var2 = (CFileCheckResult)var1;
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
      boolean var3 = this.fileCheckStatus == var2.getFileCheckStatus()
         && (
            this.availableRequiredFiles == null && var2.getAvailableRequiredFiles() == null
               || this.availableRequiredFiles != null && Arrays.equals(this.availableRequiredFiles, var2.getAvailableRequiredFiles())
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
      var1 += this.getFileCheckStatus();
      if (this.getAvailableRequiredFiles() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getAvailableRequiredFiles()); var2++) {
            Object var3 = Array.get(this.getAvailableRequiredFiles(), var2);
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "CFileCheckResult")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("fileCheckStatus");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "FileCheckStatus"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("availableRequiredFiles");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "AvailableRequiredFiles")
      );
      var0.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "CRequiredFile"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
