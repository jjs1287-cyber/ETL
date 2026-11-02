package com.etlforge.integration.client.sfdc.data;

import com.etlforge.integration.salesforce.wsdl.SalesforceWsdlInfo;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.id.SalesforceConnectorId;
import org.etlforge.integration.data.entity.sfdc.ScriptModificationWarning;
import org.etlforge.integration.data.script.ScriptConstants;
import org.etlforge.integration.dataelement.GlobalDE;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.config.ConstantHeaderMapping;
import org.etlforge.integration.salesforce.config.SfdcMethodConfig;
import org.etlforge.integration.salesforce.soap.DefaultSoapHeaderLookup;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;
import org.etlforge.integration.structure.mapping.MapStringFormat;

public abstract class AbstractMapper {
   private List<String> mapList;
   private boolean noSourceDataUsed;
   private SalesforceWsdlInfo wsdlInfo;
   private Set<String> headersDefinedInWsdl;

   @Deprecated
   public final void setWsdlInfo(SalesforceWsdlInfo var1) {
      this.wsdlInfo = var1;
   }

   protected final void setNoSourceDataUsed(boolean var1) {
      this.noSourceDataUsed = var1;
   }

   protected final void setMapList(List<String> var1) {
      this.mapList = var1;
   }

   protected final void apply(Transformation var1) {
      var1.setMapList(this.mapList);
      var1.setNoSourceDataUsedInMapping(this.noSourceDataUsed);
   }

   protected final void insertRequestGeneratorMappings() {
      String var1 = MapStringFormat.format("[root$]", "[]");
      this.mapList.add(var1);
      var1 = MapStringFormat.format("[root$transaction.]", "[]");
      this.mapList.add(var1);
   }

   protected final void insertExpression(String var1, String var2) {
      String var3 = ScriptModificationWarning.forMapping() + var2;
      String var4 = MapStringFormat.format(var1, ScriptConstants.wrapInTags(var3));
      this.mapList.add(var4);
   }

   protected final void insertMapping(String var1, String var2) {
      String var3 = this.getMapping(var1, var2);
      this.mapList.add(var3);
   }

   private String getMapping(String var1, String var2) {
      String var3 = ScriptModificationWarning.forMapping();
      if (GlobalDE.isGlobalDE(var2)) {
         var3 = var3 + var2;
      } else {
         var3 = var3 + "\"" + this.escapeDoubleQuotes(var2) + "\"";
      }

      var3 = ScriptConstants.wrapInTags(var3);
      return MapStringFormat.format(var1, var3);
   }

   private String escapeDoubleQuotes(String var1) {
      return var1.replace("\"", "\\\"");
   }

   protected final void insertMapping(String var1, int var2) {
      String var3 = ScriptModificationWarning.forMapping() + Integer.toString(var2);
      var3 = ScriptConstants.wrapInTags(var3);
      String var4 = MapStringFormat.format(var1, var3);
      this.mapList.add(var4);
   }

   protected final void insertMapping(String var1, boolean var2) {
      String var3 = ScriptModificationWarning.forMapping() + (var2 ? "true" : "false");
      var3 = ScriptConstants.wrapInTags(var3);
      String var4 = MapStringFormat.format(var1, var3);
      this.mapList.add(var4);
   }

   @Deprecated
   protected final void insertConstantRequestHeaderMappings(SfdcMethodConfig var1) {
      this.headersDefinedInWsdl = this.getMethodRequestHeadersFromWsdl(var1);
      this.mapSessionId();
      this.mapMruHeader(var1);
      this.insertConstantHeaders(var1);
   }

   private void mapSessionId() {
      this.insertMapping("root$transaction.header$SessionHeader$sessionId$", GlobalDE.forScript("salesforce.sessionID"));
   }

   private void mapMruHeader(SfdcMethodConfig var1) {
      if (this.doesHeaderExist("root$transaction.header$MruHeader$updateMru$")) {
         this.insertMapping("root$transaction.header$MruHeader$updateMru$", false);
      }
   }

   private void insertConstantHeaders(SfdcMethodConfig var1) {
      for (ConstantHeaderMapping var3 : var1.getConstantHeaderMappings()) {
         if (this.doesHeaderExist(var3)) {
            this.insertMapping(var3);
         }
      }
   }

   private Set<String> getMethodRequestHeadersFromWsdl(SfdcMethodConfig var1) {
      return this.wsdlInfo == null ? null : this.wsdlInfo.getRequestHeaderElements(var1.getMethodName());
   }

   private boolean doesHeaderExist(ConstantHeaderMapping var1) {
      if (this.headersDefinedInWsdl == null) {
         return true;
      }

      String var2 = var1.getTargetPath();
      return this.doesHeaderExist(var2);
   }

   private boolean doesHeaderExist(String var1) {
      if (this.headersDefinedInWsdl == null) {
         return true;
      }

      var1 = StringUtils.removeStart(var1, "root$transaction.header$");
      String[] var2 = var1.split("\\.|\\$", 2);
      String var3 = var2[0];
      return this.headersDefinedInWsdl.contains(var3);
   }

   private void insertMapping(ConstantHeaderMapping var1) {
      Object var2 = var1.getValue();
      if (var2 instanceof String) {
         this.insertMapping(var1.getTargetPath(), (String)var2);
      } else if (var2 instanceof Boolean) {
         this.insertMapping(var1.getTargetPath(), (Boolean)var2);
      } else {
         if (!(var2 instanceof Integer)) {
            throw new RuntimeException(
               "I do not know how to insert a mapping with the value " + var2 + (var2 == null ? "" : " [of type " + var2.getClass().getName() + "]")
            );
         }

         this.insertMapping(var1.getTargetPath(), (Integer)var2);
      }
   }

   protected final void mapRetryPolicy(RetryPolicy var1) {
      RetryPolicyMapper var2 = new RetryPolicyMapper(var1);
      var2.applyRetryPolicy(this.mapList);
   }

   protected final void insertHeaderMappings(SfdcMethod var1, SalesforceConnector var2, SoapHeader... var3) {
      SalesforceConnectorId var4 = var2 == null ? null : var2.getID();

      for (SoapHeader var7 : DefaultSoapHeaderLookup.getDefaultHeadersWithOverrides(var1, var4, var3)) {
         this.mapList.addAll(var7.getMapList());
      }
   }
}
