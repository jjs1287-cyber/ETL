package org.etlforge.integration.salesforce.parser;

public interface SoqlParser {
   String AGGREGATE_RESULT = "AggregateResult";

   ParsedQuery parse(String var1);

   String extractObjectName(String var1);
}
