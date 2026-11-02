package org.etlforge.integration.debug.client;

import java.util.Collection;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.debug.client.de.DataElement;

public interface TestResult {
   DebugSession getSession();

   IntegrationEntityId getDebuggeeId();

   IntegrationEntityId getIdOfEntityToDisplay();

   boolean isDone();

   Collection<DataElement> getDataElements();

   BreakLocation getCurrentLocation();

   BreakLocation[] getCallStack();

   String getResult();

   String getWarnings();

   String getErrors();
}
