package org.etlforge.integration.server.engine.connector.msgp.adapter;

import java.io.IOException;
import org.etlforge.integration.server.engine.connector.ConnectorExecutionSystem;
import org.etlforge.integration.server.engine.connector.framework.ConnectorXsdSchemaFiles;
import org.etlforge.integration.server.engine.connector.util.XmlUtils;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.RelativePathRoot;
import org.xml.sax.SAXException;

public class MsGPXsdSchemas {
   public ConnectorXsdSchemaFiles obtainSchemas(String var1) throws SAXException, IOException {
      MsGPXsdSchemas.GPXsdSchemaHanlder var2 = new MsGPXsdSchemas.GPXsdSchemaHanlder("specificSchemaResponses/specificSchemaResponse/schema/request/xsd");
      MsGPXsdSchemas.GPXsdSchemaHanlder var3 = new MsGPXsdSchemas.GPXsdSchemaHanlder("specificSchemaResponses/specificSchemaResponse/schema/response/xsd");
      KongaSaxParser var4 = new KongaSaxParser(var2, var3);
      var4.parseString(var1);
      return new ConnectorXsdSchemaFiles(var2.xsdSchema, var2.rootName, var3.xsdSchema, var3.rootName);
   }

   public static void main(String[] var0) {
      MsGPXsdSchemas var1 = new MsGPXsdSchemas();
      String var2 = "Z:/etlforge/branches/v5/5.1.x/dev/ETLForgeConnectorProject/MSGP/Data/Request&Response/SpecificSchema_Response.xml";

      try {
         String var3 = ConnectorExecutionSystem.readFileUTF8(var2).trim();
         var3 = XmlUtils.removeXmlHeader(var3);
         ConnectorXsdSchemaFiles var4 = var1.obtainSchemas(var3);
      } catch (SAXException var5) {
         var5.printStackTrace();
      } catch (IOException var6) {
         var6.printStackTrace();
      }
   }

   private static class GPXsdSchemaHanlder {
      String xsdSchema;
      String rootName;
      @RelativePathRoot
      public final String relativeRoot;

      GPXsdSchemaHanlder(String var1) {
         this.relativeRoot = var1;
      }

      @EndElementHandler("rootname")
      public void rootname(String var1) {
         this.rootName = var1.trim();
      }

      @EndElementHandler("content")
      public void content(String var1) {
         this.xsdSchema = var1.trim();
      }
   }
}
