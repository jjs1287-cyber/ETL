package org.etlforge.integration.data.test;

import java.util.Date;
import java.util.EnumSet;

public interface DetailedTestResultFilter extends TestResultFilter {
   boolean acceptsSubjectId(Object var1);

   EnumSet<TestResult.Outcome> acceptedOutcomes();

   Date startDate();

   Date endDate();

   boolean acceptsDetails(String var1);
}
