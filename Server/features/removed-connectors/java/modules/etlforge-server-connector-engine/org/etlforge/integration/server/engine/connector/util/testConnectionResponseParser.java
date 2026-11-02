package org.etlforge.integration.server.engine.connector.util;

import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.etlforge.integration.server.engine.connector.exception.ConnectorEndpointRuntimeException;
import org.etlforge.integration.server.engine.connector.exception.ConnectorResponseProcessRuntimeException;
import org.etlforge.xml.KongaXmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class testConnectionResponseParser {
   public static boolean testConnectionResponse(String var0, String var1) {
      Element var2 = parseTestConnectionXml(var1, var0);
      if (var2 != null) {
         String var3 = KongaXmlUtils.getChildElementValue(var2, "success");
         if (var3 != null && var3.equalsIgnoreCase("true")) {
            return true;
         }

         String var4 = KongaXmlUtils.getChildElementValue(var2, "message");
         throw new ConnectorEndpointRuntimeException("message: " + var4);
      } else {
         throw new ConnectorResponseProcessRuntimeException("Invalid test connection response: \n" + var1);
      }
   }

   private static Element parseTestConnectionXml(String var0, String var1) {
      try {
         Document var2 = newDocument(var0);
         NodeList var3 = var2.getElementsByTagName(var1);
         if (var3.getLength() == 1) {
            Node var4 = var3.item(0);
            if (var4.getNodeType() == 1) {
               return (Element)var4;
            }
         }

         throw new ConnectorResponseProcessRuntimeException("invalid test connection response:\n" + var0);
      } catch (ParserConfigurationException var5) {
         throw new ConnectorResponseProcessRuntimeException("fail to parse:\n" + var0, var5);
      } catch (SAXException var6) {
         throw new ConnectorResponseProcessRuntimeException("fail to parse:\n" + var0, var6);
      } catch (IOException var7) {
         throw new ConnectorResponseProcessRuntimeException("fail to parse:\n" + var0, var7);
      }
   }

   private static Document newDocument(String var0) throws ParserConfigurationException, SAXException, IOException {
      DocumentBuilderFactory var1 = DocumentBuilderFactory.newInstance();
      var1.setNamespaceAware(true);
      var1.setIgnoringElementContentWhitespace(true);
      var1.setIgnoringComments(true);
      DocumentBuilder var2 = var1.newDocumentBuilder();
      StringReader var3 = new StringReader(var0);
      return var2.parse(new InputSource(var3));
   }
}
