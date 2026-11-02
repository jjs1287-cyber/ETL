package org.etlforge.integration.client.ui.interchange.entity.transformation;

import org.etlforge.integration.debug.client.BreakLocation;
import org.etlforge.integration.debug.client.BreakLocationDecorator;
import org.etlforge.integration.debug.client.TestResult;
import org.etlforge.integration.debug.client.TestResultDecorator;

final class MissingTargetNodePathResult extends TestResultDecorator {
   private final String targetPath;

   public MissingTargetNodePathResult(TestResult var1, String var2) {
      super(var1);
      this.targetPath = var2;
   }

   @Override
   public BreakLocation getCurrentLocation() {
      BreakLocation var1 = super.getCurrentLocation();
      return new BreakLocationDecorator(var1) {
         @Override
         public String getTargetNodePath() {
            return MissingTargetNodePathResult.this.targetPath;
         }
      };
   }
}
