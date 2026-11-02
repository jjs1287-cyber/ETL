package org.etlforge.integration.data.validation;

public interface ValidationMessageCollector {
   ValidationContext context();

   void addMessage(String var1, ValidationStatus var2);
}
