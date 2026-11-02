package org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client;

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
import org.etlforge.integration.server.implementation.webservice.WsKeyValuePairs;

public class WsTestOperationResult implements Serializable {
   private String debuggingSessionId;
   private WsDataElement[] dataElements;
   private BreakLocation[] callStack;
   private String warnings;
   private String errors;
   private String intermediateResult;
   private WsDataFilesInfo dataFileInfo;
   private WsKeyValuePairs generalKeyValues;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsTestOperationResult.class, true);

   public WsTestOperationResult() {
   }

   public WsTestOperationResult(
      String var1, WsDataElement[] var2, BreakLocation[] var3, String var4, String var5, String var6, WsDataFilesInfo var7, WsKeyValuePairs var8
   ) {
      this.debuggingSessionId = var1;
      this.dataElements = var2;
      this.callStack = var3;
      this.warnings = var4;
      this.errors = var5;
      this.intermediateResult = var6;
      this.dataFileInfo = var7;
      this.generalKeyValues = var8;
   }

   public String getDebuggingSessionId() {
      return this.debuggingSessionId;
   }

   public void setDebuggingSessionId(String var1) {
      this.debuggingSessionId = var1;
   }

   public WsDataElement[] getDataElements() {
      return this.dataElements;
   }

   public void setDataElements(WsDataElement[] var1) {
      this.dataElements = var1;
   }

   public BreakLocation[] getCallStack() {
      return this.callStack;
   }

   public void setCallStack(BreakLocation[] var1) {
      this.callStack = var1;
   }

   public String getWarnings() {
      return this.warnings;
   }

   public void setWarnings(String var1) {
      this.warnings = var1;
   }

   public String getErrors() {
      return this.errors;
   }

   public void setErrors(String var1) {
      this.errors = var1;
   }

   public String getIntermediateResult() {
      return this.intermediateResult;
   }

   public void setIntermediateResult(String var1) {
      this.intermediateResult = var1;
   }

   public WsDataFilesInfo getDataFileInfo() {
      return this.dataFileInfo;
   }

   public void setDataFileInfo(WsDataFilesInfo var1) {
      this.dataFileInfo = var1;
   }

   public WsKeyValuePairs getGeneralKeyValues() {
      return this.generalKeyValues;
   }

   public void setGeneralKeyValues(WsKeyValuePairs var1) {
      this.generalKeyValues = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsTestOperationResult)) {
         return false;
      }

      WsTestOperationResult var2 = (WsTestOperationResult)var1;
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
            this.debuggingSessionId == null && var2.getDebuggingSessionId() == null
               || this.debuggingSessionId != null && this.debuggingSessionId.equals(var2.getDebuggingSessionId())
         )
         && (
            this.dataElements == null && var2.getDataElements() == null
               || this.dataElements != null && Arrays.equals(this.dataElements, var2.getDataElements())
         )
         && (this.callStack == null && var2.getCallStack() == null || this.callStack != null && Arrays.equals(this.callStack, var2.getCallStack()))
         && (this.warnings == null && var2.getWarnings() == null || this.warnings != null && this.warnings.equals(var2.getWarnings()))
         && (this.errors == null && var2.getErrors() == null || this.errors != null && this.errors.equals(var2.getErrors()))
         && (
            this.intermediateResult == null && var2.getIntermediateResult() == null
               || this.intermediateResult != null && this.intermediateResult.equals(var2.getIntermediateResult())
         )
         && (this.dataFileInfo == null && var2.getDataFileInfo() == null || this.dataFileInfo != null && this.dataFileInfo.equals(var2.getDataFileInfo()))
         && (
            this.generalKeyValues == null && var2.getGeneralKeyValues() == null
               || this.generalKeyValues != null && this.generalKeyValues.equals(var2.getGeneralKeyValues())
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
      if (this.getDebuggingSessionId() != null) {
         var1 += this.getDebuggingSessionId().hashCode();
      }

      if (this.getDataElements() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getDataElements()); var2++) {
            Object var3 = Array.get(this.getDataElements(), var2);
            if (var3 != null && !var3.getClass().isArray()) {
               var1 += var3.hashCode();
            }
         }
      }

      if (this.getCallStack() != null) {
         for (int var4 = 0; var4 < Array.getLength(this.getCallStack()); var4++) {
            Object var5 = Array.get(this.getCallStack(), var4);
            if (var5 != null && !var5.getClass().isArray()) {
               var1 += var5.hashCode();
            }
         }
      }

      if (this.getWarnings() != null) {
         var1 += this.getWarnings().hashCode();
      }

      if (this.getErrors() != null) {
         var1 += this.getErrors().hashCode();
      }

      if (this.getIntermediateResult() != null) {
         var1 += this.getIntermediateResult().hashCode();
      }

      if (this.getDataFileInfo() != null) {
         var1 += this.getDataFileInfo().hashCode();
      }

      if (this.getGeneralKeyValues() != null) {
         var1 += this.getGeneralKeyValues().hashCode();
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "WsTestOperationResult")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("debuggingSessionId");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "DebuggingSessionId")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("dataElements");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "DataElements"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "WsDataElement")
      );
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("callStack");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "CallStack"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "BreakLocation")
      );
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("warnings");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "Warnings"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("errors");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "Errors"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("intermediateResult");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "IntermediateResult")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("dataFileInfo");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "DataFileInfo"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "WsDataFilesInfo")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("generalKeyValues");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "GeneralKeyValues")
      );
      var0.setXmlType(new QName("http://webservice.implementation.server.integration.etlforge.invalid", "WsKeyValuePairs"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
