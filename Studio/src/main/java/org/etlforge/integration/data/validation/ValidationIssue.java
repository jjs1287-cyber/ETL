package org.etlforge.integration.data.validation;

public interface ValidationIssue {
   String getMessage();

   void report(ValidationMessageCollector var1);
}
