package org.etlforge.integration.client.plugin.pipeline;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import org.etlforge.integration.data.entity.IntegrationEntity;

public interface MutableAppliedPipelinePluginStore extends AppliedPipelinePluginStore {
   void setAssociations(IntegrationEntity var1, Iterable<AppliedPipelinePlugin> var2);

   boolean removeAll(IntegrationEntity var1);

   void copy(IntegrationEntity var1, IntegrationEntity var2);

   void persist(Writer var1) throws IOException;

   void load(Reader var1) throws IOException;
}
