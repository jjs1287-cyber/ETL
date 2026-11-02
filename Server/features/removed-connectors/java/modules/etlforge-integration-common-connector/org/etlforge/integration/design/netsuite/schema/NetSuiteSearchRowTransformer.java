package org.etlforge.integration.design.netsuite.schema;

import org.etlforge.integration.design.exception.ConnectorNetSuiteDefinitionError;
import org.etlforge.integration.design.netsuite.adapter.NetSuiteObjects;
import org.etlforge.integration.design.netsuite.adapter.NetSuiteSelectedObject;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.etlforgeconnector.XmlQName;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchRowPropertyies;
import org.etlforge.xml.sax.SaxParsers;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class NetSuiteSearchRowTransformer {
   private final NetSuiteSelectedObject selectObject;

   public NetSuiteSearchRowTransformer(NetSuiteObjects var1) {
      this.selectObject = var1.getNetSuiteSelectedObject();
   }

   public String transform(String var1) {
      return this.hasSearchRows() ? this.transformImpl(var1) : var1;
   }

   private boolean hasSearchRows() {
      return this.selectObject == null ? false : this.selectObject.hasSearchRows();
   }

   private String transformImpl(String var1) {
      try {
         NetSuiteSearchRowPropertyies var2 = this.selectObject.getNetSuiteSearchRowPropertyies();
         NetSuiteSearchRowTransformer.Handler var3 = new NetSuiteSearchRowTransformer.Handler(var2);
         SaxParsers.parseString(var1, var3, false);
         return var3.sb.toString();
      } catch (SAXException var4) {
         throw new ConnectorNetSuiteDefinitionError(var4);
      }
   }

   private static class Handler extends DefaultHandler {
      private final NetSuiteSearchRowPropertyies searchRows;
      StringBuilder sb = new StringBuilder();
      private static final String elementName = "element";
      private int elementLevel = -1;
      private int level = 0;
      private boolean shouldWrite = true;

      private Handler(NetSuiteSearchRowPropertyies var1) {
         this.searchRows = var1;
      }

      private boolean isElement(String var1) {
         return XmlQName.getLocalName(var1).equals("element");
      }

      private boolean shouldWriteSchema() {
         return this.shouldWrite || this.level < this.elementLevel;
      }

      @Override
      public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
         this.level++;
         if (this.elementLevel < 0 && this.isElement(var3)) {
            this.elementLevel = this.level;
         }

         if (this.elementLevel == this.level) {
            if (this.isElement(var3)) {
               String var5 = var4.getValue("type");
               String var6 = XmlQName.getLocalName(var5);
               this.shouldWrite = this.searchRows.hasColumns(var6);
            } else {
               this.shouldWrite = true;
            }
         }

         if (this.shouldWriteSchema()) {
            XmlElementUtils.startElementTag(this.sb, var3, var4);
         }
      }

      @Override
      public void endElement(String var1, String var2, String var3) throws SAXException {
         if (this.shouldWriteSchema()) {
            XmlElementUtils.endElementTag(this.sb, var3);
         }

         this.level--;
      }
   }
}
