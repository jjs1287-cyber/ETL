package org.etlforge.integration.server.engine.connector.mscrm.adapter;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.mscrm.metadata.MSCrmMetaDataException;
import org.etlforge.integration.mscrm.query.FetchXml;
import org.etlforge.xml.Elements;
import org.etlforge.xml.KongaXmlUtils;
import org.etlforge.xml.XmlPrettyPrinter;
import org.etlforge.xml.dom.DomParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

final class SavedQueryResponseParser {
   private static final String RESPONSE_ROOT = "/savedQueryResponses/savedQueryResponse/";

   public static List<FetchXml> parse(String var0) throws MSCrmMetaDataException {
      try {
         ArrayList var1 = Lists.newArrayList();
         Document var2 = DomParser.parseXmlString(var0);
         checkSuccessStatus(var2, var0);
         XmlPrettyPrinter var3 = XmlPrettyPrinter.withIndent(4);
         int var4 = 1;

         for (Element var6 : Elements.matching("/savedQueryResponses/savedQueryResponse/fetchXml/fetch", var2)) {
            String var7 = "Query #" + var4;
            KongaXmlUtils.removeWhitespace(var6);
            String var8 = var3.prettyPrintToString(var6);
            FetchXml var9 = new FetchXml(var7, var8);
            var1.add(var9);
            var4++;
         }

         return var1;
      } catch (Exception var10) {
         Throwables.propagateIfInstanceOf(var10, MSCrmMetaDataException.class);
         throw new MSCrmMetaDataException("Failed to process the returned list of saved queries: " + var10.getMessage(), var10);
      }
   }

   private static void checkSuccessStatus(Document var0, String var1) throws MSCrmMetaDataException {
      Element var2 = Elements.firstMatch("/savedQueryResponses/savedQueryResponse/success", var0);
      if (var2 != null) {
         if (!KongaXmlUtils.isTrue(var2)) {
            String var3 = getErrorMessage(var0);
            throw new MSCrmMetaDataException(var3);
         }
      }
   }

   private static String getErrorMessage(Document var0) {
      Element var1 = Elements.firstMatch("/savedQueryResponses/savedQueryResponse/message", var0);
      if (var1 != null) {
         String var2 = KongaXmlUtils.getElementValue(var1);
         if (!Strings.isNullOrEmpty(var2)) {
            return var2;
         }
      }

      return "Unknown error.";
   }

   private SavedQueryResponseParser() {
   }
}
