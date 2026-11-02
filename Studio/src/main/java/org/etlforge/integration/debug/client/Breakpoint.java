package org.etlforge.integration.debug.client;

import java.util.Map;

public interface Breakpoint {
   BreakLocation getLocation();

   String getConditionScript();

   Map<String, String> getConditionVariables();
}
