package org.etlforge.integration.design.netsuite.definition;

import org.etlforge.integration.design.util.XmlElementUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NetSuiteDerivedTypeSimpleHandler extends NetSuiteDerivedTypeDefaultHandler {
   @Override
   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      this.shouldSkip = isDerivedTypeElement(var3);
      if (!this.shouldSkip) {
         XmlElementUtils.startElementTag(this.sb, var3, var4);
      }
   }
}
