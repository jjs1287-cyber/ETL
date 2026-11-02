package org.etlforge.integration.data.test;

import org.etlforge.util.persist.Persistable;

public interface TestResult extends Persistable {
   TestResult.Outcome SUCCESS = TestResult.Outcome.SUCCESS;
   TestResult.Outcome PARTIAL = TestResult.Outcome.PARTIAL;
   TestResult.Outcome ERROR = TestResult.Outcome.ERROR;

   Object getSubjectId();

   long getTimestamp();

   TestResult.Outcome getOutcome();

   String getDetails();

   enum Outcome {
      SUCCESS,
      PARTIAL,
      ERROR;
   }
}
