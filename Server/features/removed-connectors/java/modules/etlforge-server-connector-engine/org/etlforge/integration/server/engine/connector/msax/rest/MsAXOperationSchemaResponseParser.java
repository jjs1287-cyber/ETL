package org.etlforge.integration.server.engine.connector.msax.rest;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.HashMap;
import org.etlforge.integration.server.engine.connector.ConnectorExecutionSystem;
import org.etlforge.integration.server.engine.connector.exception.ConnectorIllegalResponseRuntimeException;
import org.etlforge.integration.server.engine.connector.exception.ConnectorResponseProcessRuntimeException;
import org.etlforge.integration.server.engine.connector.framework.ConnectorXsdSchemaFiles;
import org.etlforge.integration.server.engine.connector.msax.adapter.MsAXSchemaset;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.RelativePathRoot;
import org.xml.sax.SAXException;

public class MsAXOperationSchemaResponseParser {
   public static ConnectorXsdSchemaFiles parseOperationSchemaResponse(String var0) {
      KongaSaxParser var1 = new KongaSaxParser();
      MsAXOperationSchemaResponseParser.MsAXOperationSchemaResponseHandler var4 = new MsAXOperationSchemaResponseParser.MsAXOperationSchemaResponseHandler(
         "operationSchemasResponses/operationSchemasResponse/request/parameter"
      );
      MsAXOperationSchemaResponseParser.MsAXOperationSchemaResponseHandler var5 = new MsAXOperationSchemaResponseParser.MsAXOperationSchemaResponseHandler(
         "operationSchemasResponses/operationSchemasResponse/response"
      );
      var1.registerHandler(var4);
      var1.registerHandler(var5);

      try {
         var1.parseString(var0);
         return combine(var4.schemaset, var5.schemaset);
      } catch (SAXException var7) {
         throw new ConnectorResponseProcessRuntimeException(var7);
      }
   }

   private static String expandName(String var0, String var1) {
      return var0.isEmpty() ? var1 : '{' + var0 + '}' + var1;
   }

   private static ConnectorXsdSchemaFiles combine(MsAXSchemaset var0, MsAXSchemaset var1) {
      String var2 = var0.getRootFilename();
      String var3 = expandName(var0.getRootNamespace(), var0.getRootName());
      String var4 = var1.getRootFilename();
      String var5 = expandName(var1.getRootNamespace(), var1.getRootName());
      HashMap var6 = Maps.newHashMap();
      var6.putAll(var0.getSchemas());
      var6.putAll(var1.getSchemas());
      return new ConnectorXsdSchemaFiles(var2, var3, var4, var5, var6);
   }

   public static void main(String[] var0) throws IOException {
      String var1 = "Z:/etlforge/branches/v5/5.1.x/dev/ETLForgeConnectorProject/MSAX/Request&Response Samples/test.xml";
      String var2 = ConnectorExecutionSystem.readFileUTF8(var1);
      parseOperationSchemaResponse(var2);
   }

   private static class MsAXOperationSchemaResponseHandler {
      @RelativePathRoot
      public final String relativePathRoot;
      final MsAXSchemaset schemaset;
      private String filename = "";

      MsAXOperationSchemaResponseHandler(String var1) {
         this.relativePathRoot = var1;
         this.schemaset = new MsAXSchemaset();
      }

      @EndElementHandler("rootelement")
      public void rootelement(String var1) {
         this.schemaset.setRootName(var1.trim());
      }

      @EndElementHandler("rootnamespace")
      public void rootnamespace(String var1) {
         this.schemaset.setRootNamespace(var1.trim());
      }

      @EndElementHandler("schemaset/schema/name")
      public void schemaFileName(String var1) {
         this.filename = var1.trim();
      }

      @EndElementHandler("schemaset/schema/xsd")
      public void schemaContent(String var1) {
         if (this.filename.isEmpty()) {
            throw new ConnectorIllegalResponseRuntimeException("Schema file name can not be empty!");
         }

         this.schemaset.addSchemaContent(this.filename, var1.trim());
         this.filename = "";
      }
   }
}
