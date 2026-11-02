package org.etlforge.integration.server.engine.connector.netsuite.operation;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.server.engine.connector.ConnectorExecutionSystem;
import org.etlforge.integration.server.engine.connector.exception.ConnectorWebServiceRuntimeException;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.RelativePathRoot;
import org.etlforge.xml.sax.StartElementHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NetSuiteGetCustomizationIdResponse {
   static List<NetSuiteCustomizationRef> parseResponse(String var0) {
      KongaSaxParser var1 = new KongaSaxParser();
      NetSuiteGetCustomizationIdResponse.Handler var2 = new NetSuiteGetCustomizationIdResponse.Handler();
      var1.registerHandler(var2);

      try {
         var1.parseString(var0);
      } catch (SAXException var4) {
         throw new ConnectorWebServiceRuntimeException(var4);
      }

      return var2.customizationRefs;
   }

   public static void main(String[] var0) {
      String var1 = "Z:/etlforge/connectors/etlforgeconnectorsample/NETSUITE/Sample/getCustomizationId/";
      String var2 = var1 + "getCustomizationIdRecordTypeRes.xml";
      String var3 = ConnectorExecutionSystem.readFileUTF8(var2);

      for (NetSuiteCustomizationRef var6 : parseResponse(var3)) {
         System.out.println(var6.getInternalId());
         System.out.println(var6.getRefType());
         System.out.println(var6.getName());
         System.out.println();
      }
   }

   @RelativePathRoot("Envelope/Body/getCustomizationIdResponse/getCustomizationIdResult/customizationRefList/customizationRef")
   private static class Handler {
      public final List<NetSuiteCustomizationRef> customizationRefs = Lists.newArrayList();
      private NetSuiteCustomizationRef ref;

      private Handler() {
      }

      @StartElementHandler("/")
      public void startCustomizationRef(Attributes var1) {
         this.ref = new NetSuiteCustomizationRef();
         this.customizationRefs.add(this.ref);
         this.ref.setInternalId(var1.getValue("internalId"));
         this.ref.setRefType(var1.getValue("type"));
      }

      @EndElementHandler("name")
      public void endName(String var1) {
         this.ref.setName(var1);
      }
   }
}
