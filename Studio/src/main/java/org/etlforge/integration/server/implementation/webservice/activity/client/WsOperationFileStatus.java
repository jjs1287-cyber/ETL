package org.etlforge.integration.server.implementation.webservice.activity.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsOperationFileStatus implements Serializable {
   private String operationGUID;
   private long operationInstanceId;
   private int category;
   private String fileStatusMessage;
   private String filename;
   private String fileCharEncode;
   private long fileSize;
   private long numberOfRecords;
   private String GUID;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsOperationFileStatus.class, true);

   public WsOperationFileStatus() {
   }

   public WsOperationFileStatus(String var1, long var2, int var4, String var5, String var6, String var7, long var8, long var10, String var12) {
      this.operationGUID = var1;
      this.operationInstanceId = var2;
      this.category = var4;
      this.fileStatusMessage = var5;
      this.filename = var6;
      this.fileCharEncode = var7;
      this.fileSize = var8;
      this.numberOfRecords = var10;
      this.GUID = var12;
   }

   public String getOperationGUID() {
      return this.operationGUID;
   }

   public void setOperationGUID(String var1) {
      this.operationGUID = var1;
   }

   public long getOperationInstanceId() {
      return this.operationInstanceId;
   }

   public void setOperationInstanceId(long var1) {
      this.operationInstanceId = var1;
   }

   public int getCategory() {
      return this.category;
   }

   public void setCategory(int var1) {
      this.category = var1;
   }

   public String getFileStatusMessage() {
      return this.fileStatusMessage;
   }

   public void setFileStatusMessage(String var1) {
      this.fileStatusMessage = var1;
   }

   public String getFilename() {
      return this.filename;
   }

   public void setFilename(String var1) {
      this.filename = var1;
   }

   public String getFileCharEncode() {
      return this.fileCharEncode;
   }

   public void setFileCharEncode(String var1) {
      this.fileCharEncode = var1;
   }

   public long getFileSize() {
      return this.fileSize;
   }

   public void setFileSize(long var1) {
      this.fileSize = var1;
   }

   public long getNumberOfRecords() {
      return this.numberOfRecords;
   }

   public void setNumberOfRecords(long var1) {
      this.numberOfRecords = var1;
   }

   public String getGUID() {
      return this.GUID;
   }

   public void setGUID(String var1) {
      this.GUID = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsOperationFileStatus)) {
         return false;
      }

      WsOperationFileStatus var2 = (WsOperationFileStatus)var1;
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
            this.operationGUID == null && var2.getOperationGUID() == null || this.operationGUID != null && this.operationGUID.equals(var2.getOperationGUID())
         )
         && this.operationInstanceId == var2.getOperationInstanceId()
         && this.category == var2.getCategory()
         && (
            this.fileStatusMessage == null && var2.getFileStatusMessage() == null
               || this.fileStatusMessage != null && this.fileStatusMessage.equals(var2.getFileStatusMessage())
         )
         && (this.filename == null && var2.getFilename() == null || this.filename != null && this.filename.equals(var2.getFilename()))
         && (
            this.fileCharEncode == null && var2.getFileCharEncode() == null
               || this.fileCharEncode != null && this.fileCharEncode.equals(var2.getFileCharEncode())
         )
         && this.fileSize == var2.getFileSize()
         && this.numberOfRecords == var2.getNumberOfRecords()
         && (this.GUID == null && var2.getGUID() == null || this.GUID != null && this.GUID.equals(var2.getGUID()));
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
      if (this.getOperationGUID() != null) {
         var1 += this.getOperationGUID().hashCode();
      }

      var1 += new Long(this.getOperationInstanceId()).hashCode();
      var1 += this.getCategory();
      if (this.getFileStatusMessage() != null) {
         var1 += this.getFileStatusMessage().hashCode();
      }

      if (this.getFilename() != null) {
         var1 += this.getFilename().hashCode();
      }

      if (this.getFileCharEncode() != null) {
         var1 += this.getFileCharEncode().hashCode();
      }

      var1 += new Long(this.getFileSize()).hashCode();
      var1 += new Long(this.getNumberOfRecords()).hashCode();
      if (this.getGUID() != null) {
         var1 += this.getGUID().hashCode();
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
      typeDesc.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "WsOperationFileStatus"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("operationGUID");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "OperationGUID"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("operationInstanceId");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "OperationInstanceId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("category");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "Category"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("fileStatusMessage");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "FileStatusMessage"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("filename");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "Filename"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("fileCharEncode");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "FileCharEncode"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("fileSize");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "FileSize"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("numberOfRecords");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "NumberOfRecords"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("GUID");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "GUID"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
