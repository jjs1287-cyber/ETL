package org.etlforge.xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;

public final class DomBuilderFactory {
   private DomBuilderFactory() {
      throw new UnsupportedOperationException();
   }

   public static DomBuilder newBuilder(String var0) throws DomBuilderException {
      try {
         DocumentBuilderFactory var1 = DocumentBuilderFactory.newInstance();
         DocumentBuilder var2 = var1.newDocumentBuilder();
         Document var3 = var2.newDocument();
         return new DomBuilderImpl(var3, var0);
      } catch (ParserConfigurationException var4) {
         throw new DomBuilderException("Failed to create a DomBuilder instance.", var4);
      }
   }
}
