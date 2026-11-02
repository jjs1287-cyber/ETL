package org.etlforge.integration.data.test;

import java.util.EnumSet;

public final class TestResultOutcomeFilter implements TestResultFilter {
   private final EnumSet<TestResult.Outcome> accepted;

   public TestResultOutcomeFilter(TestResult.Outcome var1) {
      this(EnumSet.of(var1));
   }

   public TestResultOutcomeFilter(EnumSet<TestResult.Outcome> var1) {
      this.accepted = var1.clone();
   }

   @Override
   public boolean accepts(TestResult var1) {
      return this.accepted.contains(var1.getOutcome());
   }
}
