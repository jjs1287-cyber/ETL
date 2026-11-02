package org.etlforge.integration.server.backend.webservice.jdbc;

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

public class WsWriteTargetParams implements Serializable {
   private String targetGuid;
   private WsConnectionParams connectionParams;
   private String jtrFileName;
   private String dataFileName;
   private boolean transaction;
   private int insertUpdateMode;
   private WsKeyValue[] keyValues;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsWriteTargetParams.class, true);

   public WsWriteTargetParams() {
   }

   public WsWriteTargetParams(String var1, WsConnectionParams var2, String var3, String var4, boolean var5, int var6, WsKeyValue[] var7) {
      this.targetGuid = var1;
      this.connectionParams = var2;
      this.jtrFileName = var3;
      this.dataFileName = var4;
      this.transaction = var5;
      this.insertUpdateMode = var6;
      this.keyValues = var7;
   }

   public String getTargetGuid() {
      return this.targetGuid;
   }

   public void setTargetGuid(String var1) {
      this.targetGuid = var1;
   }

   public WsConnectionParams getConnectionParams() {
      return this.connectionParams;
   }

   public void setConnectionParams(WsConnectionParams var1) {
      this.connectionParams = var1;
   }

   public String getJtrFileName() {
      return this.jtrFileName;
   }

   public void setJtrFileName(String var1) {
      this.jtrFileName = var1;
   }

   public String getDataFileName() {
      return this.dataFileName;
   }

   public void setDataFileName(String var1) {
      this.dataFileName = var1;
   }

   public boolean isTransaction() {
      return this.transaction;
   }

   public void setTransaction(boolean var1) {
      this.transaction = var1;
   }

   public int getInsertUpdateMode() {
      return this.insertUpdateMode;
   }

   public void setInsertUpdateMode(int var1) {
      this.insertUpdateMode = var1;
   }

   public WsKeyValue[] getKeyValues() {
      return this.keyValues;
   }

   public void setKeyValues(WsKeyValue[] var1) {
      this.keyValues = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsWriteTargetParams)) {
         return false;
      }

      WsWriteTargetParams var2 = (WsWriteTargetParams)var1;
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
      boolean var3 = (this.targetGuid == null && var2.getTargetGuid() == null || this.targetGuid != null && this.targetGuid.equals(var2.getTargetGuid()))
         && (
            this.connectionParams == null && var2.getConnectionParams() == null
               || this.connectionParams != null && this.connectionParams.equals(var2.getConnectionParams())
         )
         && (this.jtrFileName == null && var2.getJtrFileName() == null || this.jtrFileName != null && this.jtrFileName.equals(var2.getJtrFileName()))
         && (this.dataFileName == null && var2.getDataFileName() == null || this.dataFileName != null && this.dataFileName.equals(var2.getDataFileName()))
         && this.transaction == var2.isTransaction()
         && this.insertUpdateMode == var2.getInsertUpdateMode()
         && (this.keyValues == null && var2.getKeyValues() == null || this.keyValues != null && Arrays.equals(this.keyValues, var2.getKeyValues()));
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
      if (this.getTargetGuid() != null) {
         var1 += this.getTargetGuid().hashCode();
      }

      if (this.getConnectionParams() != null) {
         var1 += this.getConnectionParams().hashCode();
      }

      if (this.getJtrFileName() != null) {
         var1 += this.getJtrFileName().hashCode();
      }

      if (this.getDataFileName() != null) {
         var1 += this.getDataFileName().hashCode();
      }

      var1 += (this.isTransaction() ? Boolean.TRUE : Boolean.FALSE).hashCode();
      var1 += this.getInsertUpdateMode();
      if (this.getKeyValues() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getKeyValues()); var2++) {
            Object var3 = Array.get(this.getKeyValues(), var2);
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
      typeDesc.setXmlType(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsWriteTargetParams"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("targetGuid");
      var0.setXmlName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "targetGuid"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("connectionParams");
      var0.setXmlName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "connectionParams"));
      var0.setXmlType(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsConnectionParams"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("jtrFileName");
      var0.setXmlName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "jtrFileName"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("dataFileName");
      var0.setXmlName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "dataFileName"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("transaction");
      var0.setXmlName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "transaction"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("insertUpdateMode");
      var0.setXmlName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "insertUpdateMode"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("keyValues");
      var0.setXmlName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "keyValues"));
      var0.setXmlType(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsKeyValue"));
      var0.setNillable(true);
      var0.setItemQName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
