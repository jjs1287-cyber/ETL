package com.etlforge.integration.client.ui.sfdc.delete.wizard;

import com.etlforge.integration.client.sfdc.data.SalesforceModifierResponseMapper;
import org.etlforge.integration.salesforce.SfdcMethod;

final class DeleteResponseMapper extends SalesforceModifierResponseMapper {
   public DeleteResponseMapper() {
      super(SfdcMethod.DELETE);
   }

   @Override
   protected void appendErrorMessage(StringBuilder var1) {
      String var2 = this.getPathToRequestId();
      var1.append("\"The following record failed to process. ID = ")
         .append(" \" +\n\tFindByPos(SourceInstanceCount(), ")
         .append(var2)
         .append(") +\n\t\" The issue was: \" +\n\t");
   }

   private String getPathToRequestId() {
      String var1 = "root$transaction.request$body$delete$ids.";
      if (var1.endsWith(".") && !var1.endsWith("#.")) {
         var1 = var1.substring(0, var1.length() - 1);
         var1 = var1 + "#.";
      }

      return var1;
   }
}
