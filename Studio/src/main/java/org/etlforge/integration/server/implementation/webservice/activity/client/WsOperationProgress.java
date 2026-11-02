package org.etlforge.integration.server.implementation.webservice.activity.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsOperationProgress implements Serializable {
   private long operationInstanceId;
   private String operationGuid;
   private String section;
   private String message;
   private int percentageDone;
   private String estimatedEndTimeUtc;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsOperationProgress.class, true);

   public WsOperationProgress() {
   }

   public WsOperationProgress(long var1, String var3, String var4, String var5, int var6, String var7) {
      this.operationInstanceId = var1;
      this.operationGuid = var3;
      this.section = var4;
      this.message = var5;
      this.percentageDone = var6;
      this.estimatedEndTimeUtc = var7;
   }

   public long getOperationInstanceId() {
      return this.operationInstanceId;
   }

   public void setOperationInstanceId(long var1) {
      this.operationInstanceId = var1;
   }

   public String getOperationGuid() {
      return this.operationGuid;
   }

   public void setOperationGuid(String var1) {
      this.operationGuid = var1;
   }

   public String getSection() {
      return this.section;
   }

   public void setSection(String var1) {
      this.section = var1;
   }

   public String getMessage() {
      return this.message;
   }

   public void setMessage(String var1) {
      this.message = var1;
   }

   public int getPercentageDone() {
      return this.percentageDone;
   }

   public void setPercentageDone(int var1) {
      this.percentageDone = var1;
   }

   public String getEstimatedEndTimeUtc() {
      return this.estimatedEndTimeUtc;
   }

   public void setEstimatedEndTimeUtc(String var1) {
      this.estimatedEndTimeUtc = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsOperationProgress)) {
         return false;
      }

      WsOperationProgress var2 = (WsOperationProgress)var1;
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
      boolean var3 = this.operationInstanceId == var2.getOperationInstanceId()
         && (this.operationGuid == null && var2.getOperationGuid() == null || this.operationGuid != null && this.operationGuid.equals(var2.getOperationGuid()))
         && (this.section == null && var2.getSection() == null || this.section != null && this.section.equals(var2.getSection()))
         && (this.message == null && var2.getMessage() == null || this.message != null && this.message.equals(var2.getMessage()))
         && this.percentageDone == var2.getPercentageDone()
         && (
            this.estimatedEndTimeUtc == null && var2.getEstimatedEndTimeUtc() == null
               || this.estimatedEndTimeUtc != null && this.estimatedEndTimeUtc.equals(var2.getEstimatedEndTimeUtc())
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
      var1 += new Long(this.getOperationInstanceId()).hashCode();
      if (this.getOperationGuid() != null) {
         var1 += this.getOperationGuid().hashCode();
      }

      if (this.getSection() != null) {
         var1 += this.getSection().hashCode();
      }

      if (this.getMessage() != null) {
         var1 += this.getMessage().hashCode();
      }

      var1 += this.getPercentageDone();
      if (this.getEstimatedEndTimeUtc() != null) {
         var1 += this.getEstimatedEndTimeUtc().hashCode();
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
      typeDesc.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "WsOperationProgress"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("operationInstanceId");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "OperationInstanceId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("operationGuid");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "OperationGuid"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("section");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "Section"));
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
      var0.setFieldName("percentageDone");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "PercentageDone"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("estimatedEndTimeUtc");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "EstimatedEndTimeUtc"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
