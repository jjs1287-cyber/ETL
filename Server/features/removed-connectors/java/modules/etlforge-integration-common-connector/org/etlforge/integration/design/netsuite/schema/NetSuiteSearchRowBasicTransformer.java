package org.etlforge.integration.design.netsuite.schema;

import java.util.Set;
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

public class NetSuiteSearchRowBasicTransformer {
   private final NetSuiteSelectedObject selectObject;

   public NetSuiteSearchRowBasicTransformer(NetSuiteObjects var1) {
      this.selectObject = var1.getNetSuiteSelectedObject();
   }

   public String transform(String var1, String var2) {
      return this.hasSearchRows() ? this.transformImpl(var1, var2) : var2;
   }

   private boolean hasSearchRows() {
      return this.selectObject == null ? false : this.selectObject.hasSearchRows();
   }

   private String transformImpl(String var1, String var2) {
      try {
         NetSuiteSearchRowPropertyies var3 = this.selectObject.getNetSuiteSearchRowPropertyies();
         if (!var3.hasColumns(var1)) {
            return "";
         }

         NetSuiteSearchRowBasicTransformer.Handler var4 = new NetSuiteSearchRowBasicTransformer.Handler(var3.getFields(var1), var3.hasCustomFields(var1));
         SaxParsers.parseString(var2, var4, false);
         return var4.sb.toString();
      } catch (SAXException var5) {
         throw new ConnectorNetSuiteDefinitionError(var5);
      }
   }

   private static class Handler extends DefaultHandler {
      private final Set<String> selectedColumns;
      private final boolean hasCustomFields;
      StringBuilder sb = new StringBuilder();
      private static final String elementName = "element";
      private int elementLevel = -1;
      private int level = 0;
      private boolean shouldWrite = true;

      private Handler(Set<String> var1, boolean var2) {
         this.selectedColumns = var1;
         this.hasCustomFields = var2;
      }

      private boolean isElement(String var1) {
         return XmlQName.getLocalName(var1).equals("element");
      }

      private boolean shouldWriteSchema() {
         return this.shouldWrite || this.level < this.elementLevel;
      }

      private boolean isSelectedColumn(String var1) {
         return this.selectedColumns.contains(var1) ? true : this.hasCustomFields && "customFieldList".equals(var1);
      }

      @Override
      public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
         this.level++;
         if (this.elementLevel < 0 && this.isElement(var3)) {
            this.elementLevel = this.level;
         }

         if (this.elementLevel == this.level) {
            if (this.isElement(var3)) {
               String var5 = var4.getValue("name");
               this.shouldWrite = this.isSelectedColumn(var5);
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
