package org.etlforge.integration.server.engine.connector.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class SortedXmlWriter {
   public static void main(String[] var0) {
      String var1 = "Z:/etlforge/connectors/etlforgeconnectorsample/connectorengine/";
      String var2 = var1 + "konga_connector_engine.wsdl";
      String var3 = var1 + "konga_connector_engine_sorted.wsdl";
      sortFile(var2, var3);
   }

   public static void sortFolder(String var0, String var1) {
      File var2 = new File(var0);

      for (int var3 = 0; var3 < var2.listFiles().length; var3++) {
         File var4 = var2.listFiles()[var3];
         if (var4.isFile()) {
            File var5 = new File(var1, var4.getName());
            sortFile(var4, var5);
         }
      }
   }

   public static void sortFile(String var0, String var1) {
      sortFile(new File(var0), new File(var1));
   }

   static void sortFile(File var0, File var1) {
      Document var2 = newDocument(var0);
      if (var2 != null) {
         SortedXmlNode var3 = new SortedXmlNode(var2);
         Node var4 = var3.getRoot();
         writeDocument(var4, var1);
      }
   }

   static void writeDocument(Node var0, File var1) {
      try {
         TransformerFactory var2 = TransformerFactory.newInstance();
         Transformer var3 = var2.newTransformer();
         DOMSource var4 = new DOMSource(var0);
         StreamResult var5 = new StreamResult(var1);
         var3.transform(var4, var5);
      } catch (TransformerConfigurationException var6) {
         var6.printStackTrace();
      } catch (TransformerException var7) {
         var7.printStackTrace();
      }
   }

   private static Document newDocument(File var0) {
      FileInputStream var1 = null;

      try {
         var1 = new FileInputStream(var0);
         return newDocument(var1);
      } catch (FileNotFoundException var9) {
         var9.printStackTrace();
      } catch (ParserConfigurationException var10) {
         var10.printStackTrace();
      } catch (SAXException var11) {
         var11.printStackTrace();
      } catch (IOException var12) {
         var12.printStackTrace();
      } finally {
         close(var1);
      }

      return null;
   }

   public static void close(Closeable var0) {
      try {
         if (var0 != null) {
            var0.close();
         }
      } catch (IOException var2) {
         var2.printStackTrace();
      }
   }

   private static Document newDocument(InputStream var0) throws ParserConfigurationException, SAXException, IOException {
      DocumentBuilderFactory var1 = DocumentBuilderFactory.newInstance();
      var1.setNamespaceAware(true);
      var1.setIgnoringElementContentWhitespace(false);
      var1.setIgnoringComments(false);
      DocumentBuilder var2 = var1.newDocumentBuilder();
      return var2.parse(new InputSource(var0));
   }
}
