package com.etlforge.integration.client.sfdc.data;

import org.etlforge.integration.data.entity.sfdc.ScriptModificationWarning;
import org.etlforge.integration.dataelement.GlobalDE;

public final class RetryPolicyExpression {
   public static String getVariableAssignment(RetryPolicy var0) {
      StringBuilder var1 = new StringBuilder(ScriptModificationWarning.forMapping());
      appendVariableAssignment(var1, "etlforge.web_service_call.number_of_retries", var0.numberOfRetries);
      var1.append("\n");
      appendVariableAssignment(var1, "etlforge.web_service_call.retry_wait_seconds", var0.retryIntervalInSeconds);
      return var1.toString();
   }

   private static void appendVariableAssignment(StringBuilder var0, String var1, int var2) {
      var0.append(GlobalDE.forScript(var1)).append(" = ").append(var2).append(";");
   }

   private RetryPolicyExpression() {
   }
}
