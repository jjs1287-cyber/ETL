package com.etlforge.integration.client.sfdc.data;

import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import org.etlforge.integration.data.entity.SystemTransformation;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.script.ScriptConstants;
import org.etlforge.integration.data.structure.WebServiceRequestStructure;
import org.etlforge.integration.structure.mapping.MapStringFormat;
import org.etlforge.integration.structure.mapping.Mapping;

public final class RetryPolicyMapper {
   private final RetryPolicy policy;
   private Logger logger = Logger.getAnonymousLogger();

   public static RetryPolicy fromTransformation(Transformation var0) {
      if (var0 == SystemTransformation.SALESFORCE_QUERY_REQUEST) {
         return RetryPolicy.DATA_LOADER;
      }

      Preconditions.checkArgument(var0.getOutputStructure() instanceof WebServiceRequestStructure, "tx must be a web service request");

      for (String var2 : var0.getMapList()) {
         Mapping var3 = MapStringFormat.parse(var2);
         if ("root$transaction./PRECONDITION/".equals(var3.getTargetPath().toString())) {
            RetryPolicyExpressionParser var4 = new RetryPolicyExpressionParser(var3.getExpression());
            return var4.parse();
         }
      }

      return RetryPolicy.NONE;
   }

   public RetryPolicyMapper(RetryPolicy var1) {
      this.policy = var1;
   }

   public void setLogger(Logger var1) {
      this.logger = Preconditions.checkNotNull(var1);
   }

   public void applyRetryPolicy(List<String> var1) {
      String var2 = this.removeExistingCondition(var1);
      String var3 = this.getExpression(var2);
      this.applyNewExpression(var3, var1);
   }

   private String removeExistingCondition(List<String> var1) {
      Iterator var2 = var1.iterator();

      while (var2.hasNext()) {
         String var3 = (String)var2.next();
         Mapping var4 = MapStringFormat.parse(var3);
         if (var4.getTargetPath().toString().equals("root$transaction./PRECONDITION/")) {
            this.logger.fine("Removing existing mapping: " + var3);
            var2.remove();
            return var4.getExpression();
         }
      }

      return null;
   }

   private String getExpression(String var1) {
      if (var1 == null) {
         if (this.policy == RetryPolicy.NONE) {
            return null;
         }

         String var3 = RetryPolicyExpression.getVariableAssignment(this.policy);
         return ScriptConstants.wrapInTags(var3 + "\ntrue");
      } else {
         RetryPolicyExpressionParser var2 = new RetryPolicyExpressionParser(var1);
         return var2.replace(this.policy);
      }
   }

   private void applyNewExpression(String var1, List<String> var2) {
      if (var1 != null) {
         String var3 = MapStringFormat.format("root$transaction./PRECONDITION/", var1);
         this.logger.info("Adding new mapping: " + var3);
         var2.add(var3);
      } else {
         this.logger.info("No RetryPolicy --> No mapping to add.");
      }
   }
}
