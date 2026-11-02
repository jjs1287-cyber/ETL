package org.etlforge.integration.design.netsuite.definition;

import com.google.common.base.Strings;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NetSuiteDerivedTypeHandler extends NetSuiteDerivedTypeDefaultHandler {
   private boolean doneWithExtraElement;
   private final String baseTypeElements;

   public NetSuiteDerivedTypeHandler(String var1) {
      this.baseTypeElements = var1;
      this.doneWithExtraElement = true;
   }

   @Override
   public void init() {
      super.init();
      this.doneWithExtraElement = Strings.isNullOrEmpty(this.baseTypeElements);
   }

   @Override
   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      this.shouldSkip = isDerivedTypeElement(var3);
      if (!this.shouldSkip) {
         String var5 = XmlElementUtils.startElementTag(var3, var4);
         if (!this.doneWithExtraElement && isElementQName(var3, "sequence")) {
            this.sb.append(var5);
            this.sb.append(this.baseTypeElements);
            this.doneWithExtraElement = true;
         } else {
            if (!this.doneWithExtraElement && isElementQName(var3, "attribute")) {
               this.sb.append("<sequence>");
               this.sb.append(this.baseTypeElements);
               this.sb.append("</sequence>");
               this.doneWithExtraElement = true;
            }

            this.sb.append(var5);
         }
      }
   }
}
