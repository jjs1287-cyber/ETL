package org.etlforge.integration.server.implementation.webservice.activity.client;

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

public class WsOperationLogInstance implements Serializable {
   private String operationGuid;
   private String operationName;
   private long operationInstanceId;
   private long parentOperationInstanceId;
   private long msgId;
   private String from;
   private int status;
   private String receivedTimeStamp;
   private String startTimeStamp;
   private String statusTimeStamp;
   private String message;
   private WsOperationMessage[] operationMessages;
   private int numberOfSourceFiles;
   private int numberOfTargetFiles;
   private long sourceTransferBytes;
   private long targetTransferBytes;
   private long numberOfSourceRecords;
   private long numberOfTargetRecords;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsOperationLogInstance.class, true);

   public WsOperationLogInstance() {
   }

   public WsOperationLogInstance(
      String var1,
      String var2,
      long var3,
      long var5,
      long var7,
      String var9,
      int var10,
      String var11,
      String var12,
      String var13,
      String var14,
      WsOperationMessage[] var15,
      int var16,
      int var17,
      long var18,
      long var20,
      long var22,
      long var24
   ) {
      this.operationGuid = var1;
      this.operationName = var2;
      this.operationInstanceId = var3;
      this.parentOperationInstanceId = var5;
      this.msgId = var7;
      this.from = var9;
      this.status = var10;
      this.receivedTimeStamp = var11;
      this.startTimeStamp = var12;
      this.statusTimeStamp = var13;
      this.message = var14;
      this.operationMessages = var15;
      this.numberOfSourceFiles = var16;
      this.numberOfTargetFiles = var17;
      this.sourceTransferBytes = var18;
      this.targetTransferBytes = var20;
      this.numberOfSourceRecords = var22;
      this.numberOfTargetRecords = var24;
   }

   public String getOperationGuid() {
      return this.operationGuid;
   }

   public void setOperationGuid(String var1) {
      this.operationGuid = var1;
   }

   public String getOperationName() {
      return this.operationName;
   }

   public void setOperationName(String var1) {
      this.operationName = var1;
   }

   public long getOperationInstanceId() {
      return this.operationInstanceId;
   }

   public void setOperationInstanceId(long var1) {
      this.operationInstanceId = var1;
   }

   public long getParentOperationInstanceId() {
      return this.parentOperationInstanceId;
   }

   public void setParentOperationInstanceId(long var1) {
      this.parentOperationInstanceId = var1;
   }

   public long getMsgId() {
      return this.msgId;
   }

   public void setMsgId(long var1) {
      this.msgId = var1;
   }

   public String getFrom() {
      return this.from;
   }

   public void setFrom(String var1) {
      this.from = var1;
   }

   public int getStatus() {
      return this.status;
   }

   public void setStatus(int var1) {
      this.status = var1;
   }

   public String getReceivedTimeStamp() {
      return this.receivedTimeStamp;
   }

   public void setReceivedTimeStamp(String var1) {
      this.receivedTimeStamp = var1;
   }

   public String getStartTimeStamp() {
      return this.startTimeStamp;
   }

   public void setStartTimeStamp(String var1) {
      this.startTimeStamp = var1;
   }

   public String getStatusTimeStamp() {
      return this.statusTimeStamp;
   }

   public void setStatusTimeStamp(String var1) {
      this.statusTimeStamp = var1;
   }

   public String getMessage() {
      return this.message;
   }

   public void setMessage(String var1) {
      this.message = var1;
   }

   public WsOperationMessage[] getOperationMessages() {
      return this.operationMessages;
   }

   public void setOperationMessages(WsOperationMessage[] var1) {
      this.operationMessages = var1;
   }

   public int getNumberOfSourceFiles() {
      return this.numberOfSourceFiles;
   }

   public void setNumberOfSourceFiles(int var1) {
      this.numberOfSourceFiles = var1;
   }

   public int getNumberOfTargetFiles() {
      return this.numberOfTargetFiles;
   }

   public void setNumberOfTargetFiles(int var1) {
      this.numberOfTargetFiles = var1;
   }

   public long getSourceTransferBytes() {
      return this.sourceTransferBytes;
   }

   public void setSourceTransferBytes(long var1) {
      this.sourceTransferBytes = var1;
   }

   public long getTargetTransferBytes() {
      return this.targetTransferBytes;
   }

   public void setTargetTransferBytes(long var1) {
      this.targetTransferBytes = var1;
   }

   public long getNumberOfSourceRecords() {
      return this.numberOfSourceRecords;
   }

   public void setNumberOfSourceRecords(long var1) {
      this.numberOfSourceRecords = var1;
   }

   public long getNumberOfTargetRecords() {
      return this.numberOfTargetRecords;
   }

   public void setNumberOfTargetRecords(long var1) {
      this.numberOfTargetRecords = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsOperationLogInstance)) {
         return false;
      }

      WsOperationLogInstance var2 = (WsOperationLogInstance)var1;
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
            this.operationGuid == null && var2.getOperationGuid() == null || this.operationGuid != null && this.operationGuid.equals(var2.getOperationGuid())
         )
         && (this.operationName == null && var2.getOperationName() == null || this.operationName != null && this.operationName.equals(var2.getOperationName()))
         && this.operationInstanceId == var2.getOperationInstanceId()
         && this.parentOperationInstanceId == var2.getParentOperationInstanceId()
         && this.msgId == var2.getMsgId()
         && (this.from == null && var2.getFrom() == null || this.from != null && this.from.equals(var2.getFrom()))
         && this.status == var2.getStatus()
         && (
            this.receivedTimeStamp == null && var2.getReceivedTimeStamp() == null
               || this.receivedTimeStamp != null && this.receivedTimeStamp.equals(var2.getReceivedTimeStamp())
         )
         && (
            this.startTimeStamp == null && var2.getStartTimeStamp() == null
               || this.startTimeStamp != null && this.startTimeStamp.equals(var2.getStartTimeStamp())
         )
         && (
            this.statusTimeStamp == null && var2.getStatusTimeStamp() == null
               || this.statusTimeStamp != null && this.statusTimeStamp.equals(var2.getStatusTimeStamp())
         )
         && (this.message == null && var2.getMessage() == null || this.message != null && this.message.equals(var2.getMessage()))
         && (
            this.operationMessages == null && var2.getOperationMessages() == null
               || this.operationMessages != null && Arrays.equals(this.operationMessages, var2.getOperationMessages())
         )
         && this.numberOfSourceFiles == var2.getNumberOfSourceFiles()
         && this.numberOfTargetFiles == var2.getNumberOfTargetFiles()
         && this.sourceTransferBytes == var2.getSourceTransferBytes()
         && this.targetTransferBytes == var2.getTargetTransferBytes()
         && this.numberOfSourceRecords == var2.getNumberOfSourceRecords()
         && this.numberOfTargetRecords == var2.getNumberOfTargetRecords();
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
      if (this.getOperationGuid() != null) {
         var1 += this.getOperationGuid().hashCode();
      }

      if (this.getOperationName() != null) {
         var1 += this.getOperationName().hashCode();
      }

      var1 += new Long(this.getOperationInstanceId()).hashCode();
      var1 += new Long(this.getParentOperationInstanceId()).hashCode();
      var1 += new Long(this.getMsgId()).hashCode();
      if (this.getFrom() != null) {
         var1 += this.getFrom().hashCode();
      }

      var1 += this.getStatus();
      if (this.getReceivedTimeStamp() != null) {
         var1 += this.getReceivedTimeStamp().hashCode();
      }

      if (this.getStartTimeStamp() != null) {
         var1 += this.getStartTimeStamp().hashCode();
      }

      if (this.getStatusTimeStamp() != null) {
         var1 += this.getStatusTimeStamp().hashCode();
      }

      if (this.getMessage() != null) {
         var1 += this.getMessage().hashCode();
      }

      if (this.getOperationMessages() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getOperationMessages()); var2++) {
            Object var3 = Array.get(this.getOperationMessages(), var2);
            if (var3 != null && !var3.getClass().isArray()) {
               var1 += var3.hashCode();
            }
         }
      }

      var1 += this.getNumberOfSourceFiles();
      var1 += this.getNumberOfTargetFiles();
      var1 += new Long(this.getSourceTransferBytes()).hashCode();
      var1 += new Long(this.getTargetTransferBytes()).hashCode();
      var1 += new Long(this.getNumberOfSourceRecords()).hashCode();
      var1 += new Long(this.getNumberOfTargetRecords()).hashCode();
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
      typeDesc.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "WsOperationLogInstance"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("operationGuid");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "OperationGuid"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("operationName");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "OperationName"));
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
      var0.setFieldName("parentOperationInstanceId");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "ParentOperationInstanceId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("msgId");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "MsgId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("from");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "From"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("status");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "Status"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("receivedTimeStamp");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "ReceivedTimeStamp"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("startTimeStamp");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "StartTimeStamp"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("statusTimeStamp");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "StatusTimeStamp"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("message");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "Message"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("operationMessages");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "OperationMessages"));
      var0.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "WsOperationMessage"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("numberOfSourceFiles");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "NumberOfSourceFiles"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("numberOfTargetFiles");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "NumberOfTargetFiles"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("sourceTransferBytes");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "SourceTransferBytes"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("targetTransferBytes");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "TargetTransferBytes"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("numberOfSourceRecords");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "NumberOfSourceRecords"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("numberOfTargetRecords");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "NumberOfTargetRecords"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
   }
}
