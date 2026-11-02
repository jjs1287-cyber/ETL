package org.etlforge.integration.data.test;

import java.util.Date;
import java.util.EnumSet;

public abstract class AbstractDetailedTestResultFilter implements DetailedTestResultFilter {
   @Override
   public boolean acceptsSubjectId(Object var1) {
      return true;
   }

   @Override
   public EnumSet<TestResult.Outcome> acceptedOutcomes() {
      return EnumSet.allOf(TestResult.Outcome.class);
   }

   @Override
   public Date endDate() {
      return null;
   }

   @Override
   public Date startDate() {
      return null;
   }

   @Override
   public boolean acceptsDetails(String var1) {
      return true;
   }

   @Override
   public boolean accepts(TestResult var1) {
      return this.checkOutcome(var1) && this.checkTimestamp(var1) && this.acceptsDetails(var1.getDetails());
   }

   private boolean checkOutcome(TestResult var1) {
      return this.acceptedOutcomes().contains(var1.getOutcome());
   }

   private boolean checkTimestamp(TestResult var1) {
      return TestResultUtils.isValidDate(new Date(var1.getTimestamp()), this);
   }
}
