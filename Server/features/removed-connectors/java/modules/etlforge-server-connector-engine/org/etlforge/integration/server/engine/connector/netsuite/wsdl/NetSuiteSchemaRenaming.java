package org.etlforge.integration.server.engine.connector.netsuite.wsdl;

import java.util.Map;
import javax.xml.transform.TransformerException;
import org.etlforge.integration.design.exception.ConnectorNetSuiteDefinitionError;
import org.etlforge.integration.design.util.SchemaRenameParser;
import org.etlforge.integration.server.netsuite.wsdl.NetSuiteWsdlContents;
import org.etlforge.xml.dom.DomParser;
import org.w3c.dom.Document;

public class NetSuiteSchemaRenaming {
   public static NetSuiteWsdlContents newName(Map<String, String> var0, NetSuiteWsdlContents var1) {
      NetSuiteSchemaRenameStrategy var2 = new NetSuiteSchemaRenameStrategy(var0);
      String var3 = var2.newName(var1.getNetsuiteWsdlKey());
      NetSuiteWsdlContents var4 = new NetSuiteWsdlContents(var3);

      for (String var6 : var1.getXsdSchemaKeys()) {
         String var7 = var1.getXsdSchema(var6);
         String var8 = var2.newName(var6);
         var4.setXsdSchema(var8, newContent(var2, var7));
      }

      return var4;
   }

   private static String newContent(NetSuiteSchemaRenameStrategy var0, String var1) {
      try {
         return newContentImpl(var0, var1);
      } catch (TransformerException var3) {
         var3.printStackTrace();
         throw new ConnectorNetSuiteDefinitionError(var3);
      } catch (Exception var4) {
         var4.printStackTrace();
         throw new ConnectorNetSuiteDefinitionError(var4);
      }
   }

   private static String newContentImpl(NetSuiteSchemaRenameStrategy var0, String var1) throws TransformerException, Exception {
      Document var2 = DomParser.parseXmlString(var1, true);
      SchemaRenameParser var3 = new SchemaRenameParser(var0, var2);
      return var3.process() ? var3.domToString() : var1;
   }
}
