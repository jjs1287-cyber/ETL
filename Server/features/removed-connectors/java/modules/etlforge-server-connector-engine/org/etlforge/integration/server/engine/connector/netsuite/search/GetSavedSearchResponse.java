package org.etlforge.integration.server.engine.connector.netsuite.search;

import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.RelativePathRoot;
import org.etlforge.xml.sax.StartElementHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class GetSavedSearchResponse {
   public Map<String, String> getSavedSearchesMock() {
      HashMap var1 = Maps.newHashMap();
      var1.put("mock_id1", "1_mock_saveSearch 1");
      var1.put("mock_id2", "2_mock_saveSearch 2");
      return var1;
   }

   public static Map<String, String> getSavedSearches(String var0) {
      KongaSaxParser var1 = new KongaSaxParser();
      GetSavedSearchResponse.GetSavedSearchResponseHandler var2 = new GetSavedSearchResponse.GetSavedSearchResponseHandler();
      var1.registerHandler(var2);

      try {
         var1.parseString(var0);
         return var2.savedSearches;
      } catch (SAXException var4) {
         throw new ConnectorServerRuntimeException(var4);
      }
   }

   @RelativePathRoot("Envelope/Body/getSavedSearchResponse/getSavedSearchResult/recordRefList/recordRef")
   private static class GetSavedSearchResponseHandler {
      private final Map<String, String> savedSearches = Maps.newHashMap();
      private String internalId;
      private String name;

      private GetSavedSearchResponseHandler() {
      }

      @StartElementHandler("/")
      public void startRecordRef(Attributes var1) {
         this.internalId = var1.getValue("internalId");
         this.name = "No Name";
      }

      @EndElementHandler("name")
      public void endName(String var1) {
         this.name = var1;
      }

      @EndElementHandler("/")
      public void endRecordRef(String var1) {
         this.savedSearches.put(this.internalId, this.name);
      }
   }
}
