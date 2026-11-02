package org.etlforge.integration.design.netsuite.definition;

import org.etlforge.integration.design.util.XmlElementUtils;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public abstract class NetSuiteDerivedTypeDefaultHandler extends DefaultHandler {
   protected static final String sequenceName = "sequence";
   protected static final String attributeName = "attribute";
   protected StringBuilder sb = null;
   protected boolean shouldSkip = true;

   protected static boolean isElementQName(String var0, String var1) {
      return var0.endsWith(var1);
   }

   protected static boolean isDerivedTypeElement(String var0) {
      return isElementQName(var0, "complexContent") || isElementQName(var0, "extension");
   }

   protected NetSuiteDerivedTypeDefaultHandler() {
   }

   public void init() {
      this.shouldSkip = false;
      this.sb = new StringBuilder();
   }

   @Override
   public void endElement(String var1, String var2, String var3) throws SAXException {
      this.shouldSkip = isDerivedTypeElement(var3);
      if (!this.shouldSkip) {
         XmlElementUtils.endElementTag(this.sb, var3);
      }
   }

   public String getNewSchema() {
      return this.sb.toString();
   }

   public void setObjectName(String var1) {
   }
}
