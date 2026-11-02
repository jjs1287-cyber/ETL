package org.etlforge.integration.loadsource.client;

import org.etlforge.integration.loadsource.serverapi.LoadSourceDataInput;
import org.etlforge.integration.plugin.pipeline.PipelinePosition;

public final class LoadLdapSourceDataInput implements LoadSourceDataInput {
   @Override
   public String getCompressedBase64EncodedData() {
      return null;
   }

   @Override
   public PipelinePosition[] getPlugins() {
      return new PipelinePosition[0];
   }
}
