package org.etlforge.integration.design.netsuite.schema;

import org.apache.commons.lang.StringEscapeUtils;
import org.etlforge.integration.design.exception.ConnectorNetSuiteDefinitionError;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.etlforgeconnector.XmlQName;
import org.etlforge.xml.sax.SaxParsers;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class NetSuiteSearchRowBasicLoopRemover {
   public static String removeLoop(String var0) {
      try {
         return removeImpl(var0);
      } catch (SAXException var2) {
         throw new ConnectorNetSuiteDefinitionError(var2);
      }
   }

   public static String removeImpl(String var0) throws SAXException {
      NetSuiteSearchRowBasicLoopRemover.Handler var1 = new NetSuiteSearchRowBasicLoopRemover.Handler();
      SaxParsers.parseString(var0, var1, false);
      return var1.sb.toString();
   }

   private static class Handler extends DefaultHandler {
      StringBuilder sb = new StringBuilder();
      private static final String elementName = "element";
      private static final String maxOccursName = "maxOccurs";
      private int elementLevel = -1;
      private int level = 0;

      private Handler() {
      }

      private boolean isElement(String var1) {
         return XmlQName.getLocalName(var1).equals("element");
      }

      @Override
      public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
         this.level++;
         if (this.elementLevel < 0 && this.isElement(var3)) {
            this.elementLevel = this.level;
         }

         if (this.elementLevel == this.level) {
            XmlElementUtils.startElementTagExcludingAttribute(this.sb, var3, var4, "maxOccurs");
         } else {
            XmlElementUtils.startElementTagExcludingAttribute(this.sb, var3, var4, null);
         }
      }

      @Override
      public void endElement(String var1, String var2, String var3) throws SAXException {
         XmlElementUtils.endElementTag(this.sb, var3);
         if (this.elementLevel == this.level) {
            this.elementLevel = -1;
         }

         this.level--;
      }

      @Override
      public void characters(char[] var1, int var2, int var3) throws SAXException {
         String var4 = new String(var1, var2, var3);
         this.sb.append(StringEscapeUtils.escapeXml(var4));
      }
   }
}
