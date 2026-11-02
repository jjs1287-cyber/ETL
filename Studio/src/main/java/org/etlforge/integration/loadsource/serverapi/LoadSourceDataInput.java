package org.etlforge.integration.loadsource.serverapi;

import org.etlforge.integration.plugin.pipeline.PipelinePosition;

public interface LoadSourceDataInput {
   String getCompressedBase64EncodedData() throws SourceDataEncodingException;

   PipelinePosition[] getPlugins();
}
