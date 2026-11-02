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

public class WsTestTransformationResult implements Serializable {
   private String debuggingSessionId;
   private String dataFileGuid;
   private WsDataElement[] dataElements;
   private BreakLocation[] callStack;
   private String warnings;
   private String errors;
   private String intermediateResult;
   private WsDataFile linkFile;
   private WsDataFile targetFile;
   private WsKeyValuePairs generalKeyValues;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsTestTransformationResult.class, true);

   public WsTestTransformationResult() {
   }

   public WsTestTransformationResult(
      String var1,
      String var2,
      WsDataElement[] var3,
      BreakLocation[] var4,
      String var5,
      String var6,
      String var7,
      WsDataFile var8,
      WsDataFile var9,
      WsKeyValuePairs var10
   ) {
      this.debuggingSessionId = var1;
      this.dataFileGuid = var2;
      this.dataElements = var3;
      this.callStack = var4;
      this.warnings = var5;
      this.errors = var6;
      this.intermediateResult = var7;
      this.linkFile = var8;
      this.targetFile = var9;
      this.generalKeyValues = var10;
   }

   public String getDebuggingSessionId() {
      return this.debuggingSessionId;
   }

   public void setDebuggingSessionId(String var1) {
      this.debuggingSessionId = var1;
   }

   public String getDataFileGuid() {
      return this.dataFileGuid;
   }

   public void setDataFileGuid(String var1) {
      this.dataFileGuid = var1;
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

   public WsDataFile getLinkFile() {
      return this.linkFile;
   }

   public void setLinkFile(WsDataFile var1) {
      this.linkFile = var1;
   }

   public WsDataFile getTargetFile() {
      return this.targetFile;
   }

   public void setTargetFile(WsDataFile var1) {
      this.targetFile = var1;
   }

   public WsKeyValuePairs getGeneralKeyValues() {
      return this.generalKeyValues;
   }

   public void setGeneralKeyValues(WsKeyValuePairs var1) {
      this.generalKeyValues = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsTestTransformationResult)) {
         return false;
      }

      WsTestTransformationResult var2 = (WsTestTransformationResult)var1;
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
         && (this.dataFileGuid == null && var2.getDataFileGuid() == null || this.dataFileGuid != null && this.dataFileGuid.equals(var2.getDataFileGuid()))
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
         && (this.linkFile == null && var2.getLinkFile() == null || this.linkFile != null && this.linkFile.equals(var2.getLinkFile()))
         && (this.targetFile == null && var2.getTargetFile() == null || this.targetFile != null && this.targetFile.equals(var2.getTargetFile()))
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

      if (this.getDataFileGuid() != null) {
         var1 += this.getDataFileGuid().hashCode();
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

      if (this.getLinkFile() != null) {
         var1 += this.getLinkFile().hashCode();
      }

      if (this.getTargetFile() != null) {
         var1 += this.getTargetFile().hashCode();
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "WsTestTransformationResult")
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
      var0.setFieldName("dataFileGuid");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "DataFileGuid"));
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
      var0.setFieldName("linkFile");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "LinkFile"));
      var0.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "WsDataFile"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("targetFile");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "TargetFile"));
      var0.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "WsDataFile"));
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
