package org.etlforge.integration.client.ui.interchange.entity.transformation;

import org.etlforge.integration.client.ui.interchange.debug.TestResultDisplayer;
import org.etlforge.integration.debug.client.BreakLocation;
import org.etlforge.integration.debug.client.DebugService;
import org.etlforge.integration.debug.client.DebugSession;
import org.etlforge.integration.debug.client.TargetNode;
import org.etlforge.integration.debug.client.TestResult;

public final class TransformationTestResultDisplayer implements TestResultDisplayer {
   private final DebugService debugService;
   private final TestResultDisplayer impl;

   public TransformationTestResultDisplayer(DebugService var1, TestResultDisplayer var2) {
      this.debugService = var1;
      this.impl = var2;
   }

   @Override
   public void showTestResult(TestResult var1) {
      if (!this.isCompletedTestTransformationOrOperation(var1)) {
         BreakLocation var2 = var1.getCurrentLocation();
         if (var2 == null) {
            var1 = new MissingTargetNodePathResult(var1, this.getTargetNodePath(var1));
         } else if (var2.getTargetNodePath() == null) {
            this.displayMissingNodePathMessage();
            return;
         }

         this.impl.showTestResult(var1);
      }
   }

   private String getTargetNodePath(TestResult var1) {
      DebugSession var2 = var1.getSession();
      TargetNode var3 = var2.getTargetNode();
      return var3 == null ? null : var3.getPath();
   }

   private boolean isCompletedTestTransformationOrOperation(TestResult var1) {
      return var1.isDone() ? this.getTargetNodePath(var1) == null : false;
   }

   private void displayMissingNodePathMessage() {
      MissingTargetNodePathMessage var1 = new MissingTargetNodePathMessage(this.debugService);
      var1.open();
   }
}
