package org.etlforge.xml.sax;

import org.xml.sax.Attributes;

public final class StartOfElement {
   private final String uri;
   private final String localName;
   private final String qName;
   private final Attributes attributes;

   public StartOfElement(String var1, String var2, String var3, Attributes var4) {
      this.uri = var1;
      this.localName = var2;
      this.qName = var3;
      this.attributes = var4;
   }

   public String uri() {
      return this.uri;
   }

   public String localName() {
      return this.localName;
   }

   public String qName() {
      return this.qName;
   }

   public Attributes attributes() {
      return this.attributes;
   }

   public String getAttribute(String var1) {
      return this.attributes.getValue(var1);
   }
}
