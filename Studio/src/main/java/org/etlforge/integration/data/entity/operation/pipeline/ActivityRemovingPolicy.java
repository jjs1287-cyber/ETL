package org.etlforge.integration.data.entity.operation.pipeline;

public interface ActivityRemovingPolicy {
   void checkRemovalAllowed(PipelineActivity var1) throws RemoveActivityVetoException;
}
