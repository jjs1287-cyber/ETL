package org.etlforge.xml.dom;

import com.google.common.io.Closeables;
import com.google.common.io.InputSupplier;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.etlforge.xml.KongaXmlUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public final class DomParser {
   public static Document parseXmlString(String var0) throws SAXException {
      return parseXmlString(var0, false);
   }

   public static Document parseXmlString(String var0, boolean var1) throws SAXException {
      try {
         StringReader var2 = new StringReader(var0);
         return parseInputSource(new InputSource(var2), var1);
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public static Document parseInputSource(InputSource var0) throws SAXException, IOException {
      return parseInputSource(var0, false);
   }

   public static Document parseInputSource(InputSource var0, boolean var1) throws SAXException, IOException {
      DocumentBuilder var2 = createDomBuilder(var1);
      return var2.parse(var0);
   }

   public static Document parseResource(String var0) throws SAXException, IOException {
      InputStream var1 = null;

      try {
         var1 = DomParser.class.getClassLoader().getResourceAsStream(var0);
         return parseInputStream(var1);
      } finally {
         KongaXmlUtils.close(var1);
      }
   }

   public static Document parseInput(InputSupplier<? extends InputStream> var0) throws SAXException, IOException {
      return parseInput(var0, false);
   }

   public static Document parseInput(InputSupplier<? extends InputStream> var0, boolean var1) throws SAXException, IOException {
      InputStream var2 = (InputStream)var0.getInput();

      try {
         return parseInputStream(var2, var1);
      } finally {
         Closeables.closeQuietly(var2);
      }
   }

   public static Document parseInputStream(InputStream var0) throws SAXException, IOException {
      return parseInputStream(var0, false);
   }

   public static Document parseInputStream(InputStream var0, boolean var1) throws SAXException, IOException {
      DocumentBuilder var2 = createDomBuilder(var1);
      return var2.parse(var0);
   }

   private static DocumentBuilder createDomBuilder(boolean var0) {
      try {
         DocumentBuilderFactory var1 = DocumentBuilderFactory.newInstance();
         var1.setNamespaceAware(var0);
         return var1.newDocumentBuilder();
      } catch (ParserConfigurationException var3) {
         throw new RuntimeException(var3);
      }
   }

   public static Document parseFile(File var0) throws Exception {
      return parseFile(var0, false);
   }

   public static Document parseFile(File var0, boolean var1) throws Exception {
      FileInputStream var2 = new FileInputStream(var0);

      try {
         return parseInputStream(var2, var1);
      } finally {
         KongaXmlUtils.close(var2);
      }
   }

   private DomParser() {
   }
}
