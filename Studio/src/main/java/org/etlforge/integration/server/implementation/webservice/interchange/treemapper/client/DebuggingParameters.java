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

public class DebuggingParameters implements Serializable {
   private boolean observeBreakpoints;
   private boolean breakOnWarning;
   private boolean breakOnError;
   private String command;
   private String debuggingSessionId;
   private WsDataElement[] globalVariables;
   private Breakpoint[] breakpoints;
   private WsKeyValuePairs generalKeyValues;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(DebuggingParameters.class, true);

   public DebuggingParameters() {
   }

   public DebuggingParameters(boolean var1, boolean var2, boolean var3, String var4, String var5, WsDataElement[] var6, Breakpoint[] var7, WsKeyValuePairs var8) {
      this.observeBreakpoints = var1;
      this.breakOnWarning = var2;
      this.breakOnError = var3;
      this.command = var4;
      this.debuggingSessionId = var5;
      this.globalVariables = var6;
      this.breakpoints = var7;
      this.generalKeyValues = var8;
   }

   public boolean isObserveBreakpoints() {
      return this.observeBreakpoints;
   }

   public void setObserveBreakpoints(boolean var1) {
      this.observeBreakpoints = var1;
   }

   public boolean isBreakOnWarning() {
      return this.breakOnWarning;
   }

   public void setBreakOnWarning(boolean var1) {
      this.breakOnWarning = var1;
   }

   public boolean isBreakOnError() {
      return this.breakOnError;
   }

   public void setBreakOnError(boolean var1) {
      this.breakOnError = var1;
   }

   public String getCommand() {
      return this.command;
   }

   public void setCommand(String var1) {
      this.command = var1;
   }

   public String getDebuggingSessionId() {
      return this.debuggingSessionId;
   }

   public void setDebuggingSessionId(String var1) {
      this.debuggingSessionId = var1;
   }

   public WsDataElement[] getGlobalVariables() {
      return this.globalVariables;
   }

   public void setGlobalVariables(WsDataElement[] var1) {
      this.globalVariables = var1;
   }

   public Breakpoint[] getBreakpoints() {
      return this.breakpoints;
   }

   public void setBreakpoints(Breakpoint[] var1) {
      this.breakpoints = var1;
   }

   public WsKeyValuePairs getGeneralKeyValues() {
      return this.generalKeyValues;
   }

   public void setGeneralKeyValues(WsKeyValuePairs var1) {
      this.generalKeyValues = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof DebuggingParameters)) {
         return false;
      }

      DebuggingParameters var2 = (DebuggingParameters)var1;
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
      boolean var3 = this.observeBreakpoints == var2.isObserveBreakpoints()
         && this.breakOnWarning == var2.isBreakOnWarning()
         && this.breakOnError == var2.isBreakOnError()
         && (this.command == null && var2.getCommand() == null || this.command != null && this.command.equals(var2.getCommand()))
         && (
            this.debuggingSessionId == null && var2.getDebuggingSessionId() == null
               || this.debuggingSessionId != null && this.debuggingSessionId.equals(var2.getDebuggingSessionId())
         )
         && (
            this.globalVariables == null && var2.getGlobalVariables() == null
               || this.globalVariables != null && Arrays.equals(this.globalVariables, var2.getGlobalVariables())
         )
         && (this.breakpoints == null && var2.getBreakpoints() == null || this.breakpoints != null && Arrays.equals(this.breakpoints, var2.getBreakpoints()))
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
      var1 += (this.isObserveBreakpoints() ? Boolean.TRUE : Boolean.FALSE).hashCode();
      var1 += (this.isBreakOnWarning() ? Boolean.TRUE : Boolean.FALSE).hashCode();
      var1 += (this.isBreakOnError() ? Boolean.TRUE : Boolean.FALSE).hashCode();
      if (this.getCommand() != null) {
         var1 += this.getCommand().hashCode();
      }

      if (this.getDebuggingSessionId() != null) {
         var1 += this.getDebuggingSessionId().hashCode();
      }

      if (this.getGlobalVariables() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getGlobalVariables()); var2++) {
            Object var3 = Array.get(this.getGlobalVariables(), var2);
            if (var3 != null && !var3.getClass().isArray()) {
               var1 += var3.hashCode();
            }
         }
      }

      if (this.getBreakpoints() != null) {
         for (int var7 = 0; var7 < Array.getLength(this.getBreakpoints()); var7++) {
            Object var8 = Array.get(this.getBreakpoints(), var7);
            if (var8 != null && !var8.getClass().isArray()) {
               var1 += var8.hashCode();
            }
         }
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "DebuggingParameters")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("observeBreakpoints");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "ObserveBreakpoints")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("breakOnWarning");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "BreakOnWarning")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("breakOnError");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "BreakOnError"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("command");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "Command"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("debuggingSessionId");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "DebuggingSessionId")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("globalVariables");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "GlobalVariables")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "WsDataElement")
      );
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("breakpoints");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "Breakpoints"));
      var0.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "Breakpoint"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "item"));
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
