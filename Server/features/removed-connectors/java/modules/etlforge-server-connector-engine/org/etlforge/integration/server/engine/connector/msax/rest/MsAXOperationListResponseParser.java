package org.etlforge.integration.server.engine.connector.msax.rest;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import org.etlforge.integration.server.engine.connector.ConnectorExecutionSystem;
import org.etlforge.integration.server.engine.connector.exception.ConnectorIllegalResponseRuntimeException;
import org.etlforge.integration.server.engine.connector.exception.ConnectorResponseProcessRuntimeException;
import org.etlforge.integration.server.engine.connector.msax.adapter.MsAXOperation;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.RelativePathRoot;
import org.xml.sax.SAXException;

public class MsAXOperationListResponseParser {
   public static List<MsAXOperation> parseOperationListResponse(String var0) {
      KongaSaxParser var1 = new KongaSaxParser();
      MsAXOperationListResponseParser.MsAXOperationListHandler var2 = new MsAXOperationListResponseParser.MsAXOperationListHandler(var0);
      var1.registerHandler(var2);

      try {
         var1.parseString(var0);
         return var2.operations;
      } catch (SAXException var4) {
         throw new ConnectorResponseProcessRuntimeException(var4);
      }
   }

   public static void main(String[] var0) throws IOException {
      String var1 = "Z:/etlforgeproject/connector/AX/AX.APIs.Request&Response.Structure/test.xml";
      String var2 = ConnectorExecutionSystem.readFileUTF8(var1);

      for (MsAXOperation var5 : parseOperationListResponse(var2)) {
         System.out.println("s=" + var5.getServiceName() + "v=" + var5.getOperationName());
      }
   }

   @RelativePathRoot("operationsResponses/operationsResponse/services/service")
   private static class MsAXOperationListHandler {
      List<MsAXOperation> operations = Lists.newArrayList();
      private String serviceName = null;
      private final String xml;

      MsAXOperationListHandler(String var1) {
         this.xml = var1;
      }

      @EndElementHandler("/")
      public void service(String var1) {
         this.serviceName = null;
      }

      @EndElementHandler("name")
      public void name(String var1) {
         this.serviceName = var1;
      }

      @EndElementHandler("operations/operation")
      public void operation(String var1) {
         if (this.serviceName == null) {
            throw new ConnectorIllegalResponseRuntimeException("AX Connector Service Name cannot be null, Check the response from AX service:" + this.xml);
         }

         this.operations.add(new MsAXOperation(this.serviceName, var1));
      }
   }
}
