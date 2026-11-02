package com.etlforge.integration.client.sfdc.data;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.SimpleTextStructure;
import org.etlforge.integration.data.structure.text.Field;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.wsdl.SalesforceWsdlConstants;
import org.etlforge.integration.structure.mapping.MapStringFormat;

public abstract class SalesforceModifierResponseMapper extends AbstractMapper {
   private final SfdcMethod method;

   public SalesforceModifierResponseMapper(SfdcMethod var1) {
      this.method = var1;
   }

   public final void insertResponseMappings(Transformation var1) {
      ArrayList var2 = Lists.newArrayList();
      this.setMapList(var2);
      this.insertGeneratorMapping(var2);
      String var3 = this.buildExpression();
      synchronized (var1.getDataLock()) {
         String var5 = this.extractTargetFieldName(var1);
         this.insertExpression(var5, var3);
         this.apply(var1);
      }
   }

   private void insertGeneratorMapping(List<String> var1) {
      String var2 = MapStringFormat.format("[]", SalesforceWsdlConstants.getPathToGeneratorInUcuResponse(this.method));
      var1.add(var2);
   }

   private String buildExpression() {
      String var1 = SalesforceWsdlConstants.getPathToSuccessElement(this.method);
      String var2 = SalesforceWsdlConstants.getPathToErrorMessageElement(this.method);
      StringBuilder var3 = new StringBuilder("If(");
      var3.append(var1).append(" == false,\n\tWriteToOperationLog(");
      this.appendErrorMessage(var3);
      var3.append("SumString(").append(var2).append(", \". \", false)))");
      return var3.toString();
   }

   protected abstract void appendErrorMessage(StringBuilder var1);

   private String extractTargetFieldName(Transformation var1) {
      SimpleTextStructure var2 = (SimpleTextStructure)var1.getOutputStructure();
      Field var3 = var2.getFieldList().get(0);
      return var3.getName();
   }
}
