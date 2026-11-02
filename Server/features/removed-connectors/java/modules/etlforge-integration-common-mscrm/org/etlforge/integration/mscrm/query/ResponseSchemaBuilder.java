package org.etlforge.integration.mscrm.query;

import java.io.IOException;
import org.etlforge.integration.etlforgeconnector.SchemaData;
import org.etlforge.integration.mscrm.metadata.MSCrmMetaDataException;
import org.etlforge.integration.mscrm.metadata.MSCrmObjectLookup;
import org.etlforge.util.zip.CompressedAndBase64EncodedString;

public final class ResponseSchemaBuilder {
   public static SchemaData buildSchema(String var0, MSCrmObjectLookup var1) throws MSCrmMetaDataException, IOException {
      ResponseStructureParser var2 = new ResponseStructureParser(var1);
      ResponseStructure var3 = var2.parse(var0);
      String var4 = "retrieveResponses";
      String var5 = var3.toXmlSchema(var4);
      CompressedAndBase64EncodedString var6 = CompressedAndBase64EncodedString.compress(var5);
      return new SchemaData(var4, var6);
   }

   private ResponseSchemaBuilder() {
   }
}
