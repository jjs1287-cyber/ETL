package org.etlforge.integration.data.entity.operation.flow;

import org.etlforge.integration.data.entity.IntegrationEntityId;

public interface OperationFlowActivity {
   IntegrationEntityId getID();

   String getName();
}
