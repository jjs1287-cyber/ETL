package org.etlforge.integration.design.util;

import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public abstract class XmlSaxHandler extends DefaultHandler {
   private int level = 0;
   protected boolean isShortElement = false;
   protected final PrefixNamespace prefixNamespace = new PrefixNamespace();

   protected XmlSaxHandler() {
   }

   public int getLevel() {
      return this.level;
   }

   public boolean isRoot() {
      return this.level == 0;
   }

   protected final PrefixNamespace getPrefixNamespace() {
      return this.prefixNamespace;
   }

   @Override
   public void startPrefixMapping(String var1, String var2) throws SAXException {
      this.prefixNamespace.startPrefixMapping(var1, var2);
   }

   @Override
   public void endPrefixMapping(String var1) throws SAXException {
      this.prefixNamespace.endPrefixMapping(var1);
   }

   @Override
   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      this.startElementImpl(var1, var2, var3, var4);
      this.level++;
      this.getPrefixNamespace().clearWithCurrentPrefixNamespaces();
      this.isShortElement = true;
   }

   public abstract void startElementImpl(String var1, String var2, String var3, Attributes var4) throws SAXException;

   @Override
   public void endElement(String var1, String var2, String var3) throws SAXException {
      this.level--;
      this.endElementImpl(var1, var2, var3);
      this.isShortElement = false;
   }

   public abstract void endElementImpl(String var1, String var2, String var3) throws SAXException;

   @Override
   public void characters(char[] var1, int var2, int var3) throws SAXException {
      this.isShortElement = false;
   }

   public static String addDefinedPrefixNamespaceMap(String var0, String var1, Map<String, String> var2) {
      String var3 = var0;
      int var4 = 0;

      while (var2.containsKey(var3)) {
         if (var3.isEmpty()) {
            var3 = "ob";
         } else {
            var3 = var0 + var4++;
         }
      }

      var2.put(var3, var1);
      return var3;
   }
}
