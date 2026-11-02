package org.etlforge.integration.client.ui.interchange.entity.transformation.data.db;

import java.io.IOException;
import org.etlforge.integration.client.server.ETLForgeServerEncoding;
import org.etlforge.integration.loadsource.serverapi.LoadSourceDataInput;
import org.etlforge.integration.loadsource.serverapi.SourceDataEncodingException;
import org.etlforge.integration.plugin.pipeline.PipelinePosition;

public final class LoadDatabaseSourceDataInput implements LoadSourceDataInput {
   private final String whereClause;

   public LoadDatabaseSourceDataInput(String var1) {
      this.whereClause = var1;
   }

   @Override
   public String getCompressedBase64EncodedData() throws SourceDataEncodingException {
      if (this.whereClause == null) {
         return null;
      }

      try {
         return ETLForgeServerEncoding.deflateAndBase64EncodeString(this.whereClause);
      } catch (IOException var2) {
         throw new SourceDataEncodingException("Failed to encode the WHERE clause. Reason: " + var2.getMessage(), var2);
      }
   }

   @Override
   public PipelinePosition[] getPlugins() {
      return new PipelinePosition[0];
   }
}
