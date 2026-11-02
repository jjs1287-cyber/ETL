package org.etlforge.xml.sax;

import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.google.common.io.Resources;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public final class SaxParsers {
   public static void parseResource(String var0, DefaultHandler var1, boolean var2) throws SAXException, IOException {
      URL var3 = Resources.getResource(var0);
      InputSupplier var4 = Resources.newInputStreamSupplier(var3);
      parseInput(var4, var1, var2);
   }

   public static void parseFile(File var0, DefaultHandler var1, boolean var2) throws SAXException, IOException {
      parseInput(Files.newInputStreamSupplier(var0), var1, var2);
   }

   public static void parseInput(InputSupplier<? extends InputStream> var0, DefaultHandler var1, boolean var2) throws SAXException, IOException {
      SAXParser var3 = createParser(var2);
      InputStream var4 = (InputStream)var0.getInput();

      try {
         var3.parse(var4, var1);
      } finally {
         Closeables.closeQuietly(var4);
      }
   }

   private static SAXParser createParser(boolean var0) throws SAXException {
      try {
         SAXParserFactory var1 = SAXParserFactory.newInstance();
         var1.setNamespaceAware(var0);
         return var1.newSAXParser();
      } catch (ParserConfigurationException var2) {
         throw new RuntimeException(var2);
      }
   }

   public static void parseString(String var0, DefaultHandler var1, boolean var2) throws SAXException {
      try {
         parseReader(CharStreams.newReaderSupplier(var0), var1, var2);
      } catch (IOException var4) {
         throw new RuntimeException(var4);
      }
   }

   public static void parseReader(InputSupplier<? extends Reader> var0, DefaultHandler var1, boolean var2) throws SAXException, IOException {
      SAXParser var3 = createParser(var2);
      Reader var4 = (Reader)var0.getInput();

      try {
         InputSource var5 = new InputSource(var4);
         var3.parse(var5, var1);
      } finally {
         Closeables.closeQuietly(var4);
      }
   }

   private SaxParsers() {
   }
}
