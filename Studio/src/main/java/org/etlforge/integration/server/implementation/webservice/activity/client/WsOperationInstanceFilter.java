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

public class WsOperationInstanceFilter implements Serializable {
   private long[] operationInstances;
   private String[] guids;
   private int[] guidTypes;
   private int[] logStatuses;
   private String startDate;
   private String endDate;
   private String searchString;
   private String operationName;
   private int maxRecords;
   private int sortType;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsOperationInstanceFilter.class, true);

   public WsOperationInstanceFilter() {
   }

   public WsOperationInstanceFilter(long[] var1, String[] var2, int[] var3, int[] var4, String var5, String var6, String var7, String var8, int var9, int var10) {
      this.operationInstances = var1;
      this.guids = var2;
      this.guidTypes = var3;
      this.logStatuses = var4;
      this.startDate = var5;
      this.endDate = var6;
      this.searchString = var7;
      this.operationName = var8;
      this.maxRecords = var9;
      this.sortType = var10;
   }

   public long[] getOperationInstances() {
      return this.operationInstances;
   }

   public void setOperationInstances(long[] var1) {
      this.operationInstances = var1;
   }

   public String[] getGuids() {
      return this.guids;
   }

   public void setGuids(String[] var1) {
      this.guids = var1;
   }

   public int[] getGuidTypes() {
      return this.guidTypes;
   }

   public void setGuidTypes(int[] var1) {
      this.guidTypes = var1;
   }

   public int[] getLogStatuses() {
      return this.logStatuses;
   }

   public void setLogStatuses(int[] var1) {
      this.logStatuses = var1;
   }

   public String getStartDate() {
      return this.startDate;
   }

   public void setStartDate(String var1) {
      this.startDate = var1;
   }

   public String getEndDate() {
      return this.endDate;
   }

   public void setEndDate(String var1) {
      this.endDate = var1;
   }

   public String getSearchString() {
      return this.searchString;
   }

   public void setSearchString(String var1) {
      this.searchString = var1;
   }

   public String getOperationName() {
      return this.operationName;
   }

   public void setOperationName(String var1) {
      this.operationName = var1;
   }

   public int getMaxRecords() {
      return this.maxRecords;
   }

   public void setMaxRecords(int var1) {
      this.maxRecords = var1;
   }

   public int getSortType() {
      return this.sortType;
   }

   public void setSortType(int var1) {
      this.sortType = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsOperationInstanceFilter)) {
         return false;
      }

      WsOperationInstanceFilter var2 = (WsOperationInstanceFilter)var1;
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
            this.operationInstances == null && var2.getOperationInstances() == null
               || this.operationInstances != null && Arrays.equals(this.operationInstances, var2.getOperationInstances())
         )
         && (this.guids == null && var2.getGuids() == null || this.guids != null && Arrays.equals(this.guids, var2.getGuids()))
         && (this.guidTypes == null && var2.getGuidTypes() == null || this.guidTypes != null && Arrays.equals(this.guidTypes, var2.getGuidTypes()))
         && (this.logStatuses == null && var2.getLogStatuses() == null || this.logStatuses != null && Arrays.equals(this.logStatuses, var2.getLogStatuses()))
         && (this.startDate == null && var2.getStartDate() == null || this.startDate != null && this.startDate.equals(var2.getStartDate()))
         && (this.endDate == null && var2.getEndDate() == null || this.endDate != null && this.endDate.equals(var2.getEndDate()))
         && (this.searchString == null && var2.getSearchString() == null || this.searchString != null && this.searchString.equals(var2.getSearchString()))
         && (this.operationName == null && var2.getOperationName() == null || this.operationName != null && this.operationName.equals(var2.getOperationName()))
         && this.maxRecords == var2.getMaxRecords()
         && this.sortType == var2.getSortType();
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
      if (this.getOperationInstances() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getOperationInstances()); var2++) {
            Object var3 = Array.get(this.getOperationInstances(), var2);
            if (var3 != null && !var3.getClass().isArray()) {
               var1 += var3.hashCode();
            }
         }
      }

      if (this.getGuids() != null) {
         for (int var6 = 0; var6 < Array.getLength(this.getGuids()); var6++) {
            Object var9 = Array.get(this.getGuids(), var6);
            if (var9 != null && !var9.getClass().isArray()) {
               var1 += var9.hashCode();
            }
         }
      }

      if (this.getGuidTypes() != null) {
         for (int var7 = 0; var7 < Array.getLength(this.getGuidTypes()); var7++) {
            Object var10 = Array.get(this.getGuidTypes(), var7);
            if (var10 != null && !var10.getClass().isArray()) {
               var1 += var10.hashCode();
            }
         }
      }

      if (this.getLogStatuses() != null) {
         for (int var8 = 0; var8 < Array.getLength(this.getLogStatuses()); var8++) {
            Object var11 = Array.get(this.getLogStatuses(), var8);
            if (var11 != null && !var11.getClass().isArray()) {
               var1 += var11.hashCode();
            }
         }
      }

      if (this.getStartDate() != null) {
         var1 += this.getStartDate().hashCode();
      }

      if (this.getEndDate() != null) {
         var1 += this.getEndDate().hashCode();
      }

      if (this.getSearchString() != null) {
         var1 += this.getSearchString().hashCode();
      }

      if (this.getOperationName() != null) {
         var1 += this.getOperationName().hashCode();
      }

      var1 += this.getMaxRecords();
      var1 += this.getSortType();
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "WsOperationInstanceFilter")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("operationInstances");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "OperationInstances"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("guids");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "Guids"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("guidTypes");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "GuidTypes"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("logStatuses");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "LogStatuses"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("startDate");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "StartDate"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("endDate");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "EndDate"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("searchString");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "SearchString"));
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
      var0.setFieldName("maxRecords");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "MaxRecords"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("sortType");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.activity.ActivityInfoProvider", "SortType"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
   }
}
