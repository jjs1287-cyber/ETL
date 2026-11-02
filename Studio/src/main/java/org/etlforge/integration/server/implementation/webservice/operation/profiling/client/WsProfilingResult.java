package org.etlforge.integration.server.implementation.webservice.operation.profiling.client;

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

public class WsProfilingResult implements Serializable {
   private String operationGuid;
   private String[] profilingData;
   private String operationErrors;
   private String operationWarnings;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsProfilingResult.class, true);

   public WsProfilingResult() {
   }

   public WsProfilingResult(String var1, String[] var2, String var3, String var4) {
      this.operationGuid = var1;
      this.profilingData = var2;
      this.operationErrors = var3;
      this.operationWarnings = var4;
   }

   public String getOperationGuid() {
      return this.operationGuid;
   }

   public void setOperationGuid(String var1) {
      this.operationGuid = var1;
   }

   public String[] getProfilingData() {
      return this.profilingData;
   }

   public void setProfilingData(String[] var1) {
      this.profilingData = var1;
   }

   public String getOperationErrors() {
      return this.operationErrors;
   }

   public void setOperationErrors(String var1) {
      this.operationErrors = var1;
   }

   public String getOperationWarnings() {
      return this.operationWarnings;
   }

   public void setOperationWarnings(String var1) {
      this.operationWarnings = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsProfilingResult)) {
         return false;
      }

      WsProfilingResult var2 = (WsProfilingResult)var1;
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
         && (
            this.profilingData == null && var2.getProfilingData() == null
               || this.profilingData != null && Arrays.equals(this.profilingData, var2.getProfilingData())
         )
         && (
            this.operationErrors == null && var2.getOperationErrors() == null
               || this.operationErrors != null && this.operationErrors.equals(var2.getOperationErrors())
         )
         && (
            this.operationWarnings == null && var2.getOperationWarnings() == null
               || this.operationWarnings != null && this.operationWarnings.equals(var2.getOperationWarnings())
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
      if (this.getOperationGuid() != null) {
         var1 += this.getOperationGuid().hashCode();
      }

      if (this.getProfilingData() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getProfilingData()); var2++) {
            Object var3 = Array.get(this.getProfilingData(), var2);
            if (var3 != null && !var3.getClass().isArray()) {
               var1 += var3.hashCode();
            }
         }
      }

      if (this.getOperationErrors() != null) {
         var1 += this.getOperationErrors().hashCode();
      }

      if (this.getOperationWarnings() != null) {
         var1 += this.getOperationWarnings().hashCode();
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.operation.profiling.WsOperationProfiler", "WsProfilingResult")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("operationGuid");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.operation.profiling.WsOperationProfiler", "operationGuid"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("profilingData");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.operation.profiling.WsOperationProfiler", "profilingData"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.operation.profiling.WsOperationProfiler", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("operationErrors");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.operation.profiling.WsOperationProfiler", "operationErrors"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("operationWarnings");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.operation.profiling.WsOperationProfiler", "operationWarnings"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
   }
}
