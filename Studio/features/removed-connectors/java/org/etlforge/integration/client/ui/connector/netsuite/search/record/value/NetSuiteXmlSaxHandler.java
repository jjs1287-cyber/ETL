package org.etlforge.integration.client.ui.connector.netsuite.search.record.value;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public abstract class NetSuiteXmlSaxHandler extends DefaultHandler {
   private int level = 0;

   public int getLevel() {
      return this.level;
   }

   public boolean isRoot() {
      return this.level == 0;
   }

   @Override
   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      this.startElementImpl(var1, var2, var3, var4);
      this.level++;
   }

   public abstract void startElementImpl(String var1, String var2, String var3, Attributes var4) throws SAXException;

   @Override
   public void endElement(String var1, String var2, String var3) throws SAXException {
      this.level--;
      this.endElementImpl(var1, var2, var3);
   }

   public abstract void endElementImpl(String var1, String var2, String var3) throws SAXException;
}
