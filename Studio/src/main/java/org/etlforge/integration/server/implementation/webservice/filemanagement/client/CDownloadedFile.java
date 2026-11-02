package org.etlforge.integration.server.implementation.webservice.filemanagement.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class CDownloadedFile implements Serializable {
   private String guid;
   private String path;
   private String MD5;
   private String zippedBase64EncodedContents;
   private long modifiedTime;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(CDownloadedFile.class, true);

   public CDownloadedFile() {
   }

   public CDownloadedFile(String var1, String var2, String var3, String var4, long var5) {
      this.guid = var1;
      this.path = var2;
      this.MD5 = var3;
      this.zippedBase64EncodedContents = var4;
      this.modifiedTime = var5;
   }

   public String getGuid() {
      return this.guid;
   }

   public void setGuid(String var1) {
      this.guid = var1;
   }

   public String getPath() {
      return this.path;
   }

   public void setPath(String var1) {
      this.path = var1;
   }

   public String getMD5() {
      return this.MD5;
   }

   public void setMD5(String var1) {
      this.MD5 = var1;
   }

   public String getZippedBase64EncodedContents() {
      return this.zippedBase64EncodedContents;
   }

   public void setZippedBase64EncodedContents(String var1) {
      this.zippedBase64EncodedContents = var1;
   }

   public long getModifiedTime() {
      return this.modifiedTime;
   }

   public void setModifiedTime(long var1) {
      this.modifiedTime = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof CDownloadedFile)) {
         return false;
      }

      CDownloadedFile var2 = (CDownloadedFile)var1;
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
      boolean var3 = (this.guid == null && var2.getGuid() == null || this.guid != null && this.guid.equals(var2.getGuid()))
         && (this.path == null && var2.getPath() == null || this.path != null && this.path.equals(var2.getPath()))
         && (this.MD5 == null && var2.getMD5() == null || this.MD5 != null && this.MD5.equals(var2.getMD5()))
         && (
            this.zippedBase64EncodedContents == null && var2.getZippedBase64EncodedContents() == null
               || this.zippedBase64EncodedContents != null && this.zippedBase64EncodedContents.equals(var2.getZippedBase64EncodedContents())
         )
         && this.modifiedTime == var2.getModifiedTime();
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
      if (this.getGuid() != null) {
         var1 += this.getGuid().hashCode();
      }

      if (this.getPath() != null) {
         var1 += this.getPath().hashCode();
      }

      if (this.getMD5() != null) {
         var1 += this.getMD5().hashCode();
      }

      if (this.getZippedBase64EncodedContents() != null) {
         var1 += this.getZippedBase64EncodedContents().hashCode();
      }

      var1 += new Long(this.getModifiedTime()).hashCode();
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
      typeDesc.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.filemanagement.FileManager", "CDownloadedFile"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("guid");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.filemanagement.FileManager", "Guid"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("path");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.filemanagement.FileManager", "Path"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("MD5");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.filemanagement.FileManager", "MD5"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("zippedBase64EncodedContents");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.filemanagement.FileManager", "ZippedBase64EncodedContents"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("modifiedTime");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.filemanagement.FileManager", "ModifiedTime"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
   }
}
