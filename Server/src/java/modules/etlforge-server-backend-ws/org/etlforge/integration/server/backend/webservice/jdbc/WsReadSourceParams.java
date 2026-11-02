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

public class WsReadSourceParams implements Serializable {
   private String sourceGuid;
   private WsConnectionParams connectionParams;
   private String jtrFileName;
   private String dataFileName;
   private String structureFileName;
   private String adHocFilter;
   private int fetchSize;
   private WsKeyValue[] keyValues;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsReadSourceParams.class, true);

   public WsReadSourceParams() {
   }

   public WsReadSourceParams(String var1, WsConnectionParams var2, String var3, String var4, String var5, String var6, int var7, WsKeyValue[] var8) {
      this.sourceGuid = var1;
      this.connectionParams = var2;
      this.jtrFileName = var3;
      this.dataFileName = var4;
      this.structureFileName = var5;
      this.adHocFilter = var6;
      this.fetchSize = var7;
      this.keyValues = var8;
   }

   public String getSourceGuid() {
      return this.sourceGuid;
   }

   public void setSourceGuid(String var1) {
      this.sourceGuid = var1;
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

   public String getStructureFileName() {
      return this.structureFileName;
   }

   public void setStructureFileName(String var1) {
      this.structureFileName = var1;
   }

   public String getAdHocFilter() {
      return this.adHocFilter;
   }

   public void setAdHocFilter(String var1) {
      this.adHocFilter = var1;
   }

   public int getFetchSize() {
      return this.fetchSize;
   }

   public void setFetchSize(int var1) {
      this.fetchSize = var1;
   }

   public WsKeyValue[] getKeyValues() {
      return this.keyValues;
   }

   public void setKeyValues(WsKeyValue[] var1) {
      this.keyValues = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsReadSourceParams)) {
         return false;
      }

      WsReadSourceParams var2 = (WsReadSourceParams)var1;
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
      boolean var3 = (this.sourceGuid == null && var2.getSourceGuid() == null || this.sourceGuid != null && this.sourceGuid.equals(var2.getSourceGuid()))
         && (
            this.connectionParams == null && var2.getConnectionParams() == null
               || this.connectionParams != null && this.connectionParams.equals(var2.getConnectionParams())
         )
         && (this.jtrFileName == null && var2.getJtrFileName() == null || this.jtrFileName != null && this.jtrFileName.equals(var2.getJtrFileName()))
         && (this.dataFileName == null && var2.getDataFileName() == null || this.dataFileName != null && this.dataFileName.equals(var2.getDataFileName()))
         && (
            this.structureFileName == null && var2.getStructureFileName() == null
               || this.structureFileName != null && this.structureFileName.equals(var2.getStructureFileName())
         )
         && (this.adHocFilter == null && var2.getAdHocFilter() == null || this.adHocFilter != null && this.adHocFilter.equals(var2.getAdHocFilter()))
         && this.fetchSize == var2.getFetchSize()
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
      if (this.getSourceGuid() != null) {
         var1 += this.getSourceGuid().hashCode();
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

      if (this.getStructureFileName() != null) {
         var1 += this.getStructureFileName().hashCode();
      }

      if (this.getAdHocFilter() != null) {
         var1 += this.getAdHocFilter().hashCode();
      }

      var1 += this.getFetchSize();
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
      typeDesc.setXmlType(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsReadSourceParams"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("sourceGuid");
      var0.setXmlName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "sourceGuid"));
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
      var0.setFieldName("structureFileName");
      var0.setXmlName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "structureFileName"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("adHocFilter");
      var0.setXmlName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "adHocFilter"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("fetchSize");
      var0.setXmlName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "fetchSize"));
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
