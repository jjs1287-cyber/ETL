package org.etlforge.tools.xml.xmlviewer;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public final class XmlFile {
   private final File file;
   private Document domDocument;

   private XmlFile(File var1) {
      this.file = var1;
   }

   private void parse() throws ParserConfigurationException, IOException, SAXException {
      if (this.file != null) {
         DocumentBuilderFactory var1 = DocumentBuilderFactory.newInstance();
         DocumentBuilder var2 = var1.newDocumentBuilder();
         this.domDocument = var2.parse(this.file);
      }
   }

   public static XmlFile createXmlFile(File var0) throws ParserConfigurationException, IOException, SAXException {
      XmlFile var1 = new XmlFile(var0);
      var1.parse();
      return var1;
   }

   public File getFileObject() {
      return this.file;
   }

   public String getName() {
      return this.file.getName();
   }

   public String toFormattedText() {
      XMLSerializer var1 = new XMLSerializer(new OutputFormat(this.domDocument, "UTF-8", true));
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      var1.setOutputByteStream(var2);

      try {
         var1.serialize(this.domDocument);
      } catch (IOException var6) {
         var6.printStackTrace();
      }

      String var3 = var2.toString();

      try {
         var2.close();
      } catch (IOException var5) {
         var5.printStackTrace();
      }

      return var3;
   }

   public void saveFile(String var1) throws IOException {
      this.saveFileAs(var1, this.file);
   }

   public void saveFileAs(String var1, File var2) throws IOException {
      BufferedWriter var3 = null;

      try {
         var3 = new BufferedWriter(new FileWriter(var2));
         char var4 = '\n';
         String var5 = String.valueOf(var4);
         StringTokenizer var6 = new StringTokenizer(var1, var5, false);

         while (var6.hasMoreTokens()) {
            var3.write(var6.nextToken());
            var3.newLine();
         }
      } finally {
         if (var3 != null) {
            try {
               var3.close();
            } catch (IOException var12) {
               var12.printStackTrace();
            }
         }
      }
   }

   public Element getRoot() {
      return this.domDocument.getDocumentElement();
   }
}
