package org.etlforge.integration.client.connector.serverapi;

import com.google.common.base.Preconditions;
import java.util.zip.DataFormatException;
import org.etlforge.integration.etlforgeconnector.SchemaData;

public final class SingleSchemas implements Schemas {
   private final SchemaData request;
   private final SchemaData response;

   public SingleSchemas(SchemaData var1, SchemaData var2) {
      this.request = Preconditions.checkNotNull(var1, "request");
      this.response = Preconditions.checkNotNull(var2, "response");
   }

   @Override
   public String getRequestRootName() {
      return this.request.getRootName();
   }

   @Override
   public String getResponseRootName() {
      return this.response.getRootName();
   }

   public String getRequest() throws DataFormatException {
      return this.request.getContents().inflate();
   }

   public String getResponse() throws DataFormatException {
      return this.response.getContents().inflate();
   }
}
