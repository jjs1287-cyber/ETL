package com.etlforge.integration.client.ui.script.fcn.runop;

import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.script.Function;
import org.etlforge.integration.data.script.ScriptConstants;

final class ExpressionBuilder {
   private final Function runOpFunction;
   private final RunOperationPageChain pages;

   public ExpressionBuilder(Function var1, RunOperationPageChain var2) {
      this.runOpFunction = var1;
      this.pages = var2;
   }

   public String buildExpression() {
      StringBuilder var1 = new StringBuilder();
      if (this.handleError()) {
         this.prepareErrorHandling(var1);
      }

      this.callRunOperation(var1);
      if (this.handleError()) {
         this.implementErrorHandling(var1);
      }

      return var1.toString();
   }

   private void prepareErrorHandling(StringBuilder var1) {
      var1.append("If(!");
   }

   private void callRunOperation(StringBuilder var1) {
      var1.append(this.runOpFunction.getName()).append("(");
      Operation var2 = this.pages.getSelectedOperation();
      var1.append(ScriptConstants.getProjectItemTagString(var2));
      if (!this.pages.isSynchronous()) {
         var1.append(", false");
      }

      var1.append(")");
   }

   private void implementErrorHandling(StringBuilder var1) {
      var1.append(",\n");
      if (this.pages.cancelChainOnFailure()) {
         var1.append("\tCancelOperationChain(GetLastError())");
         if (this.pages.raiseErrorOnFailure()) {
            var1.append(";");
         }

         var1.append("\n");
      }

      if (this.pages.raiseErrorOnFailure()) {
         var1.append("\tRaiseError(GetLastError())\n");
      }

      var1.append(")");
   }

   private boolean handleError() {
      return this.pages.raiseErrorOnFailure() || this.pages.cancelChainOnFailure();
   }
}
