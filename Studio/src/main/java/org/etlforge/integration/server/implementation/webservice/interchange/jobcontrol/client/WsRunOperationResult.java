package org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsRunOperationResult implements Serializable {
   private long operationInstanceId;
   private String operationInstanceInfo;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsRunOperationResult.class, true);

   public WsRunOperationResult() {
   }

   public WsRunOperationResult(long var1, String var3) {
      this.operationInstanceId = var1;
      this.operationInstanceInfo = var3;
   }

   public long getOperationInstanceId() {
      return this.operationInstanceId;
   }

   public void setOperationInstanceId(long var1) {
      this.operationInstanceId = var1;
   }

   public String getOperationInstanceInfo() {
      return this.operationInstanceInfo;
   }

   public void setOperationInstanceInfo(String var1) {
      this.operationInstanceInfo = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsRunOperationResult)) {
         return false;
      }

      WsRunOperationResult var2 = (WsRunOperationResult)var1;
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
         && (
            this.operationInstanceInfo == null && var2.getOperationInstanceInfo() == null
               || this.operationInstanceInfo != null && this.operationInstanceInfo.equals(var2.getOperationInstanceInfo())
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
      if (this.getOperationInstanceInfo() != null) {
         var1 += this.getOperationInstanceInfo().hashCode();
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
      typeDesc.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.JobControl", "WsRunOperationResult"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("operationInstanceId");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.JobControl", "OperationInstanceId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("operationInstanceInfo");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.JobControl", "OperationInstanceInfo"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
