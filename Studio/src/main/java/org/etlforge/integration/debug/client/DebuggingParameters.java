package org.etlforge.integration.debug.client;

import org.etlforge.integration.debug.client.de.DataElement;
import org.etlforge.util.id.KongaGuid;

public interface DebuggingParameters {
   KongaGuid getSessionId();

   String getCommand();

   DataElement[] getDataElements();

   DataElement[] getGlobalDataElements();

   Breakpoint[] getBreakpoints();

   boolean isObservingBreakpoints();

   boolean isBreakingOnWarnings();

   boolean isBreakingOnErrors();
}
